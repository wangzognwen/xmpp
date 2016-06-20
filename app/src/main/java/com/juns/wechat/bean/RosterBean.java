package com.juns.wechat.bean;

import android.database.Cursor;

import com.juns.wechat.dao.UserDao;
import com.juns.wechat.database.RosterTable;

/**
 * Created by 王宗文 on 2016/6/20.
 */


public class RosterBean {
    private int rosterId;
    private String ownerName;
    private String contactName;
    private UserBean contactUser;
    private String subType;
    private String remark;

    public RosterBean(){

    }

    public RosterBean(Cursor cursor){
        rosterId = cursor.getInt(cursor.getColumnIndex(RosterTable.COLUMN_ID));
        ownerName = cursor.getString(cursor.getColumnIndex(RosterTable.COLUMN_OWNER_NAME));
        contactName = cursor.getString(cursor.getColumnIndex(RosterTable.COLUMN_CONTACT_NAME));
        subType = cursor.getString(cursor.getColumnIndex(RosterTable.COLUMN_SUB_TYPE));
        remark = cursor.getString(cursor.getColumnIndex(RosterTable.COLUMN_REMARK));
    }

    public int getRosterId() {
        return rosterId;
    }

    public void setRosterId(int rosterId) {
        this.rosterId = rosterId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public UserBean getContactUser() {
        if(contactUser == null){
            contactUser = UserDao.getInstance().getUserByName(contactName);
        }
        return contactUser;
    }

    public void setContactUser(UserBean contactUser) {
        this.contactUser = contactUser;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
