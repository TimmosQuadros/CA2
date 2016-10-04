/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TimmosQuadros
 */
@Inheritance(strategy=InheritanceType.JOINED)
@Entity
@Table(name = "infoentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Infoentity.findAll", query = "SELECT i FROM Infoentity i"),
    @NamedQuery(name = "Infoentity.findById", query = "SELECT i FROM Infoentity i WHERE i.id = :id"),
    @NamedQuery(name = "Infoentity.findByDtype", query = "SELECT i FROM Infoentity i WHERE i.dtype = :dtype"),
    @NamedQuery(name = "Infoentity.findByCompanyID", query = "SELECT i FROM Infoentity i WHERE i.companyID = :companyID"),
    @NamedQuery(name = "Infoentity.findByEmail", query = "SELECT i FROM Infoentity i WHERE i.email = :email"),
    @NamedQuery(name = "Infoentity.findByPersonID", query = "SELECT i FROM Infoentity i WHERE i.personID = :personID")})
public class Infoentity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 31)
    @Column(name = "DTYPE")
    private String dtype;
    @Column(name = "Company_ID")
    private Integer companyID;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Column(name = "Person_ID")
    private Integer personID;
    @ManyToMany(mappedBy = "infoentityCollection")
    private Collection<Hobby> hobbyCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "infoentity")
    private Person person;
    @OneToMany(mappedBy = "infoEntityidInfoEntity")
    private Collection<Phone> phoneCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "infoentity")
    private Company company;
    @JoinColumn(name = "address_idAddress", referencedColumnName = "idAddress")
    @ManyToOne(optional = false)
    private Address addressidAddress;

    public Infoentity() {
    }
    
    public Infoentity(Integer id) {
        this.id = id;
    }

    public Infoentity(String dtype, Integer companyID, String email, Integer personID, Collection<Hobby> hobbyCollection, Person person, Collection<Phone> phoneCollection, Company company, Address addressidAddress) {
        this.dtype = dtype;
        this.companyID = companyID;
        this.email = email;
        this.personID = personID;
        this.hobbyCollection = hobbyCollection;
        this.person = person;
        this.phoneCollection = phoneCollection;
        this.company = company;
        this.addressidAddress = addressidAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    @XmlTransient
    public Collection<Hobby> getHobbyCollection() {
        return hobbyCollection;
    }

    public void setHobbyCollection(Collection<Hobby> hobbyCollection) {
        this.hobbyCollection = hobbyCollection;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @XmlTransient
    public Collection<Phone> getPhoneCollection() {
        return phoneCollection;
    }

    public void setPhoneCollection(Collection<Phone> phoneCollection) {
        this.phoneCollection = phoneCollection;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Infoentity)) {
            return false;
        }
        Infoentity other = (Infoentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Infoentity[ id=" + id + " ]";
    }
    
}
