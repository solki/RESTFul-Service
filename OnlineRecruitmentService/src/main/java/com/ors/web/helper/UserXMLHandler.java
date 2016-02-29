package com.ors.web.helper;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ors.bean.UserBean;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<UserBean> translateToUser() {

		NodeList entryNodes = doc.getElementsByTagName("Entry");
		ArrayList<UserBean> entryList = new ArrayList<UserBean>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			UserBean user = new UserBean();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("login")) {
					NodeList loginElements = e.getChildNodes();

					for (int k = 0; k < loginElements.getLength(); k++) {
						Node d = loginElements.item(k);

						if (d.getNodeName().equalsIgnoreCase("_uid"))
							user.setUid(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("_pwd"))
							user.setPwd(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("ShortKey"))
							user.setShortKey(d.getTextContent());

					}

				}

				if (e.getNodeName().equalsIgnoreCase("details")) {
					NodeList detailsElements = e.getChildNodes();

					for (int k = 0; k < detailsElements.getLength(); k++) {
						Node d = detailsElements.item(k);

						if (d.getNodeName().equalsIgnoreCase("LastName"))
							user.setLastName(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("FirstName"))
							user.setFirstName(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("Role"))
							user.setRole(d.getTextContent());
						if (d.getNodeName().equalsIgnoreCase("Department"))
							user.setDepartment(d.getTextContent());

					}

				}

			}

			entryList.add(user);

		}

		return entryList;

	}
}
