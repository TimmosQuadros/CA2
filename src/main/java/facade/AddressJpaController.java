/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Address;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Cityinfo;
import entity.Infoentity;
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
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) throws PreexistingEntityException, Exception {
        if (address.getInfoentityCollection() == null) {
            address.setInfoentityCollection(new ArrayList<Infoentity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cityinfo cityinfoidCityinfo = address.getCityinfoidCityinfo();
            if (cityinfoidCityinfo != null) {
                cityinfoidCityinfo = em.getReference(cityinfoidCityinfo.getClass(), cityinfoidCityinfo.getIdCityinfo());
                address.setCityinfoidCityinfo(cityinfoidCityinfo);
            }
            Collection<Infoentity> attachedInfoentityCollection = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionInfoentityToAttach : address.getInfoentityCollection()) {
                infoentityCollectionInfoentityToAttach = em.getReference(infoentityCollectionInfoentityToAttach.getClass(), infoentityCollectionInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollection.add(infoentityCollectionInfoentityToAttach);
            }
            address.setInfoentityCollection(attachedInfoentityCollection);
            em.persist(address);
            if (cityinfoidCityinfo != null) {
                cityinfoidCityinfo.getAddressCollection().add(address);
                cityinfoidCityinfo = em.merge(cityinfoidCityinfo);
            }
            for (Infoentity infoentityCollectionInfoentity : address.getInfoentityCollection()) {
                Address oldAddressidAddressOfInfoentityCollectionInfoentity = infoentityCollectionInfoentity.getAddressidAddress();
                infoentityCollectionInfoentity.setAddressidAddress(address);
                infoentityCollectionInfoentity = em.merge(infoentityCollectionInfoentity);
                if (oldAddressidAddressOfInfoentityCollectionInfoentity != null) {
                    oldAddressidAddressOfInfoentityCollectionInfoentity.getInfoentityCollection().remove(infoentityCollectionInfoentity);
                    oldAddressidAddressOfInfoentityCollectionInfoentity = em.merge(oldAddressidAddressOfInfoentityCollectionInfoentity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAddress(address.getIdAddress()) != null) {
                throw new PreexistingEntityException("Address " + address + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getIdAddress());
            Cityinfo cityinfoidCityinfoOld = persistentAddress.getCityinfoidCityinfo();
            Cityinfo cityinfoidCityinfoNew = address.getCityinfoidCityinfo();
            Collection<Infoentity> infoentityCollectionOld = persistentAddress.getInfoentityCollection();
            Collection<Infoentity> infoentityCollectionNew = address.getInfoentityCollection();
            List<String> illegalOrphanMessages = null;
            for (Infoentity infoentityCollectionOldInfoentity : infoentityCollectionOld) {
                if (!infoentityCollectionNew.contains(infoentityCollectionOldInfoentity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Infoentity " + infoentityCollectionOldInfoentity + " since its addressidAddress field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cityinfoidCityinfoNew != null) {
                cityinfoidCityinfoNew = em.getReference(cityinfoidCityinfoNew.getClass(), cityinfoidCityinfoNew.getIdCityinfo());
                address.setCityinfoidCityinfo(cityinfoidCityinfoNew);
            }
            Collection<Infoentity> attachedInfoentityCollectionNew = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionNewInfoentityToAttach : infoentityCollectionNew) {
                infoentityCollectionNewInfoentityToAttach = em.getReference(infoentityCollectionNewInfoentityToAttach.getClass(), infoentityCollectionNewInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollectionNew.add(infoentityCollectionNewInfoentityToAttach);
            }
            infoentityCollectionNew = attachedInfoentityCollectionNew;
            address.setInfoentityCollection(infoentityCollectionNew);
            address = em.merge(address);
            if (cityinfoidCityinfoOld != null && !cityinfoidCityinfoOld.equals(cityinfoidCityinfoNew)) {
                cityinfoidCityinfoOld.getAddressCollection().remove(address);
                cityinfoidCityinfoOld = em.merge(cityinfoidCityinfoOld);
            }
            if (cityinfoidCityinfoNew != null && !cityinfoidCityinfoNew.equals(cityinfoidCityinfoOld)) {
                cityinfoidCityinfoNew.getAddressCollection().add(address);
                cityinfoidCityinfoNew = em.merge(cityinfoidCityinfoNew);
            }
            for (Infoentity infoentityCollectionNewInfoentity : infoentityCollectionNew) {
                if (!infoentityCollectionOld.contains(infoentityCollectionNewInfoentity)) {
                    Address oldAddressidAddressOfInfoentityCollectionNewInfoentity = infoentityCollectionNewInfoentity.getAddressidAddress();
                    infoentityCollectionNewInfoentity.setAddressidAddress(address);
                    infoentityCollectionNewInfoentity = em.merge(infoentityCollectionNewInfoentity);
                    if (oldAddressidAddressOfInfoentityCollectionNewInfoentity != null && !oldAddressidAddressOfInfoentityCollectionNewInfoentity.equals(address)) {
                        oldAddressidAddressOfInfoentityCollectionNewInfoentity.getInfoentityCollection().remove(infoentityCollectionNewInfoentity);
                        oldAddressidAddressOfInfoentityCollectionNewInfoentity = em.merge(oldAddressidAddressOfInfoentityCollectionNewInfoentity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = address.getIdAddress();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
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
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getIdAddress();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Infoentity> infoentityCollectionOrphanCheck = address.getInfoentityCollection();
            for (Infoentity infoentityCollectionOrphanCheckInfoentity : infoentityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Address (" + address + ") cannot be destroyed since the Infoentity " + infoentityCollectionOrphanCheckInfoentity + " in its infoentityCollection field has a non-nullable addressidAddress field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cityinfo cityinfoidCityinfo = address.getCityinfoidCityinfo();
            if (cityinfoidCityinfo != null) {
                cityinfoidCityinfo.getAddressCollection().remove(address);
                cityinfoidCityinfo = em.merge(cityinfoidCityinfo);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
