/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

import com.hardware.db.DBUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/buy")
public class BuyItemsServlet {
    public static class BuyRequest  
    {
        public int itemId;
        public int quantity;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyItem(BuyRequest request)
    {
        String selectQuery = "SELECT quantity FROM items WHERE itemid = ?";
        String updateQuery = "UPDATE items SET quantity = quantity -? WHERE itemid = ?";
        try(Connection conn = DBUtil.getConnection())
        {
            try(PreparedStatement selectStmt = conn.prepareStatement(selectQuery))
            {
                selectStmt.setInt(1,request.itemId);
                ResultSet rs = selectStmt.executeQuery();
                
                if(rs.next())
                {
                   int availableQuantity = rs.getInt("quantity");
                   if(availableQuantity >= request.quantity)
                   {
                       try(PreparedStatement updateStmt = conn.prepareStatement(updateQuery))
                       {
                           updateStmt.setInt(1,request.quantity);
                           updateStmt.setInt(2,request.itemId);
                           int rowsAffected = updateStmt.executeUpdate();
                           
                          if (rowsAffected > 0) {
                                return Response.ok("{\"message\":\"Purchase successful.\"}").build();
                            } else {
                                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                               .entity("{\"error\":\"Failed to update item quantity.\"}")
                                               .build();
                            }
                        }

                    } else {
                        return Response.status(Response.Status.BAD_REQUEST)
                                       .entity("{\"error\":\"Not enough stock available.\"}")
                                       .build();
                    }

                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                                   .entity("{\"error\":\"Item not found.\"}")
                                   .build();
                }
            }

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Database error: " + e.getMessage() + "\"}")
                           .build();
        }
    }
}
