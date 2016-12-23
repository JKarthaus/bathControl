package de.filiberry.bathcontrol.worker;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import de.filiberry.bathcontrol.model.GuiDataModel;

public class GuiConnector {

	private final static Logger LOGGER = LoggerFactory.getLogger(BathControlWorker.class);
	private final static String MQTT_CLIENT_ID = "bathControlGuiConnector";
	public static final int mqttQOS = 2;
	private MemoryPersistence persistence = new MemoryPersistence();
	private String mqttFrontendHost;
	private String mqttFrontendTopic;
	private MqttClient mqttClient;
	private Gson gson = new Gson();

	private int activeHash = 0;

	public void updateFrontend(GuiDataModel guiDataModel) {

		if (guiDataModel.getHash() != activeHash) {
			activeHash = guiDataModel.getHash();
			try {
				// Connect Broker
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				LOGGER.info("Connecting to MQTT Broker:" + mqttFrontendHost);
				mqttClient.connect(connOpts);
				LOGGER.info("connected");
				// --
				mqttClient.publish(mqttFrontendTopic, gson.toJson(guiDataModel).getBytes(), mqttQOS, true);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			} finally {
				if (mqttClient != null && mqttClient.isConnected()) {
					try {
						LOGGER.info("Disconnected");
						mqttClient.disconnect();
					} catch (MqttException e) {
						LOGGER.error(e.getMessage());
					}
				}
			}
		} else {
			LOGGER.debug("NO GUI update neccessary");
		}

	}

	public String getMqttFrontendHost() {
		return mqttFrontendHost;
	}

	public void setMqttFrontendHost(String mqttFrontendHost) {
		this.mqttFrontendHost = mqttFrontendHost;
		try {
			mqttClient = new MqttClient(mqttFrontendHost, MQTT_CLIENT_ID, persistence);
		} catch (MqttException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String getMqttFrontendTopic() {
		return mqttFrontendTopic;
	}

	public void setMqttFrontendTopic(String mqttFrontendTopic) {
		this.mqttFrontendTopic = mqttFrontendTopic;
	}

}
