package modelo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
@Entity
@Table(name="CuentaAcceso")
public class CuentaAcceso implements Serializable {
 private static final long serialVersionUID=1L;
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer idCuentaAcceso;
 @NotNull @OneToOne(optional=false) @JoinColumn(name="idUsuario",nullable=false,unique=true) private Usuario usuario;
 @NotNull @Column(nullable=false,length=255) private String hashClave;
 public Integer getIdCuentaAcceso(){return idCuentaAcceso;}
 public void setIdCuentaAcceso(Integer value){idCuentaAcceso=value;}
 public Usuario getUsuario(){return usuario;}
 public void setUsuario(Usuario value){usuario=value;}
 public String getHashClave(){return hashClave;}
 public void setHashClave(String value){hashClave=value;}
}
