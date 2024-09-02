package cs.login.Services;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs.login.Db;
import cs.login.MyResource;
import cs.login.Entity.Emp;
import cs.login.Entity.Resources;
import cs.login.Util.Response.ErrorResponse;
import cs.login.Util.Response.SuccessResponse;
public class User {
	public static String count_user(String user) throws Exception{
		try {
			Connection c = Db.connect();
			String query = "select count(*) from user where UserName like ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1,user+"%");
			ResultSet rs = ps.executeQuery();
			int ct = 0;
			if(rs.next())
			{
				ct = rs.getInt(1);
			} 
			if(ct == 0) return user;
			return user+ct;
		}
		catch(Exception ex)
		{
			return ex.getMessage();
		}
		
	}
	public static String encrypt(String pwd) {
		String str[] = "ABCDEFGHI JKLMNOPQR STUVWXYZa bcdefghij klmnopqrs tuvwxyz01 23456789 ~!@#$%^&* ()-_=+[{] }|;:',<.> /?".split(" ");
		String ans = "";
		for(int i = 0 ; i < pwd.length(); i++)
		{
			char ch = pwd.charAt(i);
			for(String s:str)
			{
				if(s.indexOf(ch) == -1) continue;
				ans += s.indexOf(ch)+1;
			}
			
		}
		return ans;
	}
	public static String changePassword(String user,String password) {
		try {
    		Connection c = Db.connect();
    		String query = "update user set Passwd=? where UserName = ?";
        	PreparedStatement pt = c.prepareStatement(query);
    		pt.setString(1, encrypt(password));

    		pt.setString(2, user);
    		int res = pt.executeUpdate();
    		
    		return "Password Updated"+res;
    	}
    	catch(Exception ex) {
    		return ex.getMessage();
    	}
		
		
	}
		//String password = "supersecret";	
		
//		        
//		        // Hash the password and store it
//		        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//		        //BCrypt.checkpw(password, storedHashedPassword)
//		        return hashedPassword;
		       
		    
	public static String authenticate(String username , String password,@Context HttpServletResponse response) throws Exception {
		try{
			Connection c = Db.connect();
		
    	String query = "select UserName from user where username = ? and Passwd = ?";
    	PreparedStatement ps = c.prepareStatement(query);
    	ps.setString(1, username);
    	ps.setString(2, encrypt(password));
    	ResultSet rs = ps.executeQuery();
    	if(rs.next()) {
    		if(rs.getString(1).equals(username)) {
    			String role = getRole(username);
    			System.out.println(role);
    			if(role.equals("Admin"))
    			{
    				
//    				response.sendRedirect("http://localhost:9009/login/Admin.html");
    				//redirectUrl = "http://localhost:9009/login/Admin.html?username=" + URLEncoder.encode(username, "UTF-8");
    				response.sendRedirect("http://localhost:9009/login/Admin.html?username=" + URLEncoder.encode(username, "UTF-8"));

    			}
    			else if(role.equalsIgnoreCase("Employee"))
    			{
    				//redirectUrl = ;
    				response.sendRedirect("http://localhost:9009/login/User.html?username=" + URLEncoder.encode(username, "UTF-8"));
//    				response.sendRedirect("http://localhost:9009/login/Employee.html");
    			}
    			
				response.sendRedirect("http://localhost:9009/login/Manager.html?username=" + URLEncoder.encode(username, "UTF-8"));

//    				response.sendRedirect("http://localhost:9009/login/Manager.html");

    			}	
                return null;
    		}
    		
    		
    	
    	return "Incorrect username or password";
		}
		catch(Exception ex) { return ex.getMessage();}
    	
		
	}
	
    @Produces(MediaType.APPLICATION_JSON)
    public static Response create(Emp emp) throws Exception {
    	 ObjectMapper objectMapper = new ObjectMapper();
    	try {
    		String count=User.count_user(emp.firstname+"."+emp.lastname);
    		Connection c = Db.connect();
    		String query = "INSERT INTO user (UserName,FirstName, LastName, PhoneNo, Email, Passwd,Role,Date) VALUES(?,?,?,?,?,?,?,?)";
    		PreparedStatement ps = c.prepareStatement(query);
                ps.setString(1,count);
                ps.setString(2,emp.firstname);
                ps.setString(3,emp.lastname);
                ps.setString(4,emp.phone);
                ps.setString(5,emp.email);
                ps.setString(6,User.encrypt(emp.psw));
                if (Integer.parseInt(countUsers()) == 0) {
                    ps.setString(7, "Admin");
                  
                } else {
                    ps.setString(7, "Employee");
                }
                ps.setDate(8,  Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
                
                SuccessResponse successResponse = new SuccessResponse(count);
                String jsonResponse = objectMapper.writeValueAsString(successResponse);
                
                return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    		}

		    	catch(Exception ex)
		    	{
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
    
    //@Produces(MediaType.APPLICATION_JSON)
    public static Response deleteUser(String username) throws Exception {
    	 ObjectMapper objectMapper = new ObjectMapper();
    	try {
    		Connection c = Db.connect();
    		String query = "delete from user where UserName=?";
    		PreparedStatement ps = c.prepareStatement(query);
    		ps.setString(1, username);
            ps.executeUpdate();
            SuccessResponse successResponse = new SuccessResponse("deleted user"+username);
            String jsonResponse = objectMapper.writeValueAsString(successResponse);
            
            return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
			}

	    	catch(Exception ex)
	    	{
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
    
    public static String getRole(String username) {
    	try {
    		Connection c = Db.connect();
    		String query = "select Role from user where UserName = ?";
    		PreparedStatement pt = c.prepareStatement(query);
    		pt.setString(1, username);
    		ResultSet rs = pt.executeQuery();
    		if(rs.next()) {
    			
    			return rs.getString(1);
    		}
    		return "";
    	}
    	catch(Exception ex) {
    		return ex.getMessage();
    	}
    }
    //find admin
    public static String findAdmin() {
    	try {
    		Connection c = Db.connect();
    		String query = "select UserName from user where Role = 'Admin' and Manager_UserName is null limit 1";
    		Statement st = c.createStatement();
    		ResultSet rs = st.executeQuery(query);
    		if(rs.next()) {
    			return rs.getString(1);
    		}
    		return "";
    	}
    	catch(Exception ex) {
    		return ex.getMessage();
    	}
    	
    }
    
    public static String countUsers() {
    	try {
    		Connection c = Db.connect();
    		String query = "select count(*) from user";
    		Statement st = c.createStatement();
    		ResultSet rs = st.executeQuery(query);
    		 if (rs.next()) {
    			 return rs.getInt(1)+"";  
    		 }
    		 return 0+"";
    		
    	}
    	catch(Exception ex)
    	{
    		return ex.getMessage();
    	}
    	
    }
    
    public static ArrayList<Resources>showUserRes(String username) {
    	
    	 ArrayList<Resources> resourcesList = new ArrayList<>();
    	 ResultSet rs = null;
	        try {
	            Connection c = Db.connect();
	            String query = "SELECT resourceName FROM userres where username=?";
				PreparedStatement ps = c.prepareStatement(query);
	            ps.setString(1, username);
//executeQuery which checking for a param and executeUpdate when changing the row or deleting
				rs = ps.executeQuery();

	            while (rs.next()) {
	                Resources res = new Resources();
	                res.setRes_name(rs.getString(1));
	                resourcesList.add(res);
	            }
	            rs.close();
	            ps.close();
	            c.close();
	        }
	        catch (Exception ex) {	
	           
	        }

	        return resourcesList;
    }
    //getAvailableResources to user
    public static ArrayList<Resources>getAvailableResources(String username) {
   	 ArrayList<Resources> resourcesList = new ArrayList<>();
   	 ResultSet rs = null;
	        try {
	            Connection c = Db.connect();
	           String query = "select Res_name from resources where Res_name not in (select resourceName from userres where username = ?) ";
				PreparedStatement ps = c.prepareStatement(query);
	            ps.setString(1, username);

//executeQuery which checking for a param and executeUpdate when changing the row or deleting
				rs = ps.executeQuery();

	            while (rs.next()) {
	                Resources res = new Resources();
	                res.setRes_name(rs.getString(1));
	                resourcesList.add(res);
	            }
	            rs.close();
	            ps.close();
	            c.close();
	        }
	        catch (Exception ex) {	
	           
	        }

	        return resourcesList;
   }
    //function to add resource when the request was accepted
    public static String addToUserResources(String res_name) throws Exception {
		try {
	Connection c = Db.connect();
	String query = "insert into userres(Res_name)values(?)";
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
	
    
    
    
    @Produces(MediaType.APPLICATION_JSON)
	 public static String removeUserRes(String res_name,String user_name) throws Exception {
	        if (res_name == null || res_name.trim().isEmpty()) {
	            return "Resource name cannot be null or empty.";
	        }
	        res_name = res_name.trim();
	        String message;
	        try (Connection con = Db.connect()) {
		    	String query = "delete from userres where resourceName = ? and username = ?";
	            try (PreparedStatement deletePst = con.prepareStatement(query)) {
	                deletePst.setString(1, res_name);
	                deletePst.setString(2, user_name);
	                int rowsAffected = deletePst.executeUpdate();
	                if (rowsAffected > 0) {
	                    message = "Resource removed successfully.";

	                } else {
	                    message = "Resource not found.";
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                message = "Database error occurred.";
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            message = "Error connecting to the database.";
	        }
	        return message;
	    }
    // remove user form team
    public static String removeUserFromTeam(String user_name) throws Exception {
        
    	user_name = user_name.trim();
        String message;
    	try {
    		Connection c = Db.connect();
    		String query = "update user set Manager_UserName = null where UserName = ?";
        	PreparedStatement pt = c.prepareStatement(query);

    		pt.setString(1, user_name);
    		int res = pt.executeUpdate();
    		
    		return user_name+" removed";
    	}
    	catch(Exception ex) {
    		return ex.getMessage();
    	}
    }
    //
    public static void addResources(String username, String resourceName) throws Exception {
		try {
	Connection c = Db.connect();
	String query = "insert into userres(username,resourceName)values(?,?)";
	PreparedStatement ps = c.prepareStatement(query);
	ps.setString(1, username);
	ps.setString(2, resourceName);

	ps.executeUpdate();
	
	}
	
	catch(Exception ex) {
		
	}		
    }
    //change mananger of manager to admin
    public static void changeManager(String userName,String managerName) {
    	try {
			Connection c = Db.connect();
			String query = "UPDATE user SET Manager_UserName = ? WHERE UserName = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, managerName);
			ps.setString(2, userName);
			ps.executeUpdate();

			}
			
			catch(Exception ex) {
				
			}	
    	
    }
    public static void changeRole(String userName, String roleName) throws Exception {
		try {
			Connection c = Db.connect();
			String query = "UPDATE user SET Role = ? WHERE UserName = ?";
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, roleName);
			ps.setString(2, userName);
			ps.executeUpdate();
			if(roleName.equals("Manager") || roleName.equals("Admin")) changeManager(userName,MyResource.adminBhai);

			}
			
			catch(Exception ex) {
				
			}		
    }
    public static List<String> getUsers() throws Exception{
    	   List<String> userList = new ArrayList<>();
           
           try (Connection c = Db.connect();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT UserName FROM user where role != 'Admin'")) {

               while (rs.next()) {
                   String username = rs.getString(1);
                   userList.add(username);
               }
               
           } catch (SQLException ex) {
               ex.printStackTrace();
           }

           return userList;
 	    }
    public static Emp getUserDetails(String username) {
    	Emp emp = new Emp();
    	ResultSet rs;
    	try {
    		Connection c = Db.connect();
    	String query = "select FirstName,LastName,PhoneNo,Email from user where UserName = ?";
    	PreparedStatement ps = c.prepareStatement(query);
    	ps.setString(1, username);
    	rs = ps.executeQuery();
    	while(rs.next()) {
    		emp.setFirstname(rs.getString(1));
    		emp.setLastname(rs.getString(2));
    		emp.setPhone(rs.getString(3));
    		emp.setEmail(rs.getString(4));
    	}
    	}
    	catch(Exception ex) {ex.printStackTrace(); };
    	System.out.println(emp);
    	return emp;	
    }
    
    public static String changeDetails(String username,String firstname,String lastname,String email,String phone) {
		try {
    		Connection c = Db.connect();
    /*		String newUsername=username;
            String patternString = "^" + Pattern.quote(firstname) + "\\." + Pattern.quote(lastname) + "\\d*$";
            Pattern pattern = Pattern.compile(patternString);

    		if(!pattern.matcher(username).matches()) {
    			
    			newUsername = count_user(username);
    		System.out.println(newUsername);
    		}
    		*/
    		String query = "update user set UserName = ? , FirstName = ? , LastName = ? , Email = ?, PhoneNo = ? where UserName = ?";
        	PreparedStatement pt = c.prepareStatement(query);
    		pt.setString(1, username);
    		pt.setString(2, firstname);
    		pt.setString(3, lastname);
    		pt.setString(4, email);
    		pt.setString(5, phone);
    		pt.setString(6, username);

    		pt.executeUpdate();
    		
    		return "Details Updated";
    	}
    	catch(Exception ex) {
    		return ex.getMessage();
    	}
		
		
	}
		//String password = "supersecret";	
		
//		 
    
    
    
    
    
    
    }
	 
	 

    
		


	
	


