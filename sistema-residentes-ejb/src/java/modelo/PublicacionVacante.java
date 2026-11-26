/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jesusmn
 */
@Entity
@Table(name = "publicacionVacante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PublicacionVacante.findAll", query = "SELECT p FROM PublicacionVacante p"),
    @NamedQuery(name = "PublicacionVacante.findByIdPublicacionVacante", query = "SELECT p FROM PublicacionVacante p WHERE p.idPublicacionVacante = :idPublicacionVacante"),
    @NamedQuery(name = "PublicacionVacante.findByFechaPublicacion", query = "SELECT p FROM PublicacionVacante p WHERE p.fechaPublicacion = :fechaPublicacion")})
public class PublicacionVacante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPublicacionVacante")
    private Integer idPublicacionVacante;
    @Column(name = "fechaPublicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;
    @JoinColumn(name = "idVacante", referencedColumnName = "idVacante")
    @ManyToOne(optional = false)
    private Vacante idVacante;

    public PublicacionVacante() {
    }

    public PublicacionVacante(Integer idPublicacionVacante) {
        this.idPublicacionVacante = idPublicacionVacante;
    }

    public Integer getIdPublicacionVacante() {
        return idPublicacionVacante;
    }

    public void setIdPublicacionVacante(Integer idPublicacionVacante) {
        this.idPublicacionVacante = idPublicacionVacante;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Vacante getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(Vacante idVacante) {
        this.idVacante = idVacante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPublicacionVacante != null ? idPublicacionVacante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PublicacionVacante)) {
            return false;
        }
        PublicacionVacante other = (PublicacionVacante) object;
        if ((this.idPublicacionVacante == null && other.idPublicacionVacante != null) || (this.idPublicacionVacante != null && !this.idPublicacionVacante.equals(other.idPublicacionVacante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.PublicacionVacante[ idPublicacionVacante=" + idPublicacionVacante + " ]";
    }
    
}
