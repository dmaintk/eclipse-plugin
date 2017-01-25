package issueslist.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import connectors.ConnectorList;
import issueslist.Constants;

public class ViewContentProvider implements IStructuredContentProvider {
	
	private ConnectorList connectorList;
	
	public ViewContentProvider() {
		connectorList = new ConnectorList(Constants.SOURCES_XML);
	}
	
	public void rescanFile() {
		connectorList.dispose();
		connectorList = new ConnectorList(Constants.SOURCES_XML);
	}
	
	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	
	@Override
	public void dispose() {
		connectorList.dispose();
	}
	
	@Override
	public Object[] getElements(Object parent) {
		return connectorList.getIssuesList().toArray();
	}
	
}
