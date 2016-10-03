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
import entity.Phone;
import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class PhoneJpaController implements Serializable {

    public PhoneJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Phone phone) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Infoentity infoEntityidInfoEntity = phone.getInfoEntityidInfoEntity();
            if (infoEntityidInfoEntity != null) {
                infoEntityidInfoEntity = em.getReference(infoEntityidInfoEntity.getClass(), infoEntityidInfoEntity.getId());
                phone.setInfoEntityidInfoEntity(infoEntityidInfoEntity);
            }
            em.persist(phone);
            if (infoEntityidInfoEntity != null) {
                infoEntityidInfoEntity.getPhoneCollection().add(phone);
                infoEntityidInfoEntity = em.merge(infoEntityidInfoEntity);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPhone(phone.getIdPhone()) != null) {
                throw new PreexistingEntityException("Phone " + phone + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Phone phone) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Phone persistentPhone = em.find(Phone.class, phone.getIdPhone());
            Infoentity infoEntityidInfoEntityOld = persistentPhone.getInfoEntityidInfoEntity();
            Infoentity infoEntityidInfoEntityNew = phone.getInfoEntityidInfoEntity();
            if (infoEntityidInfoEntityNew != null) {
                infoEntityidInfoEntityNew = em.getReference(infoEntityidInfoEntityNew.getClass(), infoEntityidInfoEntityNew.getId());
                phone.setInfoEntityidInfoEntity(infoEntityidInfoEntityNew);
            }
            phone = em.merge(phone);
            if (infoEntityidInfoEntityOld != null && !infoEntityidInfoEntityOld.equals(infoEntityidInfoEntityNew)) {
                infoEntityidInfoEntityOld.getPhoneCollection().remove(phone);
                infoEntityidInfoEntityOld = em.merge(infoEntityidInfoEntityOld);
            }
            if (infoEntityidInfoEntityNew != null && !infoEntityidInfoEntityNew.equals(infoEntityidInfoEntityOld)) {
                infoEntityidInfoEntityNew.getPhoneCollection().add(phone);
                infoEntityidInfoEntityNew = em.merge(infoEntityidInfoEntityNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = phone.getIdPhone();
                if (findPhone(id) == null) {
                    throw new NonexistentEntityException("The phone with id " + id + " no longer exists.");
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
            Phone phone;
            try {
                phone = em.getReference(Phone.class, id);
                phone.getIdPhone();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The phone with id " + id + " no longer exists.", enfe);
            }
            Infoentity infoEntityidInfoEntity = phone.getInfoEntityidInfoEntity();
            if (infoEntityidInfoEntity != null) {
                infoEntityidInfoEntity.getPhoneCollection().remove(phone);
                infoEntityidInfoEntity = em.merge(infoEntityidInfoEntity);
            }
            em.remove(phone);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Phone> findPhoneEntities() {
        return findPhoneEntities(true, -1, -1);
    }

    public List<Phone> findPhoneEntities(int maxResults, int firstResult) {
        return findPhoneEntities(false, maxResults, firstResult);
    }

    private List<Phone> findPhoneEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Phone.class));
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

    public Phone findPhone(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Phone.class, id);
        } finally {
            em.close();
        }
    }

    public int getPhoneCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Phone> rt = cq.from(Phone.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
