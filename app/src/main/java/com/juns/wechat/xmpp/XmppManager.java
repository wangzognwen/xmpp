package com.juns.wechat.xmpp;

import com.juns.wechat.xmpp.bean.SearchResult;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.RosterEntry;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：xmpp功能的抽象接口
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public interface XmppManager {
    boolean connect() throws IOException, XMPPException, SmackException;
    boolean login(String accountName, String passWord);
    void regNewUser(String accountName, String passWord);
    Set<RosterEntry> getRoster(String userName);
    void disConnect();
    void shutDownConn();
    boolean isConnected();
    boolean sendPacket(Stanza packet);
    boolean sendMessage(MessageEntity messageEntity);
    boolean isFriends(int otherUserId);
    boolean addFriend(int otherUserId, String nickName);
    List<SearchResult> searchUser(String name);
}
