/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.controller.resources;

import com.andersoncarlosfs.controller.services.PictureService;
import com.andersoncarlosfs.model.AbstractResource;
import com.andersoncarlosfs.model.daos.PictureDAO;
import com.andersoncarlosfs.model.entities.Picture;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@RequestScoped
@Path("picture")
public class PictureResource extends AbstractResource<PictureService, PictureDAO, Picture, Long> {

    @Inject
    private PictureService service;
    
    /**
     *
     * @return the service
     */
    @Override
    protected PictureService getService() {
        return service;
    }
    
}
