package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MNotificacion {
    @EJB private NotificacionFacade dao;
    @EJB private UsuarioFacade daoUsuario;
    public List<Notificacion> consultar() { return dao.findAll(); }
    public List<Notificacion> consultarDestinatario(Integer idUsuario) {
        return dao.consultarDestinatario(idUsuario);
    }
    public List<Usuario> consultarUsuario() { return daoUsuario.findAll(); }
    public void guardar(Notificacion dato, Integer idUsuario) {
        if (dato.getTitulo() == null || dato.getTitulo().trim().isEmpty()) throw new IllegalArgumentException("Título es obligatorio.");
        if (dato.getCuerpo() == null || dato.getCuerpo().trim().isEmpty()) throw new IllegalArgumentException("Contenido es obligatorio.");
        Usuario refidUsuario = idUsuario == null ? null : daoUsuario.find(idUsuario);
        if (refidUsuario == null) throw new IllegalArgumentException("Selecciona un registro existente para destinatario.");
        dato.setIdUsuario(refidUsuario);
        dato.setTipo("Aviso");
        if (dato.getIdNotificacion() == null) dato.setFechaEmision(new Date());
        if (dato.getIdNotificacion() == null) dao.create(dato);
        else { if (dao.find(dato.getIdNotificacion()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
    public void eliminar(Integer id) {
        Notificacion existente = id == null ? null : dao.find(id);
        if (existente == null) throw new IllegalArgumentException("El registro ya no existe.");
        dao.remove(existente);
    }
}
