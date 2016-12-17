package de.filiberry.bathcontrol.model;

public class GuiDataModel {

	private double tempBadezimmer;
	private double tempWintergarten;
	private double tempAussen;
	private int feuchteBad;

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

	public int getFeuchteBad() {
		return feuchteBad;
	}

	public void setFeuchteBad(int feuchteBad) {
		this.feuchteBad = feuchteBad;
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

	private String statusZuluft;
	private String statusAbluft;

}
