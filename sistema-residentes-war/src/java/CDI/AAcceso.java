package CDI;
import java.io.Serializable;import javax.ejb.EJB;import javax.inject.Named;import javax.enterprise.context.SessionScoped;import javax.faces.context.FacesContext;import javax.faces.application.FacesMessage;import modelo.Usuario;import logneg.MAcceso;
@Named("aAcceso") @SessionScoped public class AAcceso implements Serializable {
 private static final long serialVersionUID=1L;@EJB private MAcceso servicio;
 private String correo,clave;private Usuario usuario;
 public String entrar(){try{Usuario u=servicio.autenticar(correo,clave);clave=null;if(u!=null&&("Administrador".equals(u.getRol())||"Vinculación".equals(u.getRol()))){FacesContext.getCurrentInstance().getExternalContext().invalidateSession();usuario=u;FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioPanel",u);return "/vistas/dashboard?faces-redirect=true";}}catch(Exception ex){java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING,"Error de acceso",ex);}FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Datos incorrectos o usuario sin acceso al panel.",null));return null;}
 public String salir(){FacesContext.getCurrentInstance().getExternalContext().invalidateSession();return "/login?faces-redirect=true";}
 public String getCorreo(){return correo;}public void setCorreo(String v){correo=v;}
 public String getClave(){return clave;}public void setClave(String v){clave=v;}
 public Usuario getUsuario(){return (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioPanel");}
}
