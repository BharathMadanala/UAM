package cs.login.Entity;

public class UserRes {
	private String username;
	private String resourceName;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	@Override
	public String toString() {
		return "UserRes [username=" + username + ", resourceName=" + resourceName + "]";
	}
	

}
