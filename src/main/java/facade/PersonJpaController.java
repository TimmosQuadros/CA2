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
import entity.Hobby;
import java.util.ArrayList;
import java.util.Collection;
import entity.Infoentity;
import entity.Person;
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

    public void create(Person person) throws PreexistingEntityException, Exception {
        if (person.getHobbyCollection() == null) {
            person.setHobbyCollection(new ArrayList<Hobby>());
        }
        if (person.getInfoentityCollection() == null) {
            person.setInfoentityCollection(new ArrayList<Infoentity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Hobby> attachedHobbyCollection = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionHobbyToAttach : person.getHobbyCollection()) {
                hobbyCollectionHobbyToAttach = em.getReference(hobbyCollectionHobbyToAttach.getClass(), hobbyCollectionHobbyToAttach.getIdHobby());
                attachedHobbyCollection.add(hobbyCollectionHobbyToAttach);
            }
            person.setHobbyCollection(attachedHobbyCollection);
            Collection<Infoentity> attachedInfoentityCollection = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionInfoentityToAttach : person.getInfoentityCollection()) {
                infoentityCollectionInfoentityToAttach = em.getReference(infoentityCollectionInfoentityToAttach.getClass(), infoentityCollectionInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollection.add(infoentityCollectionInfoentityToAttach);
            }
            person.setInfoentityCollection(attachedInfoentityCollection);
            em.persist(person);
            for (Hobby hobbyCollectionHobby : person.getHobbyCollection()) {
                hobbyCollectionHobby.getPersonCollection().add(person);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
            }
            for (Infoentity infoentityCollectionInfoentity : person.getInfoentityCollection()) {
                Person oldPersonOfInfoentityCollectionInfoentity = infoentityCollectionInfoentity.getPerson();
                infoentityCollectionInfoentity.setPerson(person);
                infoentityCollectionInfoentity = em.merge(infoentityCollectionInfoentity);
                if (oldPersonOfInfoentityCollectionInfoentity != null) {
                    oldPersonOfInfoentityCollectionInfoentity.getInfoentityCollection().remove(infoentityCollectionInfoentity);
                    oldPersonOfInfoentityCollectionInfoentity = em.merge(oldPersonOfInfoentityCollectionInfoentity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerson(person.getIdPerson()) != null) {
                throw new PreexistingEntityException("Person " + person + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person persistentPerson = em.find(Person.class, person.getIdPerson());
            Collection<Hobby> hobbyCollectionOld = persistentPerson.getHobbyCollection();
            Collection<Hobby> hobbyCollectionNew = person.getHobbyCollection();
            Collection<Infoentity> infoentityCollectionOld = persistentPerson.getInfoentityCollection();
            Collection<Infoentity> infoentityCollectionNew = person.getInfoentityCollection();
            List<String> illegalOrphanMessages = null;
            for (Infoentity infoentityCollectionOldInfoentity : infoentityCollectionOld) {
                if (!infoentityCollectionNew.contains(infoentityCollectionOldInfoentity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Infoentity " + infoentityCollectionOldInfoentity + " since its person field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Hobby> attachedHobbyCollectionNew = new ArrayList<Hobby>();
            for (Hobby hobbyCollectionNewHobbyToAttach : hobbyCollectionNew) {
                hobbyCollectionNewHobbyToAttach = em.getReference(hobbyCollectionNewHobbyToAttach.getClass(), hobbyCollectionNewHobbyToAttach.getIdHobby());
                attachedHobbyCollectionNew.add(hobbyCollectionNewHobbyToAttach);
            }
            hobbyCollectionNew = attachedHobbyCollectionNew;
            person.setHobbyCollection(hobbyCollectionNew);
            Collection<Infoentity> attachedInfoentityCollectionNew = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionNewInfoentityToAttach : infoentityCollectionNew) {
                infoentityCollectionNewInfoentityToAttach = em.getReference(infoentityCollectionNewInfoentityToAttach.getClass(), infoentityCollectionNewInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollectionNew.add(infoentityCollectionNewInfoentityToAttach);
            }
            infoentityCollectionNew = attachedInfoentityCollectionNew;
            person.setInfoentityCollection(infoentityCollectionNew);
            person = em.merge(person);
            for (Hobby hobbyCollectionOldHobby : hobbyCollectionOld) {
                if (!hobbyCollectionNew.contains(hobbyCollectionOldHobby)) {
                    hobbyCollectionOldHobby.getPersonCollection().remove(person);
                    hobbyCollectionOldHobby = em.merge(hobbyCollectionOldHobby);
                }
            }
            for (Hobby hobbyCollectionNewHobby : hobbyCollectionNew) {
                if (!hobbyCollectionOld.contains(hobbyCollectionNewHobby)) {
                    hobbyCollectionNewHobby.getPersonCollection().add(person);
                    hobbyCollectionNewHobby = em.merge(hobbyCollectionNewHobby);
                }
            }
            for (Infoentity infoentityCollectionNewInfoentity : infoentityCollectionNew) {
                if (!infoentityCollectionOld.contains(infoentityCollectionNewInfoentity)) {
                    Person oldPersonOfInfoentityCollectionNewInfoentity = infoentityCollectionNewInfoentity.getPerson();
                    infoentityCollectionNewInfoentity.setPerson(person);
                    infoentityCollectionNewInfoentity = em.merge(infoentityCollectionNewInfoentity);
                    if (oldPersonOfInfoentityCollectionNewInfoentity != null && !oldPersonOfInfoentityCollectionNewInfoentity.equals(person)) {
                        oldPersonOfInfoentityCollectionNewInfoentity.getInfoentityCollection().remove(infoentityCollectionNewInfoentity);
                        oldPersonOfInfoentityCollectionNewInfoentity = em.merge(oldPersonOfInfoentityCollectionNewInfoentity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getIdPerson();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
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
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getIdPerson();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Infoentity> infoentityCollectionOrphanCheck = person.getInfoentityCollection();
            for (Infoentity infoentityCollectionOrphanCheckInfoentity : infoentityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Infoentity " + infoentityCollectionOrphanCheckInfoentity + " in its infoentityCollection field has a non-nullable person field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Hobby> hobbyCollection = person.getHobbyCollection();
            for (Hobby hobbyCollectionHobby : hobbyCollection) {
                hobbyCollectionHobby.getPersonCollection().remove(person);
                hobbyCollectionHobby = em.merge(hobbyCollectionHobby);
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
    
}
