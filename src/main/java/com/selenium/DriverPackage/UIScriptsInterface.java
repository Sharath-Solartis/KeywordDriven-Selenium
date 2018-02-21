package com.selenium.DriverPackage;

import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.openqa.selenium.WebDriver;

import com.selenium.exception.DatabaseException;

public interface UIScriptsInterface
{
	public WebDriver launchBrowser() throws MalformedURLException;
	public void login(LinkedHashMap<String, String> InputData,LinkedHashMap<String, String> outputData) throws SQLException, IOException, InterruptedException,AWTException;
	public void executeTestScript(LinkedHashMap<String, String> InputData,LinkedHashMap<String, String> outputData) throws SQLException, IOException, InterruptedException, AWTException,DatabaseException;
	public void closeBrowser();
}
