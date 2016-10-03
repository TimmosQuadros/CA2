/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author TimmosQuadros
 */
@Embeddable
public class InfoentityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idInfoEntity")
    private int idInfoEntity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Company_idCompany")
    private int companyidCompany;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Person_idPerson")
    private int personidPerson;

    public InfoentityPK() {
    }

    public InfoentityPK(int idInfoEntity, int companyidCompany, int personidPerson) {
        this.idInfoEntity = idInfoEntity;
        this.companyidCompany = companyidCompany;
        this.personidPerson = personidPerson;
    }

    public int getIdInfoEntity() {
        return idInfoEntity;
    }

    public void setIdInfoEntity(int idInfoEntity) {
        this.idInfoEntity = idInfoEntity;
    }

    public int getCompanyidCompany() {
        return companyidCompany;
    }

    public void setCompanyidCompany(int companyidCompany) {
        this.companyidCompany = companyidCompany;
    }

    public int getPersonidPerson() {
        return personidPerson;
    }

    public void setPersonidPerson(int personidPerson) {
        this.personidPerson = personidPerson;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idInfoEntity;
        hash += (int) companyidCompany;
        hash += (int) personidPerson;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfoentityPK)) {
            return false;
        }
        InfoentityPK other = (InfoentityPK) object;
        if (this.idInfoEntity != other.idInfoEntity) {
            return false;
        }
        if (this.companyidCompany != other.companyidCompany) {
            return false;
        }
        if (this.personidPerson != other.personidPerson) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.InfoentityPK[ idInfoEntity=" + idInfoEntity + ", companyidCompany=" + companyidCompany + ", personidPerson=" + personidPerson + " ]";
    }
    
}
