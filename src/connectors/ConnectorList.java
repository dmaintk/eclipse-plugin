package connectors;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import sources.IssuesSourceXml;
import sources.IssuesSourceFileReader;

public class ConnectorList implements Connector {

	private List<Connector> connectors;
	
	public ConnectorList(String xmlFile) {
		connectors = new ArrayList<Connector>();
		
		IssuesSourceFileReader sourcesReader = new IssuesSourceFileReader(xmlFile);
		for(IssuesSourceXml source : sourcesReader.getSourceList()) {
			try {
				Class<?> exceptionClazz = Class.forName("connectors."+source.getField("connectorClass"));
				Constructor<?> constructor = exceptionClazz.getConstructor(IssuesSourceXml.class);
				connectors.add((Connector) constructor.newInstance(source));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Issue> getIssuesList() {
		List<Issue> issues = new ArrayList<Issue>();
		
		for(Connector connector : connectors) {
			issues.addAll(connector.getIssuesList());
		}
		
		return issues;
	}

	@Override
	public void dispose() {
		for(Connector connector : connectors) {
			connector.dispose();
		}
	}
	
}
