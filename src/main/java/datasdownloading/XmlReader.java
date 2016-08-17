package main.java.datasdownloading;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import main.java.datasdownloading.entities.CampaignHeader;
import main.java.datasdownloading.entities.CampaignStatus;
import main.java.datasdownloading.entities.Record;
import main.java.utils.Utils;

public class XmlReader {

    public static String getSessionID(String xmlDatas) throws LoginException {
        String sessionID = "";

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("OpenSession");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if ("OK".equals(status)) {
                    sessionID = eElement.getElementsByTagName("sessionID")
                            .item(0).getTextContent();
                } else {
                    throw new LoginException(
                            eElement.getElementsByTagName("errorDescription")
                                    .item(0).getTextContent());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return sessionID;
    }

    public static List<CampaignHeader> getHeaderList(String xmlDatas)
            throws LoginException {
        List<CampaignHeader> headerList = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("campaign");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String campaignID = eElement
                            .getElementsByTagName("campaignID").item(0)
                            .getTextContent();
                    String campaignName = eElement.getElementsByTagName("name")
                            .item(0).getTextContent();
                    String clientName = eElement
                            .getElementsByTagName("clientName").item(0)
                            .getTextContent();
                    CampaignStatus campaignStatus = CampaignStatus
                            .valueOf(eElement.getElementsByTagName("status")
                                    .item(0).getTextContent().toUpperCase());
                    Date creationDate = new Date(Long.parseLong(
                            eElement.getElementsByTagName("creationTS").item(0)
                                    .getTextContent())
                            * 1000);
                    Date startDate = new Date(Long
                            .parseLong(eElement.getElementsByTagName("startTS")
                                    .item(0).getTextContent())
                            * 1000);
                    Date endDate = new Date(Long
                            .parseLong(eElement.getElementsByTagName("endTS")
                                    .item(0).getTextContent())
                            * 1000);

                    headerList.add(new CampaignHeader(campaignID, campaignName,
                            clientName, campaignStatus, creationDate, startDate,
                            endDate));

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return headerList;
    }

    public static List<Record> getAllRecords(String xmlRecords) throws LoginException {
    	List<Record> records = new ArrayList<>();
    	
    	try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlRecords.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }
            
            
            NodeList nList = doc.getElementsByTagName("statisticsRecord");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String publisherName = eElement
                            .getElementsByTagName("publisherName").item(0)
                            .getTextContent();
                    String countryName = eElement
                            .getElementsByTagName("countryName").item(0)
                            .getTextContent();
                    
                    int impressions = Utils.parseInt(eElement
                            .getElementsByTagName("impressions").item(0)
                            .getTextContent());
                    
                    int clicks = Utils.parseInt(eElement
                            .getElementsByTagName("clicks").item(0)
                            .getTextContent());
                    
                    records.add(new Record(publisherName, countryName, impressions, clicks));
                }
            }
    	} catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    	
    	return records;
            
    }

    
public static String getStatus(String xmlDatas) {
        

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                return status;
            }
        } catch (Exception e) {
        }

        return "";

    }
    
}
