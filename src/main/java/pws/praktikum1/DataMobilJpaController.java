/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pws.praktikum1;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pws.praktikum1.exceptions.NonexistentEntityException;
import pws.praktikum1.exceptions.PreexistingEntityException;

/**
 *
 * @author asus
 */
public class DataMobilJpaController implements Serializable {

    public DataMobilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("pws_praktikum1_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DataMobil dataMobil) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi transaksi = dataMobil.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getIdTransaksi());
                dataMobil.setTransaksi(transaksi);
            }
            em.persist(dataMobil);
            if (transaksi != null) {
                DataMobil oldNomorMobilOfTransaksi = transaksi.getNomorMobil();
                if (oldNomorMobilOfTransaksi != null) {
                    oldNomorMobilOfTransaksi.setTransaksi(null);
                    oldNomorMobilOfTransaksi = em.merge(oldNomorMobilOfTransaksi);
                }
                transaksi.setNomorMobil(dataMobil);
                transaksi = em.merge(transaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataMobil(dataMobil.getNomorMobil()) != null) {
                throw new PreexistingEntityException("DataMobil " + dataMobil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DataMobil dataMobil) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataMobil persistentDataMobil = em.find(DataMobil.class, dataMobil.getNomorMobil());
            Transaksi transaksiOld = persistentDataMobil.getTransaksi();
            Transaksi transaksiNew = dataMobil.getTransaksi();
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getIdTransaksi());
                dataMobil.setTransaksi(transaksiNew);
            }
            dataMobil = em.merge(dataMobil);
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                transaksiOld.setNomorMobil(null);
                transaksiOld = em.merge(transaksiOld);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                DataMobil oldNomorMobilOfTransaksi = transaksiNew.getNomorMobil();
                if (oldNomorMobilOfTransaksi != null) {
                    oldNomorMobilOfTransaksi.setTransaksi(null);
                    oldNomorMobilOfTransaksi = em.merge(oldNomorMobilOfTransaksi);
                }
                transaksiNew.setNomorMobil(dataMobil);
                transaksiNew = em.merge(transaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dataMobil.getNomorMobil();
                if (findDataMobil(id) == null) {
                    throw new NonexistentEntityException("The dataMobil with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataMobil dataMobil;
            try {
                dataMobil = em.getReference(DataMobil.class, id);
                dataMobil.getNomorMobil();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataMobil with id " + id + " no longer exists.", enfe);
            }
            Transaksi transaksi = dataMobil.getTransaksi();
            if (transaksi != null) {
                transaksi.setNomorMobil(null);
                transaksi = em.merge(transaksi);
            }
            em.remove(dataMobil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DataMobil> findDataMobilEntities() {
        return findDataMobilEntities(true, -1, -1);
    }

    public List<DataMobil> findDataMobilEntities(int maxResults, int firstResult) {
        return findDataMobilEntities(false, maxResults, firstResult);
    }

    private List<DataMobil> findDataMobilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DataMobil.class));
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

    public DataMobil findDataMobil(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataMobil.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataMobilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DataMobil> rt = cq.from(DataMobil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
