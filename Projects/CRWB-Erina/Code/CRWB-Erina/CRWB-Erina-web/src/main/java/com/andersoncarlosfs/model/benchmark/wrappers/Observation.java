/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.benchmark.wrappers;

import com.andersoncarlosfs.controller.resources.PictureResource;
import com.andersoncarlosfs.model.entities.Picture;
import java.util.Base64;
import java.util.Date;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author anderson
 */
@XmlRootElement
@XmlType(name = "observation", namespace = "CRWB")
public final class Observation extends com.andersoncarlosfs.model.entities.Observation {

    private UriInfo context;

    public Observation() {
    }

    public Observation(com.andersoncarlosfs.model.entities.Observation observation) {
        setObservation(observation.getObservation());
        setText(observation.getText());
        setDate(observation.getDate());
        setNaturalPerson(observation.getNaturalPerson());
        setDetail(observation.getDetails());
        setObservation(observation.getObservation());
    }

    /**
     *
     * @return the date
     */
    @XmlTransient
    public Date getDate() {
        return super.getDate();
    }

    @XmlElement(name = "date")
    public Long getDateAsPOSIXTime() {
        return super.getDate().getTime();
    }

    /**
     *
     * @return the picture
     */
    @XmlTransient
    @Override
    public Picture getPicture() {
        return super.getPicture();
    }

    /**
     *
     * @return the picture
     */
    @XmlElement(name = "picture")
    public String getPictureAsString() {
        if (getPicture() == null || getPicture().getData() == null) {
            return null;
        }
        if (context == null) {
            return Base64.getEncoder().encodeToString(getPicture().getData());
        }
        return context.getBaseUriBuilder().path(PictureResource.class).path(getPicture().getPrimaryKey().toString()).build().toString();
    }

    /**
     *
     * @param context the context to set
     */
    public void setContext(UriInfo context) {
        this.context = context;
    }

}
