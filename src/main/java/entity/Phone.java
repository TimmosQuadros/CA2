/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TimmosQuadros
 */
@Entity
@Table(name = "phone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Phone.findAll", query = "SELECT p FROM Phone p"),
    @NamedQuery(name = "Phone.findByIdPhone", query = "SELECT p FROM Phone p WHERE p.idPhone = :idPhone"),
    @NamedQuery(name = "Phone.findByDescription", query = "SELECT p FROM Phone p WHERE p.description = :description"),
    @NamedQuery(name = "Phone.findByNumber", query = "SELECT p FROM Phone p WHERE p.number = :number")})
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPhone")
    private Integer idPhone;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "number")
    private Integer number;
    @JoinColumn(name = "InfoEntity_idInfoEntity", referencedColumnName = "ID")
    @ManyToOne
    private Infoentity infoEntityidInfoEntity;

    public Phone() {
    }

    public Phone(String description, Integer number, Infoentity infoEntityidInfoEntity) {
        this.description = description;
        this.number = number;
        this.infoEntityidInfoEntity = infoEntityidInfoEntity;
    }

    public Phone(Integer idPhone) {
        this.idPhone = idPhone;
    }

    public Integer getIdPhone() {
        return idPhone;
    }

    public void setIdPhone(Integer idPhone) {
        this.idPhone = idPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Infoentity getInfoEntityidInfoEntity() {
        return infoEntityidInfoEntity;
    }

    public void setInfoEntityidInfoEntity(Infoentity infoEntityidInfoEntity) {
        this.infoEntityidInfoEntity = infoEntityidInfoEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPhone != null ? idPhone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Phone)) {
            return false;
        }
        Phone other = (Phone) object;
        if ((this.idPhone == null && other.idPhone != null) || (this.idPhone != null && !this.idPhone.equals(other.idPhone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Phone[ idPhone=" + idPhone + " ]";
    }

}
