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
import entity.Hobby;
import entity.Infoentity;
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
public class InfoentityJpaController implements Serializable {

    public InfoentityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Infoentity infoentity) throws PreexistingEntityException, Exception {
        if (infoentity.getHobbyCollection() == null) {
            infoentity.setHobbyCollection(new ArrayList<Hobby>());
        }
        if (infoentity.getPhoneCollection() == null) {
            infoentity.setPhoneCollection(new ArrayList<Phone>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person = infoentity.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getId());
                infoentity.setPerson(person);
            }
            Company company = infoentity.getCompany();
            if (company != null) {
                company = em.getReference(company.getClass(), company.getId());
                infoentity.setCompany(company);
            }
            Address addressidAddress = infoentity.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress = em.getReference(addressidAddress.getClass(), addressidAddress.getIdAddress());
                infoentity.setAddressidAddress(addressidAddress);
            }
            Collection<Hobby> attachedHobbyCollection = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionHobbyToAttach : infoentity.getHobbyCollection()) {
                hobbyCollectionHobbyToAttach = em.getReference(hobbyCollectionHobbyToAttach.getClass(), hobbyCollectionHobbyToAttach.getIdHobby());
                attachedHobbyCollection.add(hobbyCollectionHobbyToAttach);
            }
            infoentity.setHobbyCollection(attachedHobbyCollection);
            Collection<Phone> attachedPhoneCollection = new ArrayList<Phone>();
            for (Phone phoneCollectionPhoneToAttach : infoentity.getPhoneCollection()) {
                phoneCollectionPhoneToAttach = em.getReference(phoneCollectionPhoneToAttach.getClass(), phoneCollectionPhoneToAttach.getIdPhone());
                attachedPhoneCollection.add(phoneCollectionPhoneToAttach);
            }
            infoentity.setPhoneCollection(attachedPhoneCollection);
            em.persist(infoentity);
            if (person != null) {
                Infoentity oldInfoentityOfPerson = person.getInfoentity();
                if (oldInfoentityOfPerson != null) {
                    oldInfoentityOfPerson.setPerson(null);
                    oldInfoentityOfPerson = em.merge(oldInfoentityOfPerson);
                }
                person.setInfoentity(infoentity);
                person = em.merge(person);
            }
            if (company != null) {
                Infoentity oldInfoentityOfCompany = company.getInfoentity();
                if (oldInfoentityOfCompany != null) {
                    oldInfoentityOfCompany.setCompany(null);
                    oldInfoentityOfCompany = em.merge(oldInfoentityOfCompany);
                }
                company.setInfoentity(infoentity);
                company = em.merge(company);
            }
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().add(infoentity);
                addressidAddress = em.merge(addressidAddress);
            }
            for (Hobby hobbyCollectionHobby : infoentity.getHobbyCollection()) {
                hobbyCollectionHobby.getInfoentityCollection().add(infoentity);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
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
            if (findInfoentity(infoentity.getId()) != null) {
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity persistentInfoentity = em.find(Infoentity.class, infoentity.getId());
            Person personOld = persistentInfoentity.getPerson();
            Person personNew = infoentity.getPerson();
            Company companyOld = persistentInfoentity.getCompany();
            Company companyNew = infoentity.getCompany();
            Address addressidAddressOld = persistentInfoentity.getAddressidAddress();
            Address addressidAddressNew = infoentity.getAddressidAddress();
            Collection<Hobby> hobbyCollectionOld = persistentInfoentity.getHobbyCollection();
            Collection<Hobby> hobbyCollectionNew = infoentity.getHobbyCollection();
            Collection<Phone> phoneCollectionOld = persistentInfoentity.getPhoneCollection();
            Collection<Phone> phoneCollectionNew = infoentity.getPhoneCollection();
            List<String> illegalOrphanMessages = null;
            if (personOld != null && !personOld.equals(personNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Person " + personOld + " since its infoentity field is not nullable.");
            }
            if (companyOld != null && !companyOld.equals(companyNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Company " + companyOld + " since its infoentity field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personNew != null) {
                personNew = em.getReference(personNew.getClass(), personNew.getId());
                infoentity.setPerson(personNew);
            }
            if (companyNew != null) {
                companyNew = em.getReference(companyNew.getClass(), companyNew.getId());
                infoentity.setCompany(companyNew);
            }
            if (addressidAddressNew != null) {
                addressidAddressNew = em.getReference(addressidAddressNew.getClass(), addressidAddressNew.getIdAddress());
                infoentity.setAddressidAddress(addressidAddressNew);
            }
            Collection<Hobby> attachedHobbyCollectionNew = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionNewHobbyToAttach : hobbyCollectionNew) {
                hobbyCollectionNewHobbyToAttach = em.getReference(hobbyCollectionNewHobbyToAttach.getClass(), hobbyCollectionNewHobbyToAttach.getIdHobby());
                attachedHobbyCollectionNew.add(hobbyCollectionNewHobbyToAttach);
            }
            hobbyCollectionNew = attachedHobbyCollectionNew;
            infoentity.setHobbyCollection(hobbyCollectionNew);
            Collection<Phone> attachedPhoneCollectionNew = new ArrayList<Phone>();
            for (Phone phoneCollectionNewPhoneToAttach : phoneCollectionNew) {
                phoneCollectionNewPhoneToAttach = em.getReference(phoneCollectionNewPhoneToAttach.getClass(), phoneCollectionNewPhoneToAttach.getIdPhone());
                attachedPhoneCollectionNew.add(phoneCollectionNewPhoneToAttach);
            }
            phoneCollectionNew = attachedPhoneCollectionNew;
            infoentity.setPhoneCollection(phoneCollectionNew);
            infoentity = em.merge(infoentity);
            if (personNew != null && !personNew.equals(personOld)) {
                Infoentity oldInfoentityOfPerson = personNew.getInfoentity();
                if (oldInfoentityOfPerson != null) {
                    oldInfoentityOfPerson.setPerson(null);
                    oldInfoentityOfPerson = em.merge(oldInfoentityOfPerson);
                }
                personNew.setInfoentity(infoentity);
                personNew = em.merge(personNew);
            }
            if (companyNew != null && !companyNew.equals(companyOld)) {
                Infoentity oldInfoentityOfCompany = companyNew.getInfoentity();
                if (oldInfoentityOfCompany != null) {
                    oldInfoentityOfCompany.setCompany(null);
                    oldInfoentityOfCompany = em.merge(oldInfoentityOfCompany);
                }
                companyNew.setInfoentity(infoentity);
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
            for (Hobby hobbyCollectionOldHobby : hobbyCollectionOld) {
                if (!hobbyCollectionNew.contains(hobbyCollectionOldHobby)) {
                    hobbyCollectionOldHobby.getInfoentityCollection().remove(infoentity);
                    hobbyCollectionOldHobby = em.merge(hobbyCollectionOldHobby);
                }
            }
            for (Hobby hobbyCollectionNewHobby : hobbyCollectionNew) {
                if (!hobbyCollectionOld.contains(hobbyCollectionNewHobby)) {
                    hobbyCollectionNewHobby.getInfoentityCollection().add(infoentity);
                    hobbyCollectionNewHobby = em.merge(hobbyCollectionNewHobby);
                }
            }
            for (Phone phoneCollectionOldPhone : phoneCollectionOld) {
                if (!phoneCollectionNew.contains(phoneCollectionOldPhone)) {
                    phoneCollectionOldPhone.setInfoEntityidInfoEntity(null);
                    phoneCollectionOldPhone = em.merge(phoneCollectionOldPhone);
                }
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
                Integer id = infoentity.getId();
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity infoentity;
            try {
                infoentity = em.getReference(Infoentity.class, id);
                infoentity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infoentity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Person personOrphanCheck = infoentity.getPerson();
            if (personOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Infoentity (" + infoentity + ") cannot be destroyed since the Person " + personOrphanCheck + " in its person field has a non-nullable infoentity field.");
            }
            Company companyOrphanCheck = infoentity.getCompany();
            if (companyOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Infoentity (" + infoentity + ") cannot be destroyed since the Company " + companyOrphanCheck + " in its company field has a non-nullable infoentity field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Address addressidAddress = infoentity.getAddressidAddress();
            if (addressidAddress != null) {
                addressidAddress.getInfoentityCollection().remove(infoentity);
                addressidAddress = em.merge(addressidAddress);
            }
            Collection<Hobby> hobbyCollection = infoentity.getHobbyCollection();
            for (Hobby hobbyCollectionHobby : hobbyCollection) {
                hobbyCollectionHobby.getInfoentityCollection().remove(infoentity);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            Collection<Phone> phoneCollection = infoentity.getPhoneCollection();
            for (Phone phoneCollectionPhone : phoneCollection) {
                phoneCollectionPhone.setInfoEntityidInfoEntity(null);
                phoneCollectionPhone = em.merge(phoneCollectionPhone);
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

    public Infoentity findInfoentity(Integer id) {
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
