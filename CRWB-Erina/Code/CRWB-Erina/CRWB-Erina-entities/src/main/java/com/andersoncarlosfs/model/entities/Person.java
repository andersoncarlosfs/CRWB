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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@RequestScoped
@Entity
@Table(name = "persons", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
    ,
    @NamedQuery(name = "Person.findByIdPerson", query = "SELECT p FROM Person p WHERE p.idPerson = :idPerson")})
public class Person extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persons_id_person_seq")
    @SequenceGenerator(name = "persons_id_person_seq", allocationSize = 1, sequenceName = "persons_id_person_seq")
    @Basic(optional = false)
    @Column(name = "id_person", nullable = false)
    private Long idPerson;
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY)
    private Collection<Group> groups;
    @JoinTable(name = "persons_emails", joinColumns = {
        @JoinColumn(name = "id_person", referencedColumnName = "id_person", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_email", referencedColumnName = "id_email", nullable = false)})
    @ManyToMany
    private Collection<Email> emails;
    @JoinTable(name = "persons_pictures", joinColumns = {
        @JoinColumn(name = "id_person", referencedColumnName = "id_person", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_picture", referencedColumnName = "id_picture", nullable = false)})
    @ManyToMany
    private Collection<Picture> pictures;
    @JoinColumn(name = "id_legal_person", referencedColumnName = "id_legal_person", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LegalPerson legalPerson;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    private NaturalPerson naturalPerson;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
    private Collection<Alias> aliases;

    public Person() {
    }

    public Person(Long idPerson) {
        this.idPerson = idPerson;
    }

    public Person(LegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }

    public Person(LegalPerson legalPerson, NaturalPerson naturalPerson) {
        this.legalPerson = legalPerson;
        this.naturalPerson = naturalPerson;
    }

    public Person(Long idPerson, LegalPerson legalPerson, NaturalPerson naturalPerson) {
        this.idPerson = idPerson;
        this.legalPerson = legalPerson;
        this.naturalPerson = naturalPerson;
    }

    /**
     *
     * @return the idPerson
     */
    public Long getIdPerson() {
        return idPerson;
    }

    /**
     *
     * @param idPerson the idPerson to set
     */
    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }

    /**
     *
     * @return the groups
     */
    @XmlTransient
    public Collection<Group> getGroups() {
        return groups;
    }

    /**
     *
     * @param groups the groups to set
     */
    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    /**
     *
     * @return the emails
     */
    @XmlTransient
    public Collection<Email> getEmails() {
        return emails;
    }

    /**
     *
     * @param emails the emails to set
     */
    public void setEmails(Collection<Email> emails) {
        this.emails = emails;
    }

    /**
     *
     * @return the pictures
     */
    @XmlTransient
    public Collection<Picture> getPictures() {
        return pictures;
    }

    /**
     *
     * @param pictures the pictures to set
     */
    public void setPictures(Collection<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     *
     * @return the legalPerson
     */
    public LegalPerson getLegalPerson() {
        return legalPerson;
    }

    /**
     *
     * @param legalPerson the legalPerson to set
     */
    public void setLegalPerson(LegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * @return the naturalPerson
     */
    public NaturalPerson getNaturalPerson() {
        return naturalPerson;
    }

    /**
     *
     * @param naturalPerson the naturalPerson to set
     */
    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    /**
     * @return the aliases
     */
    @XmlTransient
    public Collection<Alias> getAliases() {
        return aliases;
    }

    /**
     *
     * @param aliases the aliases to set
     */
    public void setAliases(Collection<Alias> aliases) {
        this.aliases = aliases;
    }

    /**
     *
     * @see AbstractEntity#getPrimaryKey()
     * @return the idPerson
     */
    @Override
    public Long getPrimaryKey() {
        return idPerson;
    }

}
