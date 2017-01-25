package connectors;

import java.util.ArrayList;
import java.util.Collection;

import issueslist.model.Issue;

public abstract class ThreadedConnector implements Runnable, Connector {
	
	private Thread currThread;
	private Collection<Issue> issuesBuffer;
	
	@Override
	public void scanForIssues() {
		issuesBuffer = new ArrayList<Issue>();
		currThread = new Thread(this);
		currThread.start();
	}
	
	@Override
	public Collection<Issue> getIssuesList() {
		ArrayList<Issue> issues = new ArrayList<Issue>(issuesBuffer);
		issuesBuffer.clear();
		return issues;
	}

	@Override
	public Boolean isActive() {
		return currThread.isAlive();
	}
	
	protected void addIssueToBuffer(Issue issue) {
		issuesBuffer.add(issue);
	}

}
