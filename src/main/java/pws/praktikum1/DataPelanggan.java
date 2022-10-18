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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "data_pelanggan")
@NamedQueries({
    @NamedQuery(name = "DataPelanggan.findAll", query = "SELECT d FROM DataPelanggan d"),
    @NamedQuery(name = "DataPelanggan.findByIdPelanggan", query = "SELECT d FROM DataPelanggan d WHERE d.idPelanggan = :idPelanggan"),
    @NamedQuery(name = "DataPelanggan.findByNamaPelanggan", query = "SELECT d FROM DataPelanggan d WHERE d.namaPelanggan = :namaPelanggan"),
    @NamedQuery(name = "DataPelanggan.findByAlamat", query = "SELECT d FROM DataPelanggan d WHERE d.alamat = :alamat"),
    @NamedQuery(name = "DataPelanggan.findByNomorKontak", query = "SELECT d FROM DataPelanggan d WHERE d.nomorKontak = :nomorKontak")})
public class DataPelanggan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pelanggan")
    private String idPelanggan;
    @Column(name = "nama_pelanggan")
    private String namaPelanggan;
    @Column(name = "alamat")
    private String alamat;
    @Column(name = "nomor_kontak")
    private Integer nomorKontak;
    @JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pelanggan", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transaksi transaksi;

    public DataPelanggan() {
    }

    public DataPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Integer getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(Integer nomorKontak) {
        this.nomorKontak = nomorKontak;
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
        hash += (idPelanggan != null ? idPelanggan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataPelanggan)) {
            return false;
        }
        DataPelanggan other = (DataPelanggan) object;
        if ((this.idPelanggan == null && other.idPelanggan != null) || (this.idPelanggan != null && !this.idPelanggan.equals(other.idPelanggan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pws.praktikum1.DataPelanggan[ idPelanggan=" + idPelanggan + " ]";
    }
    
}
