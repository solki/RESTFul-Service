package au.edu.unsw.soacourse.crv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class CRVXMLHandler {

	Document doc;

	public CRVXMLHandler() {
		super();
		
		try {
			String fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/CRV.xml";
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("=======================");
		}
	}
	
	public ArrayList<DriverCRV> translateToDriverCRV() {

		NodeList driverNodes = doc.getElementsByTagName("driver");
		ArrayList<DriverCRV> driverlist = new ArrayList<DriverCRV>();

		for (int i = 0; i < driverNodes.getLength(); i++) {

			Node n = driverNodes.item(i);
			NodeList driverElements = n.getChildNodes();
			DriverCRV a = new DriverCRV();

			for (int j = 0; j < driverElements.getLength(); j++) {
				Node e = driverElements.item(j);

				//System.out.println(e.getNodeName() + "---" + e.getNodeType());
				if (e.getNodeName().equalsIgnoreCase("licenseNumber"))
					a.setLicenseNumber(e.getTextContent());
				
				if (e.getNodeName().equalsIgnoreCase("isClean")) {
					if (e.getTextContent().equals("YES"))
						a.setIsClean(true);
					else if (e.getTextContent().equals("NO")) 
						a.setIsClean(false);
					
				}				
				if (e.getNodeName().equalsIgnoreCase("description"))
					a.setDescription(e.getTextContent());


			}


			driverlist.add(a);

		}

		return driverlist;

	}
	
	
}
