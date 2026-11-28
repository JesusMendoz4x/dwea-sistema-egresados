package dao;
import java.util.List;
import modelo.Evento;
import javax.ejb.Stateless;
import javax.persistence.*;
@Stateless public class EventoFacade extends AbstractFacade<Evento> {
 @PersistenceContext(unitName="sistema-residentes-ejbPU") private EntityManager em;
 public EventoFacade(){super(Evento.class);}
 public List<Evento> consultarProximos() {
     return em.createQuery(
             "SELECT e FROM Evento e WHERE e.fecha >= CURRENT_TIMESTAMP ORDER BY e.fecha, e.idEvento", Evento.class)
             .getResultList();
 }
 @Override protected EntityManager getEntityManager(){return em;}
}
