/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Hobby;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Person;
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
public class HobbyJpaController implements Serializable {

    public HobbyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Hobby hobby) throws PreexistingEntityException, Exception {
        if (hobby.getPersonCollection() == null) {
            hobby.setPersonCollection(new ArrayList<Person>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Person> attachedPersonCollection = new ArrayList<Person>();
            for (Person personCollectionPersonToAttach : hobby.getPersonCollection()) {
                personCollectionPersonToAttach = em.getReference(personCollectionPersonToAttach.getClass(), personCollectionPersonToAttach.getIdPerson());
                attachedPersonCollection.add(personCollectionPersonToAttach);
            }
            hobby.setPersonCollection(attachedPersonCollection);
            em.persist(hobby);
            for (Person personCollectionPerson : hobby.getPersonCollection()) {
                personCollectionPerson.getHobbyCollection().add(hobby);
                personCollectionPerson = em.merge(personCollectionPerson);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHobby(hobby.getIdHobby()) != null) {
                throw new PreexistingEntityException("Hobby " + hobby + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Hobby hobby) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hobby persistentHobby = em.find(Hobby.class, hobby.getIdHobby());
            Collection<Person> personCollectionOld = persistentHobby.getPersonCollection();
            Collection<Person> personCollectionNew = hobby.getPersonCollection();
            Collection<Person> attachedPersonCollectionNew = new ArrayList<Person>();
            for (Person personCollectionNewPersonToAttach : personCollectionNew) {
                personCollectionNewPersonToAttach = em.getReference(personCollectionNewPersonToAttach.getClass(), personCollectionNewPersonToAttach.getIdPerson());
                attachedPersonCollectionNew.add(personCollectionNewPersonToAttach);
            }
            personCollectionNew = attachedPersonCollectionNew;
            hobby.setPersonCollection(personCollectionNew);
            hobby = em.merge(hobby);
            for (Person personCollectionOldPerson : personCollectionOld) {
                if (!personCollectionNew.contains(personCollectionOldPerson)) {
                    personCollectionOldPerson.getHobbyCollection().remove(hobby);
                    personCollectionOldPerson = em.merge(personCollectionOldPerson);
                }
            }
            for (Person personCollectionNewPerson : personCollectionNew) {
                if (!personCollectionOld.contains(personCollectionNewPerson)) {
                    personCollectionNewPerson.getHobbyCollection().add(hobby);
                    personCollectionNewPerson = em.merge(personCollectionNewPerson);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hobby.getIdHobby();
                if (findHobby(id) == null) {
                    throw new NonexistentEntityException("The hobby with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Hobby hobby;
            try {
                hobby = em.getReference(Hobby.class, id);
                hobby.getIdHobby();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hobby with id " + id + " no longer exists.", enfe);
            }
            Collection<Person> personCollection = hobby.getPersonCollection();
            for (Person personCollectionPerson : personCollection) {
                personCollectionPerson.getHobbyCollection().remove(hobby);
                personCollectionPerson = em.merge(personCollectionPerson);
            }
            em.remove(hobby);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Hobby> findHobbyEntities() {
        return findHobbyEntities(true, -1, -1);
    }

    public List<Hobby> findHobbyEntities(int maxResults, int firstResult) {
        return findHobbyEntities(false, maxResults, firstResult);
    }

    private List<Hobby> findHobbyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Hobby.class));
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

    public Hobby findHobby(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Hobby.class, id);
        } finally {
            em.close();
        }
    }

    public int getHobbyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Hobby> rt = cq.from(Hobby.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
