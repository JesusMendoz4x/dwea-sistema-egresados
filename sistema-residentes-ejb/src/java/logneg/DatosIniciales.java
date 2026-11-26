package logneg;

import dao.*;
import modelo.*;
import java.util.*;
import javax.ejb.*;
import javax.annotation.PostConstruct;

@Singleton
@Startup
public class DatosIniciales {

    private static final String CLAVE_ADMIN = "2026";

    @EJB
    private CatalogoFacade catalogos;
    @EJB
    private UsuarioFacade usuarios;
    @EJB
    private EmpresaFacade empresas;
    @EJB
    private EgresadoFacade egresados;
    @EJB
    private AdministradorFacade administradores;
    @EJB
    private CuentaAccesoFacade cuentas;
    @EJB
    private EventoFacade eventos;
    @EJB
    private VacanteFacade vacantes;
    @EJB
    private PublicacionVacanteFacade publicaciones;
    @EJB
    private ConversacionFacade conversaciones;
    @EJB
    private MensajeFacade mensajes;
    @EJB
    private NotificacionFacade notificaciones;
    @EJB
    private CredencialFacade credenciales;

    @PostConstruct
    public void iniciar() {
        agregar("roles", "Administrador", "Vinculación", "Egresado");
        agregar("estadosUsuario", "Activo", "Inactivo");
        agregar("carreras", "Ingeniería en Sistemas Computacionales", "Ingeniería Industrial", "Ingeniería Civil", "Licenciatura en Administración");
        agregar("tiposEvento", "Conferencia", "Taller", "Feria de empleo", "Encuentro de egresados");
        agregar("estadosCredencial", "Pendiente", "Requiere corrección", "Aprobada", "Emitida", "Entregada");
        Usuario admin = usuario("Administrador inicial", "admin@residentes.local", "Administrador");
        configurarAccesoAdmin(admin);
        for (Catalogo c : catalogos.findAll()) {
            if ("sistema".equals(c.getCategoria()) && "datos-iniciales-v1".equals(c.getValor())) {
                return;
            }
        }
        usuario("Vinculación inicial", "vinculacion@residentes.local", "Vinculación");
        Usuario egresado = usuario("Egresado de ejemplo", "egresado@example.com", "Egresado");
        boolean encontrado = false;
        for (Administrador a : administradores.findAll()) {
            if (a.getIdUsuario().getIdUsuario().equals(admin.getIdUsuario())) {
                encontrado = true;
            }
        }
        if (!encontrado && administradores.find(1001) == null) {
            Administrador a = new Administrador(1001);
            a.setIdUsuario(admin);
            administradores.create(a);
        }
        encontrado = false;
        for (Egresado e : egresados.findAll()) {
            if (e.getIdUsuario().getIdUsuario().equals(egresado.getIdUsuario())) {
                encontrado = true;
            }
        }
        if (!encontrado) {
            Egresado e = new Egresado();
            e.setIdUsuario(egresado);
            e.setCarrera("Ingeniería en Sistemas Computacionales");
            e.setAñoEgreso(2025);
            egresados.create(e);
        }
        encontrado = false;
        for (Empresa e : empresas.findAll()) {
            if ("Empresa de ejemplo".equals(e.getNombre())) {
                encontrado = true;
            }
        }
        if (!encontrado) {
            Empresa e = new Empresa();
            e.setNombre("Empresa de ejemplo");
            empresas.create(e);
        }
        completarEjemplos(admin, egresado);
        agregar("sistema", "datos-iniciales-v1");
    }

    private void configurarAccesoAdmin(Usuario admin) {
        for (CuentaAcceso cuenta : cuentas.findAll()) {
            if (cuenta.getUsuario().getIdUsuario().equals(admin.getIdUsuario())) {
                if (!MAcceso.verificar(CLAVE_ADMIN, cuenta.getHashClave())) {
                    cuenta.setHashClave(MAcceso.hash(CLAVE_ADMIN));
                    cuentas.edit(cuenta);
                }
                return;
            }
        }
        CuentaAcceso cuenta = new CuentaAcceso();
        cuenta.setUsuario(admin);
        cuenta.setHashClave(MAcceso.hash(CLAVE_ADMIN));
        cuentas.create(cuenta);
    }

    private void completarEjemplos(Usuario admin, Usuario usuarioEgresado) {
        boolean existe = false;
        for (Evento e : eventos.findAll()) {
            if ("Encuentro de egresados — ejemplo".equals(e.getTitulo())) {
                existe = true;
            }
        }
        if (!existe) {
            Evento e = new Evento();
            e.setTitulo("Encuentro de egresados — ejemplo");
            e.setTipo("Encuentro de egresados");
            Calendar fecha = Calendar.getInstance();
            fecha.add(Calendar.DAY_OF_MONTH, 30);
            e.setFecha(fecha.getTime());
            e.setSede("Auditorio — ejemplo");
            e.setDescripcion("Registro inicial de demostración. Puedes editarlo o eliminarlo.");
            eventos.create(e);
        }
        Empresa empresa = null;
        for (Empresa e : empresas.findAll()) {
            if ("Empresa de ejemplo".equals(e.getNombre())) {
                empresa = e;
            }
        }
        existe = false;
        for (Vacante v : vacantes.findAll()) {
            if ("Desarrollador Java — ejemplo".equals(v.getPuesto())) {
                existe = true;
            }
        }
        if (!existe && empresa != null) {
            Vacante v = new Vacante();
            v.setPuesto("Desarrollador Java — ejemplo");
            v.setDescripcion("Vacante inicial de demostración.");
            v.setRequisitos("Java y bases de datos");
            v.setUbicacion("Oaxaca");
            Calendar fecha = Calendar.getInstance();
            fecha.add(Calendar.DAY_OF_MONTH, 30);
            v.setVigencia(fecha.getTime());
            v.setIdUsuario(admin);
            v.setIdEmpresa(empresa);
            vacantes.create(v);
            PublicacionVacante p = new PublicacionVacante();
            p.setIdVacante(v);
            p.setFechaPublicacion(new Date());
            publicaciones.create(p);
        }
        Conversacion conversacion = null;
        for (Conversacion c : conversaciones.findAll()) {
            if ("Bienvenida — ejemplo".equals(c.getAsunto())) {
                conversacion = c;
            }
        }
        if (conversacion == null) {
            conversacion = new Conversacion();
            conversacion.setAsunto("Bienvenida — ejemplo");
            conversacion.setIdUsuario1(admin);
            conversacion.setIdUsuario2(usuarioEgresado);
            conversacion.setFechaCreacion(new Date());
            conversacion.setFechaUltimActividad(new Date());
            conversacion.setEstadoConversacion("Abierta");
            conversaciones.create(conversacion);
            Mensaje m = new Mensaje();
            m.setIdConversacion(conversacion);
            m.setIdUsuario(admin);
            m.setContenido("Mensaje de ejemplo para conocer el módulo de comunicación.");
            m.setFechaHoraEnvio(new Date());
            m.setLeido(false);
            mensajes.create(m);
        }
        existe = false;
        for (Notificacion n : notificaciones.findAll()) {
            if ("Bienvenida — ejemplo".equals(n.getTitulo()) && n.getIdUsuario().getIdUsuario().equals(usuarioEgresado.getIdUsuario())) {
                existe = true;
            }
        }
        if (!existe) {
            Notificacion n = new Notificacion();
            n.setTitulo("Bienvenida — ejemplo");
            n.setCuerpo("Aviso inicial de demostración.");
            n.setTipo("Aviso");
            n.setIdUsuario(usuarioEgresado);
            n.setFechaEmision(new Date());
            notificaciones.create(n);
        }
        Egresado egresado = null;
        for (Egresado e : egresados.findAll()) {
            if (e.getIdUsuario().getIdUsuario().equals(usuarioEgresado.getIdUsuario())) {
                egresado = e;
            }
        }
        if (egresado != null) {
            existe = false;
            for (Credencial c : credenciales.findAll()) {
                if (c.getEgresado().getIdEgresado().equals(egresado.getIdEgresado())) {
                    existe = true;
                }
            }
            if (!existe) {
                Credencial c = new Credencial();
                c.setEgresado(egresado);
                c.setEstado("Pendiente");
                c.setFechaSolicitud(new Date());
                c.setObservaciones("Solicitud de ejemplo.");
                credenciales.create(c);
            }
        }
    }

    private void agregar(String categoria, String... valores) {
        for (String valor : valores) {
            boolean existe = false;
            for (Catalogo c : catalogos.findAll()) {
                if (categoria.equals(c.getCategoria()) && valor.equals(c.getValor())) {
                    existe = true;
                }
            }
            if (!existe) {
                Catalogo c = new Catalogo();
                c.setCategoria(categoria);
                c.setValor(valor);
                catalogos.create(c);
            }
        }
    }

    private Usuario usuario(String nombre, String correo, String rol) {
        for (Usuario u : usuarios.findAll()) {
            if (correo.equalsIgnoreCase(u.getCorreo())) {
                return u;
            }
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setRol(rol);
        u.setEstado("Activo");
        usuarios.create(u);
        return u;
    }
}
