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
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@Path("observation")
public class ObservationResource extends AbstractResource<ObservationService, ObservationDAO, Observation, Long> {
    
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
        getService().getDAO().create(observation);
    }

}
