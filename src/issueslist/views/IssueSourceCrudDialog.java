package issueslist.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import issueslist.Constants;
import issueslist.model.IssueSource;
import sources.IssueSourceConnectorType;
import sources.IssueSourceXmlIO;

public class IssueSourceCrudDialog extends Dialog {

	private ScrolledComposite scrolledComposite;
	
	private Button newButton;
	private Button removeButton;
	private ComboViewer sourcesCombo;
	private TextField sourceId;
	private Button enabledCheckbox;
	private ComboViewer connectorsCombo;
	private Collection<TextField> connectorFields;
	private Button applyButton;
	
	private IssueSource currSource;
	
	public IssueSourceCrudDialog(Composite parent) {
		super(parent.getShell());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		
        scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        final Composite composite = new Composite(scrolledComposite, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        scrolledComposite.setContent(composite);
        scrolledComposite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        final Composite sourcesPanel = new Composite(composite, SWT.NONE);
        sourcesPanel.setLayout(new GridLayout(4, false));
        sourcesPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        final Label sourcesLbl = new Label(sourcesPanel, SWT.NONE);
        sourcesLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        sourcesLbl.setText("Sources:");
        
		sourcesCombo = new ComboViewer(sourcesPanel, SWT.READ_ONLY);
		sourcesCombo.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sourcesCombo.setContentProvider(ArrayContentProvider.getInstance());
		sourcesCombo.setLabelProvider(new LabelProvider() {
	        @Override
	        public String getText(Object obj) {
	            return ((IssueSource) obj).getId();
	        }
	    });
        
		newButton = new Button(sourcesPanel, SWT.NONE);
		newButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		newButton.setText("New");
	    
		removeButton = new Button(sourcesPanel, SWT.NONE);
		removeButton.setText("Remove");
		
		sourceId = new TextField(composite, "Source Id");
		
		final Composite checkboxPanel = new Composite(composite, SWT.NONE);
		checkboxPanel.setLayout(new GridLayout(2, false));
		checkboxPanel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
        final Label enabledLbl = new Label(checkboxPanel, SWT.NONE);
        enabledLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        enabledLbl.setText("Enabled:");
        
		enabledCheckbox = new Button(checkboxPanel, SWT.CHECK);
		enabledCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		final Composite connectorPanel = new Composite(composite, SWT.NONE);
        connectorPanel.setLayout(new GridLayout(2, false));
        connectorPanel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
        final Label connectorsLbl = new Label(connectorPanel, SWT.NONE);
        connectorsLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        connectorsLbl.setText("Connector:");
        
		connectorsCombo = new ComboViewer(connectorPanel, SWT.READ_ONLY);
		connectorsCombo.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		connectorsCombo.setContentProvider(ArrayContentProvider.getInstance());
		connectorsCombo.setLabelProvider(new LabelProvider() {
	        @Override
	        public String getText(Object obj) {
	            return ((IssueSourceConnectorType) obj).getClassName();
	        }
	    });		
		connectorsCombo.setInput(IssueSourceConnectorType.values());
        
        final Composite panel_3 = new Composite(composite, SWT.NONE);
        panel_3.setLayout(new GridLayout());
        panel_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
        connectorFields = new ArrayList<TextField>();
		
		applyButton = new Button(composite, SWT.NONE);
		applyButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		applyButton.setText("Apply");
		
		initListeners(panel_3);
		
	    IssueSourceXmlIO sourcesIO = new IssueSourceXmlIO(Constants.SOURCES_XML);
	    sourcesCombo.setInput(sourcesIO.readSourceList());
		sourcesCombo.setSelection(new StructuredSelection(sourcesCombo.getElementAt(0)), true);
				
		return container;
	}

	private void initListeners(final Composite dynamicFieldsPanel) {
	    sourcesCombo.addSelectionChangedListener(new ISelectionChangedListener() {
	        @Override
	        public void selectionChanged(SelectionChangedEvent event) {
	            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
	            IssueSource source = (IssueSource) selection.getFirstElement();
	    		
	            if(source != null) {
	            	currSource = source;
		            sourceId.setText(currSource.getId());
		            enabledCheckbox.setSelection(currSource.isEnabled());
		    		IssueSourceConnectorType defaultType = IssueSourceConnectorType.getByClassName(currSource.getConnectorClass());
		    		connectorsCombo.setSelection(new StructuredSelection(defaultType), true);
	
		            sourcesCombo.refresh();
	            }
	        }
	    });
	    
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IssueSource newSource = new IssueSource();
				newSource.setId("New source");
				newSource.setConnectorClass(IssueSourceConnectorType.values()[0].getClassName());
				
				sourcesCombo.add(newSource);  // lo agrega solo a la vista, no afecta la lista
				sourcesCombo.setSelection(new StructuredSelection(newSource), true);
			}
		});
		
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String msg = "You're about to remove source '"+currSource.getId()+"'. Continue?";
				if(showConfirmation(msg)) {
					Collection<IssueSource> input = (Collection<IssueSource>) sourcesCombo.getInput();
					input.remove(currSource);
					sourcesCombo.setInput(input);
					if(sourcesCombo.getCombo().getItemCount() > 0) {
						sourcesCombo.setSelection(new StructuredSelection(sourcesCombo.getElementAt(0)), true);
					}
					else {
						sourceId.setText("");
						enabledCheckbox.setSelection(false);
						connectorsCombo.setSelection(new StructuredSelection(IssueSourceConnectorType.values()[0]), true);
					}
					sourcesCombo.refresh();
				}
			}
		});
		
		connectorsCombo.addSelectionChangedListener(new ISelectionChangedListener() {
	        @Override
	        public void selectionChanged(SelectionChangedEvent event) {
	            IStructuredSelection selection = (IStructuredSelection) event.getSelection();
	            IssueSourceConnectorType connector = (IssueSourceConnectorType) selection.getFirstElement();

	            for(TextField field : connectorFields) {
	            	field.dispose();
	            }
	            connectorFields.clear();
	            
				for(String tag : connector.getFields()) {
					TextField field = new TextField(dynamicFieldsPanel, tag);
					if(currSource != null && connector.getClassName().equals(currSource.getConnectorClass())) {
						field.setText(currSource.getFields().get(tag));
					}
					connectorFields.add(field);
				}

				resetLayout();
				connectorsCombo.refresh();
	        }
	    });
		
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currSource.setId(sourceId.getText());
				currSource.setEnabled(enabledCheckbox.getSelection());
				StructuredSelection sel = (StructuredSelection) connectorsCombo.getSelection();
				IssueSourceConnectorType connector = (IssueSourceConnectorType) sel.getFirstElement();
				currSource.setConnectorClass(connector.getClassName());
				
				for(TextField field : connectorFields) {
					currSource.getFields().put(field.getLabelText(), field.getText());
				}
				
				Boolean isNewSource = true;
				Collection<IssueSource> input = (Collection<IssueSource>) sourcesCombo.getInput();
				for(int i=0; i<input.size(); i++) {  // chequeo si es un source nuevo o modificado
					if(sourcesCombo.getElementAt(i).equals(currSource)) {
						isNewSource = false;
						break;
					}
				}
				
				if(isNewSource) {
					input.add(currSource);
					sourcesCombo.setInput(input);
					sourcesCombo.setSelection(new StructuredSelection(currSource), true);
					showMessage("Source '"+currSource.getId()+"' has been created!");
				}
				else {
					showMessage("Source '"+currSource.getId()+"' has been modified!");
				}
				
				sourcesCombo.refresh();
			}
		});
	}
	
	private void resetLayout() {
		scrolledComposite.layout(true, true);
        scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Issues - Source Configuration");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Save to xml", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
	}

    @Override
    protected void okPressed() {
    	IssueSourceXmlIO sourceIO = new IssueSourceXmlIO(Constants.SOURCES_XML);
    	sourceIO.writeSourceList((Collection<IssueSource>) sourcesCombo.getInput());
    	super.okPressed();
    }
    
	private void showMessage(String message) {
		MessageDialog.openInformation(this.getShell(), this.getShell().getText(), message);
	}
	
	private Boolean showConfirmation(String message) {
		return MessageDialog.openConfirm(this.getShell(), this.getShell().getText(), message);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(410, 460);
	}
	
	@Override
	protected boolean isResizable() {
	    return false;
	}

}