package issueslist.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import issueslist.model.Issue;

import org.eclipse.jface.viewers.*;

import java.text.SimpleDateFormat;

import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;


public class IssuesView extends ViewPart {

	public static final String ID = "issueslist.views.IssuesView";

	private TableViewer viewer;
	private IssueSourceCrudDialog dialog;
	private ViewContentProvider contentProvider;
	private Action refreshList;
	private Action openIssuesSourceCrudDialog;


	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		createColumns();
		
		contentProvider = new ViewContentProvider(); 
		viewer.setContentProvider(contentProvider);
		viewer.setInput(getViewSite());  // esto llama a getElements()

		getSite().setSelectionProvider(viewer);
		dialog = new IssueSourceCrudDialog(viewer.getControl().getShell());

		initActions();
		initActionBars();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void createColumns() {
        String[] titles = { "Title", "Description", "User", "Created On", "Source Id" };
        int[] sizes = { 220, 240, 100, 180, 100 };

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
		
		TableViewerColumn colSource = createTableViewerColumn(titles[4], sizes[4]);
		colSource.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
                return ((Issue) element).getSource();
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
		refreshList = new Action() {
			@Override
			public void run() {
				String msg = "Do you wish to also rescan config file?";
				if(showQuestion(msg)) {
					contentProvider.rescanFile();
				}
				viewer.refresh();  // esto tambien llama a getElements()
			}
		};

		refreshList.setToolTipText("Refresh list");
		refreshList.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
		
		openIssuesSourceCrudDialog = new Action() {
			@Override
			public void run() {
				dialog.open();
			}
		};
		
		openIssuesSourceCrudDialog.setToolTipText("Configure sources");
		openIssuesSourceCrudDialog.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_DEF_PERSPECTIVE));
	}
	
	private Boolean showQuestion(String message) {
		return MessageDialog.openQuestion(viewer.getControl().getShell(), "Issues List", message);
	}

	private void initActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(openIssuesSourceCrudDialog);
		bars.getToolBarManager().add(refreshList);
	}
	
}
