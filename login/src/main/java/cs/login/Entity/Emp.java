package cs.login.Entity;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Emp {
	public int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	public int getManager_id() {
		return Manager_id;
	}
	public void setManager_id(int manager_id) {
		Manager_id = manager_id;
	}
	public String getManager_UserName() {
		return Manager_UserName;
	}
	public void setManager_UserName(String manager_UserName) {
		Manager_UserName = manager_UserName;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public LocalDate getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}
	public String username;
	public String firstname;
	public String lastname;
	public String email;
	public String phone;
	public String psw;
	public int Manager_id;
	public String Manager_UserName;
	public String Role;
	private LocalDate requestDate;


	

	
}
