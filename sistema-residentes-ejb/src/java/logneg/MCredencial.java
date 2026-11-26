package logneg;
import dao.*;import modelo.*;import java.util.*;import javax.ejb.*;
@Stateless @LocalBean public class MCredencial {
 @EJB private CredencialFacade dao;
 @EJB private MCatalogo catalogos;
 @EJB private EgresadoFacade egresados;
 public List<Egresado> consultarEgresado(){return egresados.findAll();}
 public Credencial buscar(Integer id){return id==null?null:dao.find(id);}
 public List<Credencial> consultar(){return dao.findAll();}
 public void guardar(Credencial dato, Integer egresado){
 Egresado e=egresado==null?null:egresados.find(egresado);
 if(e==null)throw new IllegalArgumentException("Selecciona un egresado existente.");
 for(Credencial otra:dao.findAll())if(otra.getEgresado().getIdEgresado().equals(egresado)&&!Objects.equals(otra.getIdCredencial(),dato.getIdCredencial()))throw new IllegalArgumentException("El egresado ya tiene una credencial registrada.");
 dato.setEgresado(e);catalogos.validar("estadosCredencial",dato.getEstado());
 if(dato.getIdCredencial()==null){if(!"Pendiente".equals(dato.getEstado()))throw new IllegalArgumentException("La solicitud debe comenzar como Pendiente.");dato.setFechaSolicitud(new Date());}
 else {
 Credencial anterior=dao.find(dato.getIdCredencial());
 if(anterior==null)throw new IllegalArgumentException("La credencial ya no existe.");
 if(!anterior.getEgresado().getIdEgresado().equals(egresado))throw new IllegalArgumentException("No puedes cambiar el egresado de una solicitud.");
 String desde=anterior.getEstado(),hasta=dato.getEstado();
 boolean valido=desde.equals(hasta)||(("Pendiente".equals(desde)||"Requiere corrección".equals(desde))&&("Aprobada".equals(hasta)||"Requiere corrección".equals(hasta)))||("Aprobada".equals(desde)&&"Emitida".equals(hasta))||("Emitida".equals(desde)&&"Entregada".equals(hasta));
 if(!valido)throw new IllegalArgumentException("El estado debe seguir: Pendiente, Aprobada, Emitida y Entregada.");
 dato.setFechaSolicitud(anterior.getFechaSolicitud());dato.setFechaEmision(anterior.getFechaEmision());dato.setFolio(anterior.getFolio());
 if("Emitida".equals(hasta)&&dato.getFolio()==null){dato.setFolio("EG-"+UUID.randomUUID().toString());dato.setFechaEmision(new Date());}
 }
 if("Requiere corrección".equals(dato.getEstado())&&(dato.getObservaciones()==null||dato.getObservaciones().trim().isEmpty()))throw new IllegalArgumentException("Indica el motivo de corrección.");
 if(dato.getIdCredencial()==null)dao.create(dato);else{if(dao.find(dato.getIdCredencial())==null)throw new IllegalArgumentException("Registro inexistente.");dao.edit(dato);}
 }
 public void eliminar(Integer id){Credencial dato=id==null?null:dao.find(id);if(dato==null)throw new IllegalArgumentException("Registro inexistente.");if(!"Pendiente".equals(dato.getEstado()))throw new IllegalArgumentException("Solo se pueden eliminar solicitudes pendientes.");dao.remove(dato);}
}
