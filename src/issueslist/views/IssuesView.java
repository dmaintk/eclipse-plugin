package issueslist.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import connectors.Issue;

import org.eclipse.jface.viewers.*;

import java.text.SimpleDateFormat;

import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;


public class IssuesView extends ViewPart {

	public static final String ID = "issueslist.views.IssuesView";

	private TableViewer viewer;
	private Action refresh;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		viewer.setContentProvider(new ViewContentProvider());
		createColumns();
		viewer.setInput(getViewSite());  // esto llama a getElements()
		getSite().setSelectionProvider(viewer);
		
		initActions();
		initActionBars();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void createColumns() {
        String[] titles = { "Title", "Description", "User", "Created on" };
        int[] sizes = { 200, 200, 100, 180 };

		TableViewerColumn colTitle = createTableViewerColumn(titles[0], sizes[0]);
		colTitle.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
                return ((Issue) element).getTitle();
	        }
		});
		
		TableViewerColumn colDescription = createTableViewerColumn(titles[1], sizes[1]);
		colDescription.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
                return ((Issue) element).getDescription();
	        }
		});
		
		TableViewerColumn colUser = createTableViewerColumn(titles[2], sizes[2]);
		colUser.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
                return ((Issue) element).getCreatorName();
	        }
		});
		
		TableViewerColumn colCreationDate = createTableViewerColumn(titles[3], sizes[3]);
		colCreationDate.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                return format.format(((Issue) element).getCreationDate());
	        }
		});
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int width) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
		col.getColumn().setText(title);
		col.getColumn().setWidth(width);
		return col;
	}
	
	private void initActions() {
		refresh = new Action() {
			@Override
			public void run() {
				viewer.refresh();  // esto tambien llama a getElements()
				showMessage("Refresh executed");
			}
		};

		refresh.setToolTipText("Refresh list");
		refresh.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Issues View",
			message);
	}

	private void initActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(refresh);
	}
	
}
