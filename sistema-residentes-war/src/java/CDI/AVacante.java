package CDI;
import logneg.MVacante;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
@Named("aVacante")
@ViewScoped
public class AVacante implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MVacante servicio;
    private Vacante actual = new Vacante();
    private List<Vacante> registros = Collections.emptyList();
    private Integer idEmpresa;
    private Integer idUsuario;
    private List<Empresa> opcionesEmpresa = Collections.emptyList();
    private List<Usuario> opcionesUsuario = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        try {
            registros = isPublicacion() ? Collections.<Vacante>emptyList() : servicio.consultar();
            if (!isConsulta()) {
                opcionesEmpresa = servicio.consultarEmpresa();
                opcionesUsuario = servicio.consultarUsuario();
            }
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() { actual = new Vacante();
        idEmpresa = null;
        idUsuario = null;
    }
    public boolean isConsulta() { return vistaActual("consultar"); }
    public boolean isPublicacion() { return vistaActual("publicar"); }
    public boolean isEdicion() { return vistaActual("editar"); }
    public boolean isRegistroSeleccionado() { return actual.getIdVacante() != null; }
    private boolean vistaActual(String nombre) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        return contexto != null && contexto.getViewRoot() != null
                && ("/vistas/vacantes/" + nombre + ".xhtml").equals(contexto.getViewRoot().getViewId());
    }
    public void editar(Vacante dato) {
        if (!isEdicion()) { error(new IllegalArgumentException("La edición solo está disponible en Editar vacantes.")); return; }
        actual = new Vacante();
        actual.setIdVacante(dato.getIdVacante());
        actual.setPuesto(dato.getPuesto());
        actual.setRequisitos(dato.getRequisitos());
        actual.setDescripcion(dato.getDescripcion());
        actual.setUbicacion(dato.getUbicacion());
        actual.setVigencia(dato.getVigencia());
        actual.setIdEmpresa(dato.getIdEmpresa());
        actual.setIdUsuario(dato.getIdUsuario());

        idEmpresa = dato.getIdEmpresa() == null ? null : dato.getIdEmpresa().getIdEmpresa();
        idUsuario = dato.getIdUsuario() == null ? null : dato.getIdUsuario().getIdUsuario();
    }
    public void guardar() {
        try {
            if (!((isPublicacion() && !isRegistroSeleccionado()) || (isEdicion() && isRegistroSeleccionado()))) {
                throw new IllegalArgumentException("Publica las vacantes desde Publicar vacante o selecciona un registro en Editar vacantes.");
            }
            servicio.guardar(actual, idEmpresa, idUsuario); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public String formatearVigencia(Date vigencia) {
        return vigencia == null ? "Sin fecha" : new java.text.SimpleDateFormat("dd/MM/yyyy").format(vigencia);
    }

    public Vacante getActual() { return actual; }
    public List<Vacante> getRegistros() { return registros; }
    public Integer getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Integer value) { idEmpresa = value; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer value) { idUsuario = value; }
    public List<Empresa> getOpcionesEmpresa() { return opcionesEmpresa; }
    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
    public void eliminar(Vacante dato) {
        try {
            if (!isEdicion()) throw new IllegalArgumentException("La eliminación solo está disponible en Editar vacantes.");
            servicio.eliminar(dato.getIdVacante()); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro eliminado."));
        } catch (Exception ex) { error(ex); }
    }
}
