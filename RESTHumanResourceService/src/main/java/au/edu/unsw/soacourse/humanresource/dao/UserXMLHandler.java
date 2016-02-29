package au.edu.unsw.soacourse.humanresource.dao;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import au.edu.unsw.soacourse.humanresource.model.User;
import au.edu.unsw.soacourse.humanresource.model.User.RoleType;





public class UserXMLHandler {

	Document doc;

	public UserXMLHandler() {
		super();

		try {
			String fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/RegisteredUsers.xml";
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<User> translateToUser() {

		NodeList entryNodes = doc.getElementsByTagName("Entry");
		ArrayList<User> entryList = new ArrayList<User>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			User a = new User();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("login")) {
					NodeList loginElements = e.getChildNodes();

					for (int k = 0; k < loginElements.getLength(); k++) {
						Node d = loginElements.item(k);

						if (d.getNodeName().equalsIgnoreCase("_uid"))
							a.set_uid(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("_pwd"))
							a.set_pwd(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("ShortKey"))
							a.setShortKey(d.getTextContent());
						
					}


				}
				
				if (e.getNodeName().equalsIgnoreCase("details")) {
					NodeList detailsElements = e.getChildNodes();

					for (int k = 0; k < detailsElements.getLength(); k++) {
						Node d = detailsElements.item(k);

						if (d.getNodeName().equalsIgnoreCase("LastName"))
							a.setLastName(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("FirstName"))
							a.setFirstName(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("Role")) {
							if (d.getTextContent().equals("manager"))
								a.setRole(RoleType.manager);
							if (d.getTextContent().equals("reviewer"))
								a.setRole(RoleType.reviewer);							
						}
						if (d.getNodeName().equalsIgnoreCase("Department"))
							a.setDepartment(d.getTextContent());
						
					}


				}

			}


			entryList.add(a);

		}

		return entryList;

	}
}
