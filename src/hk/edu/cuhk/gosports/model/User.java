package hk.edu.cuhk.gosports.model;

import java.io.Serializable;

/**
 * user info
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午8:22:07
 * @version V1.0
 * 
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userServerId = 0;
	private int userId = 0;
	private String username = "";
	private String password = "";
	private int age = 0;
	private int sex = 0;
	private String mailbox = "";
	private int credit = 0;
	private String description = "";
	private boolean isLogin = false;
	
	public int getUserServerId() {
		return userServerId;
	}

	public void setUserServerId(int userServerId) {
		this.userServerId = userServerId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null) {
			this.username = username;
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password != null) {
			this.password = password;
		}
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age > 0) {
			this.age = age;
		}
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		if (sex > -1) {
			this.sex = sex;
		}
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		if (mailbox != null) {
			this.mailbox = mailbox;
		}
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		if (credit > -1) {
			this.credit = credit;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

}
