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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Path("/search")

public class SearchItemsServlet 
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> searchItems(@QueryParam("keyword") String keyword) {
        List<Items> itemList = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return itemList; 
        }

        String sql = "SELECT * FROM items WHERE name LIKE ? OR description LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String search = "%" + keyword + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = new Items();
                item.setId(rs.getInt("itemid"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                itemList.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace(); // log error in real app
        }

        return itemList;
    }
}
