package de.filiberry.bathcontrol.worker;

import java.util.Date;

import de.filiberry.bathcontrol.model.ActionModel;
import de.filiberry.bathcontrol.model.BathControlContext;
import de.filiberry.bathcontrol.model.GuiDataModel;

public class Tools {

	public static GuiDataModel CastFrontentModel(BathControlContext bathControlContext) {
		GuiDataModel result = new GuiDataModel();
		result.setFeuchteBad(bathControlContext.getMoistureBadezimmer());
		result.setStatusAbluft(bathControlContext.getStatusAbluft());
		result.setStatusZuluft(bathControlContext.getStatusZuluft());
		result.setTempBadezimmer(bathControlContext.getTempBadezimmer());
		result.setTempWintergarten(bathControlContext.getTempWintergarten());
		return result;
	}

	/**
	 * 
	 * @param istState
	 * @param sollState
	 * @return
	 */
	public static boolean isInTargetState(String istState, String sollState) {
		if (istState.equalsIgnoreCase(sollState)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param istState
	 * @param sollState
	 * @return
	 */
	public static boolean isNotInTargetState(String istState, String sollState) {
		if (Tools.isInTargetState(istState, sollState)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDateReached(Date date) {
		if (date == null) {
			return false;
		}
		Date now = new Date();
		if (date.getTime() >= now.getTime()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isON(String state) {
		return Tools.isInTargetState(state, ActionModel.ACTION_ON);
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isOFF(String state) {
		return Tools.isInTargetState(state, ActionModel.ACTION_OFF);
	}

}