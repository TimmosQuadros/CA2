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
import entity.Infoentity;
import entity.Person;
import entity.Company;
import entity.Address;
import entity.Hobby;
import java.util.ArrayList;
import java.util.Collection;
import entity.Phone;
import facade.exceptions.IllegalOrphanException;
import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class CompanyJpaController implements Serializable {

    public CompanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (company.getHobbyCollection() == null) {
            company.setHobbyCollection(new ArrayList<Hobby>());
        }
        if (company.getPhoneCollection() == null) {
            company.setPhoneCollection(new ArrayList<Phone>());
        }
        List<String> illegalOrphanMessages = null;
        Infoentity infoentityOrphanCheck = company.getInfoentity();
        if (infoentityOrphanCheck != null) {
            Company oldCompanyOfInfoentity = infoentityOrphanCheck.getCompany();
            if (oldCompanyOfInfoentity != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Infoentity " + infoentityOrphanCheck + " already has an item of type Company whose infoentity column cannot be null. Please make another selection for the infoentity field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity infoentity = company.getInfoentity();
            if (infoentity != null) {
                infoentity = em.getReference(infoentity.getClass(), infoentity.getId());
                company.setInfoentity(infoentity);
            }
            Person person = company.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getId());
                company.setPerson(person);
            }
            Company companyRel = company.getCompany();
            if (companyRel != null) {
                companyRel = em.getReference(companyRel.getClass(), companyRel.getId());
                company.setCompany(companyRel);
            }
            Address addressidAddress = company.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress = em.getReference(addressidAddress.getClass(), addressidAddress.getIdAddress());
                company.setAddressidAddress(addressidAddress);
            }
            Collection<Hobby> attachedHobbyCollection = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionHobbyToAttach : company.getHobbyCollection()) {
                hobbyCollectionHobbyToAttach = em.getReference(hobbyCollectionHobbyToAttach.getClass(), hobbyCollectionHobbyToAttach.getIdHobby());
                attachedHobbyCollection.add(hobbyCollectionHobbyToAttach);
            }
            company.setHobbyCollection(attachedHobbyCollection);
            Collection<Phone> attachedPhoneCollection = new ArrayList<Phone>();
            for (Phone phoneCollectionPhoneToAttach : company.getPhoneCollection()) {
                phoneCollectionPhoneToAttach = em.getReference(phoneCollectionPhoneToAttach.getClass(), phoneCollectionPhoneToAttach.getIdPhone());
                attachedPhoneCollection.add(phoneCollectionPhoneToAttach);
            }
            company.setPhoneCollection(attachedPhoneCollection);
            em.persist(company);
            if (infoentity != null) {
                infoentity.setCompany(company);
                infoentity = em.merge(infoentity);
            }
            if (person != null) {
                entity.Infoentity oldInfoentityOfPerson = person.getInfoentity();
                if (oldInfoentityOfPerson != null) {
                    oldInfoentityOfPerson.setPerson(null);
                    oldInfoentityOfPerson = em.merge(oldInfoentityOfPerson);
                }
                person.setInfoentity(company);
                person = em.merge(person);
            }
            if (companyRel != null) {
                entity.Infoentity oldInfoentityOfCompanyRel = companyRel.getInfoentity();
                if (oldInfoentityOfCompanyRel != null) {
                    oldInfoentityOfCompanyRel.setCompany(null);
                    oldInfoentityOfCompanyRel = em.merge(oldInfoentityOfCompanyRel);
                }
                companyRel.setInfoentity(company);
                companyRel = em.merge(companyRel);
            }
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().add(company);
                addressidAddress = em.merge(addressidAddress);
            }
            for (Hobby hobbyCollectionHobby : company.getHobbyCollection()) {
                hobbyCollectionHobby.getInfoentityCollection().add(company);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            for (Phone phoneCollectionPhone : company.getPhoneCollection()) {
                entity.Infoentity oldInfoEntityidInfoEntityOfPhoneCollectionPhone = phoneCollectionPhone.getInfoEntityidInfoEntity();
                phoneCollectionPhone.setInfoEntityidInfoEntity(company);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
                if (oldInfoEntityidInfoEntityOfPhoneCollectionPhone != null) {
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone.getPhoneCollection().remove(phoneCollectionPhone);
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone = em.merge(oldInfoEntityidInfoEntityOfPhoneCollectionPhone);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompany(company.getId()) != null) {
                throw new PreexistingEntityException("Company " + company + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Person personOrphanCheck = company.getPerson();
            if (personOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Company (" + company + ") cannot be destroyed since the Person " + personOrphanCheck + " in its person field has a non-nullable infoentity field.");
            }
            Company companyOrphanCheck = company.getCompany();
            if (companyOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Company (" + company + ") cannot be destroyed since the Company " + companyOrphanCheck + " in its company field has a non-nullable infoentity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Infoentity infoentity = company.getInfoentity();
            if (infoentity != null) {
                infoentity.setCompany(null);
                infoentity = em.merge(infoentity);
            }
            Address addressidAddress = company.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().remove(company);
                addressidAddress = em.merge(addressidAddress);
            }
            Collection<Hobby> hobbyCollection = company.getHobbyCollection();
            for (Hobby hobbyCollectionHobby : hobbyCollection) {
                hobbyCollectionHobby.getInfoentityCollection().remove(company);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            Collection<Phone> phoneCollection = company.getPhoneCollection();
            for (Phone phoneCollectionPhone : phoneCollection) {
                phoneCollectionPhone.setInfoEntityidInfoEntity(null);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Company.class));
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

    public Company findCompany(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Company> rt = cq.from(Company.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
