/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.entities;

import com.andersoncarlosfs.model.AbstractEntity;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@RequestScoped
@Entity
@Table(name = "groups", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g"),
    @NamedQuery(name = "Group.findByIdGroup", query = "SELECT g FROM Group g WHERE g.idGroup = :idGroup"),
    @NamedQuery(name = "Group.findByName", query = "SELECT g FROM Group g WHERE g.name = :name")})
public class Group extends AbstractEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_id_group_seq")
    @SequenceGenerator(name = "groups_id_group_seq", allocationSize = 1, sequenceName = "groups_id_group_seq")
    @Basic(optional = false)
    @Column(name = "id_group", nullable = false)
    private Integer idGroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(nullable = false, length = 2147483647)
    private String name;
    @JoinTable(name = "persons_groups", joinColumns = {
        @JoinColumn(name = "id_group", referencedColumnName = "id_group", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_person", referencedColumnName = "id_person", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Person> persons;

    public Group() {
    }

    public Group(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(Integer idGroup, String name) {
        this.idGroup = idGroup;
        this.name = name;
    }

    /**
     *
     * @return the idGroup
     */
    public Integer getIdGroup() {
        return idGroup;
    }

    /**
     *
     * @param idGroup the idGroup to set
     */
    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the persons
     */
    @XmlTransient
    public Collection<Person> getPersons() {
        return persons;
    }

    /**
     *
     * @param persons the persons to set
     */
    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    /**
     *
     * @see AbstractEntity#getPrimaryKey()
     * @return the idGroup
     */
    @Override
    public Integer getPrimaryKey() {
        return idGroup;
    }

}
