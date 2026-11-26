/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jesusmn
 */
@Entity
@Table(name = "Egresado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Egresado.findAll", query = "SELECT e FROM Egresado e"),
    @NamedQuery(name = "Egresado.findByIdEgresado", query = "SELECT e FROM Egresado e WHERE e.idEgresado = :idEgresado"),
    @NamedQuery(name = "Egresado.findByCarrera", query = "SELECT e FROM Egresado e WHERE e.carrera = :carrera"),
    @NamedQuery(name = "Egresado.findByA\u00f1oEgreso", query = "SELECT e FROM Egresado e WHERE e.a\u00f1oEgreso = :a\u00f1oEgreso")})
public class Egresado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEgresado")
    private Integer idEgresado;
    @Size(max = 255)
    @Column(name = "carrera")
    private String carrera;
    @Column(name = "a\u00f1oEgreso")
    private Integer añoEgreso;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Egresado() {
    }

    public Egresado(Integer idEgresado) {
        this.idEgresado = idEgresado;
    }

    public Integer getIdEgresado() {
        return idEgresado;
    }

    public void setIdEgresado(Integer idEgresado) {
        this.idEgresado = idEgresado;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getAñoEgreso() {
        return añoEgreso;
    }

    public void setAñoEgreso(Integer añoEgreso) {
        this.añoEgreso = añoEgreso;
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
        hash += (idEgresado != null ? idEgresado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Egresado)) {
            return false;
        }
        Egresado other = (Egresado) object;
        if ((this.idEgresado == null && other.idEgresado != null) || (this.idEgresado != null && !this.idEgresado.equals(other.idEgresado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Egresado[ idEgresado=" + idEgresado + " ]";
    }
    
}
