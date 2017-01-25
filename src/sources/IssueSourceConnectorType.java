package sources;

public enum IssueSourceConnectorType {
	GITHUB("GitHubConnector", new String[] {"user", "repository"}),
	DATABASE("DataBaseConnector", new String[] {"dbUrl", "user", "pass"});
	
	private final String className;
	private final String[] fields;
	
	private IssueSourceConnectorType(String className, String[] fields) {
		this.className = className;
		this.fields = fields;
	}
	
	public static IssueSourceConnectorType getByClassName(String className) {
		for(IssueSourceConnectorType type : values()) {
			if(type.className.equals(className)) {
				return type;
			}
		}
		return null;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String[] getFields() {
		return fields;
	}
	
}
