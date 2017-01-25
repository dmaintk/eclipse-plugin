package connectors;

import java.util.Collection;

import issueslist.model.Issue;

public interface Connector {
	
	public void scanForIssues();
	
	public Collection<Issue> getIssuesList();
	
	public Boolean isActive();
	
	public void dispose();
	
}
