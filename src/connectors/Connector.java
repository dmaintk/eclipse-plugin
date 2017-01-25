package connectors;

import java.util.Collection;

import issueslist.model.Issue;

public interface Connector {

	public Collection<Issue> getIssuesList();
	
	public void dispose();
	
}
