package CDI;
import logneg.MMensaje;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aMensaje")
@ViewScoped
public class AMensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MMensaje servicio;
    private Mensaje actual = new Mensaje();
    private List<Mensaje> registros = Collections.emptyList();
    private Integer idConversacion;
    private Integer idUsuario;
    private List<Conversacion> opcionesConversacion = Collections.emptyList();
    private List<Usuario> opcionesUsuario = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false)) return;

        try { registros = servicio.consultar();
            opcionesConversacion = servicio.consultarConversacion();
            opcionesUsuario = servicio.consultarUsuario();
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() {
        if (!AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false)) return;
 actual = new Mensaje();
        idConversacion = null;
        idUsuario = null;
    }
    public void editar(Mensaje dato) {
        if (!AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false)) return;
 actual = new Mensaje();
        actual.setIdMensaje(dato.getIdMensaje());
        actual.setContenido(dato.getContenido());
        actual.setFechaHoraEnvio(dato.getFechaHoraEnvio());
        actual.setLeido(dato.getLeido());
        actual.setIdConversacion(dato.getIdConversacion());
        actual.setIdUsuario(dato.getIdUsuario());

        idConversacion = dato.getIdConversacion() == null ? null : dato.getIdConversacion().getIdConversacion();
        idUsuario = dato.getIdUsuario() == null ? null : dato.getIdUsuario().getIdUsuario();
    }
    public void guardar() {
        if (!AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false)) return;

        try { servicio.guardar(actual, idConversacion, idUsuario); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Mensaje getActual() { return actual; }
    public List<Mensaje> getRegistros() { return registros; }
    public Integer getIdConversacion() { return idConversacion; }
    public void setIdConversacion(Integer value) { idConversacion = value; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer value) { idUsuario = value; }
    public List<Conversacion> getOpcionesConversacion() { return opcionesConversacion; }
    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
    public void eliminar(Mensaje dato) {
        if (!AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false)) return;

        try { servicio.eliminar(dato.getIdMensaje()); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro eliminado."));
        } catch (Exception ex) { error(ex); }
    }
}
