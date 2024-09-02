package cs.login.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import cs.login.Db;
import cs.login.Entity.Request;
import cs.login.Util.Response.ErrorResponse;
import cs.login.Util.Response.SuccessResponse;

public class Admin {
	public static List<Request> getAllRequests() {
	    List<Request> requests = new ArrayList<>();
	    Connection c = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        c = Db.connect();
	        String query = "SELECT distinct userName , requestName , status, requestDate FROM requests where status = 'Pending' ";
	        ps = c.prepareStatement(query);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            Request req = new Request();
	            req.setUserName(rs.getString(1));
	            req.setResourceName(rs.getString(2));
	            req.setStatus(rs.getString(3));
	            
	            java.sql.Date sqlDate = rs.getDate(4);
	            if (sqlDate != null) {
	                req.setRequestDate(sqlDate.toLocalDate()); // Convert SQL Date to LocalDate
	            } else {
	                req.setRequestDate(null);
	            }

	            System.out.println(req);
	            requests.add(req);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace(); // Log the exception for debugging
	    } finally {
	        // Ensure resources are closed
	        try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
	        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
	        try { if (c != null) c.close(); } catch (Exception e) { e.printStackTrace(); }
	    }

	    return requests;
	}
	
	public static Response approveOrRejectRequest(String userName, String requestName, String action) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    try (Connection c = Db.connect()) {
	    	
	        String query = "UPDATE requests SET status = ? WHERE username = ? and requestName = ?";
	        try (PreparedStatement ps = c.prepareStatement(query)) {
	            ps.setString(1, action);
	            ps.setString(2, userName);
	            ps.setString(3, requestName);
	            int affectedRows = ps.executeUpdate();
	            
	            
	            if (affectedRows > 0) {
	            	if(action.equals("approve") && (requestName.equals("Admin")||requestName.equals("Manager"))) User.changeRole(userName,requestName);
	            	else if(action.equals("approve")) User.addResources(userName,requestName);
	            	
	                SuccessResponse successResponse = new SuccessResponse(action + " " + requestName + " for " + userName);
	                String jsonResponse = objectMapper.writeValueAsString(successResponse);
	                return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
	            	
	            } else {
	                ErrorResponse errorResponse = new ErrorResponse("No rows affected. Possible issue with request data.");
	                String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
	                return Response.status(Response.Status.NOT_FOUND)
	                        .entity(jsonErrorResponse)
	                        .type(MediaType.APPLICATION_JSON)
	                        .build();
	            }
	        }
	    } catch (Exception ex) {
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
	        try {
	            String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity(jsonErrorResponse)
	                    .type(MediaType.APPLICATION_JSON)
	                    .build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	}

	//add roles
	//view roles
	//change user role
	//accept or 

}
