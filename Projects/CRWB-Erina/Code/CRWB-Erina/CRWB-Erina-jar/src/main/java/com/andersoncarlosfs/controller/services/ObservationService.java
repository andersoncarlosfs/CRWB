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
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
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
@Path("observation")
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
     * @param data
     * @throws java.io.IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void create(byte[] data) throws IOException {
        Picture picture = new Picture(data, new HashSet<Observation>());
        Observation observation = new Observation("Teste", new Date(), picture);
        picture.getObservations().add(observation);
        dao.create(observation);
    }

}
