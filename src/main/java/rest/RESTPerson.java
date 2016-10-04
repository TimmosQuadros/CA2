/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import entity.Infoentity;
import entity.Person;
import facade.Facade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utility.JSONConverter;

/**
 * REST Web Service
 *
 * @author TimmosQuadros
 */
@Path("person")
public class RESTPerson {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory( "com.mycompany_CA2_war_1.0-SNAPSHOTPU" );
    @Context
    private UriInfo context;

    
    public RESTPerson() {
        
    }

    /**
     * Retrieves representation of an instance of rest.RESTPerson
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("complete")
    public String getAllPersons() {
        //Facade f = new Facade(emf);
        //List<Person> l = f.getPersons();
        String jsonResponse = "";//JSONConverter.getJSONfromPerson(l);
        return jsonResponse;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("complete/{id}")
    public String getPerson(@PathParam("id") int id) {
        Facade f = new Facade(emf);
        Person p = f.getPerson(id);
        String jsonResponse = JSONConverter.getJSONfromPerson(p);
        return jsonResponse;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("contactinfo")
    public String getAllContactinfo(){
        Facade f = new Facade(emf);
        List<Infoentity> lis = f.getInfoEntities();
        String jsonResponse = new Gson().toJson(lis);
        return jsonResponse;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("contactinfo/{id}")
    public String getContactInfo(@PathParam("id") int id){
        Facade f = new Facade(emf);
        Infoentity inf = f.getInfoEntity(id);
        String jsonResponse = new Gson().toJson(inf);
        return jsonResponse;
    }


    
    

    /**
     * PUT method for updating or creating an instance of RESTPerson
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
