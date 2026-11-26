package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MMensaje {
    @EJB private MensajeFacade dao;
    @EJB private ConversacionFacade daoConversacion;
    @EJB private UsuarioFacade daoUsuario;
    public List<Mensaje> consultar() { return dao.findAll(); }
    public List<Conversacion> consultarConversacion() { return daoConversacion.findAll(); }
    public List<Usuario> consultarUsuario() { return daoUsuario.findAll(); }
    public void guardar(Mensaje dato, Integer idConversacion, Integer idUsuario) {
        if (dato.getContenido() == null || dato.getContenido().trim().isEmpty()) throw new IllegalArgumentException("Mensaje es obligatorio.");
        Conversacion refidConversacion = idConversacion == null ? null : daoConversacion.find(idConversacion);
        if (refidConversacion == null) throw new IllegalArgumentException("Selecciona un registro existente para conversación.");
        dato.setIdConversacion(refidConversacion);
        Usuario refidUsuario = idUsuario == null ? null : daoUsuario.find(idUsuario);
        if (refidUsuario == null) throw new IllegalArgumentException("Selecciona un registro existente para remitente.");
        dato.setIdUsuario(refidUsuario);
        Conversacion c = dato.getIdConversacion();
        if (!dato.getIdUsuario().equals(c.getIdUsuario1()) && !dato.getIdUsuario().equals(c.getIdUsuario2())) throw new IllegalArgumentException("El remitente debe participar en la conversación.");
        if (dato.getIdMensaje() == null) { dato.setFechaHoraEnvio(new Date()); dato.setLeido(false); }
        c.setFechaUltimActividad(new Date());
        daoConversacion.edit(c);
        if (dato.getIdMensaje() == null) dao.create(dato);
        else { if (dao.find(dato.getIdMensaje()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
    public void eliminar(Integer id) {
        Mensaje existente = id == null ? null : dao.find(id);
        if (existente == null) throw new IllegalArgumentException("El registro ya no existe.");
        dao.remove(existente);
    }
    public List<Conversacion> consultarConversacionesUsuario(Integer usuario) {
        if (usuario == null) throw new IllegalArgumentException("Inicia sesión para consultar tus conversaciones.");
        return daoConversacion.consultarPorUsuario(usuario);
    }
    private Conversacion conversacionPermitida(Integer idConversacion, Integer usuario) {
        Conversacion c = idConversacion == null ? null : daoConversacion.find(idConversacion);
        if (c == null || usuario == null || (!usuario.equals(c.getIdUsuario1().getIdUsuario())
                && !usuario.equals(c.getIdUsuario2().getIdUsuario()))) {
            throw new IllegalArgumentException("La conversación no está disponible para tu usuario.");
        }
        return c;
    }
    public List<Mensaje> consultarMensajesConversacion(Integer conversacion, Integer usuario) {
        conversacionPermitida(conversacion, usuario);
        return dao.consultarPorConversacion(conversacion);
    }
    public void enviarMensaje(String contenido, Integer conversacion, Integer usuario) {
        Conversacion c = conversacionPermitida(conversacion, usuario);
        if (!"Abierta".equals(c.getEstadoConversacion())) {
            throw new IllegalArgumentException("Esta conversación está cerrada y no admite nuevos mensajes.");
        }
        if (contenido == null || contenido.trim().isEmpty() || contenido.trim().length() > 1000) {
            throw new IllegalArgumentException("Escribe un mensaje de entre 1 y 1000 caracteres.");
        }
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(contenido.trim());
        guardar(mensaje, conversacion, usuario);
    }
}
