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
public class PersonJpaController implements Serializable {

    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (person.getHobbyCollection() == null) {
            person.setHobbyCollection(new ArrayList<Hobby>());
        }
        if (person.getPhoneCollection() == null) {
            person.setPhoneCollection(new ArrayList<Phone>());
        }
        List<String> illegalOrphanMessages = null;
        Infoentity infoentityOrphanCheck = person.getInfoentity();
        if (infoentityOrphanCheck != null) {
            Person oldPersonOfInfoentity = infoentityOrphanCheck.getPerson();
            if (oldPersonOfInfoentity != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Infoentity " + infoentityOrphanCheck + " already has an item of type Person whose infoentity column cannot be null. Please make another selection for the infoentity field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity infoentity = person.getInfoentity();
            if (infoentity != null) {
                infoentity = em.getReference(infoentity.getClass(), infoentity.getId());
                person.setInfoentity(infoentity);
            }
            Person personRel = person.getPerson();
            if (personRel != null) {
                personRel = em.getReference(personRel.getClass(), personRel.getId());
                person.setPerson(personRel);
            }
            Company company = person.getCompany();
            if (company != null) {
                company = em.getReference(company.getClass(), company.getId());
                person.setCompany(company);
            }
            Address addressidAddress = person.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress = em.getReference(addressidAddress.getClass(), addressidAddress.getIdAddress());
                person.setAddressidAddress(addressidAddress);
            }
            Collection<Hobby> attachedHobbyCollection = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionHobbyToAttach : person.getHobbyCollection()) {
                hobbyCollectionHobbyToAttach = em.getReference(hobbyCollectionHobbyToAttach.getClass(), hobbyCollectionHobbyToAttach.getIdHobby());
                attachedHobbyCollection.add(hobbyCollectionHobbyToAttach);
            }
            person.setHobbyCollection(attachedHobbyCollection);
            Collection<Phone> attachedPhoneCollection = new ArrayList<Phone>();
            for (Phone phoneCollectionPhoneToAttach : person.getPhoneCollection()) {
                phoneCollectionPhoneToAttach = em.getReference(phoneCollectionPhoneToAttach.getClass(), phoneCollectionPhoneToAttach.getIdPhone());
                attachedPhoneCollection.add(phoneCollectionPhoneToAttach);
            }
            person.setPhoneCollection(attachedPhoneCollection);
            em.persist(person);
            if (infoentity != null) {
                infoentity.setPerson(person);
                infoentity = em.merge(infoentity);
            }
            if (personRel != null) {
                entity.Infoentity oldInfoentityOfPersonRel = personRel.getInfoentity();
                if (oldInfoentityOfPersonRel != null) {
                    oldInfoentityOfPersonRel.setPerson(null);
                    oldInfoentityOfPersonRel = em.merge(oldInfoentityOfPersonRel);
                }
                personRel.setInfoentity(person);
                personRel = em.merge(personRel);
            }
            if (company != null) {
                entity.Infoentity oldInfoentityOfCompany = company.getInfoentity();
                if (oldInfoentityOfCompany != null) {
                    oldInfoentityOfCompany.setCompany(null);
                    oldInfoentityOfCompany = em.merge(oldInfoentityOfCompany);
                }
                company.setInfoentity(person);
                company = em.merge(company);
            }
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().add(person);
                addressidAddress = em.merge(addressidAddress);
            }
            for (Hobby hobbyCollectionHobby : person.getHobbyCollection()) {
                hobbyCollectionHobby.getInfoentityCollection().add(person);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            for (Phone phoneCollectionPhone : person.getPhoneCollection()) {
                entity.Infoentity oldInfoEntityidInfoEntityOfPhoneCollectionPhone = phoneCollectionPhone.getInfoEntityidInfoEntity();
                phoneCollectionPhone.setInfoEntityidInfoEntity(person);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
                if (oldInfoEntityidInfoEntityOfPhoneCollectionPhone != null) {
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone.getPhoneCollection().remove(phoneCollectionPhone);
                    oldInfoEntityidInfoEntityOfPhoneCollectionPhone = em.merge(oldInfoEntityidInfoEntityOfPhoneCollectionPhone);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerson(person.getId()) != null) {
                throw new PreexistingEntityException("Person " + person + " already exists.", ex);
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
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Person personOrphanCheck = person.getPerson();
            if (personOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Person " + personOrphanCheck + " in its person field has a non-nullable infoentity field.");
            }
            Company companyOrphanCheck = person.getCompany();
            if (companyOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Company " + companyOrphanCheck + " in its company field has a non-nullable infoentity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Infoentity infoentity = person.getInfoentity();
            if (infoentity != null) {
                infoentity.setPerson(null);
                infoentity = em.merge(infoentity);
            }
            Address addressidAddress = person.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().remove(person);
                addressidAddress = em.merge(addressidAddress);
            }
            Collection<Hobby> hobbyCollection = person.getHobbyCollection();
            for (Hobby hobbyCollectionHobby : hobbyCollection) {
                hobbyCollectionHobby.getInfoentityCollection().remove(person);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            Collection<Phone> phoneCollection = person.getPhoneCollection();
            for (Phone phoneCollectionPhone : phoneCollection) {
                phoneCollectionPhone.setInfoEntityidInfoEntity(null);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    // Peter

    public void edit(Person person) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
        em.close();
    }
}
