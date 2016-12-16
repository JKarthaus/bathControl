package de.filiberry.bathcontrol.model;

import java.util.Date;

public class StatusModel {

	private String statusIst;
	private String statusSoll;

	private Date statusIstDatum;
	private Date statusSollDatum;

	public String getStatusIst() {
		return statusIst;
	}

	public void setStatusIst(String statusIst) {
		this.statusIst = statusIst;
	}

	public String getStatusSoll() {
		return statusSoll;
	}

	public void setStatusSoll(String statusSoll) {
		this.statusSoll = statusSoll;
	}

	public Date getStatusIstDatum() {
		return statusIstDatum;
	}

	public void setStatusIstDatum(Date statusIstDatum) {
		this.statusIstDatum = statusIstDatum;
	}

	public Date getStatusSollDatum() {
		return statusSollDatum;
	}

	public void setStatusSollDatum(Date statusSollDatum) {
		this.statusSollDatum = statusSollDatum;
	}

}
