package CDI;
import logneg.MUsuario;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aUsuario")
@ViewScoped
public class AUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MUsuario servicio;
    private String clave;
    public String getClave(){return clave;}public void setClave(String valor){clave=valor;}
    private Usuario actual = new Usuario();
    private List<Usuario> registros = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/administracion/usuarios.xhtml", true)) return;

        try { registros = servicio.consultar();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/administracion/usuarios.xhtml", true)) return;
 actual = new Usuario();clave=null;
    }
    public void editar(Usuario dato) {
        if (!AccesoVista.permitir("/vistas/administracion/usuarios.xhtml", true)) return;
 actual = new Usuario();
        actual.setIdUsuario(dato.getIdUsuario());
        actual.setNombre(dato.getNombre());
        actual.setCorreo(dato.getCorreo());
        actual.setEstado(dato.getEstado());
        actual.setNumeroTarjeta(dato.getNumeroTarjeta());
        actual.setCarrera(dato.getCarrera());
        actual.setAñoEgreso(dato.getAñoEgreso());
        actual.setRol(dato.getRol());

    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/administracion/usuarios.xhtml", true)) return;

        try { servicio.guardar(actual,clave); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Usuario getActual() { return actual; }
    public List<Usuario> getRegistros() { return registros; }
 public void eliminar(Usuario dato){
        if (!AccesoVista.permitir("/vistas/administracion/usuarios.xhtml", true)) return;
try{servicio.eliminar(dato.getIdUsuario());nuevo();cargar();FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Registro eliminado."));}catch(Exception ex){error(ex);}}
}
