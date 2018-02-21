package com.selenium.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Map.Entry;

import com.selenium.SupportingClasses.DatabaseOperation;
import com.selenium.exception.DatabaseException;
import com.selenium.exception.PropertiesHandleException;

public class PropertiesHandle extends Properties

{
	private static final long serialVersionUID = 1L;
	protected String path = null;
	protected String browser;
	protected String AppURL;
	protected String TestSctiptFilePath;
	protected String LoginSheetName;
	protected String TestScriptSheetName;
	protected String inpuTableName;
	protected String outputtableName;
	protected String jdbcDriver;
	protected String dbURL;
	protected String dbusername;
	protected String dbPassword;
	protected String FlagForExecution;
	protected String ScreenshotPath;
	protected String remoteIP;
	protected String Port;
	protected String ResultsChoice;
	protected String RMConfigtable;
	protected String lookuptable;
	protected String RMPath;
	protected String RMResultsPath;
	protected String MacroClassName;
	protected String Comparisontable;
	protected String queryresult;

	
	protected String Project;
	protected String Flow;
	protected String Env;
	
	protected String JDBC_DRIVER;
	protected String DB_URL;
	protected String USER;
	protected String password;
	protected String priority;

	protected String ResultChoice;
	static DatabaseOperation ConfigQuery = new DatabaseOperation();
			
	public PropertiesHandle(String Project,String Flow, String Env ,String FlagForExecution, String JDBC_DRIVER, String DB_URL, String USER, String password, String browser,String ResultChoice,String remoteIP,String Port) throws DatabaseException, PropertiesHandleException	
	{
		this.Project = Project;
		this.Flow=Flow;
		this.Env=Env;
		this.FlagForExecution=FlagForExecution;
		this.JDBC_DRIVER=JDBC_DRIVER;
		this.DB_URL=DB_URL;
		this.USER=USER;
		this.password=password;
		this.browser=browser;
		this.ResultChoice=ResultChoice;
		this.remoteIP=remoteIP;
		this.Port=Port;
			WriteProperty();
			
		}
		
		protected void WriteProperty() throws DatabaseException, PropertiesHandleException
		{
			DatabaseOperation.ConnectionSetup(JDBC_DRIVER, DB_URL, USER, password);
			this.put("browser", browser);
			this.put("EnvURL", this.RdbmsValue("URL"));
			this.put("TestScriptPath", this.RdbmsValue("RootFolder")+this.RdbmsValue("ProjectName")+"/"+this.RdbmsValue("FlowName")+"/Script/"+this.RdbmsValue("FileName"));
			this.put("loginSheetName", this.RdbmsValue("LoginSheetName"));
			this.put("ScriptSheetName", this.RdbmsValue("ScriptSheetName"));
			this.put("flagForExecution", FlagForExecution);
			this.put("inputQuery",  this.RdbmsQuery("InputTable"));
			this.put("outputQuery", this.RdbmsQuery("OutputTable"));
			this.put("jdbc_driver",this.RdbmsValue("JDCDriver"));
			
			this.put("db_url",this.RdbmsValue("DB_URL")+this.RdbmsValue("ProjectDBName"));
			this.put("db_username",this.RdbmsValue("DB_UserName"));
			this.put("db_password",this.RdbmsValue("DB_Password"));
			this.put("ScreenShotPath",this.RdbmsValue("RootFolder")+this.RdbmsValue("ProjectName")+"/"+this.RdbmsValue("FlowName")+"/Screenshots/");
			this.put("server",remoteIP+":"+Port );
			this.put("ResultsChoice",ResultChoice);
			this.put("RMconfig_query",this.RdbmsQuery("MacroMappingTable"));
	    	this.put("lookup_query",this.RdbmsQuery("MacroTranslationTable"));
	    	this.put("RatingModelPath",this.RdbmsValue("RootFolder")+this.RdbmsValue("ProjectName")+"/"+this.RdbmsValue("FlowName")+"/SampleRatingModel/");
	    	this.put("ExpectedRMPath",this.RdbmsValue("RootFolder")+this.RdbmsValue("ProjectName")+"/"+this.RdbmsValue("FlowName")+"/RatingModelResults/");
	    	this.put("MacroClassName",this.RdbmsValue("MacroClassName"));
	    	this.put("comparisonTableQuery",this.RdbmsQuery("ComaparisonTableName"));
	    	DatabaseOperation.CloseConn();
		 
		}
		protected String RdbmsQuery(String OutputColoumn) throws PropertiesHandleException
		{
			try
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdbmsQuery =  ConfigQuery.GetDataObjects("SELECT ComaparisonTableName,MacroClassName,MacroMappingTable,MacroTranslationTable,ProjectName,FlowName,ProjectDBName,RootFolder,FileName,ScriptSheetName,LoginSheetName,InputTable,OutputTable,UserDBName,JDCDriver,DB_URL,DB_UserName,DB_Password,Env_Name,URL FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN Flow_CONFIG ON Project_CONFIG.ProjectID = Flow_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON Flow_CONFIG.FlowID = Environment_CONFIG.FlowID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.FlowID = Flow_CONFIG.FlowID)WHERE Project_CONFIG.ProjectName ='" +Project+ "' AND Flow_CONFIG.FlowName = '" +Flow+ "' AND Environment_CONFIG.Env_Name = '" +Env+ "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
				for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdbmsQuery.entrySet())	
				{
					LinkedHashMap<String, String> rowRdbmsQuery = entry.getValue();
					queryresult =  rowRdbmsQuery.get(OutputColoumn);
				}
				return "SELECT * FROM " + queryresult;		
			}
			catch(DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN SQL - QUERY -- " + OutputColoumn, e);
			}
		}
		
		protected String RdbmsValue(String OutputColoumn) throws PropertiesHandleException
		{
			try
			{
				LinkedHashMap<Integer, LinkedHashMap<String, String>> tableRdbmsValue = ConfigQuery.GetDataObjects("SELECT ComaparisonTableName,MacroClassName,MacroMappingTable,MacroTranslationTable,ProjectName,FlowName,ProjectDBName,RootFolder,FileName,ScriptSheetName,LoginSheetName,InputTable,OutputTable,UserDBName,JDCDriver,DB_URL,DB_UserName,DB_Password,Env_Name,URL FROM Project_CONFIG INNER JOIN UserFolder_CONFIG INNER JOIN Flow_CONFIG ON Project_CONFIG.ProjectID = Flow_CONFIG.ProjectID INNER JOIN Environment_CONFIG ON Flow_CONFIG.FlowID = Environment_CONFIG.FlowID INNER JOIN Version_CONFIG ON Environment_CONFIG.Env_ID = Version_CONFIG.Env_ID INNER JOIN VersionDetail_CONFIG ON (VersionDetail_CONFIG.Verision = Version_CONFIG.Version and VersionDetail_CONFIG.FlowID = Flow_CONFIG.FlowID)WHERE Project_CONFIG.ProjectName ='" +Project+ "' AND Flow_CONFIG.FlowName = '" +Flow+ "' AND Environment_CONFIG.Env_Name = '" +Env+ "' ORDER BY Version_CONFIG.Version DESC LIMIT 1");
				for (Entry<Integer, LinkedHashMap<String, String>> entry : tableRdbmsValue.entrySet())	
				{
					LinkedHashMap<String, String> rowRdbmsValue = entry.getValue();
					queryresult = rowRdbmsValue.get(OutputColoumn);
				}
				return queryresult;
			}
			catch(DatabaseException e)
			{
				throw new PropertiesHandleException("ERROR IN RETRIVING DATA FROM -- " + OutputColoumn, e);
			}
		}
		
	
		public PropertiesHandle(String path) throws PropertiesHandleException
		{
			this.path = path;
			
			FileInputStream configuration = null;
			
			try 
			{
				configuration = new FileInputStream(path);
			} 
			catch (FileNotFoundException e) 
			{
				throw new PropertiesHandleException("CONFIGURATION FILE PATH DOES NOT CONTAINS CONFIG FILE", e);
			}
			try 
			{
				this.load(configuration);
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN LOADING A CONFIG FILE", e);
			}
		}
		
		public void store(String newpath) throws PropertiesHandleException
		{
			Writer writer = null;
			try 
			{
				 writer = new FileWriter(newpath);
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN WRITING A CONFIG FILE", e);
			}
			try 
			{
				this.store(writer, "File saved");
			} 
			catch (IOException e) 
			{
				throw new PropertiesHandleException("ERROR IN STORING A CONFIG FILE", e);
			};
		}
		
		public void store()
		{
			Writer writer = null;
			try 
			{
				 writer = new FileWriter(this.path);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try 
			{
				this.store(writer, "File saved");
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		
		public static void main(String args[]) throws DatabaseException, PropertiesHandleException
		{
			System.setProperty("jsse.enableSNIExtension", "false");
			PropertiesHandle configFile = new PropertiesHandle("NITIC","Quote","QA","Y","com.mysql.jdbc.Driver","jdbc:mysql://192.168.84.225:3700/SeleniumConfig","root","redhat","Chrome","ActualOnly","192.168.4.131","5561");
			System.out.println(configFile.getProperty("browser"));
			System.out.println(configFile.getProperty("EnvURL"));
			System.out.println(configFile.getProperty("TestScriptPath"));
			System.out.println(configFile.getProperty("loginSheetName"));
			System.out.println(configFile.getProperty("ScriptSheetName"));
			System.out.println(configFile.getProperty("flagForExecution"));
			System.out.println(configFile.getProperty("inputQuery"));
			System.out.println(configFile.getProperty("outputQuery"));
			System.out.println(configFile.getProperty("jdbc_driver"));
			System.out.println(configFile.getProperty("db_url"));
			System.out.println(configFile.getProperty("db_username"));
			System.out.println(configFile.getProperty("db_password"));
			System.out.println(configFile.getProperty("ScreenShotPath"));
			System.out.println(configFile.getProperty("server"));
			System.out.println(configFile.getProperty("ResultsChoice"));
			System.out.println(configFile.getProperty("RMconfig_query"));
			System.out.println(configFile.getProperty("lookup_query"));
			System.out.println(configFile.getProperty("RatingModelPath"));
			System.out.println(configFile.getProperty("ExpectedRMPath"));
			System.out.println(configFile.getProperty("MacroClassName"));
			System.out.println(configFile.getProperty("comparisonTableQuery"));
		}
}