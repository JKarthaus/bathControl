package de.filiberry.bathcontrol.model;

public class ActionModel {

	public static final String ACTION_ON = "ON";
	public static final String ACTION_OFF = "OFF";

	private String topic;
	private String value;

	public ActionModel(String topic, String value) {
		this.topic = topic;
		this.value = value;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
