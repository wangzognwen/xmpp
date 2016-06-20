package com.juns.wechat.xmpp;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.RosterEntry;

import java.io.IOException;
import java.util.Set;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：xmpp功能的抽象接口
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public interface XmppManager {
    boolean connect();
    void login(String accountName, String passWord) throws IOException, XMPPException, SmackException;
    void regNewUser(String accountName, String passWord) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException;
    Set<RosterEntry> getRoster(String userName);
    void disConnect();
    void shutDownConn();
    boolean isConnected();
    boolean sendPacket(Stanza packet);
    boolean sendMessage(MessageEntity messageEntity);
    boolean isFriends(int otherUserId);
    boolean addFriend(int otherUserId, String nickName);
    void searchUser(String name) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException;
}
