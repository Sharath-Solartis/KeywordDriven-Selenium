package com.selenium.SupportingClasses;


import java.util.ArrayList;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;


public class ExcelFillo 
{
	private static Connection connection = null;
	private  String strQuery = null;
	private Recordset recordset =null;
	
//*******************Create Excel connection**************************************************************************************	
	public static  void ConnectionSetup(String FileName) throws FilloException
	{
		Fillo fillo=new Fillo();
		if(connection == null)
		{
		try
		{
		connection=fillo.getConnection(FileName);
		}
		catch(FilloException e)
		{
			e.printStackTrace();
		}
		}
	}
	
//******************get recordset from xl***********************************************************************************************
	public void getRecorset(String strQuery)
	{
		this.strQuery=strQuery;
		try
		{
			recordset=connection.executeQuery(strQuery);
		    recordset.moveFirst();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
//*********************************************************************************************************************************
	public boolean MoveForward() throws FilloException
	{
			return recordset.next();
		
	}
	
//*********************************************************************************************************************************	
	public String ReadData(String column_name) throws FilloException
	{
		return recordset.getField(column_name);
		
	}
//**************************************************************************************************************************************
	public void UpdateRow() throws FilloException
	{
		recordset.moveNext();
	}
//**************************************************************************************************************************************
	public void WriteData(String column_name,String value)
	{
		
	}
//**************************************************************************************************************************************************
	public static void CloseConn()
	{
		connection.close();
	}
	
//***************************************************************************************************************************************************
	public int getRecordsetCount() throws FilloException
	{
		return recordset.getCount();
	}
	
//***************************************************************************************************************************************************
	public int getFieldCount() throws FilloException
	{
		ArrayList<String> FieldNames = recordset.getFieldNames();
		return FieldNames.size();
	}
	
//***********************update query**************************************************************************************************************
	public void updateRecordset(String tableName,String ColumnName,String Value,String PrimaryKey) throws FilloException
	{
		//strQuery = "update "+tableName+" set "+ColumnName+"='"+Value+"' where "+PrimaryKey+"='"+recordset.getField(PrimaryKey)+"'";
		//strQuery = "update Sheet1 set Policy Number='001' where Seq No='1'";
		System.out.println("UPDATE "+tableName+" SET "+ColumnName+"='"+Value+"' WHERE "+PrimaryKey+"='"+recordset.getField(PrimaryKey)+"'");
		connection.executeUpdate("UPDATE "+tableName+" SET "+ColumnName+"='"+Value+"' WHERE "+PrimaryKey+"='"+recordset.getField(PrimaryKey)+"'");
		//+'"+recordset.getField(PrimaryKey)+"');
	}
	
//================================================================================================================================================	
	
	public void setRowNumber(String rowNumber)
	{
		System.setProperty("ROW", rowNumber);
	}
//===========================================================================================================================================
	public void setColumnNumber(String colNumber)
	{
		System.setProperty("COLUMN", colNumber);
	}
//===========================================================================================================================================	
	public static void main(String args[]) throws FilloException
	{
		ExcelFillo ob = new ExcelFillo();
		ConnectionSetup("E:\\Sasirekha1054\\ComparisonReport\\Result Template\\ResultTemplate.xls");
		ob.getRecorset("Select * from Sheet3");
		System.setProperty("ROW", "8");
		//System.out.println(ob.);
		System.out.println(ob.getRecordsetCount()+"-------"+ob.getFieldCount());
		
	}
}
