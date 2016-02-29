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

import au.edu.unsw.soacourse.humanresource.model.Job;
import au.edu.unsw.soacourse.humanresource.model.Job.JobStatus;

public class JobXMLHandler {

	Document doc;
	String fileLoc;

	public JobXMLHandler() {
		super();

		try {
			fileLoc = System.getProperty("catalina.home") + "/webapps/ROOT/JobPostings.xml";
			InputSource xmlFile = new InputSource(fileLoc);


			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Job> translateToJobs() {

		NodeList entryNodes = doc.getElementsByTagName("Entry");
		ArrayList<Job> entryList = new ArrayList<Job>();

		for (int i = 0; i < entryNodes.getLength(); i++) {

			Node n = entryNodes.item(i);
			NodeList entryElements = n.getChildNodes();
			Job a = new Job();

			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_jobId")) 
					a.set_jobId(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("closingDate")) 
					a.setClosingDate(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("salary")) 
					a.setSalary(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("positionType")) 
					a.setPositionType(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("location")) 
					a.setLocation(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("jobDescriptions")) 
					a.setJobDescriptions(e.getTextContent());

				if (e.getNodeName().equalsIgnoreCase("department")) 
					a.setDepartment(e.getTextContent());
				
				if (e.getNodeName().equalsIgnoreCase("status")) 
					a.setStatus(JobStatus.valueOf(e.getTextContent()));


			}


			entryList.add(a);

		}

		return entryList;

	}


	public Boolean addNewItem(Job job) {


		NodeList root = this.doc.getElementsByTagName("JobPostings");

		Element items = (Element) root.item(0);


		Element item = doc.createElement("Entry");
		items.appendChild(item);

		Element _jobId = doc.createElement("_jobId");
		_jobId.appendChild(doc.createTextNode(job.get_jobId()));
		item.appendChild(_jobId);

		Element closingDate = doc.createElement("closingDate");
		closingDate.appendChild(doc.createTextNode(job.getClosingDate()));
		item.appendChild(closingDate);

		Element salary = doc.createElement("salary");
		salary.appendChild(doc.createTextNode(job.getSalary()));
		item.appendChild(salary);

		Element positionType = doc.createElement("positionType");
		positionType.appendChild(doc.createTextNode(job.getPositionType()));
		item.appendChild(positionType);

		Element location = doc.createElement("location");
		location.appendChild(doc.createTextNode(job.getLocation()));
		item.appendChild(location);

		Element jobDescriptions = doc.createElement("jobDescriptions");
		jobDescriptions.appendChild(doc.createTextNode(job.getJobDescriptions()));
		item.appendChild(jobDescriptions);
		
		Element department = doc.createElement("department");
		department.appendChild(doc.createTextNode(job.getDepartment()));
		item.appendChild(department);

		Element status = doc.createElement("status");
		status.appendChild(doc.createTextNode(job.getStatus().toString()));
		item.appendChild(status);



		saveFile();

		return true;		


	}

	public void updateItem(Job job) {

		NodeList entryList = this.doc.getElementsByTagName("Entry");


		for (int i = 0; i < entryList.getLength(); i++) {

			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();

			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_jobId") && 
						e.getTextContent().equals(job.get_jobId())) {
					isThisOne = true;
				}
			}

			// update if it is
			if (isThisOne) {

				for (int j = 0; j < entryElements.getLength(); j++) {
					Node e = entryElements.item(j);


					if (job.getClosingDate() != null && 
							!job.getClosingDate().equals("") &&
							e.getNodeName().equalsIgnoreCase("closingDate")) {
						e.setTextContent(job.getClosingDate());
					}

					if (job.getJobDescriptions() != null && 
							!job.getJobDescriptions().equals("") &&
							e.getNodeName().equalsIgnoreCase("jobDescriptions")) {
						e.setTextContent(job.getJobDescriptions());
					}

					if (job.getLocation() != null && 
							!job.getLocation().equals("") &&
							e.getNodeName().equalsIgnoreCase("location")) {
						e.setTextContent(job.getLocation());
					}

					if (job.getPositionType() != null && 
							!job.getPositionType().equals("") &&
							e.getNodeName().equalsIgnoreCase("positionType")) {
						e.setTextContent(job.getPositionType());
					}

					if (job.getSalary() != null && 
							!job.getSalary().equals("") &&
							e.getNodeName().equalsIgnoreCase("salary")) {
						e.setTextContent(job.getSalary());
					}	
					
					if (job.getDepartment() != null && 
							!job.getDepartment().equals("") &&
							e.getNodeName().equalsIgnoreCase("department")) {
						e.setTextContent(job.getDepartment());
					}	


					if (job.getStatus() != null && 
							!job.getStatus().equals("") &&
							e.getNodeName().equalsIgnoreCase("status")) {
						e.setTextContent(job.getStatus().toString());
					}	

				}

				saveFile();


			}	

		}


	}

	public void changeItemStatus(String id, JobStatus status) {

		NodeList entryList = this.doc.getElementsByTagName("Entry");


		for (int i = 0; i < entryList.getLength(); i++) {

			Node n = entryList.item(i);
			NodeList entryElements = n.getChildNodes();

			// Check if this node is the one to update
			Boolean isThisOne = false;
			for (int j = 0; j < entryElements.getLength(); j++) {
				Node e = entryElements.item(j);

				if (e.getNodeName().equalsIgnoreCase("_jobId") && 
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
