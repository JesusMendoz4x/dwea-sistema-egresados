package logneg;
import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
@Stateless
@LocalBean
public class MUsuario {
 @EJB private MCatalogo catalogos;
 @EJB private MAcceso acceso;
 @EJB private CuentaAccesoFacade cuentas;
    @EJB private UsuarioFacade dao;
    public List<Usuario> consultar() { return dao.findAll(); }
    public void guardar(Usuario dato) { guardar(dato,null); }
    public void guardar(Usuario dato,String clave) {
        if(clave!=null&&!clave.isEmpty()&&clave.length()<10)throw new IllegalArgumentException("La contraseña debe tener al menos 10 caracteres.");
        if(dato.getIdUsuario()!=null){Usuario anterior=dao.find(dato.getIdUsuario());
        if(anterior!=null&&"Administrador".equals(anterior.getRol())&&"Activo".equals(anterior.getEstado())&&(!"Administrador".equals(dato.getRol())||!"Activo".equals(dato.getEstado()))){int otros=0;for(Usuario u:dao.findAll())if(!Objects.equals(u.getIdUsuario(),dato.getIdUsuario())&&"Administrador".equals(u.getRol())&&"Activo".equals(u.getEstado()))otros++;if(otros==0)throw new IllegalArgumentException("Debe existir al menos un administrador activo.");}}
        for(Usuario u:dao.findAll())if(u.getCorreo().equalsIgnoreCase(dato.getCorreo())&&!Objects.equals(u.getIdUsuario(),dato.getIdUsuario()))throw new IllegalArgumentException("El correo ya está registrado.");

 catalogos.validar("estadosUsuario",dato.getEstado());
 catalogos.validar("roles",dato.getRol());
        if (dato.getNombre() == null || dato.getNombre().trim().isEmpty()) throw new IllegalArgumentException("Nombre es obligatorio.");
        if (dato.getCorreo() == null || dato.getCorreo().trim().isEmpty()) throw new IllegalArgumentException("Correo es obligatorio.");
        if (dato.getRol() == null || dato.getRol().trim().isEmpty()) throw new IllegalArgumentException("Rol es obligatorio.");
        if (dato.getIdUsuario() == null) dao.create(dato);
        else { if (dao.find(dato.getIdUsuario()) == null) throw new IllegalArgumentException("El registro ya no existe."); dao.edit(dato); }
        if(clave!=null&&!clave.isEmpty())acceso.guardarClave(dato,clave);
    }
 public void eliminar(Integer id){
  Usuario u=id==null?null:dao.find(id);if(u==null)throw new IllegalArgumentException("Usuario inexistente.");
  if("Administrador".equals(u.getRol())){int otros=0;for(Usuario otro:dao.findAll())if(!Objects.equals(otro.getIdUsuario(),id)&&"Administrador".equals(otro.getRol())&&"Activo".equals(otro.getEstado()))otros++;if(otros==0)throw new IllegalArgumentException("No puedes eliminar el último administrador activo.");}
  if(!u.getEgresadoCollection().isEmpty()||!u.getAdministradorCollection().isEmpty()||!u.getVacanteCollection().isEmpty()||!u.getMensajeCollection().isEmpty()||!u.getNotificacionCollection().isEmpty()||!u.getConversacionCollection().isEmpty()||!u.getConversacionCollection1().isEmpty())throw new IllegalArgumentException("El usuario tiene registros relacionados; reasígnalos o elimínalos antes de borrar el usuario.");
  for(CuentaAcceso c:cuentas.findAll())if(c.getUsuario().getIdUsuario().equals(id))cuentas.remove(c);
  dao.remove(u);
 }
}
