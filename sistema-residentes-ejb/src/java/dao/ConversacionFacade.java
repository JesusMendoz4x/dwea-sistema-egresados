/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Conversacion;

/**
 *
 * @author jesusmn
 */
@Stateless
public class ConversacionFacade extends AbstractFacade<Conversacion> {

    @PersistenceContext(unitName = "sistema-residentes-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConversacionFacade() {
        super(Conversacion.class);
    }
    
    public java.util.List<Conversacion> consultarPorUsuario(Integer usuario) {
        return em.createQuery("SELECT c FROM Conversacion c JOIN FETCH c.idUsuario1 JOIN FETCH c.idUsuario2 "
                + "WHERE c.idUsuario1.idUsuario = :usuario OR c.idUsuario2.idUsuario = :usuario "
                + "ORDER BY c.fechaUltimActividad DESC, c.idConversacion DESC", Conversacion.class)
                .setParameter("usuario", usuario).getResultList();
    }
}
