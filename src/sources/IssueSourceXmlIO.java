package sources;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import issueslist.Activator;
import issueslist.model.IssueSource;


public class IssueSourceXmlIO {

	private static final String ROOT_ELEMENT = "sources";
	private static final String ELEMENT = "source";
	private static final String ID_ATTR = "id";
	private static final String CONNECTOR_ATTR = "connector";
	private static final String ENABLED_ATTR = "enabled";
	
	private File xmlFile;
	
	public IssueSourceXmlIO(String fileName) {
		try {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry(fileName);
			xmlFile = new File(FileLocator.resolve(fileURL).toURI());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Collection<IssueSource> readSourceList() {
		Collection<IssueSource> sourceList = new ArrayList<IssueSource>();
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList sourceElements = doc.getElementsByTagName(ELEMENT);
			for(int i = 0; i < sourceElements.getLength(); i++) {
				Node sNode = sourceElements.item(i);
				if(sNode.getNodeType() == Node.ELEMENT_NODE) {
					Element sourceElement = (Element) sNode;
					IssueSource issueSource = new IssueSource();

					issueSource.setId(sourceElement.getAttribute(ID_ATTR));
					issueSource.setConnectorClass(sourceElement.getAttribute(CONNECTOR_ATTR));
					issueSource.setEnabled(Boolean.parseBoolean(sourceElement.getAttribute(ENABLED_ATTR)));

					NodeList fieldElements = sourceElement.getChildNodes();
					for(int j = 0; j< fieldElements.getLength(); j++) {
						Node fNode = fieldElements.item(j);
						if(fNode.getNodeType() == Node.ELEMENT_NODE) {
							Element fieldElement = (Element) fNode;
							issueSource.getFields().put(fieldElement.getTagName(), 
									fieldElement.getTextContent());
						}
					}

					sourceList.add(issueSource);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return sourceList;
	}
	
	public void writeSourceList(Collection<IssueSource> sourceList) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(rootElement);
 
            for(IssueSource issueSource : sourceList) {
                Element source = doc.createElement(ELEMENT);

                source.setAttribute(ID_ATTR, issueSource.getId());
                source.setAttribute(CONNECTOR_ATTR, issueSource.getConnectorClass());
                source.setAttribute(ENABLED_ATTR, issueSource.isEnabled().toString());

                IssueSourceConnectorType connetorType = IssueSourceConnectorType.getByClassName(issueSource.getConnectorClass());
                for(String tag : connetorType.getFields()) {
                    Element node = doc.createElement(tag);
                    node.appendChild(doc.createTextNode(issueSource.getFields().get(tag)));
                	source.appendChild(node);
                }

            	rootElement.appendChild(source);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(xmlFile);
            transformer.transform(source, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
