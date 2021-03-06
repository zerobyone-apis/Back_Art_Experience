package com.art.experience.dev.model.DTO;

import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.User;

public class DTOClientResponse {

	//private String username;
	private Long clientId;
	private Long userId;
	private String name;
	private String username;
	private String cel;
	private boolean status;
	private String email;
	private Long socialNumber;

	public DTOClientResponse() {
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getSocialNumber() {
		return socialNumber;
	}

	public void setSocialNumber(Long socialNumber) {
		this.socialNumber = socialNumber;
	}

	public String getCel() {
		return cel;
	}

	public void setCel(String cel) {
		this.cel = cel;
	}

	public boolean isStatus() {
		return status;
	}
}
