package de.filiberry.bathcontrol.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GuiDataModel {

	private String statusZuluft;
	private String statusAbluft;
	private double tempBadezimmer;
	private double tempWintergarten;
	private double tempAussen;
	private double feuchteBad;

	public double getTempBadezimmer() {
		return tempBadezimmer;
	}

	public void setTempBadezimmer(double tempBadezimmer) {
		this.tempBadezimmer = tempBadezimmer;
	}

	public double getTempWintergarten() {
		return tempWintergarten;
	}

	public void setTempWintergarten(double tempWintergarten) {
		this.tempWintergarten = tempWintergarten;
	}

	public double getTempAussen() {
		return tempAussen;
	}

	public void setTempAussen(double tempAussen) {
		this.tempAussen = tempAussen;
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

	public int getHash() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public double getFeuchteBad() {
		return feuchteBad;
	}

	public void setFeuchteBad(double feuchteBad) {
		this.feuchteBad = feuchteBad;
	}

}
