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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "legal_persons", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LegalPerson.findAll", query = "SELECT l FROM LegalPerson l"),
    @NamedQuery(name = "LegalPerson.findByIdLegalPerson", query = "SELECT l FROM LegalPerson l WHERE l.idLegalPerson = :idLegalPerson"),
    @NamedQuery(name = "LegalPerson.findByLegalPerson", query = "SELECT l FROM LegalPerson l WHERE l.legalPerson = :legalPerson")})
public class LegalPerson extends AbstractEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "legal_persons_id_legal_person_seq")
    @SequenceGenerator(name = "legal_persons_id_legal_person_seq", allocationSize = 1, sequenceName = "legal_persons_id_legal_person_seq")
    @Basic(optional = false)
    @Column(name = "id_legal_person", nullable = false)
    private Integer idLegalPerson;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "legal_person", nullable = false, length = 2147483647)
    private String legalPerson;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "legalPerson", fetch = FetchType.LAZY)
    private Collection<Person> persons;

    public LegalPerson() {
    }

    public LegalPerson(Integer idLegalPerson) {
        this.idLegalPerson = idLegalPerson;
    }

    public LegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public LegalPerson(int idLegalPerson, String legalPerson) {
        this.idLegalPerson = idLegalPerson;
        this.legalPerson = legalPerson;
    }

    public LegalPerson(Integer idLegalPerson, String legalPerson) {
        this.idLegalPerson = idLegalPerson;
        this.legalPerson = legalPerson;
    }

    /**
     *
     * @return the idLegalPerson
     */
    public Integer getIdLegalPerson() {
        return idLegalPerson;
    }

    /**
     *
     * @param idLegalPerson the idLegalPerson to set
     */
    public void setIdLegalPerson(Integer idLegalPerson) {
        this.idLegalPerson = idLegalPerson;
    }

    /**
     *
     * @return the legalPerson
     */
    public String getLegalPerson() {
        return legalPerson;
    }

    /**
     *
     * @param legalPerson the legalPerson to set
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
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
     * @return the idLegalPerson
     */
    @Override
    public Integer getPrimaryKey() {
        return idLegalPerson;
    }

}
