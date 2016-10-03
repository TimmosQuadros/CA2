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
import entity.Infoentity;
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
        if (hobby.getInfoentityCollection() == null) {
            hobby.setInfoentityCollection(new ArrayList<Infoentity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Infoentity> attachedInfoentityCollection = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionInfoentityToAttach : hobby.getInfoentityCollection()) {
                infoentityCollectionInfoentityToAttach = em.getReference(infoentityCollectionInfoentityToAttach.getClass(), infoentityCollectionInfoentityToAttach.getId());
                attachedInfoentityCollection.add(infoentityCollectionInfoentityToAttach);
            }
            hobby.setInfoentityCollection(attachedInfoentityCollection);
            em.persist(hobby);
            for (Infoentity infoentityCollectionInfoentity : hobby.getInfoentityCollection()) {
                infoentityCollectionInfoentity.getHobbyCollection().add(hobby);
                infoentityCollectionInfoentity = em.merge(infoentityCollectionInfoentity);
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
            Collection<Infoentity> infoentityCollectionOld = persistentHobby.getInfoentityCollection();
            Collection<Infoentity> infoentityCollectionNew = hobby.getInfoentityCollection();
            Collection<Infoentity> attachedInfoentityCollectionNew = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionNewInfoentityToAttach : infoentityCollectionNew) {
                infoentityCollectionNewInfoentityToAttach = em.getReference(infoentityCollectionNewInfoentityToAttach.getClass(), infoentityCollectionNewInfoentityToAttach.getId());
                attachedInfoentityCollectionNew.add(infoentityCollectionNewInfoentityToAttach);
            }
            infoentityCollectionNew = attachedInfoentityCollectionNew;
            hobby.setInfoentityCollection(infoentityCollectionNew);
            hobby = em.merge(hobby);
            for (Infoentity infoentityCollectionOldInfoentity : infoentityCollectionOld) {
                if (!infoentityCollectionNew.contains(infoentityCollectionOldInfoentity)) {
                    infoentityCollectionOldInfoentity.getHobbyCollection().remove(hobby);
                    infoentityCollectionOldInfoentity = em.merge(infoentityCollectionOldInfoentity);
                }
            }
            for (Infoentity infoentityCollectionNewInfoentity : infoentityCollectionNew) {
                if (!infoentityCollectionOld.contains(infoentityCollectionNewInfoentity)) {
                    infoentityCollectionNewInfoentity.getHobbyCollection().add(hobby);
                    infoentityCollectionNewInfoentity = em.merge(infoentityCollectionNewInfoentity);
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
            Collection<Infoentity> infoentityCollection = hobby.getInfoentityCollection();
            for (Infoentity infoentityCollectionInfoentity : infoentityCollection) {
                infoentityCollectionInfoentity.getHobbyCollection().remove(hobby);
                infoentityCollectionInfoentity = em.merge(infoentityCollectionInfoentity);
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
