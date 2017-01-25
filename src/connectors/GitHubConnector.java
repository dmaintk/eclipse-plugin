package connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;

import issueslist.model.IssueSource;

public class GitHubConnector implements Connector {

	private String sourceId;
	private IssueService service;
	private String user;
	private String repository;
	
	public GitHubConnector(IssueSource source) {
		this(source.getFields().get("user"), 
			source.getFields().get("repository"));
		sourceId = source.getId();
	}
	
	public GitHubConnector(String user, String repository) {
		this.service = new IssueService();
		this.user = user;
		this.repository = repository;
	}
	
	public static void main(String[] args) {
		GitHubConnector con = new GitHubConnector("dmaintk", "eclipse-plugin");
		for(issueslist.model.Issue i : con.getIssuesList()) {
			System.out.println(i.getTitle());
		}
	}

	@Override
	public Collection<issueslist.model.Issue> getIssuesList() {
		Collection<issueslist.model.Issue> issues = new ArrayList<issueslist.model.Issue>();
		
		try {
			Collection<Issue> repoIssues = service.getIssues(user, repository, null);
		
			for(Issue repoIssue : repoIssues) {
				issueslist.model.Issue issue = new issueslist.model.Issue();

				issue.setSource(sourceId);
				issue.setTitle(repoIssue.getTitle());
				issue.setDescription(repoIssue.getBody());
				issue.setCreatorName(repoIssue.getUser().getLogin());
				issue.setCreationDate(repoIssue.getCreatedAt());
				
				issues.add(issue);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return issues;
	}
	
	@Override
	public void dispose() {
	}
	
}
