/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Company;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class CompanyJpaController implements Serializable {

    public CompanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) throws PreexistingEntityException, Exception {
        if (company.getInfoentityCollection() == null) {
            company.setInfoentityCollection(new ArrayList<Infoentity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Infoentity> attachedInfoentityCollection = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionInfoentityToAttach : company.getInfoentityCollection()) {
                infoentityCollectionInfoentityToAttach = em.getReference(infoentityCollectionInfoentityToAttach.getClass(), infoentityCollectionInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollection.add(infoentityCollectionInfoentityToAttach);
            }
            company.setInfoentityCollection(attachedInfoentityCollection);
            em.persist(company);
            for (Infoentity infoentityCollectionInfoentity : company.getInfoentityCollection()) {
                Company oldCompanyOfInfoentityCollectionInfoentity = infoentityCollectionInfoentity.getCompany();
                infoentityCollectionInfoentity.setCompany(company);
                infoentityCollectionInfoentity = em.merge(infoentityCollectionInfoentity);
                if (oldCompanyOfInfoentityCollectionInfoentity != null) {
                    oldCompanyOfInfoentityCollectionInfoentity.getInfoentityCollection().remove(infoentityCollectionInfoentity);
                    oldCompanyOfInfoentityCollectionInfoentity = em.merge(oldCompanyOfInfoentityCollectionInfoentity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompany(company.getIdCompany()) != null) {
                throw new PreexistingEntityException("Company " + company + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Company company) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company persistentCompany = em.find(Company.class, company.getIdCompany());
            Collection<Infoentity> infoentityCollectionOld = persistentCompany.getInfoentityCollection();
            Collection<Infoentity> infoentityCollectionNew = company.getInfoentityCollection();
            List<String> illegalOrphanMessages = null;
            for (Infoentity infoentityCollectionOldInfoentity : infoentityCollectionOld) {
                if (!infoentityCollectionNew.contains(infoentityCollectionOldInfoentity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Infoentity " + infoentityCollectionOldInfoentity + " since its company field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Infoentity> attachedInfoentityCollectionNew = new ArrayList<Infoentity>();
            for (Infoentity infoentityCollectionNewInfoentityToAttach : infoentityCollectionNew) {
                infoentityCollectionNewInfoentityToAttach = em.getReference(infoentityCollectionNewInfoentityToAttach.getClass(), infoentityCollectionNewInfoentityToAttach.getInfoentityPK());
                attachedInfoentityCollectionNew.add(infoentityCollectionNewInfoentityToAttach);
            }
            infoentityCollectionNew = attachedInfoentityCollectionNew;
            company.setInfoentityCollection(infoentityCollectionNew);
            company = em.merge(company);
            for (Infoentity infoentityCollectionNewInfoentity : infoentityCollectionNew) {
                if (!infoentityCollectionOld.contains(infoentityCollectionNewInfoentity)) {
                    Company oldCompanyOfInfoentityCollectionNewInfoentity = infoentityCollectionNewInfoentity.getCompany();
                    infoentityCollectionNewInfoentity.setCompany(company);
                    infoentityCollectionNewInfoentity = em.merge(infoentityCollectionNewInfoentity);
                    if (oldCompanyOfInfoentityCollectionNewInfoentity != null && !oldCompanyOfInfoentityCollectionNewInfoentity.equals(company)) {
                        oldCompanyOfInfoentityCollectionNewInfoentity.getInfoentityCollection().remove(infoentityCollectionNewInfoentity);
                        oldCompanyOfInfoentityCollectionNewInfoentity = em.merge(oldCompanyOfInfoentityCollectionNewInfoentity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = company.getIdCompany();
                if (findCompany(id) == null) {
                    throw new NonexistentEntityException("The company with id " + id + " no longer exists.");
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
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getIdCompany();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Infoentity> infoentityCollectionOrphanCheck = company.getInfoentityCollection();
            for (Infoentity infoentityCollectionOrphanCheckInfoentity : infoentityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Company (" + company + ") cannot be destroyed since the Infoentity " + infoentityCollectionOrphanCheckInfoentity + " in its infoentityCollection field has a non-nullable company field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
