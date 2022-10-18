/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pws.praktikum1;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import pws.praktikum1.exceptions.IllegalOrphanException;
import pws.praktikum1.exceptions.NonexistentEntityException;
import pws.praktikum1.exceptions.PreexistingEntityException;

/**
 *
 * @author asus
 */
public class DataPelangganJpaController implements Serializable {

    public DataPelangganJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DataPelanggan dataPelanggan) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Transaksi transaksiOrphanCheck = dataPelanggan.getTransaksi();
        if (transaksiOrphanCheck != null) {
            DataPelanggan oldDataPelangganOfTransaksi = transaksiOrphanCheck.getDataPelanggan();
            if (oldDataPelangganOfTransaksi != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Transaksi " + transaksiOrphanCheck + " already has an item of type DataPelanggan whose transaksi column cannot be null. Please make another selection for the transaksi field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi transaksi = dataPelanggan.getTransaksi();
            if (transaksi != null) {
                transaksi = em.getReference(transaksi.getClass(), transaksi.getIdTransaksi());
                dataPelanggan.setTransaksi(transaksi);
            }
            em.persist(dataPelanggan);
            if (transaksi != null) {
                transaksi.setDataPelanggan(dataPelanggan);
                transaksi = em.merge(transaksi);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataPelanggan(dataPelanggan.getIdPelanggan()) != null) {
                throw new PreexistingEntityException("DataPelanggan " + dataPelanggan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DataPelanggan dataPelanggan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataPelanggan persistentDataPelanggan = em.find(DataPelanggan.class, dataPelanggan.getIdPelanggan());
            Transaksi transaksiOld = persistentDataPelanggan.getTransaksi();
            Transaksi transaksiNew = dataPelanggan.getTransaksi();
            List<String> illegalOrphanMessages = null;
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                DataPelanggan oldDataPelangganOfTransaksi = transaksiNew.getDataPelanggan();
                if (oldDataPelangganOfTransaksi != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Transaksi " + transaksiNew + " already has an item of type DataPelanggan whose transaksi column cannot be null. Please make another selection for the transaksi field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (transaksiNew != null) {
                transaksiNew = em.getReference(transaksiNew.getClass(), transaksiNew.getIdTransaksi());
                dataPelanggan.setTransaksi(transaksiNew);
            }
            dataPelanggan = em.merge(dataPelanggan);
            if (transaksiOld != null && !transaksiOld.equals(transaksiNew)) {
                transaksiOld.setDataPelanggan(null);
                transaksiOld = em.merge(transaksiOld);
            }
            if (transaksiNew != null && !transaksiNew.equals(transaksiOld)) {
                transaksiNew.setDataPelanggan(dataPelanggan);
                transaksiNew = em.merge(transaksiNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dataPelanggan.getIdPelanggan();
                if (findDataPelanggan(id) == null) {
                    throw new NonexistentEntityException("The dataPelanggan with id " + id + " no longer exists.");
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
            DataPelanggan dataPelanggan;
            try {
                dataPelanggan = em.getReference(DataPelanggan.class, id);
                dataPelanggan.getIdPelanggan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataPelanggan with id " + id + " no longer exists.", enfe);
            }
            Transaksi transaksi = dataPelanggan.getTransaksi();
            if (transaksi != null) {
                transaksi.setDataPelanggan(null);
                transaksi = em.merge(transaksi);
            }
            em.remove(dataPelanggan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DataPelanggan> findDataPelangganEntities() {
        return findDataPelangganEntities(true, -1, -1);
    }

    public List<DataPelanggan> findDataPelangganEntities(int maxResults, int firstResult) {
        return findDataPelangganEntities(false, maxResults, firstResult);
    }

    private List<DataPelanggan> findDataPelangganEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DataPelanggan.class));
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

    public DataPelanggan findDataPelanggan(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataPelanggan.class, id);
        } finally {
            em.close();
        }
    }

    public int getDataPelangganCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DataPelanggan> rt = cq.from(DataPelanggan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
