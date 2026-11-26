/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jesusmn
 */
@Entity
@Table(name = "Vacante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vacante.findAll", query = "SELECT v FROM Vacante v"),
    @NamedQuery(name = "Vacante.findByIdVacante", query = "SELECT v FROM Vacante v WHERE v.idVacante = :idVacante"),
    @NamedQuery(name = "Vacante.findByPuesto", query = "SELECT v FROM Vacante v WHERE v.puesto = :puesto"),
    @NamedQuery(name = "Vacante.findByRequisitos", query = "SELECT v FROM Vacante v WHERE v.requisitos = :requisitos"),
    @NamedQuery(name = "Vacante.findByDescripcion", query = "SELECT v FROM Vacante v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "Vacante.findByUbicacion", query = "SELECT v FROM Vacante v WHERE v.ubicacion = :ubicacion"),
    @NamedQuery(name = "Vacante.findByVigencia", query = "SELECT v FROM Vacante v WHERE v.vigencia = :vigencia")})
public class Vacante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idVacante")
    private Integer idVacante;
    @Size(max = 255)
    @Column(name = "puesto")
    private String puesto;
    @Size(max = 255)
    @Column(name = "requisitos")
    private String requisitos;
    @Size(max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 255)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "vigencia")
    @Temporal(TemporalType.DATE)
    private Date vigencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVacante")
    private Collection<PublicacionVacante> publicacionVacanteCollection;
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Vacante() {
    }

    public Vacante(Integer idVacante) {
        this.idVacante = idVacante;
    }

    public Integer getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(Integer idVacante) {
        this.idVacante = idVacante;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    @XmlTransient
    public Collection<PublicacionVacante> getPublicacionVacanteCollection() {
        return publicacionVacanteCollection;
    }

    public void setPublicacionVacanteCollection(Collection<PublicacionVacante> publicacionVacanteCollection) {
        this.publicacionVacanteCollection = publicacionVacanteCollection;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVacante != null ? idVacante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vacante)) {
            return false;
        }
        Vacante other = (Vacante) object;
        if ((this.idVacante == null && other.idVacante != null) || (this.idVacante != null && !this.idVacante.equals(other.idVacante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Vacante[ idVacante=" + idVacante + " ]";
    }
    
}
