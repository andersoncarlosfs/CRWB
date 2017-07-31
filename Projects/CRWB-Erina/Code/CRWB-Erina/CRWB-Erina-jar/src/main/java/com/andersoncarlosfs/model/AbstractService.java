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
import javax.ws.rs.core.MediaType;

/**
 * Abstract class to manipulate DAOs
 *
 * @author Anderson Carlos Ferreira da Silva
 * @param <U> the DAO type
 * @param <S> the entity type
 * @param <T> the identifier type
 */
public abstract class AbstractService<U extends AbstractDAO<S, T>, S extends AbstractEntity<T>, T extends Comparable<T>> implements Serializable {

    /**
     *
     * @return
     */
    public abstract U getDAO();
    
    /**
     * Create
     *
     * @param entity
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(S entity) {
        getDAO().create(entity);
    }

    /**
     * Update
     *
     * @param identificator
     * @param entity
     */
    @PUT
    @Path("{identificator}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void update(@PathParam("identificator") String identificator, S entity) {
        getDAO().update(entity);
    }

    /**
     * Delete
     *
     * @param primaryKey
     */
    @DELETE
    @Path("{identificator}")
    public void delete(@PathParam("identificator") T primaryKey) {
        getDAO().delete(primaryKey);
    }

    /**
     * Find
     *
     * @param primaryKey
     * @return
     */
    @GET
    @Path("{identificator}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public S findByPrimaryKey(@PathParam("identificator") T primaryKey) {
        return getDAO().findByPrimaryKey(primaryKey);
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
        return getDAO().count().toString();
    }

    /**
     * List
     *
     * @return the list
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<S> list() {
        return getDAO().list();
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
        return getDAO().listByRange(from, to);
    }
    
}
