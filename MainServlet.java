/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hardware.api;

/**
 *
 * @author User
 */

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/home")
public class MainServlet {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String welcome()
    {
        return"{\"message\": \"Welcome to the Ramadie Hardware web application\", "+
                "\"endpoints\": [\"/login\", \"/register\",\"items\",\"/cart\"]}";
    }
}
