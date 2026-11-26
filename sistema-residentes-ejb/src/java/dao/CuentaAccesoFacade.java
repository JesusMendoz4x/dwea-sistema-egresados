package dao;

import modelo.CuentaAcceso;
import javax.ejb.Stateless;
import javax.persistence.*;

@Stateless
public class CuentaAccesoFacade extends AbstractFacade<CuentaAcceso> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    public CuentaAccesoFacade() {
        super(CuentaAcceso.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
