package CDI;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import logneg.MEvento;
import logneg.MNotificacion;
import modelo.Evento;
import modelo.Notificacion;
import modelo.Usuario;

@Named("aPortalComunicacion")
@RequestScoped
public class APortalComunicacion {
    @Inject
    private AAcceso acceso;
    @EJB
    private MNotificacion notificaciones;
    @EJB
    private MEvento eventos;
    private List<Notificacion> avisos;
    private List<Evento> proximosEventos;
    private boolean errorAvisos;
    private boolean errorEventos;

    public List<Notificacion> getAvisos() {
        if (avisos == null) {
            Usuario usuario = acceso.getUsuario();
            avisos = Collections.emptyList();
            if (usuario != null && "Egresado".equals(usuario.getRol())) {
                try {
                    avisos = notificaciones.consultarDestinatario(usuario.getIdUsuario());
                } catch (RuntimeException ex) {
                    errorAvisos = true;
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error al consultar avisos personales", ex);
                }
            }
        }
        return avisos;
    }

    public List<Evento> getProximosEventos() {
        if (proximosEventos == null) {
            try {
                proximosEventos = eventos.consultarProximos();
            } catch (RuntimeException ex) {
                errorEventos = true;
                proximosEventos = Collections.emptyList();
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error al consultar próximos eventos", ex);
            }
        }
        return proximosEventos;
    }

    public boolean isErrorAvisos() {
        return errorAvisos;
    }

    public boolean isErrorEventos() {
        return errorEventos;
    }

    public java.util.TimeZone getZonaHoraria() {
        return java.util.TimeZone.getDefault();
    }
}
