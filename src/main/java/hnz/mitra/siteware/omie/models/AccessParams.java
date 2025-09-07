package hnz.mitra.siteware.omie.models;

import com.google.gson.annotations.SerializedName;

public class AccessParams {

	@SerializedName(value = "USENAME")
	private String userName;
	@SerializedName(value = "URL")
	private String url;
	@SerializedName(value = "PASSWORD")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
