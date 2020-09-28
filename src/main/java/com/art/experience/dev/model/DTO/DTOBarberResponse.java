package com.art.experience.dev.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DTOBarberResponse {

	// barber info;
	private Long barberId;
	private String name;
	private String localName;
	private Double prestige;
	private Boolean isAdmin;
	private String barberDescription;
	private String facebook;
	private String instagram;
	private String urlProfileImage;

	// User Attributes
	private Long userId;
	//private Long socialNumber;
	private String username;
	private String email;
	private Boolean isBarber;

	public DTOBarberResponse(){
	}

	public Long getBarberId() {
		return barberId;
	}

	public void setBarberId(Long barberId) {
		this.barberId = barberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public Double getPrestige() {
		return prestige;
	}

	public void setPrestige(Double prestige) {
		this.prestige = prestige;
	}

	public Boolean getAdmin() {
		return isAdmin;
	}

	public void setAdmin(Boolean admin) {
		isAdmin = admin;
	}

	public String getBarberDescription() {
		return barberDescription;
	}

	public void setBarberDescription(String barberDescription) {
		this.barberDescription = barberDescription;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getUrlProfileImage() {
		return urlProfileImage;
	}

	public void setUrlProfileImage(String urlProfileImage) {
		this.urlProfileImage = urlProfileImage;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	//public Long getSocialNumber() {
	//	return socialNumber;
	//}

	//public void setSocialNumber(Long socialNumber) {
	//	this.socialNumber = socialNumber;
	//}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getBarber() {
		return isBarber;
	}

	public void setBarber(Boolean barber) {
		isBarber = barber;
	}
}
