package CDI;
import logneg.MConversacion;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aConversacion")
@ViewScoped
public class AConversacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MConversacion servicio;
    private Conversacion actual = new Conversacion();
    private List<Conversacion> registros = Collections.emptyList();
    private Integer idUsuario1;
    private Integer idUsuario2;
    private List<Usuario> opcionesUsuario = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/conversaciones.xhtml", false)) return;

        try { registros = servicio.consultar();
            opcionesUsuario = servicio.consultarUsuario();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/comunicacion/conversaciones.xhtml", false)) return;
 actual = new Conversacion();
        idUsuario1 = null;
        idUsuario2 = null;
    }
    public void editar(Conversacion dato) {
        if (!AccesoVista.permitir("/vistas/comunicacion/conversaciones.xhtml", false)) return;
 actual = new Conversacion();
        actual.setIdConversacion(dato.getIdConversacion());
        actual.setAsunto(dato.getAsunto());
        actual.setFechaCreacion(dato.getFechaCreacion());
        actual.setFechaUltimActividad(dato.getFechaUltimActividad());
        actual.setEstadoConversacion(dato.getEstadoConversacion());
        actual.setIdUsuario1(dato.getIdUsuario1());
        actual.setIdUsuario2(dato.getIdUsuario2());

        idUsuario1 = dato.getIdUsuario1() == null ? null : dato.getIdUsuario1().getIdUsuario();
        idUsuario2 = dato.getIdUsuario2() == null ? null : dato.getIdUsuario2().getIdUsuario();
    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/conversaciones.xhtml", false)) return;

        try { servicio.guardar(actual, idUsuario1, idUsuario2); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Conversacion getActual() { return actual; }
    public List<Conversacion> getRegistros() { return registros; }
    public Integer getIdUsuario1() { return idUsuario1; }
    public void setIdUsuario1(Integer value) { idUsuario1 = value; }
    public Integer getIdUsuario2() { return idUsuario2; }
    public void setIdUsuario2(Integer value) { idUsuario2 = value; }
    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
public void eliminar(Conversacion dato){
        if (!AccesoVista.permitir("/vistas/comunicacion/conversaciones.xhtml", false)) return;
try{servicio.eliminar(dato.getIdConversacion());nuevo();cargar();FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Registro eliminado."));}catch(Exception ex){error(ex);}}
}
