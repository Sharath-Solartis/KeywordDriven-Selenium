package com.solartis.selenium.macroPackage;

import java.util.LinkedHashMap;

import com.selenium.Configuration.PropertiesHandle;
import com.selenium.exception.DatabaseException;
import com.selenium.exception.MacroException;
import com.selenium.exception.POIException;

public interface MacroInterface 
{
	public void LoadSampleRatingmodel(PropertiesHandle configFile,LinkedHashMap<String, String> inputData) throws MacroException;
	public void GenerateExpected(LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws MacroException;
	public void PumpinData(LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws DatabaseException, POIException, MacroException;
	public void PumpoutData(LinkedHashMap<String, String> outputData,LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws POIException, MacroException, DatabaseException;
	
}
