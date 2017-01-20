package connectors;

import java.util.List;

public interface Connector {

	public List<Issue> getIssuesList();
	
	public void dispose();
	
}
