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

import au.edu.unsw.soacourse.humanresource.model.Application;
import au.edu.unsw.soacourse.humanresource.model.Application.AppStatus;


public class ApplicationXMLHandler {
	Document doc;
	String fileLoc;

	public ApplicationXMLHandler() {
		super();

		try {
			fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/Applications.xml";
			InputSource xmlFile = new InputSource(fileLoc);

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Application> translateToApplications() {

		NodeList entryNodes = doc.getElementsByTagName("Application");
		ArrayList<Application> entryList = new ArrayList<Application>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			Application a = new Application();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_appId")) 
					a.set_appId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("_jobId")) 
					a.set_jobId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("licenseNumber")) 
					a.setLicenseNumber(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("fullName")) 
					a.setFullName(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("postcode")) 
					a.setPostcode(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("coverLetter")) 
					a.setCoverLetter(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("briefResume")) 
					a.setBriefResume(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("status")) 
					a.setStatus(AppStatus.valueOf(e.getTextContent()));

			}


			entryList.add(a);

		}

		return entryList;

	}


	public Boolean addNewItem(Application application) {


		NodeList root = this.doc.getElementsByTagName("Applications");

		Element items = (Element) root.item(0);


		Element item = doc.createElement("Application");
		items.appendChild(item);

		Element _appId = doc.createElement("_appId");
		_appId.appendChild(doc.createTextNode(application.get_appId()));
		item.appendChild(_appId);

		Element _jobId = doc.createElement("_jobId");
		_jobId.appendChild(doc.createTextNode(application.get_jobId()));
		item.appendChild(_jobId);

		Element licenseNumber = doc.createElement("licenseNumber");
		licenseNumber.appendChild(doc.createTextNode(application.getLicenseNumber()));
		item.appendChild(licenseNumber);

		Element fullName = doc.createElement("fullName");
		fullName.appendChild(doc.createTextNode(application.getFullName()));
		item.appendChild(fullName);

		Element postcode = doc.createElement("postcode");
		postcode.appendChild(doc.createTextNode(application.getPostcode()));
		item.appendChild(postcode);

		Element coverLetter = doc.createElement("coverLetter");
		coverLetter.appendChild(doc.createTextNode(application.getCoverLetter()));
		item.appendChild(coverLetter);

		Element briefResume = doc.createElement("briefResume");
		briefResume.appendChild(doc.createTextNode(application.getBriefResume()));
		item.appendChild(briefResume);

		Element status = doc.createElement("status");
		status.appendChild(doc.createTextNode(application.getStatus().toString()));
		item.appendChild(status);

		saveFile();

		return true;		


	}

	public void updateItem(Application application) {

		NodeList entryList = this.doc.getElementsByTagName("Application");


		for (int i = 0; i < entryList.getLength(); i++) {

			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();

			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_appId") && 
						e.getTextContent().equals(application.get_appId())) {
					isThisOne = true;
				}
			}

			// update if it is
			if (isThisOne) {

				for (int j = 0; j < entryElements.getLength(); j++) {
					Node e = entryElements.item(j);

					if (application.getFullName() != null && 
							!application.getFullName().equals("") &&
							e.getNodeName().equalsIgnoreCase("fullName")) {
						e.setTextContent(application.getFullName());
					}

					if (application.getPostcode() != null && 
							!application.getPostcode().equals("") &&
							e.getNodeName().equalsIgnoreCase("postcode")) {
						e.setTextContent(application.getPostcode());
					}

					if (application.getCoverLetter() != null && 
							!application.getCoverLetter().equals("") &&
							e.getNodeName().equalsIgnoreCase("coverLetter")) {
						e.setTextContent(application.getCoverLetter());
					}

					if (application.getBriefResume() != null && 
							!application.getBriefResume().equals("") &&
							e.getNodeName().equalsIgnoreCase("briefResume")) {
						e.setTextContent(application.getBriefResume());
					}	
					
					if (application.getStatus() != null && 
							!application.getStatus().equals("") &&
							e.getNodeName().equalsIgnoreCase("status")) {
						e.setTextContent(application.getStatus().toString());
					}	

				}

				saveFile();


			}	

		}


	}

	public void changeItemStatus(String id, AppStatus status) {

		NodeList entryList = this.doc.getElementsByTagName("Application");


		for (int i = 0; i < entryList.getLength(); i++) {

			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();

			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_appId") && 
						e.getTextContent().equals(id)) {
					isThisOne = true;
				}
			}

			// update if it is
			if (isThisOne) {

				for (int j = 0; j < entryElements.getLength(); j++) {
					Node e = entryElements.item(j);

					if (e.getNodeName().equalsIgnoreCase("status")) {
						e.setTextContent(status.toString());
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
