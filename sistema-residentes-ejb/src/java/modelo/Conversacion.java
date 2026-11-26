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
@Table(name = "Conversacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conversacion.findAll", query = "SELECT c FROM Conversacion c"),
    @NamedQuery(name = "Conversacion.findByIdConversacion", query = "SELECT c FROM Conversacion c WHERE c.idConversacion = :idConversacion"),
    @NamedQuery(name = "Conversacion.findByAsunto", query = "SELECT c FROM Conversacion c WHERE c.asunto = :asunto"),
    @NamedQuery(name = "Conversacion.findByFechaCreacion", query = "SELECT c FROM Conversacion c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Conversacion.findByFechaUltimActividad", query = "SELECT c FROM Conversacion c WHERE c.fechaUltimActividad = :fechaUltimActividad"),
    @NamedQuery(name = "Conversacion.findByEstadoConversacion", query = "SELECT c FROM Conversacion c WHERE c.estadoConversacion = :estadoConversacion")})
public class Conversacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idConversacion")
    private Integer idConversacion;
    @Size(max = 255)
    @Column(name = "asunto")
    private String asunto;
    @Column(name = "fechaCreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fechaUltimActividad")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimActividad;
    @Size(max = 255)
    @Column(name = "estadoConversacion")
    private String estadoConversacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConversacion")
    private Collection<Mensaje> mensajeCollection;
    @JoinColumn(name = "idUsuario1", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario1;
    @JoinColumn(name = "idUsuario2", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario2;

    public Conversacion() {
    }

    public Conversacion(Integer idConversacion) {
        this.idConversacion = idConversacion;
    }

    public Integer getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(Integer idConversacion) {
        this.idConversacion = idConversacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimActividad() {
        return fechaUltimActividad;
    }

    public void setFechaUltimActividad(Date fechaUltimActividad) {
        this.fechaUltimActividad = fechaUltimActividad;
    }

    public String getEstadoConversacion() {
        return estadoConversacion;
    }

    public void setEstadoConversacion(String estadoConversacion) {
        this.estadoConversacion = estadoConversacion;
    }

    @XmlTransient
    public Collection<Mensaje> getMensajeCollection() {
        return mensajeCollection;
    }

    public void setMensajeCollection(Collection<Mensaje> mensajeCollection) {
        this.mensajeCollection = mensajeCollection;
    }

    public Usuario getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(Usuario idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public Usuario getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(Usuario idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConversacion != null ? idConversacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversacion)) {
            return false;
        }
        Conversacion other = (Conversacion) object;
        if ((this.idConversacion == null && other.idConversacion != null) || (this.idConversacion != null && !this.idConversacion.equals(other.idConversacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Conversacion[ idConversacion=" + idConversacion + " ]";
    }
    
}
