package com.art.experience.dev.model;

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

	private User user;
	private Client client;
	private Barber barber;

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

	public User getUser() {
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

	public void setUser(User user) {
		this.user = user;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Barber getBarber() {
		return barber;
	}

	public void setBarber(Barber barber) {
		this.barber = barber;
	}
}
