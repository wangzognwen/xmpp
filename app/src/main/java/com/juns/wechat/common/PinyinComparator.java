package com.juns.wechat.common;

import java.util.Comparator;

import com.juns.wechat.bean.UserBean;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// 按照名字排序
		UserBean user0 = (UserBean) arg0;
		UserBean user1 = (UserBean) arg1;
		String catalog0 = "";
		String catalog1 = "";

		if (user0 != null && user0.getUserName() != null
				&& user0.getUserName().length() > 1)
			catalog0 = PingYinUtil.converterToFirstSpell(user0.getUserName())
					.substring(0, 1);

		if (user1 != null && user1.getUserName() != null
				&& user1.getUserName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(user1.getUserName())
					.substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
