package issueslist.views;

import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import connectors.ConnectorController;
import issueslist.Constants;
import issueslist.model.Issue;

public class IssuesContentProvider implements ILazyContentProvider {
	
	private static final Boolean GO_TO_PAGE_BOTTOM_ON_UPDATE = false;
	
    private TableViewer viewer;
    public List<Issue> issuesList;
    
	private ConnectorController connectorList;
	
	public IssuesContentProvider(TableViewer viewer) {
		this.viewer = viewer;
		viewer.setContentProvider(this);
		viewer.setUseHashlookup(true);
		
		scanIssues(true);
	}
	
	public void scanIssues(Boolean reScanFile) {
		if(reScanFile) {
			if(connectorList != null) {
				connectorList.dispose();
			}
			connectorList = new ConnectorController(Constants.SOURCES_XML);
		}

		connectorList.scanForIssues();
		issuesList = (List<Issue>) connectorList.getIssuesList();
		viewer.getTable().setSelection(0);
		viewer.setInput(issuesList);
		viewer.setItemCount(issuesList.size());
		
		loadTable();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		issuesList = (List<Issue>) newInput;
	}
	
	@Override
	public void dispose() {
		connectorList.dispose();
	}

	@Override
	public void updateElement(int index) {
        if(!viewer.isBusy() && !issuesList.isEmpty()) {
            viewer.replace(issuesList.get(index), index);
        }
	}
	
	private void loadTable() {
        final Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                viewer.setItemCount(issuesList.size());
                if(GO_TO_PAGE_BOTTOM_ON_UPDATE) {
                	viewer.getTable().setSelection(issuesList.size() - 1);
                	viewer.getTable().showSelection();
                }
            }
        };
        
        Runnable addIssuesToTable = new Runnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                
                while(connectorList.isActive()) {
                    if(System.currentTimeMillis()-currentTime > 100) {
                    	issuesList = (List<Issue>) connectorList.getIssuesList();
                        viewer.getControl().getDisplay().asyncExec(updateUI);
                        currentTime = System.currentTimeMillis();
                    }

                    try {
                        Thread.sleep(0, 100);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                issuesList = (List<Issue>) connectorList.getIssuesList();
                viewer.getControl().getDisplay().asyncExec(updateUI);
            }
        };

        Thread th = new Thread(addIssuesToTable);
        th.start();        
	}
	
}
