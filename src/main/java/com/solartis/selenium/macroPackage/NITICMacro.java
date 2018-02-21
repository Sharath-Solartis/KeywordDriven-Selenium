package com.solartis.selenium.macroPackage;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.selenium.Configuration.PropertiesHandle;
import com.selenium.SupportingClasses.DatabaseOperation;
import com.selenium.SupportingClasses.ExcelOperationsPOI;
import com.selenium.exception.DatabaseException;
import com.selenium.exception.MacroException;
import com.selenium.exception.POIException;

public class NITICMacro implements MacroInterface
{
	protected ExcelOperationsPOI sampleexcel=null;
	protected String Targetpath;
	protected NITICMacro trans;
	protected String Samplepath;
	protected DatabaseOperation configTable = null;
	protected PropertiesHandle configFile;
	
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public enum Alphabet 
	{
	    A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ;

	    public static int getNum(String targ) 
	    {
	        return valueOf(targ).ordinal();
	    }

	    public static int getNum(char targ)
	    {
	        return valueOf(String.valueOf(targ)).ordinal();
	    }
	}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	public NITICMacro()
	{
		
	}
	public NITICMacro(PropertiesHandle configFile) throws MacroException
	{
		configTable = new DatabaseOperation();
		
		//configFile = new PropertiesHandle("A:/1 Projects/09 StarrGL/Release_24_UAT/RatingTrial/configuration_file/config_json.properties");
		try 
		{
			configTable.GetDataObjects(configFile.getProperty("RMconfig_query"));
		}
		catch (DatabaseException e) 
		{
			System.out.println(e);
			throw new MacroException("ERROR OCCURS INITILIZE THE OBJECT OF StarrGLMACRO", e);
		}
		
	}
	
	public void LoadSampleRatingmodel(PropertiesHandle configFile,LinkedHashMap<String, String> inputData) throws MacroException
	{
		try
		{
			String RateingModelName =configFile.getProperty("RatingModelPath")+"NITICRatingModel.xls" ;
			
			Samplepath= RateingModelName;
			sampleexcel= new ExcelOperationsPOI(Samplepath);
		}
		catch (POIException e)
		{
			System.out.println(e);
			throw new MacroException("ERROR OCCURS WHILE LOADING SAMPLE RATING MODEL", e);
		}
	}
	
	public void GenerateExpected(LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws MacroException
	{
		try
		{
			System.out.println("E:\\SeleniumConfig\\NITIC\\Quote\\RatingModelResults\\"+inputData.get("Test_data_id")+".xls");
			Targetpath = configFile.getProperty("ExpectedRMPath")+inputData.get("Test_data_id")+".xls";
			sampleexcel.Copy(Samplepath, Targetpath);
			sampleexcel.save();
			System.out.println("generate expected rating over");
		}
		catch( POIException e)
		{
			System.out.println(e);
			throw new MacroException("ERROR OCCURS WHILE GENERATING THE EXPECTED RATING MODEL", e);
		}
	}
	
	public void PumpinData(LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws MacroException
	{
		try
		{
			//DatabaseOperation configTable = new DatabaseOperation();
			LinkedHashMap<Integer, LinkedHashMap<String, String>> tablePumpinData = configTable.GetDataObjects(configFile.getProperty("RMconfig_query"));
			ExcelOperationsPOI excel=new ExcelOperationsPOI(Targetpath);
			trans= new NITICMacro(configFile);
			for (Entry<Integer, LinkedHashMap<String, String>> entry : tablePumpinData.entrySet())	
			{								
				LinkedHashMap<String, String> rowPumpinData = entry.getValue();
				if (rowPumpinData.get("flag_for_execution").equalsIgnoreCase("Y"))
				{
					if (rowPumpinData.get("Type").equals("input"))
					{
						String Datacolumntowrite = rowPumpinData.get("Input_DB_column");
						String CellAddress = rowPumpinData.get("Cell_Address");
						
						String  Datatowrite = inputData.get(Datacolumntowrite);
						String[] part = CellAddress.split("(?<=\\D)(?=\\d)");
						int columnNum=Alphabet.getNum(part[0].toUpperCase());
						int rowNum = Integer.parseInt(part[1]);
						System.out.println(columnNum+"----"+rowNum+"-----"+rowPumpinData.get("Sheet_Name")+"-----"+Datatowrite);
						excel.getsheets(rowPumpinData.get("Sheet_Name"));
						excel.getcell(rowNum, columnNum);
						
						if(rowPumpinData.get("Translation_Flag").equals("Y"))
						{
							excel.write_data(rowNum-1, columnNum, trans.Translation1(Datatowrite, rowPumpinData, configFile));
						}
						else
						{
							if(trans.isInteger(Datatowrite))
							{
								int datadata =Integer.parseInt(Datatowrite);
								excel.write_data(rowNum-1, columnNum, datadata);	
							}
							else if(trans.isFloat(Datatowrite))
							{
								float floatdata = Float.parseFloat(Datatowrite);
								excel.write_data(rowNum-1, columnNum, floatdata);
							}
							else
							{
								excel.write_data(rowNum-1, columnNum, Datatowrite);
							}
						}
					}
				}
			}
			excel.refresh();
			excel.save();
		}
		catch(DatabaseException | POIException e)
		{
			System.out.println(e);
			throw new MacroException("ERROR OCCURS WHILE PUMP-IN THE DATA TO RATING MODEL OF StarrGL MACRO", e);
			//e.printStackTrace();
		}
	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void PumpoutData(LinkedHashMap<String, String> outputData,LinkedHashMap<String, String> inputData,PropertiesHandle configFile) throws MacroException    
	{
		trans= new NITICMacro(configFile);
		try
		{
			ExcelOperationsPOI excel=new ExcelOperationsPOI(Targetpath);
			LinkedHashMap<Integer, LinkedHashMap<String, String>> tablePumpoutData = configTable.GetDataObjects(configFile.getProperty("RMconfig_query"));
			excel.refresh();
		for (Entry<Integer, LinkedHashMap<String, String>> entry : tablePumpoutData.entrySet())	
		{
			LinkedHashMap<String, String> rowPumpoutData = entry.getValue();
			if (rowPumpoutData.get("flag_for_execution").equals("Y"))
			{
				if (rowPumpoutData.get("Type").equals("output"))
				{
					String Datacolumntowrite = rowPumpoutData.get("Input_DB_column");
					String CellAddress = rowPumpoutData.get("Cell_Address");
					String[] part = CellAddress.split("(?<=\\D)(?=\\d)");
					int columnNum=Alphabet.getNum(part[0].toUpperCase());
					int rowNum = Integer.parseInt(part[1]);
					excel.getsheets(rowPumpoutData.get("Sheet_Name"));
					excel.getcell(rowNum-1, columnNum);
					String Datatowrite = excel.read_data(rowNum-1, columnNum);
					System.out.println(Datacolumntowrite+"----------" +Datatowrite+"--------"+rowNum+"-------"+columnNum);
					if(rowPumpoutData.get("Translation_Flag").equals("Y"))
					{
						outputData.put(Datacolumntowrite, trans.Translation1(Datatowrite, rowPumpoutData, configFile));
					}
					else
					{
						outputData.put(Datacolumntowrite, Datatowrite);
					
					}
				}
			}
			//outputData.UpdateRow(,outputData);
		}
		
		excel.save();
		}
		catch(DatabaseException | POIException e)
		{
			System.out.println(e);
			throw new MacroException("ERROR OCCURS WHILE PUMPOUT THE OUTPUT FROM RATING MODEL OF StarrGL MACRO", e);
		}		
		
		System.out.println("output over");
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T Translation1(String Datatowrite, LinkedHashMap<String, String> configTable,  PropertiesHandle configFile) throws   MacroException
	{
		T outputdata = null;
			switch(configTable.get("Translation_Function"))
			{
			case "Date": 
				Date DateData = Date(Datatowrite,"yyyy-mm-dd",configTable.get("Translation_Format"));
				outputdata = (T) DateData;
				break;
			case "Lookup":
				String LookupData = Lookup(Datatowrite, configFile);
				outputdata = (T) LookupData;
				break;
			case "String":
				String Stringdata = IntegertoString(Datatowrite);
				outputdata = (T) Stringdata;
				break;
			case "Round2":
				outputdata = (T) Round2(Datatowrite);
				break;
			}		
		return outputdata;
		
	}
	
	
	protected  Date Date(String Date,String InputFormat,String ExpectedFormat) throws MacroException
	{
		String value ="";
		Date Date1=null;
		
		
			Pattern p = Pattern.compile("[^A-Za-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(InputFormat);
			String InputDelimiter="";
			if(m.find())
			{
				InputDelimiter=String.valueOf(InputFormat.charAt(m.start()));
			}
			
			Matcher m1 = p.matcher(ExpectedFormat);
			String ExpectedDelimiter="";
			if(m1.find())
			{
				ExpectedDelimiter=String.valueOf(ExpectedFormat.charAt(m1.start()));
			}
			
			String[] DateInputFormat = InputFormat.split(InputDelimiter);
			String[] DateOutputFormat = ExpectedFormat.split(ExpectedDelimiter);
			String[] date = Date.split(InputDelimiter); //yyyy-mm-dd
			
			HashMap<String,String> DateMaping = new HashMap<String,String>();
			DateMaping.put(DateInputFormat[0].toLowerCase(), date[0]);
			DateMaping.put(DateInputFormat[1].toLowerCase(), date[1]);
			DateMaping.put(DateInputFormat[2].toLowerCase(), date[2]);
			value =  DateMaping.get(DateOutputFormat[0].toLowerCase())+ExpectedDelimiter+DateMaping.get(DateOutputFormat[1].toLowerCase())+ExpectedDelimiter+DateMaping.get(DateOutputFormat[2].toLowerCase());     
			DateFormat format = new SimpleDateFormat(ExpectedFormat, Locale.ENGLISH);
			try 
			{
				Date1=format.parse(value);
			} 
			catch (NumberFormatException | ParseException e) 
			{
				// TODO Auto-generated catch block
				System.out.println(e);
				throw new MacroException("ERROR OCCURS 	IN DATE FORMAT OF StarrGL MACRO", e);
			}  			
		   // System.out.println(value+"\t"+Date1);  						
		
		
		return Date1;
		
	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	protected String  Lookup(String Lookup1, PropertiesHandle configFile) throws  MacroException
	{
		
		DatabaseOperation Lookup = new DatabaseOperation();		
		HashMap<String,String> LookupMap = new HashMap<String,String>();
		try 
		{
		LinkedHashMap<Integer, LinkedHashMap<String, String>> tableLookup = Lookup.GetDataObjects(configFile.getProperty("lookup_query"));
			for (Entry<Integer, LinkedHashMap<String, String>> entry : tableLookup.entrySet())	
			{
				LinkedHashMap<String, String> rowLookup = entry.getValue();
				LookupMap.put(rowLookup.get("LookupData"), rowLookup.get("LookupValue"));				
				
			}
		} 
		catch (DatabaseException e) 
		{
			// TODO Auto-generated catch block
			System.out.println(e);
			throw new MacroException("ERROR OCCURS 	IN LOOKUP TABLE OF StarrGL MACRO", e);
		}
		
		if (LookupMap.get(Lookup1)==null)
		{
			return "Other";
		}
		else
		{
			return LookupMap.get(Lookup1);
		}
	}
	
	protected boolean isInteger(String s) 
	{
	    try 
	    { 
	    	
	        Integer.parseInt(s); 
	    } 
	    catch(NumberFormatException e) 
	    { 
	        return false; 
	    } 
	    catch(NullPointerException e) 
	    {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	protected boolean isFloat(String s)
	{
		try
		{
			Float.parseFloat(s);
		}
		catch(NumberFormatException e) 
	    { 
	        return false; 
	    } 
	    catch(NullPointerException e) 
	    {
	        return false;
	    }
		 return true;
	}
	
	protected String IntegertoString (String s)
	{
		return s;
		
	}
	
	protected int ReplaceComma(String s)
	{
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		boolean b = m.find();
		int num=0;
		if (b)
		{
			 s = s.replaceAll("(?<=\\d),(?=\\d)", "");
			 num= Integer.parseInt(s);
		}
		else
		{
			num=Integer.parseInt(s);
		}

		return num;
		
	}
	
	protected String Round2(String s)
	{
		double doublevalue = Double.parseDouble(s);
		DecimalFormat f = new DecimalFormat("##.00");		
		return f.format(doublevalue) ;
	}
	
	
	
	/*public static void main(String args[]) throws  DatabaseException, MacroException, PropertiesHandleException
	{
	 DatabaseOperation objectInput = new DatabaseOperation();
	 DatabaseOperation objectOutput = new DatabaseOperation();
	 StarrGLMacro sm;
	 PropertiesHandle configFile = new PropertiesHandle("E:/RestFullAPIDeliverable/Devolpement/admin/STARR-GL/Rating/config/config.properties");
	 
	 DatabaseOperation.ConnectionSetup(configFile);
	 objectInput.GetDataObjects(configFile.getProperty("input_query"));
	 objectOutput.GetDataObjects(configFile.getProperty("output_query"));
	 do
	 {
	  System.out.println("TestData : " + objectInput.ReadData("S.No"));   
	    if(objectInput.ReadData("Flag_for_execution").equals("Y"))
	    {
	     sm=new StarrGLMacro(configFile);
	     sm.LoadSampleRatingmodel(configFile, objectInput);
	     sm.GenerateExpected(objectInput, configFile);
	     sm.PumpinData(objectInput, configFile);
	     sm.PumpoutData(objectOutput,objectInput, configFile);
	    }
	    objectInput.WriteData("Flag_for_execution", "Completed"); 
	    objectInput.UpdateRow();
	 }while(objectInput.MoveForward()&&objectOutput.MoveForward());
	}*/
	
	public static void main(String args[])
	{
		NITICMacro nc = new NITICMacro();
		String result =nc.Round2("37");
		System.out.println(result);
	}

}
