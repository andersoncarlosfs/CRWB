/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.controller.services;

import com.andersoncarlosfs.model.daos.ObservationDAO;
import com.andersoncarlosfs.model.AbstractService;
import com.andersoncarlosfs.model.entities.Observation;
import com.andersoncarlosfs.model.entities.Picture;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@SessionScoped
@Path("/observation")
public class ObservationService extends AbstractService<ObservationDAO, Observation, Long> {

    @Inject
    private ObservationDAO dao;

    /**
     *
     * @see com.andersoncarlosfs.model.AbstractService#getDAO()
     * @return the dao
     */
    @Override
    public ObservationDAO getDAO() {
        return dao;
    }
    
    /**
     * Create
     *
     * @param picture
     * @param details
     * @throws java.io.IOException
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void create(@FormDataParam("picture") InputStream picture, @FormDataParam("file") FormDataContentDisposition details) throws IOException {
        java.nio.file.Path path = Files.createTempFile(null, details.getFileName());
        Files.copy(picture, path, StandardCopyOption.REPLACE_EXISTING);
        Observation observation = new Observation("Teste", new Date(), new Picture(Files.readAllBytes(path)));  
        dao.create(observation);
    }

}
