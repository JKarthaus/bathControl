package de.filiberry.bathcontrol.model;

import java.util.Date;

public class BathControlContext {

	public static final String STATUS_ON = "ON";
	public static final String STATUS_OFF = "OFF";
	public static final String ID = "bathControlContext";

	private double tempWintergarten;
	private double tempBadezimmer;
	private double moistureBadezimmer;

	private Date zuluftOffTime;
	private Date abluftOffTime;

	private String statusZuluft;
	private String statusAbluft;

	public double getTempWintergarten() {
		return tempWintergarten;
	}

	public void setTempWintergarten(double tempWintergarten) {
		this.tempWintergarten = tempWintergarten;
	}

	public double getTempBadezimmer() {
		return tempBadezimmer;
	}

	public void setTempBadezimmer(double tempBadezimmer) {
		this.tempBadezimmer = tempBadezimmer;
	}

	public double getMoistureBadezimmer() {
		return moistureBadezimmer;
	}

	public void setMoistureBadezimmer(double moistureBadezimmer) {
		this.moistureBadezimmer = moistureBadezimmer;
	}

	public String getStatusZuluft() {
		return statusZuluft;
	}

	public void setStatusZuluft(String statusZuluft) {
		this.statusZuluft = statusZuluft;
	}

	public String getStatusAbluft() {
		return statusAbluft;
	}

	public void setStatusAbluft(String statusAbluft) {
		this.statusAbluft = statusAbluft;
	}

	public Date getZuluftOffTime() {
		return zuluftOffTime;
	}

	public void setZuluftOffTime(Date zuluftOffTime) {
		this.zuluftOffTime = zuluftOffTime;
	}

	public Date getAbluftOffTime() {
		return abluftOffTime;
	}

	public void setAbluftOffTime(Date abluftOffTime) {
		this.abluftOffTime = abluftOffTime;
	}

}
