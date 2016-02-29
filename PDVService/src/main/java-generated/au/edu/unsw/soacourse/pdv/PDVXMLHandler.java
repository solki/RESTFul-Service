package au.edu.unsw.soacourse.pdv;



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


public class PDVXMLHandler {

	Document doc;

	public PDVXMLHandler() {
		// TODO Auto-generated constructor stub
		try {
			String fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/PDV.xml";
//			File myFile = new File(fileLoc);
//			FileInputStream fIn = new FileInputStream(myFile);
//			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("--------------------------");
		}

	}



	public ArrayList<DriverPDV> translateToDriverPDV() {

		NodeList driverNodes = doc.getElementsByTagName("driver");
		ArrayList<DriverPDV> driverlist = new ArrayList<DriverPDV>();

		for (int i = 0; i < driverNodes.getLength(); i++) {

			Node n = driverNodes.item(i);
			NodeList driverElements = n.getChildNodes();
			DriverPDV a = new DriverPDV();

			for (int j = 0; j < driverElements.getLength(); j++) {
				Node e = driverElements.item(j);

				//System.out.println(e.getNodeName() + "---" + e.getNodeType());
				if (e.getNodeName().equalsIgnoreCase("licenseNumber"))
					a.setLicenseNumber(e.getTextContent());
				if (e.getNodeName().equalsIgnoreCase("fullName"))
					a.setFullName(e.getTextContent());
				if (e.getNodeName().equalsIgnoreCase("postcode"))
					a.setPostcode(e.getTextContent());



			}



			driverlist.add(a);

		}

		return driverlist;

	}

	//	public Boolean addNewItem(AuctionItemBean aib) {
	//		NodeList test = this.doc.getElementsByTagName("ID");
	//		
	//		for (int i = 0; i < test.getLength(); i++) {
	//			if (aib.getId().equals(test.item(i).getTextContent()))
	//				return false;
	//		}
	//		
	//		
	//		NodeList root = this.doc.getElementsByTagName("AuctionItems");
	//		
	//		Element items = (Element) root.item(0);
	//		
	//		
	//		Element item = doc.createElement("AuctionItem");
	//		items.appendChild(item);
	//		
	//		Element Title = doc.createElement("Title");
	//		Title.appendChild(doc.createTextNode(aib.getTitle()));
	//		item.appendChild(Title);
	//		
	//		Element Category = doc.createElement("Category");
	//		Category.appendChild(doc.createTextNode(aib.getCategory()));
	//		item.appendChild(Category);
	//		
	//		Element Picture = doc.createElement("Picture");
	//		Picture.appendChild(doc.createTextNode(String.valueOf(aib.getPicture())));
	//		item.appendChild(Picture);
	//		
	//		Element Description = doc.createElement("Description");
	//		Description.appendChild(doc.createTextNode(aib.getDescription()));
	//		item.appendChild(Description);
	//		
	//		
	//		
	//		/////////////////////
	//		
	//		Element PostalAddress = doc.createElement("PostalAddress");
	//		item.appendChild(PostalAddress);
	//		
	//		Element streetAddress = doc.createElement("streetAddress");
	//		streetAddress.appendChild(doc.createTextNode(aib.getPostadrStreetAddr()));
	//		PostalAddress.appendChild(streetAddress);
	//		
	//		Element city = doc.createElement("city");
	//		city.appendChild(doc.createTextNode(aib.getPostadrCity()));
	//		PostalAddress.appendChild(city);
	//		
	//		Element state = doc.createElement("state");
	//		state.appendChild(doc.createTextNode(aib.getPostadrState()));
	//		PostalAddress.appendChild(state);
	//		
	//		Element country = doc.createElement("country");
	//		country.appendChild(doc.createTextNode(aib.getPostadrCountry()));
	//		PostalAddress.appendChild(country);
	//		
	//		Element postalCode = doc.createElement("postalCode");
	//		postalCode.appendChild(doc.createTextNode(String.valueOf(aib.getPostadrPostalCode())));
	//		PostalAddress.appendChild(postalCode);
	//		
	//		/////////////////////
	//		
	//				
	//		Element ReservePrice = doc.createElement("ReservePrice");
	//		ReservePrice.setAttribute("currency", aib.getCurrency());
	//		ReservePrice.appendChild(doc.createTextNode(String.valueOf(aib.getReservePrice())));
	//		item.appendChild(ReservePrice);
	//		
	//		Element BiddingStartPrice = doc.createElement("BiddingStartPrice");
	//		BiddingStartPrice.setAttribute("currency", aib.getCurrency());
	//		BiddingStartPrice.appendChild(doc.createTextNode(String.valueOf(aib.getBiddingStartPrice())));
	//		item.appendChild(BiddingStartPrice);
	//		
	//		Element BiddingIncrements = doc.createElement("BiddingIncrements");
	//		BiddingIncrements.appendChild(doc.createTextNode(String.valueOf(aib.getBiddingIncrements())));
	//		item.appendChild(BiddingIncrements);
	//		
	//		Element EndTime = doc.createElement("EndTime");
	//		EndTime.appendChild(doc.createTextNode(aib.getEndTime()));
	//		item.appendChild(EndTime);
	//		
	//		Element ID = doc.createElement("ID");
	//		ID.appendChild(doc.createTextNode(aib.getId()));
	//		item.appendChild(ID);
	//		
	//		
	//		
	//		try {
	//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	//			Transformer transformer = transformerFactory.newTransformer();
	//			DOMSource source = new DOMSource(this.doc);
	//			
	//			
	//			StreamResult result = new StreamResult(new File(ctxt.getRealPath("/WEB-INF/auction_items.xml")));
	//			
	//			transformer.transform(source, result);
	//			//System.out.println(ctxt.getRealPath("/WEB-INF/auction_items.xml"));
	//			System.out.println("File saved!");
	//			
	//		}
	//		catch (Exception e) {
	//	    	e.printStackTrace();
	//	    }
	//		
	//		
	//		return true;
	//		
	//		
	//		
	//	}

}