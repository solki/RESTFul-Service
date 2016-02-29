package au.edu.unsw.soacourse.humanresource.dao;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import au.edu.unsw.soacourse.humanresource.model.AutoCheck;

public class AutoCheckXMLHandler {
	Document doc;
	String fileLoc;
	
	public AutoCheckXMLHandler() {
		super();
		// TODO Auto-generated constructor stub
		
		try {
			fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/AutoChecks.xml";
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean addNewItem(AutoCheck autoCheck) {


		NodeList root = this.doc.getElementsByTagName("AutoChecks");

		Element items = (Element) root.item(0);


		Element item = doc.createElement("Entry");
		items.appendChild(item);

		Element _autoCheckId = doc.createElement("_autoCheckId");
		_autoCheckId.appendChild(doc.createTextNode(autoCheck.get_autoCheckId()));
		item.appendChild(_autoCheckId);

		Element _appId = doc.createElement("_appId");
		_appId.appendChild(doc.createTextNode(autoCheck.get_appId()));
		item.appendChild(_appId);

		Element resultDetails = doc.createElement("resultDetails");
		resultDetails.appendChild(doc.createTextNode(autoCheck.getResultDetails()));
		item.appendChild(resultDetails);


		saveFile();

		return true;		


	}
	
	
	public ArrayList<AutoCheck> translateToAutoChecks() {

		NodeList entryNodes = doc.getElementsByTagName("Entry");
		ArrayList<AutoCheck> entryList = new ArrayList<AutoCheck>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			AutoCheck a = new AutoCheck();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_autoCheckId")) 
					a.set_autoCheckId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("_appId")) 
					a.set_appId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("resultDetails")) 
					a.setResultDetails(e.getTextContent());


			}

			entryList.add(a);

		}

		return entryList;

	}
	
	public void updateItem(AutoCheck autoCheck) {

		NodeList entryList = this.doc.getElementsByTagName("Entry");
	
		
		for (int i = 0; i < entryList.getLength(); i++) {
			
			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();
			
			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);
				
				if (e.getNodeName().equalsIgnoreCase("_appId") && 
						e.getTextContent().equals(autoCheck.get_appId())) {
					isThisOne = true;
				}
			}
			
			// update if it is
			if (isThisOne) {
				
				for (int j = 0; j < entryElements.getLength(); j++) {
					Node e = entryElements.item(j);
								
					if (e.getNodeName().equalsIgnoreCase("resultDetails")) {
						if (autoCheck.getResultDetails() != null)
							e.setTextContent(autoCheck.getResultDetails());
				
						
					}					

				}
				
				saveFile();
				
	
			}	
			
		}
	

	}
	
	private void saveFile() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(this.doc);


			StreamResult result = new StreamResult(new File(fileLoc));

			transformer.transform(source, result);
			System.out.println("File saved!");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
