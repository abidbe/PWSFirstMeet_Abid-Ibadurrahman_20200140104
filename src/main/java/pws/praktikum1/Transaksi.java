/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pws.praktikum1;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByIdTransaksi", query = "SELECT t FROM Transaksi t WHERE t.idTransaksi = :idTransaksi"),
    @NamedQuery(name = "Transaksi.findByLamaSewa", query = "SELECT t FROM Transaksi t WHERE t.lamaSewa = :lamaSewa"),
    @NamedQuery(name = "Transaksi.findByTanggalSewa", query = "SELECT t FROM Transaksi t WHERE t.tanggalSewa = :tanggalSewa"),
    @NamedQuery(name = "Transaksi.findByTotalSewa", query = "SELECT t FROM Transaksi t WHERE t.totalSewa = :totalSewa"),
    @NamedQuery(name = "Transaksi.findByIdPelanggan", query = "SELECT t FROM Transaksi t WHERE t.idPelanggan = :idPelanggan")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_transaksi")
    private String idTransaksi;
    @Column(name = "lama_sewa")
    private Integer lamaSewa;
    @Column(name = "tanggal_sewa")
    @Temporal(TemporalType.DATE)
    private Date tanggalSewa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_sewa")
    private Float totalSewa;
    @Column(name = "id_pelanggan")
    private String idPelanggan;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transaksi")
    private DataPelanggan dataPelanggan;
    @JoinColumn(name = "nomor_mobil", referencedColumnName = "nomor_mobil")
    @OneToOne
    private DataMobil nomorMobil;

    public Transaksi() {
    }

    public Transaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Integer getLamaSewa() {
        return lamaSewa;
    }

    public void setLamaSewa(Integer lamaSewa) {
        this.lamaSewa = lamaSewa;
    }

    public Date getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(Date tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }

    public Float getTotalSewa() {
        return totalSewa;
    }

    public void setTotalSewa(Float totalSewa) {
        this.totalSewa = totalSewa;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public DataPelanggan getDataPelanggan() {
        return dataPelanggan;
    }

    public void setDataPelanggan(DataPelanggan dataPelanggan) {
        this.dataPelanggan = dataPelanggan;
    }

    public DataMobil getNomorMobil() {
        return nomorMobil;
    }

    public void setNomorMobil(DataMobil nomorMobil) {
        this.nomorMobil = nomorMobil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pws.praktikum1.Transaksi[ idTransaksi=" + idTransaksi + " ]";
    }
    
}
