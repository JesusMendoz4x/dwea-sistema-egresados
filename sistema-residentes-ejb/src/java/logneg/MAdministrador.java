/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package logneg;

import dao.AdministradorFacade;
import dao.EventoFacade;
import dao.CredencialFacade;
import modelo.Credencial;
import dao.EgresadoFacade;
import dao.MensajeFacade;
import dao.VacanteFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modelo.Administrador;

/**
 *
 * @author jesusmn
 */
@Stateless
@LocalBean
public class MAdministrador {

    @EJB
    private AdministradorFacade administradorFacade;

    @EJB
    private VacanteFacade vacanteFacade;

    @EJB
    private EgresadoFacade egresadoFacade;

    @EJB
    private MensajeFacade mensajeFacade;

    @EJB private EventoFacade eventos;
    @EJB private CredencialFacade credenciales;
    public ResumenDashboard consultarResumen() {
        int pendientes=0;for(Credencial c:credenciales.findAll())if("Pendiente".equals(c.getEstado()))pendientes++;
        return new ResumenDashboard(vacanteFacade.count(), egresadoFacade.count(),
                mensajeFacade.count(), administradorFacade.count(), eventos.count(), pendientes);
    }

    public List<Administrador> getAdministrador() {
        return (List<Administrador>) administradorFacade.findAll();
    }
}
