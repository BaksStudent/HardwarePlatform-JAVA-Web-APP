/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

/**
 *
 * @author User
 */

import com.hardware.db.DBUtil;
import com.hardware.model.Items;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/items")
public class ViewAllServlet {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> getAllItems()
    {
        List<Items> itemsList = new ArrayList<>();
        String query = "SELECT * FROM items";
        
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery())
        {
            while(rs.next())
            {
                Items item = new Items();
                item.setId(rs.getInt("itemid"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setQuantity(rs.getInt("quantity"));
                
                itemsList.add(item);
                
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    
    return itemsList;
    }
}
