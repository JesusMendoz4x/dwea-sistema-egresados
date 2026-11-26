package dao;
import modelo.Catalogo;
import javax.ejb.Stateless;
import javax.persistence.*;
@Stateless public class CatalogoFacade extends AbstractFacade<Catalogo> {
 @PersistenceContext(unitName="sistema-residentes-ejbPU") private EntityManager em;
 public CatalogoFacade(){super(Catalogo.class);}
 @Override protected EntityManager getEntityManager(){return em;}
}
