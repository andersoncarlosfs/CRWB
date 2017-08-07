/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.controller.resources;

import com.andersoncarlosfs.controller.services.ObservationService;
import com.andersoncarlosfs.model.AbstractResource;
import com.andersoncarlosfs.model.daos.ObservationDAO;
import com.andersoncarlosfs.model.entities.Observation;
import com.andersoncarlosfs.model.entities.Picture;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@RequestScoped
@Path("observation")
public class ObservationResource extends AbstractResource<ObservationService, ObservationDAO, Observation, Long> {
       
    @Inject
    private ObservationService service;
    
    /**
     *
     * @return the service
     */
    @Override
    protected ObservationService getService() {
        return service;
    }
    
    /**
     * Create
     *
     * @param data
     * @throws java.io.IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public String create(byte[] data, @Context UriInfo context) throws IOException {
        Picture picture = new Picture(data, new HashSet<Observation>());
        Observation observation = new Observation("Teste", new Date(), picture);
        picture.getObservations().add(observation);
        getService().getDAO().create(observation);
        return context.getAbsolutePathBuilder().path(observation.getPrimaryKey().toString()).build().toString();
    }
   
    /**
     * Create
     * 
     * @throws IOException 
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition details, @FormDataParam("timestamp") Date date, @Context UriInfo context) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read = 0;
        byte[] data = new byte[4096];
        while ((read = stream.read(data, 0, data.length)) > 0) {
            buffer.write(data, 0, read);
        }
        buffer.flush();
        stream.close();
        create(buffer.toByteArray(), context);
    }

}
