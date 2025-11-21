/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

import com.hardware.db.DBUtil;
import com.hardware.model.User;
import com.hardware.model.RegistrationResponse;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/register")
public class RegistrationServlet {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    
    public RegistrationResponse register(User newUser)
    {
        String username = newUser.getUsername();
        String password = newUser.getPassword();
        String role = newUser.getRole();
        
        try(Connection conn = DBUtil.getConnection())
        {
            String checkQuery = "SELECT userid FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if(rs.next())
            {
                return new RegistrationResponse(false, "Username already exists");
            }
            
            String insertQuery = "INSERT INTO users (username, password, role) VALUES (?,?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1,username);
            insertStmt.setString(2,password);
            insertStmt.setString(3, role != null ? role:"user");
            
            int rowsInserted = insertStmt.executeUpdate();
            if(rowsInserted > 0)
            {
                return new RegistrationResponse(true, "Registration successful.");
            }
            else
            {
                return new RegistrationResponse(false, "Registration failed.");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return new RegistrationResponse(false, "Database error: " + e.getMessage());
        }
    }
}
