package se.idega.ehealth.npo.navigation;

import java.util.List;

public class NavigationLink {

	private String url;
	private String name;
	private List children;
	
	public NavigationLink(String url,String name){
		setUrl(url);
		setName(name);
	}
	
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
