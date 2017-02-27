// -------------------------------------------------------------------------------------------
//                                        INITIALISIERUNGEN
var debug = false;

// -------------------------------------------------------------------------------------------
var RULE_TEMP = function(tempA, tempB) {
	var result = false;
	var minimaleTempDiff = 5;

	if (tempA + minimaleTempDiff > tempB) {
		result = true;
	}
	if (tempA < 22) {
		result = false;
		if (debug) {
			print("Minimaltemperatur von 22 unterschritten " + tempA);
		}
	}
	if (tempB > 26) {
		result = false;
		if (debug) {
			print("Maximaltemperatur 26 ueberschritten " + tempB);
		}
	}
	return result;
}
// -------------------------------------------------------------------------------------------
var RULE_TIME = function() {
	var cal = new Date();
	var result = false;
	var minHour = 7;
	var maxHour = 23;

	if (cal.getHours() > minHour && cal.getHours() < maxHour) {
		if (debug) {
			print("Innerhalb von Zeitbereich");
		}
		return true;
	} else {
		if (debug) {
			print("Aktuelle Stunde:" + cal.getHours());
			print("Ausserhalb gueltigen Zeitbereiches von " + minHour + " bis "
					+ maxHour);
		}
	}
	return result;
}
// -------------------------------------------------------------------------------------------
var RULE_MOISTURE = function(feuchte) {
	var feuchteDurchDuschen = 90;
	var result = false;
	if (feuchte >= feuchteDurchDuschen) {
		result = true;
	} else {
		if (debug) {
			print("Feuchte durch Duschen nicht erreicht.");
		}
	}
	return result;
}
// -------------------------------------------------------------------------------------------
var zuluft = function(tempWintergarten, tempBadezimmer) {
	if (RULE_TEMP(tempWintergarten, tempBadezimmer) && RULE_TIME()) {
		return "ON";
	}
	return "OFF";
}
// -------------------------------------------------------------------------------------------
var abluft = function(feuchteBad) {
	if (RULE_MOISTURE(feuchteBad) && RULE_TIME()) {
		return "ON";
	}
	return "OFF";
}

// #################### TESTS #################################
if (debug) {
	print("--TESTS ab Zeile 68---------------");
	print("----------------------------------");
	print("Ergebnis:Zuluft= " + zuluft(23, 25));
	print("Ergebnis:Abluft= " + abluft(89));
}