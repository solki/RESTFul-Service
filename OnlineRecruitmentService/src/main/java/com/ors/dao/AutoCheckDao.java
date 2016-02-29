package com.ors.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.jaxrs.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ors.bean.AutoCheckBean;

public class AutoCheckDao {

	final private static String REST_URI = "http://localhost:8080/RESTHumanResourceService";
	final private static String ORSKey = "i-am-ors";
	final private static String SOAP_URI = "http://localhost:6060/ode/processes/AutoCheckService?wsdl";
	final private static String SOAP_SERVICE = "http://localhost:6060/ode/processes/AutoCheckService";

	public AutoCheckDao() {
		super();
	}

	public String getAutoCheckResult(String licenseNumber, String fullName,
			String postcode) throws IOException, ParserConfigurationException,
					SAXException {

		URL url = null;
		// URLConnection connection = null;
		try {
			url = new URL(SOAP_URI);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("Error, SOAP URI is Wrong!");
			return null;
		}
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput = " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:aut=\"http://autocheck.soacourse.unsw.edu.au\">\n"
				+ " <soapenv:Header/>\n" + " <soapenv:Body>\n"
				+ " <aut:AutoCheckRequest>\n" + " <aut:licenseNumber>"
				+ licenseNumber + "</aut:licenseNumber>\n" + " <aut:fullName>"
				+ fullName + "</aut:fullName>\n" + " <aut:postcode>" + postcode
				+ "</aut:postcode>\n" + " </aut:AutoCheckRequest>\n"
				+ " </soapenv:Body>\n" + " </soapenv:Envelope>";

		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();

		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAP_SERVICE);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		// Write the content of the request to the outputstream of the HTTP
		out.write(b);
		out.close();

		// Read the response.
		InputSource input = new InputSource(httpConn.getInputStream());

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document doc = builder.parse(input);
		NodeList responseList = doc.getElementsByTagName("AutoCheckResponse");
		NodeList responseElements = responseList.item(0).getChildNodes();

		String pdvcode = null;
		String pdvmsg = null;
		String crvcode = null;
		String crvmsg = null;

		for (int j = 0; j < responseElements.getLength(); j++) {
			Node e = responseElements.item(j);

			if (e.getNodeName().trim().toLowerCase().contains("pdvcode"))
				pdvcode = e.getTextContent();

			if (e.getNodeName().trim().toLowerCase().contains("pdvmsg"))
				pdvmsg = e.getTextContent();

			if (e.getNodeName().trim().toLowerCase().contains("crvcode"))
				crvcode = e.getTextContent();

			if (e.getNodeName().trim().toLowerCase().contains("crvmsg"))
				crvmsg = e.getTextContent();

		}

		if (pdvcode == null || pdvmsg == null || crvcode == null
				|| crvmsg == null) {
			return null;
		}

		return "Personal Detail Validation Check Result:\n" + pdvcode + ": "
				+ pdvmsg + "\nCriminal Record Validation Check Result:\n"
				+ crvcode + ": " + crvmsg;
	}

	public Response addAutoCheck(String appId,
			String resultDetails, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/autochecks/add").type(MediaType.APPLICATION_XML)
				.header("ORSKey", ORSKey).header("ShortKey", shortKey);
//		Form form = new Form();
//		form.param("_appId", appId);
//		form.param("resultDetails", resultDetails);
		AutoCheckBean ac = new AutoCheckBean();
		ac.set_appId(appId);
		ac.setResultDetails(resultDetails);
		Response res = null;
		res = client.put(ac);
		return res;
		
	}

	public Response getAutoCheckByApp(String appId, String shortKey) {
		WebClient client = WebClient.create(REST_URI);
		client.path("/autochecks/app/" + appId)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).header("ORSKey", ORSKey)
				.header("ShortKey", shortKey);
		Response res = client.get();
		System.out.println(res.getStatus() + res.getStatusInfo().toString());
		return res;

	}

}
