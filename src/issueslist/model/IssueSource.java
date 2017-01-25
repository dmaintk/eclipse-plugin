package issueslist.model;

import java.util.HashMap;
import java.util.Map;

public class IssueSource {

	private String id;
	private String connectorClass;
	private Boolean enabled;
	private Map<String, String> fields;

	
	public IssueSource() {
		fields = new HashMap<String, String>();
		enabled = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getConnectorClass() {
		return connectorClass;
	}
	public void setConnectorClass(String connectorClass) {
		this.connectorClass = connectorClass;
	}
	
	public Boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	
}
