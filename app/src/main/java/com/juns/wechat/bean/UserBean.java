package com.juns.wechat.bean;

import android.database.Cursor;

import com.juns.wechat.database.UserTable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

@Table(name = "wcuser")
public class UserBean {
	public static final String ID = "id";
	public static final String USERNAME = "userName";
    public static final String NICKNAME = "nickName";
	public static final String PASSWORD = "passWord";
	public static final String HEADURL = "headUrl";
	public static final String SIGNATURE = "signature";
	public static final String SEX = "sex";
	public static final String LOCATION = "location";
	public static final String BIRTHDAY = "birthday";
	public static final String TYPE ="type";
	public static final String TELEPHONE = "telephone";

    @Column(name = "id", isId = true)
    public int id;//
    @Column(name = "userName")
    public String userName;// 用户名
    @Column(name = "userName")
    public String nickName; //昵称
    @Column(name = "passWord")
    public String passWord;// 头像保存路径
    @Column(name = "telephone")
    public String telephone;// 手机号
    @Column(name = "headUrl")
    public String headUrl;// 头像保存路径
    @Column(name = "signature")
    public String signature;// 个性签名
    @Column(name = "sex")
    public String sex;// 性别: M男士，W女士
    @Column(name = "location")
    public String location;// 位置信息
    @Column(name = "birthday")
    public String birthday;// 生日
    @Column(name = "type")
    public String type;// N-正常用户，P-公众账号
    @Column(name = "createDate")
    public Date createDate;
    @Column(name = "modifyDate")
    public Date modifyDate;

    public UserBean(){

    }

    public UserBean(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_ID));
        userName = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_NAME));
        passWord = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
        telephone = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_TELEPHONE));
        headUrl = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_HEAD_URL));
        signature = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_SIGNATURE));
        sex = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_SEX));
        location = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_LOCATION));
        birthday = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_BIRTHDAY));
        type = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_TYPE));
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTelephone() {
		return telephone;
	}

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
