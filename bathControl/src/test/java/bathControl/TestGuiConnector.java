package bathControl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.filiberry.bathcontrol.model.ActionModel;
import de.filiberry.bathcontrol.model.GuiDataModel;
import de.filiberry.bathcontrol.worker.GuiConnector;

public class TestGuiConnector {

	@Test
	public void testGuiConnector() {
		try {
			GuiConnector guiConnector = new GuiConnector();
			GuiDataModel guiDataModel = new GuiDataModel();

			guiDataModel.setFeuchteBad(80.5);
			guiDataModel.setStatusAbluft(ActionModel.ACTION_OFF);
			guiDataModel.setStatusZuluft(ActionModel.ACTION_ON);
			guiDataModel.setTempAussen(5.5);
			guiDataModel.setTempBadezimmer(22.1);
			guiDataModel.setTempWintergarten(28.1);

			guiConnector.setMqttFrontendHost("tcp://bathcontrol:1883");
			guiConnector.setMqttFrontendTopic("bathcontrol/gui");
			guiConnector.updateFrontend(guiDataModel);

			Thread.sleep(1000);
			guiConnector.updateFrontend(guiDataModel);
			System.out.println("Send Data to Broker");
			Thread.sleep(1000);
			guiConnector.updateFrontend(guiDataModel);
			System.out.println("Same Data not send");
			Thread.sleep(1000);
			guiDataModel.setTempBadezimmer(18.5);
			guiConnector.updateFrontend(guiDataModel);
			System.out.println("Send new Temp:" + guiDataModel.getTempBadezimmer());
			Thread.sleep(1000);
			guiDataModel.setTempBadezimmer(19.5);
			guiDataModel.setStatusZuluft(ActionModel.ACTION_OFF);
			guiConnector.updateFrontend(guiDataModel);
			System.out.println("Send new Temp:" + guiDataModel.getTempBadezimmer());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
