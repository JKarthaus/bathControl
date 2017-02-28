package de.filiberry.bathcontrol.worker;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.script.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.filiberry.bathcontrol.model.ActionModel;
import de.filiberry.bathcontrol.model.BathControlContext;

public class BathControlWorker implements Runnable {

	private final static Logger LOGGER = LoggerFactory.getLogger(BathControlWorker.class);
	private final static String MQTT_CLIENT_ID = "bathControlWorker";
	public static final int mqttQOS = 2;
	private boolean stop = false;
	private volatile BathControlContext bathControlContext;
	private GuiConnector guiConnector = new GuiConnector();
	//private Interpreter bshInterpreter = new Interpreter();
	private ScriptEngineManager factory = new ScriptEngineManager();
	private ScriptEngine scriptEngine;
	private MemoryPersistence persistence = new MemoryPersistence();
	private String mqttGPIOActorHost;
	private String mqttGuiHost;
	private String mqttGuiTopic;
	private String mqttGPIOActorZuluftTopic;
	private String mqttGPIOActorAbluftTopic;
	private String ruleScriptFile;
	private MqttClient mqttClient;

	/**
	 * 
	 */
	public BathControlWorker() {
	}

	/**
	 * 
	 */
	public void run() {
		try {
			mqttClient = new MqttClient(mqttGPIOActorHost, MQTT_CLIENT_ID, persistence);
			scriptEngine = factory.getEngineByName("nashorn");
		} catch (MqttException e) {
			LOGGER.error(e.getMessage());
		}

		while (!stop) {
			try {
				Thread.sleep(1000);
				if (checkConfigFile(getRuleScriptFile())) {
					scriptEngine.eval(new FileReader("/home/bathcontrol/test.js"));

					Invocable invocable = (Invocable) scriptEngine;

					Object result = invocable.invokeFunction("checkZuluft");
					
					LOGGER.info((String) result);
					// Set the Script Enviroment
					//bshInterpreter.set("tempWintergarten", bathControlContext.getTempWintergarten());
					//bshInterpreter.set("tempBadezimmer", bathControlContext.getTempBadezimmer());
					//bshInterpreter.set("feuchteBad", bathControlContext.getMoistureBadezimmer());
					// -- Run the Script
					//bshInterpreter.source(getRuleScriptFile());
					// -- Check the Result
					//checkAction(((String) bshInterpreter.get("zuluft")).trim().toUpperCase(),
					//		    ((String) bshInterpreter.get("abluft")).trim().toUpperCase());
				} else {
					LOGGER.error("Rules File:" + getRuleScriptFile() + " not found !");
				}
				guiConnector.updateFrontend(Tools.CastFrontentModel(bathControlContext));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param zuluftSoll
	 * @param abluftSoll
	 */
	private void checkAction(String zuluftSoll, String abluftSoll) {
		ArrayList<ActionModel> pushActions = new ArrayList<ActionModel>();

		if (Tools.isNotInTargetState(bathControlContext.getStatusZuluft(), zuluftSoll)) {
			pushActions.add(new ActionModel(getMqttGPIOActorZuluftTopic(), zuluftSoll));
			bathControlContext.setStatusZuluft(zuluftSoll);
		}
		if (Tools.isNotInTargetState(bathControlContext.getStatusAbluft(), abluftSoll)) {
			pushActions.add(new ActionModel(getMqttGPIOActorAbluftTopic(), abluftSoll));
			bathControlContext.setStatusAbluft(abluftSoll);
		}

		// Check Automatic Off
		if (Tools.isON(bathControlContext.getStatusZuluft())
				&& Tools.isDateReached(bathControlContext.getZuluftOffTime())) {
			pushActions.add(new ActionModel(getMqttGPIOActorZuluftTopic(), ActionModel.ACTION_OFF));
			bathControlContext.setZuluftOffTime(null);
			bathControlContext.setStatusZuluft(ActionModel.ACTION_OFF);
		}
		if (Tools.isON(bathControlContext.getStatusAbluft())
				&& Tools.isDateReached(bathControlContext.getAbluftOffTime())) {
			pushActions.add(new ActionModel(getMqttGPIOActorAbluftTopic(), ActionModel.ACTION_OFF));
			bathControlContext.setAbluftOffTime(null);
			bathControlContext.setStatusAbluft(ActionModel.ACTION_OFF);
		}

		// Send out..
		if (pushActions.size() > 0) {
			pushToGPIOActor(pushActions);
		} else {
			LOGGER.debug("No Actions to send on BUS ?!");
		}
	}

	/**
	 * 
	 * @param zuluft
	 * @param abluft
	 */
	private void pushToGPIOActor(ArrayList<ActionModel> actions) {
		try {
			// Connect Broker
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			LOGGER.info("Connecting to MQTT Broker:" + mqttGPIOActorHost);
			mqttClient.connect(connOpts);
			LOGGER.info("connected");
			// --
			for (int i = 0; i < actions.size(); i++) {
				mqttClient.publish(actions.get(i).getTopic(), actions.get(i).getValue().getBytes(), mqttQOS, true);
				LOGGER.info("State:" + actions.get(i).getValue() + " Send to Topic:" + actions.get(i).getTopic());
			}
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
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean checkConfigFile(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			File check = new File(fileName);
			if (check.exists() && check.canRead()) {
				return true;
			}
		}
		return false;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void setBathControlContext(BathControlContext bathControlContext) {
		this.bathControlContext = bathControlContext;
	}

	public String getMqttGPIOActorHost() {
		return mqttGPIOActorHost;
	}

	public void setMqttGPIOActorHost(String mqttGPIOActorHost) {
		this.mqttGPIOActorHost = mqttGPIOActorHost;
	}

	public String getMqttGPIOActorZuluftTopic() {
		return mqttGPIOActorZuluftTopic;
	}

	public void setMqttGPIOActorZuluftTopic(String mqttGPIOActorZuluftTopic) {
		this.mqttGPIOActorZuluftTopic = mqttGPIOActorZuluftTopic;
	}

	public String getMqttGPIOActorAbluftTopic() {
		return mqttGPIOActorAbluftTopic;
	}

	public void setMqttGPIOActorAbluftTopic(String mqttGPIOActorAbluftTopic) {
		this.mqttGPIOActorAbluftTopic = mqttGPIOActorAbluftTopic;
	}

	public String getRuleScriptFile() {
		return ruleScriptFile;
	}

	public void setRuleScriptFile(String ruleScriptFile) {
		this.ruleScriptFile = ruleScriptFile;
	}

	public String getMqttGuiHost() {
		return mqttGuiHost;
	}

	public void setMqttGuiHost(String mqttGuiHost) {
		this.mqttGuiHost = mqttGuiHost;
		guiConnector.setMqttFrontendHost(mqttGuiHost);
	}

	public String getMqttGuiTopic() {
		return mqttGuiTopic;
	}

	public void setMqttGuiTopic(String mqttGuiTopic) {
		this.mqttGuiTopic = mqttGuiTopic;
		guiConnector.setMqttFrontendTopic(mqttGuiTopic);
	}

}
