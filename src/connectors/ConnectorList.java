package connectors;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import issueslist.model.Issue;
import issueslist.model.IssueSource;
import sources.IssueSourceXmlIO;

public class ConnectorList implements Connector {

	private Collection<Connector> connectors;
	
	public ConnectorList(String xmlFile) {
		connectors = new ArrayList<Connector>();
		
		IssueSourceXmlIO sourcesIO = new IssueSourceXmlIO(xmlFile);
		for(IssueSource source : sourcesIO.readSourceList()) {
			if(source.isEnabled()) {
				try {
					Class<?> connectorClazz = Class.forName("connectors."+source.getConnectorClass());
					Constructor<?> constructor = connectorClazz.getConstructor(IssueSource.class);
					connectors.add((Connector) constructor.newInstance(source));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Collection<Issue> getIssuesList() {
		Collection<Issue> issues = new ArrayList<Issue>();
		
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
