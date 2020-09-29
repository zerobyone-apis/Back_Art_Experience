package com.art.experience.dev.model;

import com.art.experience.dev.model.DTO.DTOBarberResponse;
import com.art.experience.dev.model.DTO.DTOClientResponse;
import com.art.experience.dev.model.DTO.DTOUserResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.PrimitiveIterator;

public class DTOUserLogin {

	//private String username;
	private String email;
	private Long socialNumber;
	private String password;

	private DTOUserResponse user;
	private DTOClientResponse client;
	private DTOBarberResponse barber;

	DTOUserLogin() {
	}

	public Long getSocialNumber() {
		return socialNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public DTOUserResponse getUser() {
		return user;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSocialNumber(Long socialNumber) {
		this.socialNumber = socialNumber;
	}

	public void setUser(DTOUserResponse user) {
		this.user = user;
	}

	public DTOClientResponse getClient() {
		return client;
	}

	public void setClient(DTOClientResponse client) {
		this.client = client;
	}

	public DTOBarberResponse getBarber() {
		return barber;
	}

	public void setBarber(DTOBarberResponse barber) {
		this.barber = barber;
	}
}
