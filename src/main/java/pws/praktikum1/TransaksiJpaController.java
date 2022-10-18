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
import javax.persistence.Persistence;
import pws.praktikum1.exceptions.IllegalOrphanException;
import pws.praktikum1.exceptions.NonexistentEntityException;
import pws.praktikum1.exceptions.PreexistingEntityException;

/**
 *
 * @author asus
 */
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("pws_praktikum1_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataPelanggan dataPelanggan = transaksi.getDataPelanggan();
            if (dataPelanggan != null) {
                dataPelanggan = em.getReference(dataPelanggan.getClass(), dataPelanggan.getIdPelanggan());
                transaksi.setDataPelanggan(dataPelanggan);
            }
            DataMobil nomorMobil = transaksi.getNomorMobil();
            if (nomorMobil != null) {
                nomorMobil = em.getReference(nomorMobil.getClass(), nomorMobil.getNomorMobil());
                transaksi.setNomorMobil(nomorMobil);
            }
            em.persist(transaksi);
            if (dataPelanggan != null) {
                Transaksi oldTransaksiOfDataPelanggan = dataPelanggan.getTransaksi();
                if (oldTransaksiOfDataPelanggan != null) {
                    oldTransaksiOfDataPelanggan.setDataPelanggan(null);
                    oldTransaksiOfDataPelanggan = em.merge(oldTransaksiOfDataPelanggan);
                }
                dataPelanggan.setTransaksi(transaksi);
                dataPelanggan = em.merge(dataPelanggan);
            }
            if (nomorMobil != null) {
                Transaksi oldTransaksiOfNomorMobil = nomorMobil.getTransaksi();
                if (oldTransaksiOfNomorMobil != null) {
                    oldTransaksiOfNomorMobil.setNomorMobil(null);
                    oldTransaksiOfNomorMobil = em.merge(oldTransaksiOfNomorMobil);
                }
                nomorMobil.setTransaksi(transaksi);
                nomorMobil = em.merge(nomorMobil);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getIdTransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getIdTransaksi());
            DataPelanggan dataPelangganOld = persistentTransaksi.getDataPelanggan();
            DataPelanggan dataPelangganNew = transaksi.getDataPelanggan();
            DataMobil nomorMobilOld = persistentTransaksi.getNomorMobil();
            DataMobil nomorMobilNew = transaksi.getNomorMobil();
            List<String> illegalOrphanMessages = null;
            if (dataPelangganOld != null && !dataPelangganOld.equals(dataPelangganNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain DataPelanggan " + dataPelangganOld + " since its transaksi field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (dataPelangganNew != null) {
                dataPelangganNew = em.getReference(dataPelangganNew.getClass(), dataPelangganNew.getIdPelanggan());
                transaksi.setDataPelanggan(dataPelangganNew);
            }
            if (nomorMobilNew != null) {
                nomorMobilNew = em.getReference(nomorMobilNew.getClass(), nomorMobilNew.getNomorMobil());
                transaksi.setNomorMobil(nomorMobilNew);
            }
            transaksi = em.merge(transaksi);
            if (dataPelangganNew != null && !dataPelangganNew.equals(dataPelangganOld)) {
                Transaksi oldTransaksiOfDataPelanggan = dataPelangganNew.getTransaksi();
                if (oldTransaksiOfDataPelanggan != null) {
                    oldTransaksiOfDataPelanggan.setDataPelanggan(null);
                    oldTransaksiOfDataPelanggan = em.merge(oldTransaksiOfDataPelanggan);
                }
                dataPelangganNew.setTransaksi(transaksi);
                dataPelangganNew = em.merge(dataPelangganNew);
            }
            if (nomorMobilOld != null && !nomorMobilOld.equals(nomorMobilNew)) {
                nomorMobilOld.setTransaksi(null);
                nomorMobilOld = em.merge(nomorMobilOld);
            }
            if (nomorMobilNew != null && !nomorMobilNew.equals(nomorMobilOld)) {
                Transaksi oldTransaksiOfNomorMobil = nomorMobilNew.getTransaksi();
                if (oldTransaksiOfNomorMobil != null) {
                    oldTransaksiOfNomorMobil.setNomorMobil(null);
                    oldTransaksiOfNomorMobil = em.merge(oldTransaksiOfNomorMobil);
                }
                nomorMobilNew.setTransaksi(transaksi);
                nomorMobilNew = em.merge(nomorMobilNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = transaksi.getIdTransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getIdTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            DataPelanggan dataPelangganOrphanCheck = transaksi.getDataPelanggan();
            if (dataPelangganOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaksi (" + transaksi + ") cannot be destroyed since the DataPelanggan " + dataPelangganOrphanCheck + " in its dataPelanggan field has a non-nullable transaksi field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DataMobil nomorMobil = transaksi.getNomorMobil();
            if (nomorMobil != null) {
                nomorMobil.setTransaksi(null);
                nomorMobil = em.merge(nomorMobil);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
