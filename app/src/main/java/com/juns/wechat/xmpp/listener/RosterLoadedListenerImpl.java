package com.juns.wechat.xmpp.listener;

import com.juns.wechat.bean.RosterBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.dao.RosterDao;
import com.juns.wechat.manager.UserManager;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterLoadedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 王宗文 on 2016/6/14.
 */
public class RosterLoadedListenerImpl implements RosterLoadedListener {

    private RosterDao rosterDao = RosterDao.getInstance();

    @Override
    public void onRosterLoaded(Roster roster) {
        Set<RosterEntry> rosterEntrySet = roster.getEntries();
        if(rosterEntrySet == null || rosterEntrySet.isEmpty()) return;
        List<RosterBean> rosterBeans = new ArrayList<>();
        for(RosterEntry rosterEntry : rosterEntrySet){
            RosterBean rosterBean = new RosterBean();
            rosterBean.setOwnerName(UserManager.getInstance().getUser().getUserName());
            rosterBean.setContactName(ConfigUtil.getUserName(rosterEntry.getUser()));
            rosterBean.setSubType(rosterEntry.getType().toString());
            rosterBean.setRemark(rosterEntry.getName());
            rosterBeans.add(rosterBean);
        }
        rosterDao.replace(rosterBeans);
    }
}
