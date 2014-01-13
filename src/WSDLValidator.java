/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * 
 * @author Kaushik Sivaprasad
 */
public class WSDLValidator {
	public static boolean doParseXml(XMLEventReader reader)
			throws XMLStreamException, Exception {
		int count = 0;
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				if (startElement.getName().getLocalPart()
						.equalsIgnoreCase("definitions")) {
					while(reader.hasNext())
					{
						XMLEvent secEvent = reader.nextEvent();
						if (secEvent.isStartElement()) {
							StartElement element2 = secEvent.asStartElement();
							if(element2.getName().getLocalPart().equalsIgnoreCase("message")){
								count++;
								break;
							}
						}
					}
					while(reader.hasNext())
					{
						XMLEvent secEvent = reader.nextEvent();
						if (secEvent.isStartElement()) {
							StartElement element2 = secEvent.asStartElement();
							if(element2.getName().getLocalPart().equalsIgnoreCase("portType")){
								count++;
								break;
							}
						}
					}
				}
			}
			else if(event.isEndElement())
			{
				EndElement element = event.asEndElement();
				if (element.getName().getLocalPart()
						.equalsIgnoreCase("definitions")) {
					if(count == 2)
						return true;

				}
			}
		}
		return false;
	}

	public static boolean validateWSDL(String xml, String url){
		boolean valueToReturn = false;
		XMLEventReader reader = null;
		try {
			byte[] byteArray = xml.getBytes("UTF-8");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			reader = inputFactory.createXMLEventReader(inputStream);
			valueToReturn = doParseXml(reader);
		} catch (XMLStreamException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		finally
		{
			if(reader != null)
			{
				try{
					reader.close();
				}
				catch (XMLStreamException exception) {
					exception.printStackTrace();
					System.out.print("Exception occured.." + exception.getMessage());
				}
			}
		}
		System.out.println("URL : "+ url+" valid xml : "+valueToReturn);
		return valueToReturn;
	}
}
