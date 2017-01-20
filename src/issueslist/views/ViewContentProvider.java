package issueslist.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import connectors.ConnectorList;

public class ViewContentProvider implements IStructuredContentProvider {
	
	private ConnectorList connectorList;
	
	public ViewContentProvider() {
		connectorList = new ConnectorList("./res/sources.xml");
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
