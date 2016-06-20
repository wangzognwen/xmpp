package com.juns.wechat.xmpp;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.xmpp.listener.RosterLoadedListenerImpl;
import com.juns.wechat.xmpp.listener.XmppConnectionListener;
import com.juns.wechat.xmpp.listener.XmppReceivePacketFilter;
import com.juns.wechat.xmpp.listener.XmppReceivePacketListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterLoadedListener;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.Set;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：实现了Xmpp的各种功能
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class XmppManagerImpl implements XmppManager {
    private AbstractXMPPConnection xmppConnection;
    private ConnectionListener connectionListener;
    private StanzaListener packetListener;
    private StanzaFilter packetFilter;
    private RosterLoadedListener rosterLoadedListener;

    private static XmppManagerImpl mInstance;
    private Roster mRoster;

    private XmppManagerImpl() {
        init();
    }

    private void init(){
        xmppConnection = XmppConnUtil.getXmppConnection();
        mRoster = Roster.getInstanceFor(xmppConnection);

        connectionListener = new XmppConnectionListener();
        packetListener = new XmppReceivePacketListener();
        packetFilter = new XmppReceivePacketFilter();
        rosterLoadedListener = new RosterLoadedListenerImpl();
    }

    public static synchronized XmppManagerImpl getInstance() {
        if(mInstance == null){
            mInstance = new XmppManagerImpl();
        }
        return mInstance;
    }

    /**
     * 连接到XMPP服务器：1：检测是否已连接，如果连接，应该先断开连接再连接 2：注册各种监听事件
     */
    @Override
    public boolean connect(){
        if(xmppConnection == null){
            xmppConnection = XmppConnUtil.getXmppConnection();
        }
        if(xmppConnection.isConnected()) return true;
        try {
            xmppConnection.connect();
            registerListener();
            return true;
        } catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            XmppExceptionHandler.handleXmppExecption(e);
        }
        return false;
    }

    /**
     * 有以下几种情况会触发登录到XMPP
     * 1：用户首次进入
     * 2: 监听的广播事件触发：用户开机、网络状态发生变化（连上网）
     * @param accountName
     * @param passWord
     * @return
     */
    @Override
    public void login(String accountName, String passWord) throws SmackException, IOException, XMPPException {
        if(!connect()){
            throw new SmackException.NotConnectedException();
        }
        if(xmppConnection.isAuthenticated()) return;
        xmppConnection.login(accountName, passWord);
    }

    @Override
    public void regNewUser(String accountName, String passWord) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        if(!connect()){
            throw new SmackException.NotConnectedException();
        }
        AccountManager.getInstance(xmppConnection).createAccount(accountName, passWord);
    }

    public Set<RosterEntry> getRoster(String userName){
        if(!connect()){
            return null;
        }
        Roster roster = Roster.getInstanceFor(xmppConnection);
        return roster.getEntries();
    }

    @Override
    public void disConnect() {
        removeListener();
        xmppConnection.disconnect();
    }

    @Override
    public void shutDownConn() {
        xmppConnection.disconnect();
        removeListener();
        xmppConnection = null;
        XmppConnUtil.shutDownConn();
    }

    @Override
    public boolean isConnected() {
        return xmppConnection == null ? false : xmppConnection.isConnected();
    }

    @Override
    public boolean sendPacket(Stanza packet){
        try {
            connect();
            xmppConnection.sendStanza(packet);
            return true;
        } catch (SmackException.NotConnectedException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
        return false;
    }

    @Override
    public boolean sendMessage(MessageEntity messageEntity) {
        Message message = new Message();
        String fromJid = ConfigUtil.getXmppJid(messageEntity.getMyselfUserId());
        message.setFrom(fromJid);
        message.setType(Message.Type.chat);
        String toJid = ConfigUtil.getXmppJid(messageEntity.getOtherUserId());
        message.setTo(toJid);
        message.setStanzaId(messageEntity.getPacketId());

        return sendPacket(message);
    }

    @Override
    public boolean isFriends(int OtherUserId) {
        String otherJid = ConfigUtil.getXmppJid(OtherUserId);
        return mRoster.getEntry(otherJid) == null ? false : true;
    }

    @Override
    public boolean addFriend(int otherUserId, String nickName) {
        String jid = ConfigUtil.getXmppJid(otherUserId);
        try {
            connect();
            mRoster.createEntry(jid, nickName, null);
            return true;
        } catch (SmackException.NotLoggedInException e) {
            XmppExceptionHandler.handleSmackException(e);
        } catch (SmackException.NoResponseException e) {
            XmppExceptionHandler.handleSmackException(e);
        } catch (XMPPException.XMPPErrorException e) {
            XmppExceptionHandler.handleXmppExecption(e);
        } catch (SmackException.NotConnectedException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
        return false;
    }

    @Override
    public void searchUser(String name) throws SmackException.NotConnectedException,
            XMPPException.XMPPErrorException, SmackException.NoResponseException {
        UserSearchManager userSearchManager = new UserSearchManager(xmppConnection);
        Form searchForm = userSearchManager.getSearchForm("search");
        Form answerForm = searchForm.createAnswerForm();
        answerForm.setAnswer("name", name);
        ReportedData data = userSearchManager.getSearchResults(answerForm, "search");

    }

    /**
     * 移除各种监听事件
     */
    private void removeListener(){
        xmppConnection.removeConnectionListener(connectionListener);
        xmppConnection.removeAsyncStanzaListener(packetListener);
        Roster.getInstanceFor(xmppConnection).removeRosterLoadedListener(rosterLoadedListener);
    }

    private void registerListener() {
        xmppConnection.addConnectionListener(connectionListener);
        xmppConnection.addAsyncStanzaListener(packetListener, packetFilter);
        Roster.getInstanceFor(xmppConnection).addRosterLoadedListener(rosterLoadedListener);
    }
}
