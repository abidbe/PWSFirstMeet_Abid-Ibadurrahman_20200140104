/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pws.praktikum1;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "data_mobil")
@NamedQueries({
    @NamedQuery(name = "DataMobil.findAll", query = "SELECT d FROM DataMobil d"),
    @NamedQuery(name = "DataMobil.findByNomorMobil", query = "SELECT d FROM DataMobil d WHERE d.nomorMobil = :nomorMobil"),
    @NamedQuery(name = "DataMobil.findByJenisMobil", query = "SELECT d FROM DataMobil d WHERE d.jenisMobil = :jenisMobil"),
    @NamedQuery(name = "DataMobil.findByMerkMobil", query = "SELECT d FROM DataMobil d WHERE d.merkMobil = :merkMobil"),
    @NamedQuery(name = "DataMobil.findByHargaSewa", query = "SELECT d FROM DataMobil d WHERE d.hargaSewa = :hargaSewa")})
public class DataMobil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nomor_mobil")
    private String nomorMobil;
    @Column(name = "jenis_mobil")
    private String jenisMobil;
    @Column(name = "merk_mobil")
    private String merkMobil;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "harga_sewa")
    private Float hargaSewa;
    @OneToOne(mappedBy = "nomorMobil")
    private Transaksi transaksi;

    public DataMobil() {
    }

    public DataMobil(String nomorMobil) {
        this.nomorMobil = nomorMobil;
    }

    public String getNomorMobil() {
        return nomorMobil;
    }

    public void setNomorMobil(String nomorMobil) {
        this.nomorMobil = nomorMobil;
    }

    public String getJenisMobil() {
        return jenisMobil;
    }

    public void setJenisMobil(String jenisMobil) {
        this.jenisMobil = jenisMobil;
    }

    public String getMerkMobil() {
        return merkMobil;
    }

    public void setMerkMobil(String merkMobil) {
        this.merkMobil = merkMobil;
    }

    public Float getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(Float hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nomorMobil != null ? nomorMobil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataMobil)) {
            return false;
        }
        DataMobil other = (DataMobil) object;
        if ((this.nomorMobil == null && other.nomorMobil != null) || (this.nomorMobil != null && !this.nomorMobil.equals(other.nomorMobil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pws.praktikum1.DataMobil[ nomorMobil=" + nomorMobil + " ]";
    }
    
}
