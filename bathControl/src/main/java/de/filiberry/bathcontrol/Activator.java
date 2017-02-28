/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.filiberry.bathcontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.filiberry.bathcontrol.model.BathControlContext;
import de.filiberry.bathcontrol.worker.BathControlWorker;

public class Activator implements BundleActivator, ManagedService, MqttCallback {

	private static final String BUNDLE_ID = "bathControl";
	private BathControlWorker bathControlWorker = new BathControlWorker();

	private volatile BathControlContext bathControlContext;
	private MqttClient mqttClient = null;
	@SuppressWarnings({ "rawtypes", "unused" })
	private ServiceRegistration serviceReg;
	private final static Logger LOGGER = LoggerFactory.getLogger(Activator.class);
	private String mqttHost;
	private String mqttTopic;
	private String mqttTopicTempWintergarten;
	private String mqttTopicTempBadezimmer;
	private String mqttTopicTempAussen;
	private String mqttTopicMoistureBadezimmer;
	private BundleContext context;

	/**
	 * 
	 */
	public void start(BundleContext context) {
		LOGGER.info("Start Bundle " + BUNDLE_ID);
		mqttHost = "";
		mqttTopic = "";
		this.context = context;
		bathControlContext = new BathControlContext();
		bathControlWorker.setBathControlContext(bathControlContext);
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, BUNDLE_ID);
		serviceReg = context.registerService(ManagedService.class.getName(), this, properties);

	}

	/**
	 * 
	 */
	public void stop(BundleContext context) {
		LOGGER.info("Stop Bundle " + BUNDLE_ID);
		bathControlWorker.setStop(true);
		if (mqttClient != null && mqttClient.isConnected()) {
			try {
				mqttClient.disconnect();
			} catch (MqttException e) {
				LOGGER.error(e.getMessage());
			}
		}
		mqttClient = null;

	}

	/**
	 * 
	 */
	public void connectionLost(Throwable arg0) {
		LOGGER.error("Connection to Broker lost.");
		boolean isConnected = false;
		while (!isConnected) {
			LOGGER.info("Wait a Minute before reconnect...");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				LOGGER.error("Wait Thread was interrupted");
				e1.printStackTrace();
			}
			isConnected = connectToBroker();
		}

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.debug("MQTT Message arrived...");
		String messageData = new String(message.getPayload());
		boolean messageOK = false;
		try {
			// --
			if (StringUtils.contains(topic, mqttTopicTempWintergarten)) {
				bathControlContext.setTempWintergarten(new Double(messageData));
				messageOK = true;
			}
			// --
			if (StringUtils.contains(topic, mqttTopicTempBadezimmer)) {
				bathControlContext.setTempBadezimmer(new Double(messageData));
				messageOK = true;
			}
			// --
			if (StringUtils.contains(topic, mqttTopicMoistureBadezimmer)) {
				bathControlContext.setMoistureBadezimmer(new Double(messageData));
				messageOK = true;
			}
			if (StringUtils.contains(topic, mqttTopicTempAussen)) {
				bathControlContext.setTempAussen(new Double(messageData));
				messageOK = true;
			}

			// --
			if (messageOK) {
				LOGGER.info("Message on Topic " + topic + " Value=" + messageData + " Arrived and set to Context");
			} else {
				LOGGER.info("Unknown Message on Topic=" + topic);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}

	/**
	 * 
	 */
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		LOGGER.debug("Bundle " + BUNDLE_ID + " config set or updated ...");
		mqttHost = (String) properties.get("mqtt.listen.host");
		mqttTopic = (String) properties.get("mqtt.listen.topic");
		mqttTopicMoistureBadezimmer = (String) properties.get("mqtt.topic.moisture.badezimmer");
		mqttTopicTempBadezimmer = (String) properties.get("mqtt.topic.temp.badezimmer");
		mqttTopicTempAussen = (String) properties.get("mqtt.topic.temp.aussen");
		mqttTopicTempWintergarten = (String) properties.get("mqtt.topic.temp.wintergarten");

		// Set the Actor config to the Worker
		bathControlWorker.setMqttGPIOActorHost((String) properties.get("mqtt.publish.actor.host"));
		bathControlWorker.setMqttGPIOActorZuluftTopic((String) properties.get("mqtt.publish.actor.zuluft.topic"));
		bathControlWorker.setMqttGPIOActorAbluftTopic((String) properties.get("mqtt.publish.actor.abluft.topic"));

		// Set the GUI config to the Worker
		bathControlWorker.setMqttGuiHost((String) properties.get("mqtt.publish.gui.host"));
		bathControlWorker.setMqttGuiTopic((String) properties.get("mqtt.publish.gui.topic"));

		String scriptFile = FileUtils.getUserDirectoryPath() + "/bathControl.js";
		if (!BathControlWorker.checkConfigFile(scriptFile)) {
			try {
				IOUtils.copy(context.getBundle().getResource("/bathControl.js").openStream(),
						new FileOutputStream(new File(scriptFile)));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		bathControlWorker.setRuleScriptFile(scriptFile);
		new Thread(bathControlWorker).start();
		connectToBroker();
	}

	/**
	 * 
	 */
	private boolean connectToBroker() {
		try {
			LOGGER.info("Try to connect to:" + mqttHost + " listenOnTopic : " + mqttTopic);
			if (mqttClient == null) {
				mqttClient = new MqttClient(mqttHost, "bathControlKarafBundle");
			}
			if (!mqttClient.isConnected()) {
				mqttClient.connect();
			}
			mqttClient.subscribe(mqttTopic);
			mqttClient.setCallback(this);
			LOGGER.info(mqttHost + " connected !");
			// --

		} catch (MqttException e) {
			mqttClient = null;
			LOGGER.error(e.getMessage());
			return false;
		}
		return true;

	}

}