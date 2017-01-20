package connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.service.IssueService;

import sources.IssuesSourceXml;

public class GitHubConnector implements Connector {

	private IssueService service;
	private String user;
	private String repository;
	
	public GitHubConnector(IssuesSourceXml source) {
		this(source.getField("user"), source.getField("repository"));
	}
	
	public GitHubConnector(String user, String repository) {
		this.service = new IssueService();
		this.user = user;
		this.repository = repository;
	}
	
	public static void main(String[] args) {
		GitHubConnector con = new GitHubConnector("dmaintk", "eclipse-plugin");
		for(connectors.Issue i : con.getIssuesList()) {
			System.out.println(i.getTitle());
		}
	}

	@Override
	public List<connectors.Issue> getIssuesList() {
		List<connectors.Issue> issues = new ArrayList<connectors.Issue>();
		
		try {
			Collection<Issue> repoIssues = service.getIssues(user, repository, null);
		
			for(Issue repoIssue : repoIssues) {
				connectors.Issue issue = new connectors.Issue();

				issue.setSource("Github");
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
