package bathControl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

import de.filiberry.bathcontrol.model.GuiDataModel;

public class TestGuiModel {

	@Test
	public void test() {

		GuiDataModel guiDataModel = new GuiDataModel();
		guiDataModel.setFeuchteBad(80);
		guiDataModel.setStatusAbluft("OFF");
		guiDataModel.setStatusZuluft("ON");
		guiDataModel.setTempAussen(5.6);
		guiDataModel.setTempBadezimmer(22.3);
		guiDataModel.setTempWintergarten(29.2);

		Gson gson = new Gson();

		// 2. Java object to JSON, and assign to a String
		String jsonInString = gson.toJson(guiDataModel);

		System.out.println(jsonInString);

		fail("Not yet implemented");
	}

}
