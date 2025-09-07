package hnz.mitra.siteware.omie.models;

import com.google.gson.annotations.SerializedName;

public class AccessParams {

	@SerializedName(value = "TOKEN")
	private String token;
	@SerializedName(value = "URL")
	private String url;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
