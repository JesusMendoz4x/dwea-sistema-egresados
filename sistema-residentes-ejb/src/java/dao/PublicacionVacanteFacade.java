/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.PublicacionVacante;

/**
 *
 * @author jesusmn
 */
@Stateless
public class PublicacionVacanteFacade extends AbstractFacade<PublicacionVacante> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicacionVacanteFacade() {
        super(PublicacionVacante.class);
    }
    
}
