/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

import com.hardware.db.DBUtil;
import com.hardware.model.Items;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Path("/cart")
public class ViewCartItemsServlet {
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> getCartItems(@PathParam("userId") int userId) {
        List<Items> cartItems = new ArrayList<>();

         String query = "SELECT i.itemid, i.name, i.description, i.price, c.quantity " +
                       "FROM cart c " +
                       "JOIN items i ON c.itemid = i.itemid " +
                       "WHERE c.userid = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = new Items();
                item.setId(rs.getInt("itemid"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setQuantity(rs.getInt("quantity"));

                cartItems.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Use proper logging in production
        }

        return cartItems;
    }
}
