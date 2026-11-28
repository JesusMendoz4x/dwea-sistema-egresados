package CDI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import logneg.MMensaje;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Usuario;

@Named("aChat")
@ViewScoped
public class AChat implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB private MMensaje servicio;
    private List<Conversacion> conversaciones = Collections.emptyList();
    private List<Mensaje> mensajes = Collections.emptyList();
    private Conversacion seleccionada;
    private String contenido;
    private String busqueda;

    private Usuario usuario() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuarioPanel");
    }
    private boolean permitido() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (contexto == null || contexto.getViewRoot() == null) {
            return false;
        }
        if ("/vistas/egresado/mensajeria.xhtml".equals(contexto.getViewRoot().getViewId())) {
            Usuario actual = usuario();
            return actual != null && "Egresado".equals(actual.getRol());
        }
        return AccesoVista.permitir("/vistas/comunicacion/mensajeria.xhtml", false);
    }
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(
                java.util.logging.Level.WARNING, "No se pudo completar la operación de chat", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        FacesContext contexto = FacesContext.getCurrentInstance();
        contexto.validationFailed();
        contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                causa instanceof IllegalArgumentException ? causa.getMessage()
                : "No se pudo actualizar el chat. Intenta nuevamente.", null));
    }
    @PostConstruct public void cargar() {
        if (!permitido()) return;
        try { recargar(); } catch (Exception ex) { error(ex); }
    }
    private void recargar() {
        Integer id = seleccionada == null ? null : seleccionada.getIdConversacion();
        conversaciones = servicio.consultarConversacionesUsuario(usuario().getIdUsuario());
        seleccionada = null;
        for (Conversacion c : conversaciones) {
            if (c.getIdConversacion().equals(id)) { seleccionada = c; break; }
        }
        if (id == null && !conversaciones.isEmpty()) seleccionada = conversaciones.get(0);
        if (id != null && seleccionada == null) {
            contenido = null;
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds()
                    .add("gestionChat:compositor");
        }
        mensajes = seleccionada == null ? Collections.<Mensaje>emptyList()
                : servicio.consultarMensajesConversacion(seleccionada.getIdConversacion(), usuario().getIdUsuario());
    }
    public void actualizar() {
        if (!permitido()) return;
        try { recargar(); } catch (Exception ex) { error(ex); }
    }
    public void seleccionar(Conversacion conversacion) {
        if (!permitido()) return;
        try {
            List<Mensaje> historial = servicio.consultarMensajesConversacion(
                    conversacion.getIdConversacion(), usuario().getIdUsuario());
            if (seleccionada == null || !seleccionada.getIdConversacion().equals(conversacion.getIdConversacion())) {
                contenido = null;
            }
            seleccionada = conversacion;
            mensajes = historial;
        } catch (Exception ex) { error(ex); }
    }
    public void enviar() {
        if (!permitido()) return;
        try {
            servicio.enviarMensaje(contenido, seleccionada == null ? null : seleccionada.getIdConversacion(),
                    usuario().getIdUsuario());
        } catch (Exception ex) { error(ex); return; }
        contenido = null;
        try { recargar(); } catch (Exception ex) { error(ex); }
    }
    public boolean esPropio(Mensaje mensaje) {
        return mensaje.getIdUsuario() != null
                && usuario().getIdUsuario().equals(mensaje.getIdUsuario().getIdUsuario());
    }
    public String destinatario(Conversacion conversacion) {
        Usuario otro = usuario().getIdUsuario().equals(conversacion.getIdUsuario1().getIdUsuario())
                ? conversacion.getIdUsuario2() : conversacion.getIdUsuario1();
        return otro.getNombre();
    }
    public String iniciales(Conversacion conversacion) {
        String nombre = destinatario(conversacion);
        if (nombre == null || nombre.trim().isEmpty()) return "?";
        String[] partes = nombre.trim().split("\\s+");
        String letras = partes[0].substring(0, 1);
        if (partes.length > 1) letras += partes[partes.length - 1].substring(0, 1);
        return letras.toUpperCase(Locale.ROOT);
    }
    public boolean estaSeleccionada(Conversacion conversacion) {
        return seleccionada != null && seleccionada.getIdConversacion().equals(conversacion.getIdConversacion());
    }
    public List<Conversacion> getConversacionesFiltradas() {
        if (busqueda == null || busqueda.trim().isEmpty()) return conversaciones;
        String texto = busqueda.trim().toLowerCase(Locale.ROOT);
        List<Conversacion> filtradas = new ArrayList<>();
        for (Conversacion c : conversaciones) {
            String asunto = c.getAsunto() == null ? "" : c.getAsunto();
            if ((destinatario(c) + " " + asunto).toLowerCase(Locale.ROOT).contains(texto)) filtradas.add(c);
        }
        return filtradas;
    }
    public boolean isPuedeEnviar() { return seleccionada != null && "Abierta".equals(seleccionada.getEstadoConversacion()); }
    public List<Conversacion> getConversaciones() { return conversaciones; }
    public List<Mensaje> getMensajes() { return mensajes; }
    public Conversacion getSeleccionada() { return seleccionada; }
    public String getContenido() { return contenido; }
    public void setContenido(String valor) { contenido = valor; }
    public String getBusqueda() { return busqueda; }
    public void setBusqueda(String valor) { busqueda = valor; }
}
