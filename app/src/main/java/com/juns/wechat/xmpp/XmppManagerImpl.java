package com.juns.wechat.xmpp;

import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.util.FileUtil;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.xmpp.bean.SearchResult;
import com.juns.wechat.xmpp.event.XmppEvent;
import com.juns.wechat.xmpp.iq.FileTransferIQ;
import com.juns.wechat.xmpp.listener.RosterLoadedListenerImpl;
import com.juns.wechat.xmpp.listener.XmppConnectionListener;
import com.juns.wechat.xmpp.listener.XmppReceivePacketFilter;
import com.juns.wechat.xmpp.listener.XmppReceivePacketListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterLoadedListener;

import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;;
import org.simple.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.List;
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
    private String userName;
    private String passWord;

    private XmppManagerImpl() {
        init();
    }

    private void init(){
        xmppConnection = XmppConnUtil.getXmppConnection();
        mRoster = Roster.getInstanceFor(xmppConnection);
        userName = com.juns.wechat.manager.AccountManager.getInstance().getUserName();
        passWord = com.juns.wechat.manager.AccountManager.getInstance().getUserPassWord();

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
    public boolean connect() throws IOException, XMPPException, SmackException {
        if(xmppConnection == null){
            xmppConnection = XmppConnUtil.getXmppConnection();
        }
        if(xmppConnection.isConnected()){
            return true;
        }
        xmppConnection.connect();
        registerListener();
        return true;
    }

    @Override
    public boolean login() {
        return login(userName, passWord);
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
    public boolean login(String accountName, String passWord){
        try {
            connect();
            if(xmppConnection.isAuthenticated()){
                return true;
            }
            xmppConnection.login(accountName, passWord);
            return true;
        } catch (IOException e) {
            XmppExceptionHandler.handleIOException(e);
        } catch (XMPPException e) {
            XmppExceptionHandler.handleXmppExecption(e);
        } catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
        return false;
    }

    @Override
    public void regNewUser(String accountName, String passWord){
        try {
            connect();
            AccountManager.getInstance(xmppConnection).createAccount(accountName, passWord);
        } catch (IOException e) {
            XmppExceptionHandler.handleIOException(e);
        } catch (XMPPException e) {
            XmppExceptionHandler.handleXmppExecption(e);
        } catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
    }

    public Set<RosterEntry> getRoster(String userName){
        login(userName, passWord);
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
    public boolean isAuthenticated() {
        return xmppConnection == null ? false : xmppConnection.isAuthenticated();
    }

    @Override
    public boolean sendPacket(Stanza packet){
        try {
            if(!login(userName, passWord)){
                return false;
            }
            xmppConnection.sendStanza(packet);
            return true;
        }  catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        }
        return false;
    }

    @Override
    public boolean sendMessage(MessageBean messageBean) {
        Message message = new Message();
        message.setFrom(ConfigUtil.getXmppJid(messageBean.getMyselfName()));
        message.setType(Message.Type.chat);
        String toJid = ConfigUtil.getXmppJid(messageBean.getOtherName());
        message.setTo(toJid);
        message.setStanzaId(messageBean.getPacketId());
        message.setBody(messageBean.toSendJson());
        return sendPacket(message);
    }

    @Override
    public boolean isFriends(int OtherUserId) {
        String otherJid = ConfigUtil.getXmppJid(OtherUserId + "");
        return mRoster.getEntry(otherJid) == null ? false : true;
    }

    @Override
    public List<SearchResult> searchUser(String search){
        try {
            login(userName, passWord);
            UserSearchManager userSearchManager = new UserSearchManager(xmppConnection);
            Form searchForm = userSearchManager.getSearchForm("search." + xmppConnection.getServiceName());
            Form answerForm = searchForm.createAnswerForm();

            answerForm.setAnswer("search", search);
            answerForm.setAnswer("Name", Boolean.TRUE);
            answerForm.setAnswer("Username", Boolean.TRUE);
            answerForm.setAnswer("Email", Boolean.TRUE);

            ReportedData data = userSearchManager.getSearchResults(answerForm, "search." + xmppConnection.getServiceName());
            List<ReportedData.Row> rows = data.getRows();
            List<SearchResult> searchResults = new ArrayList<>();

            for(ReportedData.Row row : rows){
                SearchResult searchResult = new SearchResult();
                for(ReportedData.Column column : data.getColumns()){
                    if(column.getVariable().equalsIgnoreCase("username")){
                        List<String> values = row.getValues(column.getVariable());
                        if(values.size() > 0){
                            searchResult.userName = row.getValues(column.getVariable()).get(0);
                        }
                    }else if(column.getVariable().equalsIgnoreCase("userName")){
                        List<String> values = row.getValues(column.getVariable());
                        if(values.size() > 0){
                            searchResult.nickName = row.getValues(column.getVariable()).get(0);
                        }
                    }else if(column.getVariable().equalsIgnoreCase("email")){
                        List<String> values = row.getValues(column.getVariable());
                        if(values.size() > 0){
                            searchResult.email = row.getValues(column.getVariable()).get(0);
                        }
                    }
                }
                searchResults.add(searchResult);
            }
            return searchResults;
        }  catch (XMPPException e) {
            XmppExceptionHandler.handleXmppExecption(e);
        } catch (SmackException e) {
            XmppExceptionHandler.handleSmackException(e);
        }

        return null;
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
