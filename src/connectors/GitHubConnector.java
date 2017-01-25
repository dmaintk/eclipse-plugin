package connectors;

import java.util.Collection;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.IssueService;

import issueslist.model.IssueSource;

public class GitHubConnector extends ThreadedConnector {

	private static final int PAGE_SIZE = 20;
	
	private IssueService service;
	
	private IssueSource source;
	private String user;
	private String repository;

	public GitHubConnector(IssueSource source) {
		this.source = source;
		this.user = source.getFields().get("user");
		this.repository = source.getFields().get("repository");
		
		this.service = new IssueService();
	}
	
	@Override
	public void run() {
		PageIterator<Issue> issueIterator = service.pageIssues(user, repository, null, PAGE_SIZE);

		while(issueIterator.hasNext()) {
			Collection<Issue> issuesInPage = issueIterator.next();
			
			for(Issue repoIssue : issuesInPage) {
				issueslist.model.Issue issue = new issueslist.model.Issue();

				issue.setSource(source.getId());
				issue.setTitle(repoIssue.getTitle());
				issue.setDescription(repoIssue.getBody());
				issue.setCreatorName(repoIssue.getUser().getLogin());
				issue.setCreationDate(repoIssue.getCreatedAt());
				
				addIssueToBuffer(issue);
			}
			
            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
		}
	}

	@Override
	public void dispose() {
	}
	
}
