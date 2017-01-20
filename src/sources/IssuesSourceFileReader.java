package sources;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import issueslist.Activator;


public class IssuesSourceFileReader {

	private List<IssuesSourceXml> sourceList;
	
	public IssuesSourceFileReader(String fileName) {
		sourceList = new ArrayList<IssuesSourceXml>();
		
		try {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry(fileName);
			File fXmlFile = new File(FileLocator.resolve(fileURL).toURI());
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("source");

			for(int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					sourceList.add(new IssuesSourceXml((Element) nNode));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List<IssuesSourceXml> getSourceList() {
		return sourceList;
	}

}
