package com.selenium.SupportingClasses;

import java.io.File;

import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

public class createTestNg 
{
public static void GenerateTestNG()
{
	
     /*   try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.newDocument();
            Element rootElement = doc.createElement("suite");
            doc.appendChild(rootElement);

            Attr rootNameAttribute = doc.createAttribute("name");
            rootNameAttribute.setValue("Suite");

            Attr rootParallelAttribute = doc.createAttribute("parallel");
            rootParallelAttribute.setValue("none");

            rootElement.setAttributeNode(rootNameAttribute);
            rootElement.setAttributeNode(rootParallelAttribute);

            Element testElement = doc.createElement("test");
            rootElement.appendChild(testElement);

            Attr testNameAttribute = doc.createAttribute("name");
            testNameAttribute.setValue("Test1");
            testElement.setAttributeNode(testNameAttribute);

            Element classesElement = doc.createElement("classes");
            testElement.appendChild(classesElement);

            Fillo fillo = new Fillo();
            Connection con = fillo.getConnection("./testCaseStatus.xls");

            String query = "Select * from Sheet1";
            Recordset recordSet = con.executeQuery(query);

            while (recordSet.next()) {
                if (recordSet.getField("Execute").equals("Y")) {
                    Element classElement = doc.createElement("class");  
                    Attr classNameAttribute = doc.createAttribute("name");
                    classNameAttribute.setValue(recordSet.getField("TestCase"));
                    classElement.setAttributeNode(classNameAttribute);
                    classesElement.appendChild(classElement);
                }
            }
            recordSet.close();
            con.close();
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./testNg.xml"));
            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FilloException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

}
