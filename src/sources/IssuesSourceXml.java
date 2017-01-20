package sources;

import org.w3c.dom.Element;

public class IssuesSourceXml {

	private Element element;
	
	public IssuesSourceXml(Element element) {
		this.element = element;
	}
	
	public String getField(String name) {
		return element.getElementsByTagName(name).item(0).getTextContent();
	}
	
}
