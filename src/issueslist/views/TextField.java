package issueslist.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextField extends Composite {
	
	private Label nameLbl;
	private Text valueTxt;
	
	public TextField(Composite composite, String label) {
		super(composite, SWT.NONE);
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		nameLbl = new Label(this, SWT.NONE);
		nameLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        nameLbl.setText(label+":");

        valueTxt = new Text(this, SWT.BORDER);
        valueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	public void setText(String text) {
		valueTxt.setText(text != null ? text : "");
	}
	
	public String getText() {
		return valueTxt.getText();
	}
	
	public String getLabelText() {
		String text = nameLbl.getText();
		return text.substring(0, text.lastIndexOf(':'));
	}
	
	@Override
	public void dispose() {
		for(Control control : this.getChildren()) {
	        control.dispose();
	    }
		super.dispose();
	}
}
