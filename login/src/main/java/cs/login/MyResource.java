package cs.login;


import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.print.attribute.standard.Media;
//import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import cs.login.Entity.Emp;
import cs.login.Entity.Request;
import cs.login.Entity.Resources;
import cs.login.Services.Admin;
import cs.login.Services.Manager;
import cs.login.Services.User;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
	 ObjectMapper objectMapper = new ObjectMapper(); 
	 public static String adminBhai;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    //create user
    @POST
    @Path("/addrow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Emp emp) throws Exception {
    	 return User.create(emp);	
    }
    
    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String checkUser(@FormParam("username") String username , @FormParam("password") String password,@Context HttpServletResponse response) throws Exception {
    	adminBhai=User.findAdmin();
    	System.out.println("Admin is :"+adminBhai);
    	return User.authenticate(username,password,response)+User.getRole(username);
    }
    
    @POST
    @Path("/addRes")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public String addResource(@FormParam("res_name") String res_name) throws Exception {
        System.out.println("Resource name: " + res_name);
        // Call your method to add the resource
        return Resource.addResources(res_name);
        
    }
    
    
    @DELETE
    @Path("/removeRes/{res_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remRes(@PathParam("res_name")String res_name) throws Exception { 
    	System.out.println(res_name);
        return Response.ok(Resource.removeRes(res_name)).build();
    
        
    }
    
    //delete user
    @GET
    @Path("/deleteUser/{username}")
    //@Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@PathParam("username")String username) throws Exception { 
        return username; 
    }
    
    @GET
    @Path("/getUserRes/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resources> getUserResources(@PathParam("username")String username) throws Exception {
    	ArrayList<Resources> resources = User.showUserRes(username);
        return resources;	
    }
    
    @GET
    @Path("/getAvailableResources/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resources> getAvailableResources(@PathParam("username")String username) throws Exception {
    	ArrayList<Resources> resources = User.getAvailableResources(username);
        return resources;   
    }
    
    @GET
    @Path("/getResourceUsers/{resname}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getResourceUsers(@PathParam("resname")String resname) throws Exception {
    	ArrayList<String> resourceUsers = Resource.getResourceUsers(resname);
        return resourceUsers;   
    }
    
    
    @DELETE
    @Path("/removeUserRes/{res_name}/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remUserRes(@PathParam("res_name")String res_name,@PathParam("user_name")String user_name) throws Exception { 
//         return  Resource.removeRes(res_name);
        return Response.ok(User.removeUserRes(res_name,user_name)).build();   
    }
    //delete user from team
    @POST
    @Path("/removeUserFromTeam/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromTeam(@PathParam("user_name")String user_name) throws Exception { 
//         return  Resource.removeRes(res_name);
        return Response.ok(User.removeUserFromTeam(user_name)).build();   
    }

    @GET
    @Path("/getRes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resources> getResource() throws Exception {
    	ArrayList<Resources> resources = Resource.getResources();
        return resources;
    }
    
    //get users
    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getUsers() throws Exception {
    	List<String> Users = User.getUsers();
        return Users;
    }
    
    @GET
    @Path("/getTeamMembers/{managerName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> done(@PathParam("managerName")String managerName) throws Exception {
    	ArrayList<String> getTeamMembers = Manager.getTeamMembers(managerName);
        return getTeamMembers;
    }
    
    @GET
    @Path("/addToTeam")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> addToTeam() throws Exception {
    	ArrayList<String> addToTeam = Manager.addToTeam();
        return addToTeam;
    }
    
    
    
    @POST
    @Path("/addMember/{userName}/{managerName}")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public String addResource(@PathParam("userName")String userName,@PathParam("managerName")String managerName) throws Exception {
        return Manager.addMember(userName,managerName);
        
    }
    @POST
    @Path("/changeManager/{userName}/{managerName}")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public String changeManager(@PathParam("userName")String userName,@PathParam("managerName")String managerName) throws Exception {
        return Manager.changeMember(userName,managerName);
        
    }

    @POST
    @Path("/addReq")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public String addRequest(@FormParam("userName") String userName,@FormParam("reqName") String reqName) throws Exception { 
    	return Requests.addRequest(userName,reqName);
    }
    
    //to change
   
    @GET
    @Path("/getUserReq/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Request> getUserRequests(@PathParam("username")String username) throws Exception {
    	List<Request> requests = Requests.getRequests(username);
        return requests;	
    }
   
    @GET
    @Path("/getAllRequests")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Request> getAllRequests() throws Exception {
    	List<Request> requests = Admin.getAllRequests();
        return requests;	
    }
    
    @POST
    @Path("/approveOrRejectRequest/{username}/{resourceName}/{action}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveResource(@PathParam("username") String username, 
    		@PathParam("resourceName") String resourceName, 
    		@PathParam("action") String action) throws Exception {
        return Response.ok(Admin.approveOrRejectRequest(username, resourceName, action)).build();
    }
    
    @POST
    @Path("logout")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Invalidate the session
        }
        return Response.ok().build();
    }
    
    @POST
    @Path("changePassword")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@FormParam("user") String user, @FormParam("psw") String psw) {
        // Print values for debugging
        System.out.println(user + ":" + psw);

        // Call the business logic to change the password
        String result = User.changePassword(user, psw);

        // Prepare JSON response
        String jsonResponse = "{\"message\":\"" + result + "\"}";
        return Response.ok().entity(jsonResponse).build();
    }
    
    @GET
    @Path("/getUserDetails/{username}") // Correct path parameter
    @Produces(MediaType.APPLICATION_JSON)
    public Emp getUserDetails(@PathParam("username") String username) {
        // Implement logic to fetch user details
        return User.getUserDetails(username);
    }
    
    @POST
    @Path("/changeDetails")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public String changeDetails(@FormParam("username") String username,@FormParam("firstname") String firstname,@FormParam("lastname") String lastname,@FormParam("email") String email,@FormParam("phone") String phone) throws Exception {
        return User.changeDetails(username,firstname,lastname,email,phone);
        
    }
    
    //forgot password
    @POST
    @Path("/forgetpassword")
    @Consumes("application/x-www-form-urlencoded")
    public Response forgetPassword(
        @FormParam("username") String username,
        @FormParam("email") String email,
        @FormParam("new-password") String newPassword) throws Exception {
    	System.out.println(username+email+newPassword);
    	newPassword=User.encrypt(newPassword);
        Connection conn = null;
        PreparedStatement checkUserStmt = null;
        PreparedStatement updatePasswordStmt = null;
 
        try {
            // Establish database connection
            conn = Db.connect();
 
            // Check if the username and email are valid
            String checkUserQuery = "SELECT * FROM user WHERE UserName = ? AND Email = ?";
            checkUserStmt = conn.prepareStatement(checkUserQuery);
            checkUserStmt.setString(1, username);
            checkUserStmt.setString(2, email);
 
            ResultSet rs = checkUserStmt.executeQuery();
 
            if (rs.next()) {
                // Username and email are valid, update the password
                String updatePasswordQuery = "UPDATE user SET Passwd = ? WHERE UserName = ?";
                updatePasswordStmt = conn.prepareStatement(updatePasswordQuery);
                updatePasswordStmt.setString(1, newPassword);  // Note: Hash the password before storing in production
                updatePasswordStmt.setString(2, username);
                updatePasswordStmt.executeUpdate();
 
                return Response.ok("Password updated successfully.").build();
            } else {
                // Username and email are not valid
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("Username and email does not match.").build();
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Database error: " + ex.getMessage()).build();
        } finally {
            // Close resources in finally block to ensure they're always closed
            try {
                if (checkUserStmt != null) checkUserStmt.close();
                if (updatePasswordStmt != null) updatePasswordStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException exx) {
                exx.printStackTrace();
            }
        }
    }
    
    
    
    }
		



