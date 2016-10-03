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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "cityinfo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cityinfo.findAll", query = "SELECT c FROM Cityinfo c"),
    @NamedQuery(name = "Cityinfo.findByIdCityinfo", query = "SELECT c FROM Cityinfo c WHERE c.idCityinfo = :idCityinfo"),
    @NamedQuery(name = "Cityinfo.findByCity", query = "SELECT c FROM Cityinfo c WHERE c.city = :city"),
    @NamedQuery(name = "Cityinfo.findByZip", query = "SELECT c FROM Cityinfo c WHERE c.zip = :zip")})
public class Cityinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCityinfo")
    private Integer idCityinfo;
    @Size(max = 255)
    @Column(name = "City")
    private String city;
    @Column(name = "Zip")
    private Integer zip;
    @OneToMany(mappedBy = "cityinfoidCityinfo")
    private Collection<Address> addressCollection;

    public Cityinfo() {
    }

    public Cityinfo(Integer idCityinfo) {
        this.idCityinfo = idCityinfo;
    }

    public Integer getIdCityinfo() {
        return idCityinfo;
    }

    public void setIdCityinfo(Integer idCityinfo) {
        this.idCityinfo = idCityinfo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    @XmlTransient
    public Collection<Address> getAddressCollection() {
        return addressCollection;
    }

    public void setAddressCollection(Collection<Address> addressCollection) {
        this.addressCollection = addressCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCityinfo != null ? idCityinfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cityinfo)) {
            return false;
        }
        Cityinfo other = (Cityinfo) object;
        if ((this.idCityinfo == null && other.idCityinfo != null) || (this.idCityinfo != null && !this.idCityinfo.equals(other.idCityinfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cityinfo[ idCityinfo=" + idCityinfo + " ]";
    }
    
}
