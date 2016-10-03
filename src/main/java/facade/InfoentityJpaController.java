/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Person;
import entity.Company;
import entity.Address;
import entity.Infoentity;
import entity.InfoentityPK;
import entity.Phone;
import facade.exceptions.IllegalOrphanException;
import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class InfoentityJpaController implements Serializable {

    public InfoentityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Infoentity infoentity) throws PreexistingEntityException, Exception {
        if (infoentity.getInfoentityPK() == null) {
            infoentity.setInfoentityPK(new InfoentityPK());
        }
        if (infoentity.getPhoneCollection() == null) {
            infoentity.setPhoneCollection(new ArrayList<Phone>());
        }
        infoentity.getInfoentityPK().setCompanyidCompany(infoentity.getCompany().getIdCompany());
        infoentity.getInfoentityPK().setPersonidPerson(infoentity.getPerson().getIdPerson());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person = infoentity.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getIdPerson());
                infoentity.setPerson(person);
            }
            Company company = infoentity.getCompany();
            if (company != null) {
                company = em.getReference(company.getClass(), company.getIdCompany());
                infoentity.setCompany(company);
            }
            Address addressidAddress = infoentity.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress = em.getReference(addressidAddress.getClass(), addressidAddress.getIdAddress());
                infoentity.setAddressidAddress(addressidAddress);
            }
            Collection<Phone> attachedPhoneCollection = new ArrayList<Phone>();
            for (Phone phoneCollectionPhoneToAttach : infoentity.getPhoneCollection()) {
                phoneCollectionPhoneToAttach = em.getReference(phoneCollectionPhoneToAttach.getClass(), phoneCollectionPhoneToAttach.getIdPhone());
                attachedPhoneCollection.add(phoneCollectionPhoneToAttach);
            }
            infoentity.setPhoneCollection(attachedPhoneCollection);
            em.persist(infoentity);
            if (person != null) {
                person.getInfoentityCollection().add(infoentity);
                person = em.merge(person);
            }
            if (company != null) {
                company.getInfoentityCollection().add(infoentity);
                company = em.merge(company);
            }
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().add(infoentity);
                addressidAddress = em.merge(addressidAddress);
            }
            for (Phone phoneCollectionPhone : infoentity.getPhoneCollection()) {
                Infoentity oldInfoEntityidInfoEntityOfPhoneCollectionPhone = phoneCollectionPhone.getInfoEntityidInfoEntity();
                phoneCollectionPhone.setInfoEntityidInfoEntity(infoentity);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
                if (oldInfoEntityidInfoEntityOfPhoneCollectionPhone != null) {
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone.getPhoneCollection().remove(phoneCollectionPhone);
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone = em.merge(oldInfoEntityidInfoEntityOfPhoneCollectionPhone);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInfoentity(infoentity.getInfoentityPK()) != null) {
                throw new PreexistingEntityException("Infoentity " + infoentity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Infoentity infoentity) throws IllegalOrphanException, NonexistentEntityException, Exception {
        infoentity.getInfoentityPK().setCompanyidCompany(infoentity.getCompany().getIdCompany());
        infoentity.getInfoentityPK().setPersonidPerson(infoentity.getPerson().getIdPerson());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity persistentInfoentity = em.find(Infoentity.class, infoentity.getInfoentityPK());
            Person personOld = persistentInfoentity.getPerson();
            Person personNew = infoentity.getPerson();
            Company companyOld = persistentInfoentity.getCompany();
            Company companyNew = infoentity.getCompany();
            Address addressidAddressOld = persistentInfoentity.getAddressidAddress();
            Address addressidAddressNew = infoentity.getAddressidAddress();
            Collection<Phone> phoneCollectionOld = persistentInfoentity.getPhoneCollection();
            Collection<Phone> phoneCollectionNew = infoentity.getPhoneCollection();
            List<String> illegalOrphanMessages = null;
            for (Phone phoneCollectionOldPhone : phoneCollectionOld) {
                if (!phoneCollectionNew.contains(phoneCollectionOldPhone)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Phone " + phoneCollectionOldPhone + " since its infoEntityidInfoEntity field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personNew != null) {
                personNew = em.getReference(personNew.getClass(), personNew.getIdPerson());
                infoentity.setPerson(personNew);
            }
            if (companyNew != null) {
                companyNew = em.getReference(companyNew.getClass(), companyNew.getIdCompany());
                infoentity.setCompany(companyNew);
            }
            if (addressidAddressNew != null) {
                addressidAddressNew = em.getReference(addressidAddressNew.getClass(), addressidAddressNew.getIdAddress());
                infoentity.setAddressidAddress(addressidAddressNew);
            }
            Collection<Phone> attachedPhoneCollectionNew = new ArrayList<Phone>();
            for (Phone phoneCollectionNewPhoneToAttach : phoneCollectionNew) {
                phoneCollectionNewPhoneToAttach = em.getReference(phoneCollectionNewPhoneToAttach.getClass(), phoneCollectionNewPhoneToAttach.getIdPhone());
                attachedPhoneCollectionNew.add(phoneCollectionNewPhoneToAttach);
            }
            phoneCollectionNew = attachedPhoneCollectionNew;
            infoentity.setPhoneCollection(phoneCollectionNew);
            infoentity = em.merge(infoentity);
            if (personOld != null && !personOld.equals(personNew)) {
                personOld.getInfoentityCollection().remove(infoentity);
                personOld = em.merge(personOld);
            }
            if (personNew != null && !personNew.equals(personOld)) {
                personNew.getInfoentityCollection().add(infoentity);
                personNew = em.merge(personNew);
            }
            if (companyOld != null && !companyOld.equals(companyNew)) {
                companyOld.getInfoentityCollection().remove(infoentity);
                companyOld = em.merge(companyOld);
            }
            if (companyNew != null && !companyNew.equals(companyOld)) {
                companyNew.getInfoentityCollection().add(infoentity);
                companyNew = em.merge(companyNew);
            }
            if (addressidAddressOld != null && !addressidAddressOld.equals(addressidAddressNew)) {
                addressidAddressOld.getInfoentityCollection().remove(infoentity);
                addressidAddressOld = em.merge(addressidAddressOld);
            }
            if (addressidAddressNew != null && !addressidAddressNew.equals(addressidAddressOld)) {
                addressidAddressNew.getInfoentityCollection().add(infoentity);
                addressidAddressNew = em.merge(addressidAddressNew);
            }
            for (Phone phoneCollectionNewPhone : phoneCollectionNew) {
                if (!phoneCollectionOld.contains(phoneCollectionNewPhone)) {
                    Infoentity oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone = phoneCollectionNewPhone.getInfoEntityidInfoEntity();
                    phoneCollectionNewPhone.setInfoEntityidInfoEntity(infoentity);
                    phoneCollectionNewPhone = em.merge(phoneCollectionNewPhone);
                    if (oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone != null && !oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone.equals(infoentity)) {
                        oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone.getPhoneCollection().remove(phoneCollectionNewPhone);
                        oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone = em.merge(oldInfoEntityidInfoEntityOfPhoneCollectionNewPhone);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                InfoentityPK id = infoentity.getInfoentityPK();
                if (findInfoentity(id) == null) {
                    throw new NonexistentEntityException("The infoentity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(InfoentityPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity infoentity;
            try {
                infoentity = em.getReference(Infoentity.class, id);
                infoentity.getInfoentityPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infoentity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Phone> phoneCollectionOrphanCheck = infoentity.getPhoneCollection();
            for (Phone phoneCollectionOrphanCheckPhone : phoneCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Infoentity (" + infoentity + ") cannot be destroyed since the Phone " + phoneCollectionOrphanCheckPhone + " in its phoneCollection field has a non-nullable infoEntityidInfoEntity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Person person = infoentity.getPerson();
            if (person != null) {
                person.getInfoentityCollection().remove(infoentity);
                person = em.merge(person);
            }
            Company company = infoentity.getCompany();
            if (company != null) {
                company.getInfoentityCollection().remove(infoentity);
                company = em.merge(company);
            }
            Address addressidAddress = infoentity.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().remove(infoentity);
                addressidAddress = em.merge(addressidAddress);
            }
            em.remove(infoentity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Infoentity> findInfoentityEntities() {
        return findInfoentityEntities(true, -1, -1);
    }

    public List<Infoentity> findInfoentityEntities(int maxResults, int firstResult) {
        return findInfoentityEntities(false, maxResults, firstResult);
    }

    private List<Infoentity> findInfoentityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Infoentity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Infoentity findInfoentity(InfoentityPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Infoentity.class, id);
        } finally {
            em.close();
        }
    }

    public int getInfoentityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Infoentity> rt = cq.from(Infoentity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
