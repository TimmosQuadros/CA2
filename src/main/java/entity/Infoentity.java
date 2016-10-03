/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TimmosQuadros
 */
@Entity
@Table(name = "infoentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Infoentity.findAll", query = "SELECT i FROM Infoentity i"),
    @NamedQuery(name = "Infoentity.findByIdInfoEntity", query = "SELECT i FROM Infoentity i WHERE i.infoentityPK.idInfoEntity = :idInfoEntity"),
    @NamedQuery(name = "Infoentity.findByEmail", query = "SELECT i FROM Infoentity i WHERE i.email = :email"),
    @NamedQuery(name = "Infoentity.findByCompanyidCompany", query = "SELECT i FROM Infoentity i WHERE i.infoentityPK.companyidCompany = :companyidCompany"),
    @NamedQuery(name = "Infoentity.findByPersonidPerson", query = "SELECT i FROM Infoentity i WHERE i.infoentityPK.personidPerson = :personidPerson")})
public class Infoentity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InfoentityPK infoentityPK;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "infoEntityidInfoEntity")
    private Collection<Phone> phoneCollection;
    @JoinColumn(name = "Person_idPerson", referencedColumnName = "idPerson", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Person person;
    @JoinColumn(name = "Company_idCompany", referencedColumnName = "idCompany", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Company company;
    @JoinColumn(name = "Address_idAddress", referencedColumnName = "idAddress")
    @ManyToOne(optional = false)
    private Address addressidAddress;

    public Infoentity() {
    }

    public Infoentity(InfoentityPK infoentityPK) {
        this.infoentityPK = infoentityPK;
    }

    public Infoentity(int idInfoEntity, int companyidCompany, int personidPerson) {
        this.infoentityPK = new InfoentityPK(idInfoEntity, companyidCompany, personidPerson);
    }

    public InfoentityPK getInfoentityPK() {
        return infoentityPK;
    }

    public void setInfoentityPK(InfoentityPK infoentityPK) {
        this.infoentityPK = infoentityPK;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public Collection<Phone> getPhoneCollection() {
        return phoneCollection;
    }

    public void setPhoneCollection(Collection<Phone> phoneCollection) {
        this.phoneCollection = phoneCollection;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Address getAddressidAddress() {
        return addressidAddress;
    }

    public void setAddressidAddress(Address addressidAddress) {
        this.addressidAddress = addressidAddress;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (infoentityPK != null ? infoentityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Infoentity)) {
            return false;
        }
        Infoentity other = (Infoentity) object;
        if ((this.infoentityPK == null && other.infoentityPK != null) || (this.infoentityPK != null && !this.infoentityPK.equals(other.infoentityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Infoentity[ infoentityPK=" + infoentityPK + " ]";
    }
    
}
