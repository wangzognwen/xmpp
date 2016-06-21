package com.juns.wechat.bean;

import android.database.Cursor;

import com.juns.wechat.database.UserTable;

public class UserBean {
	public static final String ID = "id";
	public static final String USERNAME = "userName";
	public static final String PASSWORD = "passWord";
	public static final String HEADURL = "headUrl";
	public static final String SIGNATURE = "signature";
	public static final String SEX = "sex";
	public static final String LOCATION = "location";
	public static final String BIRTHDAY = "birthday";
	public static final String TYPE ="type";
	public static final String TELEPHONE = "telephone";

    public int id;//
    public String userName;// 用户名
    public String password;// 头像保存路径
    public String telephone;// 手机号
    public String headUrl;// 头像保存路径
    public String signature;// 个性签名
    public String sex;// 性别: M男士，W女士
    public String location;// 位置信息
    public String birthday;// 生日
    public String type;// N-正常用户，P-公众账号

    public UserBean(){

    }

    public UserBean(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_ID));
        userName = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_NAME));
        password = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
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

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
