package dao;
import modelo.Credencial;
import javax.ejb.Stateless;
import javax.persistence.*;
@Stateless public class CredencialFacade extends AbstractFacade<Credencial> {
 @PersistenceContext(unitName="sistema-residentes-ejbPU") private EntityManager em;
 public CredencialFacade(){super(Credencial.class);}
 @Override protected EntityManager getEntityManager(){return em;}
}
