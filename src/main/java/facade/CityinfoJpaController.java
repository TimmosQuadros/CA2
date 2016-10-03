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
import entity.Address;
import entity.Cityinfo;
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
public class CityinfoJpaController implements Serializable {

    public CityinfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cityinfo cityinfo) throws PreexistingEntityException, Exception {
        if (cityinfo.getAddressCollection() == null) {
            cityinfo.setAddressCollection(new ArrayList<Address>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Address> attachedAddressCollection = new ArrayList<Address>();
            for (Address addressCollectionAddressToAttach : cityinfo.getAddressCollection()) {
                addressCollectionAddressToAttach = em.getReference(addressCollectionAddressToAttach.getClass(), addressCollectionAddressToAttach.getIdAddress());
                attachedAddressCollection.add(addressCollectionAddressToAttach);
            }
            cityinfo.setAddressCollection(attachedAddressCollection);
            em.persist(cityinfo);
            for (Address addressCollectionAddress : cityinfo.getAddressCollection()) {
                Cityinfo oldCityinfoidCityinfoOfAddressCollectionAddress = addressCollectionAddress.getCityinfoidCityinfo();
                addressCollectionAddress.setCityinfoidCityinfo(cityinfo);
                addressCollectionAddress = em.merge(addressCollectionAddress);
                if (oldCityinfoidCityinfoOfAddressCollectionAddress != null) {
                    oldCityinfoidCityinfoOfAddressCollectionAddress.getAddressCollection().remove(addressCollectionAddress);
                    oldCityinfoidCityinfoOfAddressCollectionAddress = em.merge(oldCityinfoidCityinfoOfAddressCollectionAddress);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCityinfo(cityinfo.getIdCityinfo()) != null) {
                throw new PreexistingEntityException("Cityinfo " + cityinfo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cityinfo cityinfo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cityinfo persistentCityinfo = em.find(Cityinfo.class, cityinfo.getIdCityinfo());
            Collection<Address> addressCollectionOld = persistentCityinfo.getAddressCollection();
            Collection<Address> addressCollectionNew = cityinfo.getAddressCollection();
            List<String> illegalOrphanMessages = null;
            for (Address addressCollectionOldAddress : addressCollectionOld) {
                if (!addressCollectionNew.contains(addressCollectionOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressCollectionOldAddress + " since its cityinfoidCityinfo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Address> attachedAddressCollectionNew = new ArrayList<Address>();
            for (Address addressCollectionNewAddressToAttach : addressCollectionNew) {
                addressCollectionNewAddressToAttach = em.getReference(addressCollectionNewAddressToAttach.getClass(), addressCollectionNewAddressToAttach.getIdAddress());
                attachedAddressCollectionNew.add(addressCollectionNewAddressToAttach);
            }
            addressCollectionNew = attachedAddressCollectionNew;
            cityinfo.setAddressCollection(addressCollectionNew);
            cityinfo = em.merge(cityinfo);
            for (Address addressCollectionNewAddress : addressCollectionNew) {
                if (!addressCollectionOld.contains(addressCollectionNewAddress)) {
                    Cityinfo oldCityinfoidCityinfoOfAddressCollectionNewAddress = addressCollectionNewAddress.getCityinfoidCityinfo();
                    addressCollectionNewAddress.setCityinfoidCityinfo(cityinfo);
                    addressCollectionNewAddress = em.merge(addressCollectionNewAddress);
                    if (oldCityinfoidCityinfoOfAddressCollectionNewAddress != null && !oldCityinfoidCityinfoOfAddressCollectionNewAddress.equals(cityinfo)) {
                        oldCityinfoidCityinfoOfAddressCollectionNewAddress.getAddressCollection().remove(addressCollectionNewAddress);
                        oldCityinfoidCityinfoOfAddressCollectionNewAddress = em.merge(oldCityinfoidCityinfoOfAddressCollectionNewAddress);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cityinfo.getIdCityinfo();
                if (findCityinfo(id) == null) {
                    throw new NonexistentEntityException("The cityinfo with id " + id + " no longer exists.");
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
            Cityinfo cityinfo;
            try {
                cityinfo = em.getReference(Cityinfo.class, id);
                cityinfo.getIdCityinfo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cityinfo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Address> addressCollectionOrphanCheck = cityinfo.getAddressCollection();
            for (Address addressCollectionOrphanCheckAddress : addressCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cityinfo (" + cityinfo + ") cannot be destroyed since the Address " + addressCollectionOrphanCheckAddress + " in its addressCollection field has a non-nullable cityinfoidCityinfo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cityinfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cityinfo> findCityinfoEntities() {
        return findCityinfoEntities(true, -1, -1);
    }

    public List<Cityinfo> findCityinfoEntities(int maxResults, int firstResult) {
        return findCityinfoEntities(false, maxResults, firstResult);
    }

    private List<Cityinfo> findCityinfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cityinfo.class));
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

    public Cityinfo findCityinfo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cityinfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCityinfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cityinfo> rt = cq.from(Cityinfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
