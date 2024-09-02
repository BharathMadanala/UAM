package cs.login.Entity;

import java.time.LocalDate;

public class Request {
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	private String resourceName;
	private String status;
	private LocalDate requestDate;
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	 public LocalDate getRequestDate() {
	        return requestDate;
	    }

	    public void setRequestDate(LocalDate requestDate) {
	        this.requestDate = requestDate;
	    }
	@Override
	public String toString() {
		return "Request [username=" + userName + ", resourceName=" + resourceName + ", status=" + status
				+ ", requestDate=" + requestDate + "]";
	}

	
	


}
