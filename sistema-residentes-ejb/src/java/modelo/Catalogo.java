package modelo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
@Entity
@Table(name="Catalogo", uniqueConstraints=@UniqueConstraint(columnNames={"categoria","valor"}))
public class Catalogo implements Serializable {
 private static final long serialVersionUID=1L;
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer idCatalogo;
 @NotNull @Column(nullable=false,length=60) private String categoria;
 @NotNull @Column(nullable=false,length=255) private String valor;
 public Integer getIdCatalogo(){return idCatalogo;}
 public void setIdCatalogo(Integer value){idCatalogo=value;}
 public String getCategoria(){return categoria;}
 public void setCategoria(String value){categoria=value;}
 public String getValor(){return valor;}
 public void setValor(String value){valor=value;}
}
