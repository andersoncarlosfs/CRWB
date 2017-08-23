/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model;

import java.io.Serializable;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Abstract class to manipulate Beans
 *
 * @author Anderson Carlos Ferreira da Silva
 * @param <V> the service type
 * @param <U> the dao type
 * @param <S> the entity type
 * @param <T> the identifier type
 */
public abstract class AbstractResource<V extends AbstractService<U, S, T>, U extends AbstractDAO<S, T>, S extends AbstractEntity<T>, T extends Comparable<T>> implements Serializable {

    /**
     *
     * @return the service
     */
    protected abstract V getService();
    
    /**
     * Create
     *
     * @param entity
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(S entity) {
        getService().getDAO().create(entity);
    }

    /**
     * Update
     *
     * @param primaryKey
     * @param entity
     */
    @PUT
    @Path("{identificator}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void update(@PathParam("identificator") T primaryKey, S entity) {
        getService().getDAO().update(entity);
    }

    /**
     * Delete
     *
     * @param primaryKey
     */
    @DELETE
    @Path("{identificator}")
    public void delete(@PathParam("identificator") T primaryKey) {
        getService().getDAO().delete(primaryKey);
    }

    /**
     * Find
     *
     * @param primaryKey
     * @param eager
     * @param context
     * @return
     */
    @GET
    @Path("{identificator}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public S findByPrimaryKey(@PathParam("identificator") T primaryKey, @QueryParam("eager") boolean eager, @Context UriInfo context) {
        return getService().getDAO().findByPrimaryKey(primaryKey);
    }

    /**
     * Count
     *
     * @return
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return getService().getDAO().count().toString();
    }

    /**
     * List
     *
     * @return the list
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<S> list() {
        return getService().getDAO().list();
    }

    /**
     * List
     *
     * @param from
     * @param to
     * @return the list
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<S> listByRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return getService().getDAO().listByRange(from, to);
    }

}
