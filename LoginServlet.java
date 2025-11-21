/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

/**
 *
 * @author User
 */
import com.hardware.model.User;
import com.hardware.model.LoginResponse;
import com.hardware.db.DBUtil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.sql.*;

@Path("/login")
public class LoginServlet {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login(User loginRequest)
    {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        try(Connection conn = DBUtil.getConnection())
        {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                String role = rs.getString("role");
                return new LoginResponse(true, "Login sucessful",role);
            }
            else
            {
                return new LoginResponse(true, "Login sucessful",null);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return new LoginResponse(false, "Database error: "+e.getMessage(),null);
        }
    }
}
