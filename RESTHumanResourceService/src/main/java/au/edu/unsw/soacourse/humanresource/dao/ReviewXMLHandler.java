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

import au.edu.unsw.soacourse.humanresource.model.Review;
import au.edu.unsw.soacourse.humanresource.model.Review.DecisionType;

public class ReviewXMLHandler {
	Document doc;
	String fileLoc;
	public ReviewXMLHandler() {
		super();
		
		try {
			fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/Reviews.xml";
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Boolean addNewItem(Review review) {


		NodeList root = this.doc.getElementsByTagName("Reviews");

		Element items = (Element) root.item(0);


		Element item = doc.createElement("Entry");
		items.appendChild(item);

		Element _reviewId = doc.createElement("_reviewId");
		_reviewId.appendChild(doc.createTextNode(review.get_reviewId()));
		item.appendChild(_reviewId);

		Element _appId = doc.createElement("_appId");
		_appId.appendChild(doc.createTextNode(review.get_appId()));
		item.appendChild(_appId);

		Element _uId = doc.createElement("_uId");
		_uId.appendChild(doc.createTextNode(review.get_uId()));
		item.appendChild(_uId);

		Element comments = doc.createElement("comments");
		comments.appendChild(doc.createTextNode(review.getComments()));
		item.appendChild(comments);

		Element decision = doc.createElement("decision");
		decision.appendChild(doc.createTextNode(review.getDecision().toString()));
		item.appendChild(decision);

		saveFile();

		return true;		


	}
	
	
	public ArrayList<Review> translateToReviews() {

		NodeList entryNodes = doc.getElementsByTagName("Entry");
		ArrayList<Review> entryList = new ArrayList<Review>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			Review a = new Review();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_ReviewId")) 
					a.set_reviewId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("_appId")) 
					a.set_appId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("_uId")) 
					a.set_uId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("comments")) 
					a.setComments(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("decision")) 
					a.setDecision(DecisionType.valueOf(e.getTextContent()));


			}


			entryList.add(a);

		}

		return entryList;

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

	public void changeItemStatus(String id, DecisionType status) {
		NodeList entryList = this.doc.getElementsByTagName("Entry");
	
		
		for (int i = 0; i < entryList.getLength(); i++) {
			
			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();
			
			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);
				
				if (e.getNodeName().equalsIgnoreCase("_reviewId") && 
						e.getTextContent().equals(id)) {
					isThisOne = true;
				}
			}
			
			// update if it is
			if (isThisOne) {
				
				for (int j = 0; j < entryElements.getLength(); j++) {
					Node e = entryElements.item(j);
								
					if (e.getNodeName().equalsIgnoreCase("decision")) {
						e.setTextContent(status.toString());
					}		
			
				}				
				saveFile();		
			}
			
		}
	}
	
	
	
	
}
