package com.art.experience.dev.model.DTO;

import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.User;

public class DTOUserResponse {

	//private String username;
	private String email;
	private String username;
	private Long socialNumber;
	private Boolean status;
	private Boolean isBarber;

	public DTOUserResponse() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getSocialNumber() {
		return socialNumber;
	}

	public void setSocialNumber(Long socialNumber) {
		this.socialNumber = socialNumber;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getBarber() {
		return isBarber;
	}

	public void setBarber(Boolean barber) {
		isBarber = barber;
	}
}
