/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TimmosQuadros
 */
@Entity
@Table(name = "hobby")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hobby.findAll", query = "SELECT h FROM Hobby h"),
    @NamedQuery(name = "Hobby.findByIdHobby", query = "SELECT h FROM Hobby h WHERE h.idHobby = :idHobby"),
    @NamedQuery(name = "Hobby.findByDescription", query = "SELECT h FROM Hobby h WHERE h.description = :description"),
    @NamedQuery(name = "Hobby.findByName", query = "SELECT h FROM Hobby h WHERE h.name = :name")})
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idHobby")
    private Integer idHobby;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @JoinTable(name = "hobby_has_person", joinColumns = {
        @JoinColumn(name = "Hobby_idHobby", referencedColumnName = "idHobby")}, inverseJoinColumns = {
        @JoinColumn(name = "Person_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Infoentity> infoentityCollection;

    public Hobby() {
    }

    public Hobby(Integer idHobby) {
        this.idHobby = idHobby;
    }

    public Integer getIdHobby() {
        return idHobby;
    }

    public void setIdHobby(Integer idHobby) {
        this.idHobby = idHobby;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Infoentity> getInfoentityCollection() {
        return infoentityCollection;
    }

    public void setInfoentityCollection(Collection<Infoentity> infoentityCollection) {
        this.infoentityCollection = infoentityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHobby != null ? idHobby.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hobby)) {
            return false;
        }
        Hobby other = (Hobby) object;
        if ((this.idHobby == null && other.idHobby != null) || (this.idHobby != null && !this.idHobby.equals(other.idHobby))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Hobby[ idHobby=" + idHobby + " ]";
    }
    
}
