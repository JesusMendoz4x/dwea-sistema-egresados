package dao;
import modelo.Evento;
import javax.ejb.Stateless;
import javax.persistence.*;
@Stateless public class EventoFacade extends AbstractFacade<Evento> {
 @PersistenceContext(unitName="sistema-residentes-ejbPU") private EntityManager em;
 public EventoFacade(){super(Evento.class);}
 @Override protected EntityManager getEntityManager(){return em;}
}
