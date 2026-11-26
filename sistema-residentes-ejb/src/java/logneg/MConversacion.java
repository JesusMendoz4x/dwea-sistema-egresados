package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MConversacion {
    @EJB private ConversacionFacade dao;
    @EJB private UsuarioFacade daoUsuario;
    public List<Conversacion> consultar() { return dao.findAll(); }
    public List<Usuario> consultarUsuario() { return daoUsuario.findAll(); }
    public void guardar(Conversacion dato, Integer idUsuario1, Integer idUsuario2) {
        if (dato.getAsunto() == null || dato.getAsunto().trim().isEmpty()) throw new IllegalArgumentException("Asunto es obligatorio.");
        Usuario refidUsuario1 = idUsuario1 == null ? null : daoUsuario.find(idUsuario1);
        if (refidUsuario1 == null) throw new IllegalArgumentException("Selecciona un registro existente para participante 1.");
        dato.setIdUsuario1(refidUsuario1);
        Usuario refidUsuario2 = idUsuario2 == null ? null : daoUsuario.find(idUsuario2);
        if (refidUsuario2 == null) throw new IllegalArgumentException("Selecciona un registro existente para participante 2.");
        dato.setIdUsuario2(refidUsuario2);
        if (idUsuario1.equals(idUsuario2)) throw new IllegalArgumentException("Selecciona dos participantes diferentes.");
        if (dato.getIdConversacion() == null) { dato.setFechaCreacion(new Date()); dato.setEstadoConversacion("Abierta"); }
        dato.setFechaUltimActividad(new Date());
        if (dato.getIdConversacion() == null) dao.create(dato);
        else { if (dao.find(dato.getIdConversacion()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
    }
 public void eliminar(Integer id){Conversacion existente=id==null?null:dao.find(id);if(existente==null)throw new IllegalArgumentException("Registro inexistente.");if(!existente.getMensajeCollection().isEmpty())throw new IllegalArgumentException("La conversación tiene mensajes; elimina primero sus mensajes.");dao.remove(existente);}
}
