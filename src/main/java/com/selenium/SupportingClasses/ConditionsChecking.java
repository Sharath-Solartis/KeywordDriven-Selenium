package com.selenium.SupportingClasses;
import java.util.LinkedHashMap;

import com.selenium.SupportingClasses.DatabaseOperation;
import com.selenium.exception.DatabaseException;



public class ConditionsChecking extends DatabaseOperation 
{
	protected String CondColName;
	
	
	public boolean DbCol(LinkedHashMap<String, String> row) throws DatabaseException
	{
			return ConditionReading(row.get(CondColName),row);
	}

	protected boolean ConditionReading(String condition,LinkedHashMap<String, String> row) throws DatabaseException
	{
		boolean ConditionReading=false;
		
			if(condition.equals(""))
			{
				ConditionReading=true;
				return ConditionReading;
			}
			else
			{
				String[] splits=condition.split(";");
				int length=splits.length;
				
				for(int i=0;i<length;i++)
					{
						ConditionReading=false;
						String[] CondValue = new String[10];
						String operator = null;
					
							if(splits[i].contains(">="))
							{
								CondValue=splits[i].split(">=");
								operator = ">=";
							}
							else if(splits[i].contains("<="))
							{
								CondValue=splits[i].split("<=");
								operator = "<=";
							}
							else if(splits[i].contains("="))
							{
								CondValue=splits[i].split("=");
								operator = "=";
							}
							else if(splits[i].contains("<>"))
							{
								CondValue=splits[i].split("<>");
								operator = "<>";
							}
							else if(splits[i].contains(">"))
							{
								CondValue=splits[i].split(">");
								operator = ">";
							}
							else if(splits[i].contains("<"))
							{
								CondValue=splits[i].split("<");
								operator = "<";
							}
							
							
						String cond=CondValue[0];
						String value=CondValue[1];
						String[] individualValue = value.split("\\|");
			
							for(int j=0;j<individualValue.length;j++)
							{
								switch(operator)
								{
								case "=": if((row.get(cond).equals(individualValue[j])))
										   {
												ConditionReading=true;
											}
											break;
								case "<>": if((row.get(cond).equals(individualValue[j])))
											{
												ConditionReading=false;
						 						return ConditionReading;
											}
											else
											{
												ConditionReading=true;
											}
											break;	
								case ">": if(Integer.parseInt(row.get(cond)) > Integer.parseInt(individualValue[j]))
											{
												ConditionReading=true;
						 						return ConditionReading;
											}
											else
											{
												ConditionReading=false;
											}
											break;	
								case "<": if(Integer.parseInt(row.get(cond)) < Integer.parseInt(individualValue[j]))
											{
												ConditionReading=true;
						 						return ConditionReading;
											}
											else
											{
												ConditionReading=false;
											}
											break;
								case ">=": if(Integer.parseInt(row.get(cond)) >= Integer.parseInt(individualValue[j]))
											{
												ConditionReading=true;
						 						return ConditionReading;
											}
											else
											{
												ConditionReading=false;
											}
											break;
								case "<=": if(Integer.parseInt(row.get(cond)) <= Integer.parseInt(individualValue[j]))
											{
												ConditionReading=true;
						 						return ConditionReading;
											}
											else
											{
												ConditionReading=false;
											}
											break;
								}
								
							}
							
						if(!ConditionReading)
						{
							return ConditionReading;
						}
					}	
			}
		
	return ConditionReading;
	}
	
}
