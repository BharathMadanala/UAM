package cs.login.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import cs.login.Db;

public class Manager {
	 public static ArrayList<String> getTeamMembers(String managerName) {
	        ArrayList<String> resourcesList = new ArrayList<>();
	        try {
	            Connection c = Db.connect();
	            String query = "SELECT UserName FROM user where Manager_UserName = ?";
	            PreparedStatement ps = c.prepareStatement(query);
	            ps.setString(1,managerName);
	            ResultSet rs = ps.executeQuery(); 
	            while (rs.next()) {
	            	 String username = rs.getString(1);
	            	 System.out.println(username);
	            	resourcesList.add(username);
	            }
	            
	            rs.close();
	            ps.close();
	            c.close();
	        }
	        catch (Exception ex) {	
	        	 ex.printStackTrace();
	           
	        }

	        return resourcesList;
	    }
	 public static ArrayList<String> addToTeam() {
	        ArrayList<String> members = new ArrayList<>();
	        try {
	            Connection c = Db.connect();
	            String query = "SELECT UserName FROM user WHERE Manager_UserName IS NULL and role = 'Employee'";
	            Statement st = c.createStatement(); 
	            ResultSet rs = st.executeQuery(query);   
	            while (rs.next()) {
	                String username = rs.getString(1);  
	                members.add(username); 
	            }	            
	            rs.close();
	            st.close();
	            c.close();
	        }
	        catch (Exception ex) {	
	        	 ex.printStackTrace();
	           
	        }

	        return members;
	    }
	 public static String addMember(String username, String managerName) throws Exception {
		    Connection c = Db.connect();
		    String query = "UPDATE user SET Manager_UserName = ? WHERE UserName = ?";
		    PreparedStatement ps = c.prepareStatement(query);
		    ps.setString(1, managerName);
		    ps.setString(2, username);
		    int affectedRows = ps.executeUpdate();
		    if (affectedRows > 0) {
		        return "Manager assigned successfully.";
		    } else {
		        return "Failed to assign manager.";
		    }
		}
	 //change manager name for different manager
	 public static String changeMember(String username, String managerName) throws Exception {
		    Connection c = Db.connect();
		    System.out.println(username+" "+managerName);
		    String query = "UPDATE user SET Manager_UserName = ? WHERE Manager_UserName = ?";
		    PreparedStatement ps = c.prepareStatement(query);
		    ps.setString(1, managerName);
		    ps.setString(2, username);
		    int affectedRows = ps.executeUpdate();
		    if (affectedRows > 0) {
		        return "Manager assigned successfully.";
		    } else {
		        return "Failed to assign manager.";
		    }
		}

	 
}
