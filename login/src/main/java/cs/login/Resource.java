package cs.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import cs.login.Entity.Resources;


public class Resource {
	public static String addResources(String res_name) throws Exception {
		try {
			 if (res_name == null || res_name.trim().isEmpty()) {
		            return "Resource name cannot be null or empty.";
		        }
			 if(Resource.find(res_name)) return "Resource already exist"; 
			Connection c = Db.connect();
			String query = "insert into resources(Res_name)values(?)";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, res_name);
			ps.executeUpdate();
		    return "Resource added successfully"; // Return a success message
			
		}
		
		catch(Exception ex) {
			 ex.printStackTrace();
		        return "Error adding resource";
		}		
    }
	
	public static String removeResources(String res_name) throws Exception {
		try {
			Connection c = Db.connect();
			System.out.println(res_name);
			String query = "DELETE FROM resources WHERE Res_name = '?'";
			PreparedStatement ps = c.prepareStatement(query);
			ps.executeUpdate();
		    return "Resource removed successfully";
			
			}
			
		catch(Exception ex) {
				 ex.printStackTrace();
			        return "Error adding resource";
			}		
    }
	
	 public static ArrayList<Resources> getResources() {
	        ArrayList<Resources> resourcesList = new ArrayList<>();
	        try {
	            Connection c = Db.connect();
	            String query = "SELECT distinct * FROM resources";
	            Statement st = c.createStatement();
	            ResultSet rs = st.executeQuery(query);

	            while (rs.next()) {
	                Resources res = new Resources();
	                res.setRes_id(rs.getInt(1));
	                res.setRes_name(rs.getString(2));
	                resourcesList.add(res);
	            }
	            rs.close();
	            st.close();
	            c.close();
	        }
	        catch (Exception ex) {	
	           
	        }

	        return resourcesList;
	    }
//	 public static Response removeRes(String res_name) {
//    	 ObjectMapper objectMapper = new ObjectMapper();
//		 try {
//			 
//			 Connection c = Db.connect();
//				
//		    	String query = "delete from Resources where Res_Name = ?";
//		    	PreparedStatement ps = c.prepareStatement(query);
//		    	ps.setString(1, res_name);
//		    	ps.executeUpdate();
//
//		    	// This creates a object that stores msg variable as done.
//		    	SuccessResponse successResponse = new SuccessResponse("done");
//		    	//converts objects to json
//	            String jsonResponse = objectMapper.writeValueAsString(successResponse);     
//	            return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
//		 }
//		 catch(Exception ex){
//				    ErrorResponse errorResponse = new ErrorResponse("Failed to remove resource: " + ex.getMessage());
//				    try {
//				        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
//				        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//				                .entity(jsonErrorResponse)
//				                .type(MediaType.APPLICATION_JSON)
//				                .build();
//				    } catch (Exception e) {
//				        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//				    
//				}
//
//		 }
//		 
//	 }
	 public static String removeRes(String res_name) throws Exception {
		    if (res_name == null || res_name.trim().isEmpty()) {
		        return "Resource name cannot be null or empty.";
		    }
		    res_name = res_name.trim();
		    String message;

		    // Try-with-resources for Connection and PreparedStatement
		    try (Connection con = Db.connect()) {
		        // Correct SQL queries without single quotes around the placeholder
		        String query1 = "DELETE FROM Resources WHERE Res_Name = ?";
		        String query2 = "DELETE FROM userres WHERE resourceName = ?";
		        String query3 = "DELETE FROM requests  WHERE requestName = ?";

		        // First PreparedStatement
		        try (PreparedStatement deletePst = con.prepareStatement(query1)) {
		            deletePst.setString(1, res_name);
		            int rowsAffected1 = deletePst.executeUpdate();
		            if (rowsAffected1 > 0) {
		                message = "Resource removed successfully.";
		            } else {
		                message = "Resource not found.";
		            }
		        }

		        // Second PreparedStatement
		        try (PreparedStatement dur = con.prepareStatement(query2)) {
		            dur.setString(1, res_name);
		            int rowsAffected2 = dur.executeUpdate();
		            if (rowsAffected2 > 0) {
		                message += " Resource removed successfully from userres.";
		            } else {
		                message += " Resource not found in userres.";
		            }
		        }
		     // Third PreparedStatement
		        try (PreparedStatement dur = con.prepareStatement(query3)) {
		            dur.setString(1, res_name);
		            int rowsAffected3 = dur.executeUpdate();
		            if (rowsAffected3 > 0) {
		                message += " Resource removed successfully from requests.";
		            } else {
		                message += " Resource not found in requests.";
		            }
		        }


		    } catch (SQLException e) {
		        e.printStackTrace();
		        message = "Database error occurred.";
		    }

		    return message;
		}

	 public static ArrayList<String> getResourceUsers(String resname) {
	        ArrayList<String> resourcesList = new ArrayList<>();
	        try {
	            Connection c = Db.connect();
	            String query = "SELECT distinct username FROM userres where resourceName = ?";
	            PreparedStatement ps = c.prepareStatement(query);
	            ps.setString(1,resname);
	            ResultSet rs = ps.executeQuery(); 
	            while (rs.next()) {
	            	 String username = rs.getString(1);
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
	 public static boolean find(String resource) throws Exception {
		    Connection c = null;
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    try {
		        c = Db.connect();
		        String query = "SELECT * FROM resources WHERE Res_name = ?";
		        ps = c.prepareStatement(query);
		        ps.setString(1, resource);

		        rs = ps.executeQuery();
		        return rs.next(); // Returns true if a row is found, otherwise false

		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false; 
		    } finally {
		        try {
		            if (rs != null) rs.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        try {
		            if (ps != null) ps.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        try {
		            if (c != null) c.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}

	 
	 
	 


	
}
