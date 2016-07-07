package com.juns.wechat.bean;

import com.juns.wechat.dao.UserDao;
import com.juns.wechat.database.RosterTable;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by 王宗文 on 2016/6/20.
 */

@Table(name = RosterTable.TABLE_NAME, onCreated = RosterTable.CREATE_INDEX)
public class RosterBean {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "ownerName")
    private String ownerName;
    @Column(name = "contactName")
    private String contactName;
    @Column(name = "subType")
    private String subType;
    @Column(name = "remark")
    private String remark;

    public RosterBean(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UserBean getContactUser(DbManager db) {
        try {
            return db.selector(UserBean.class).where("userName", "=", contactName).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }
}
