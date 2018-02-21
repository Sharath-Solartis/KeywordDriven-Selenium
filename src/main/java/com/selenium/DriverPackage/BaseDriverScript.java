package com.selenium.DriverPackage;

import com.selenium.Configuration.PropertiesHandle;
import com.selenium.SupportingClasses.ConditionsChecking;
import com.selenium.SupportingClasses.DatabaseOperation;
import com.selenium.SupportingClasses.ExcelOperationsJXL;
import com.selenium.SupportingClasses.TheEventListener;
import com.selenium.SupportingClasses.UIoperartions;
import com.selenium.exception.DatabaseException;
import com.selenium.exception.MacroException;
import com.selenium.exception.POIException;
import com.solartis.selenium.macroPackage.MacroInterface;

import java.awt.AWTException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

public class BaseDriverScript extends UIoperartions implements UIScriptsInterface
{
//***************************global objects and variables****************************************	
	protected String dbColumnNmae=null;
	protected ExcelOperationsJXL objectTestScript=null;
	protected ExcelOperationsJXL objectLoginScript=null;
	protected ExcelOperationsJXL objectComparisonScript=null;
	protected PropertiesHandle configFile;
	protected static TheEventListener event;
	protected ArrayList<String> errorParentname = new ArrayList<String>();
	protected ArrayList<String> errorMessage=new ArrayList<String>();
	protected ConditionsChecking Conditonchecking = null;
	public static MacroInterface macro = null;
	public static DatabaseOperation TestScript;
//==========================================================================================================================================================
	
	public BaseDriverScript()
	{
		
	}
	
//===========constructor to initialize objects======================================================================================================================
public BaseDriverScript(PropertiesHandle configFile) throws SQLException, ClassNotFoundException
{
	this.configFile = configFile;
	event=new TheEventListener();
	System.out.println(this.configFile.getProperty("TestScriptPath"));
	objectLoginScript = new ExcelOperationsJXL(this.configFile.getProperty("TestScriptPath"));
	objectLoginScript.getsheets(this.configFile.getProperty("loginSheetName"));
	objectTestScript = new ExcelOperationsJXL(this.configFile.getProperty("TestScriptPath"));
	objectTestScript.getsheets(this.configFile.getProperty("ScriptSheetName"));
	//objectComparisonScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("ScriptFileName"));
	//objectComparisonScript.getsheets(this.configFile.getProperty("ComparisonSheetName"));
	
}
//====================================Function to launch browser====================================================================================================
public WebDriver launchBrowser() throws MalformedURLException
{
	    String browser = configFile.getProperty("browser");
		return this.launch_browser(browser,configFile.getProperty("server"));
			
}
//==============================================Function to login===================================================================================================
  public void login(LinkedHashMap<String, String> InputData,LinkedHashMap<String, String> outputData) throws SQLException, IOException, InterruptedException, AWTException
  {
	  objectLoginScript.set_rownumber(1);
	  while(objectLoginScript.has_next_row())
		{
			if(objectLoginScript.read_data(objectLoginScript.get_rownumber(),8).toString().equals("enabled"))
			{
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectLoginScript.read_data(objectLoginScript.get_rownumber(),2);
				String ObjectType = objectLoginScript.read_data(objectLoginScript.get_rownumber(),3);
				String PropertyString= objectLoginScript.read_data(objectLoginScript.get_rownumber(),4);
				String dbcolumnNmae = objectLoginScript.read_data(objectLoginScript.get_rownumber(),5);
				String value = objectLoginScript.read_data(objectLoginScript.get_rownumber(),6);
				String dataProvidingFlag=objectLoginScript.read_data(objectLoginScript.get_rownumber(),9);
				String  waitingTime=objectLoginScript.read_data(objectLoginScript.get_rownumber(),10);
				//System.out.println(fieldName);
				this.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,InputData,outputData,waitingTime);
				
			}
			objectLoginScript.next_row();
		}	  
  }
 //=============================================Function to run the test script========================================================================================  
public void executeTestScript(LinkedHashMap<String, String> InputData,LinkedHashMap<String, String> outputData) throws SQLException, IOException, InterruptedException, AWTException, DatabaseException
{
	objectTestScript.set_rownumber(1);
	while(objectTestScript.has_next_row())
	{
		String conditions=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
		if(objectTestScript.read_data(objectTestScript.get_rownumber(),8).toString().equals("enabled")&& this.ConditionReading(conditions, InputData))
		{	
			   //System.out.println("condition true for "+objectTestScript.read_data(objectTestScript.get_rownumber(),1));
				//String pageName = objectTestScript.read_data(objectTestScript.get_rownumber(),0);
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectTestScript.read_data(objectTestScript.get_rownumber(),2);
				String ObjectType = objectTestScript.read_data(objectTestScript.get_rownumber(),3);
				String PropertyString= objectTestScript.read_data(objectTestScript.get_rownumber(),4);
				String dbcolumnNmae = objectTestScript.read_data(objectTestScript.get_rownumber(),5);
				String value = objectTestScript.read_data(objectTestScript.get_rownumber(),6);
			    //String condtions1=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=objectTestScript.read_data(objectTestScript.get_rownumber(),9);
				String  waitingTime=objectTestScript.read_data(objectTestScript.get_rownumber(),10);
				//System.out.println(fieldName);
				this.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,InputData,outputData,waitingTime);
		}
		objectTestScript.next_row();
	} //end of while 
}
//==================================Function to compare  the results========================================================================================================
/*public void comparisonScript(DatabaseOperation objectInput,DatabaseOperation objectOutput) throws SQLException
{
	objectComparisonScript.set_rownumber(1);
	    while(objectComparisonScript.has_next_row())
		{
	    	String conditions=objectComparisonScript.read_data(objectComparisonScript.get_rownumber(),4);
			if(objectComparisonScript.read_data(objectComparisonScript.get_rownumber(),3).toString().equals("Y")&& this.ConditionReading(conditions, objectInput))
			{
				String expectedcolumn = objectComparisonScript.read_data(objectComparisonScript.get_rownumber(),0);
				String actualColumn = objectComparisonScript.read_data(objectComparisonScript.get_rownumber(),1);
				String StatusColumn = objectComparisonScript.read_data(objectComparisonScript.get_rownumber(),2);
				String expectedValue = (objectOutput.read_data(expectedcolumn)).replace("$","").replace(",","");
				String actualValue = (objectOutput.read_data(actualColumn)).replace("$","").replace(",","");
				
				if(expectedValue.equals(actualValue))
		        {
					objectOutput.write_data(StatusColumn,"Pass");
		        }
		        else
		        {
		        	objectOutput.write_data(StatusColumn, "Fail");
		        }
			}
			objectComparisonScript.next_row();
		}	
 }*/

//============================================Function to close the browser===============================================================================
public void generatExpectedResult(LinkedHashMap<String, String> inputrow,LinkedHashMap<String, String> outputrow ) throws ClassNotFoundException, MacroException, DatabaseException, POIException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
{
	//System.out.println(configFile.getProperty("comparison")+"-----------"+this.Project+"Macro");
	
		  String classname = "com.solartis.selenium.macroPackage."+configFile.getProperty("MacroClassName");
		  Class<?> classobj=Class.forName(classname);
		  Constructor<?> cons = classobj.getConstructor(PropertiesHandle.class);
		  macro = (MacroInterface) cons.newInstance(configFile);
		  macro.LoadSampleRatingmodel(configFile, inputrow);
		  macro.GenerateExpected(inputrow, configFile);
		  macro.PumpinData(inputrow, configFile);
		  macro.PumpoutData(outputrow, inputrow, configFile);
	
}


public void CompareExpectedWithActual(LinkedHashMap<String, String> outputrow) throws SQLException, DatabaseException
{
	DatabaseOperation objectcomparison=new DatabaseOperation();
	LinkedHashMap<Integer, LinkedHashMap<String, String>> comparisontable=objectcomparison.GetDataObjects(configFile.getProperty("comparisonTableQuery"));//"select * from ComparisionCondition"
	
		for (Entry<Integer, LinkedHashMap<String, String>> entry : comparisontable.entrySet()) 	
		{
			LinkedHashMap<String, String> comparisonrow = entry.getValue();
			if (comparisonrow.get("Comaparision_Flag").equals("Y"))
			{
				System.out.println("coming to comparison");
			   outputrow=this.CompareFunction(outputrow,comparisonrow);
			}
		}
		   //outputrow.updateRow();
    	
}

public void closeBrowser()
{
	this.stop_browser();
}


//==================================Function to compare  the results========================================================================================================
public LinkedHashMap<String, String> CompareFunction(LinkedHashMap<String, String> outputrow,LinkedHashMap<String, String> comparisonrow)  
{		
  try
  {
	  ConditionsChecking Conditonchecking = new ConditionsChecking();
  	System.out.println("coming to Before CompareFunction");
  	if(comparisonrow.get("Comaparision_Flag").equalsIgnoreCase("Y"))
		{
	    	System.out.println("coming to CompareFunction");
			String ExpectedColumn = comparisonrow.get("ExpectedColumn");
			String ActualColumn = comparisonrow.get("OutputColumn");
			String StatusColumn = comparisonrow.get("StatusColumn");
			if(!(StatusColumn.equals("")) && !(ExpectedColumn.equals("")))
			{
				System.out.println(ExpectedColumn+"---------------"+ActualColumn);
				System.out.println(outputrow.get(ExpectedColumn)+"--------------"+outputrow.get(ActualColumn));
				if(premium_comp(outputrow.get(ExpectedColumn),outputrow.get(ActualColumn)))
				{
					System.out.println("coming to PASS");
					outputrow.put(StatusColumn, "Pass");
				}
				else
				{
					System.out.println("coming to FAIL");
					outputrow.put(StatusColumn, "Fail");
					//outputrow.UpdateRow();
					analyse(comparisonrow,outputrow);
				}
			}
		}	    
				 			
		String message = "";
		for(int i=0;i<errorMessage.size();i++)
		{
			message=message+errorMessage.get(i)+"; ";
		}
		outputrow.put("AnalyserResult", message);
		errorMessage.clear();
		errorParentname.clear();
		return outputrow;

  }	
  catch(DatabaseException e)
  {
  	System.out.println(e);
  }
	return outputrow;
}

//======================================PRIVATE FUNCTION FOR SUPPORTING COMPARISON FUNCTION=======================================	
protected static boolean premium_comp(String expected,String actual)
{
	
	boolean status = false;
	if(actual == null||actual.equals(""))
	{
		if((expected == null || expected.equals("")||expected.equals("0") || expected.equals("0.0")))
		{
			status = true;
		}
	}
	if(expected == null||expected.equals(""))
	{
		if(actual == null|| actual.equals("")||actual.equals("0") || actual.equals("0.0"))
		{
			status = true;
		}
	}
	if(actual!=null && expected!=null)
	{
		expected = expected.replaceAll("\\[\"", "");
		actual = actual.replaceAll("\\[\"", "");	
		expected = expected.replaceAll("\"\\]", "");
		actual = actual.replaceAll("\"\\]", "");
		expected = expected.replaceAll("\\.[0-9]*", "");
		actual = actual.replaceAll("\\.[0-9]*", "");
		actual=actual.replace("$","");
		actual=actual.replace(",","");
		expected=expected.replace("$","");
		expected=expected.replace(",","");
		if(expected.equals(actual))
		{
			System.out.println(expected+"---------------------------------"+actual);
			status = true;
		}
	}

	return status;	
	
}

protected void analyse(LinkedHashMap<String, String> Conditiontablerow,LinkedHashMap<String, String> outputrow ) throws DatabaseException 
{		
	boolean flag = false;
	
	if(outputrow.get(Conditiontablerow.get("StatusColumn")).equals("Pass"))
	{		

	}

	else if(outputrow.get(Conditiontablerow.get("StatusColumn")).equals("Fail"))
	{	
		String[] Parentname =Conditiontablerow.get("ParentName").split(";");
		int noOfParentname=Parentname.length;
		for(int i=0;i<noOfParentname;i++)
		{								
			if(!this.ifexist(Conditiontablerow.get("NodeName")))
			{
				errorParentname.add(Parentname[i]);
				if(flag == false)
				{
					errorMessage.add(Conditiontablerow.get("Message"));
					flag = true;
				}
			}
		}					
	}

}

protected boolean ifexist (String NodeName)
{
	boolean exist = false;
	int arraylength =errorParentname.size();
	for(int i = 0; i<arraylength;i++)
	{
		String existParentName =errorParentname.get(i);
		if(existParentName.equals(NodeName))
		{
			exist = true;
			break;
		}
	}
	return exist;	

}
}
