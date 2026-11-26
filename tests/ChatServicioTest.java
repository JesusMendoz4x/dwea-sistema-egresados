import dao.ConversacionFacade;
import dao.MensajeFacade;
import dao.UsuarioFacade;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import logneg.MMensaje;
import modelo.Conversacion;
import modelo.Mensaje;
import modelo.Usuario;

/** Prueba aislada: no utiliza Payara ni escribe en la base de datos. */
public class ChatServicioTest {
    static int comprobaciones;
    static void verificar(boolean valor, String motivo) {
        comprobaciones++;
        if (!valor) throw new AssertionError(motivo);
    }
    static void rechazar(Runnable accion) {
        comprobaciones++;
        try { accion.run(); } catch (IllegalArgumentException esperado) { return; }
        throw new AssertionError("La operación debía rechazarse");
    }
    static void inyectar(Object objeto, String campo, Object valor) throws Exception {
        Field f = objeto.getClass().getDeclaredField(campo);
        f.setAccessible(true); f.set(objeto, valor);
    }
    static Usuario usuario(int id) {
        Usuario u = new Usuario(); u.setIdUsuario(id); return u;
    }
    static Conversacion conversacion(int id, Usuario uno, Usuario dos, String estado) {
        Conversacion c = new Conversacion(); c.setIdConversacion(id);
        c.setIdUsuario1(uno); c.setIdUsuario2(dos); c.setEstadoConversacion(estado); return c;
    }
    static class Conversaciones extends ConversacionFacade {
        final List<Conversacion> datos;
        int cambios;
        Conversaciones(Conversacion... datos) { this.datos = Arrays.asList(datos); }
        @Override public Conversacion find(Object id) {
            for (Conversacion c : datos) if (c.getIdConversacion().equals(id)) return c;
            return null;
        }
        @Override public List<Conversacion> consultarPorUsuario(Integer id) {
            List<Conversacion> resultado = new ArrayList<>();
            for (Conversacion c : datos) if (id.equals(c.getIdUsuario1().getIdUsuario())
                    || id.equals(c.getIdUsuario2().getIdUsuario())) resultado.add(c);
            return resultado;
        }
        @Override public void edit(Conversacion c) { cambios++; }
    }
    static class Mensajes extends MensajeFacade {
        final List<Mensaje> enviados = new ArrayList<>();
        int lecturas;
        @Override public List<Mensaje> consultarPorConversacion(Integer id) { lecturas++; return enviados; }
        @Override public void create(Mensaje m) { enviados.add(m); }
    }
    static class Usuarios extends UsuarioFacade {
        @Override public Usuario find(Object id) { return usuario((Integer) id); }
    }
    public static void main(String[] args) throws Exception {
        Usuario admin = usuario(1), egresado = usuario(2), externo = usuario(3);
        Conversacion abierta = conversacion(10, admin, egresado, "Abierta");
        Conversacion ajena = conversacion(20, egresado, externo, "Abierta");
        Conversacion cerrada = conversacion(30, admin, egresado, "Cerrada");
        Conversaciones conversaciones = new Conversaciones(abierta, ajena, cerrada);
        Mensajes mensajes = new Mensajes();
        MMensaje servicio = new MMensaje();
        inyectar(servicio, "dao", mensajes);
        inyectar(servicio, "daoConversacion", conversaciones);
        inyectar(servicio, "daoUsuario", new Usuarios());

        rechazar(() -> servicio.consultarConversacionesUsuario(null));
        verificar(servicio.consultarConversacionesUsuario(1).size() == 2, "Solo conversaciones del usuario");
        rechazar(() -> servicio.consultarMensajesConversacion(20, 1));
        rechazar(() -> servicio.consultarMensajesConversacion(999, 1));
        rechazar(() -> servicio.consultarMensajesConversacion(10, null));
        verificar(mensajes.lecturas == 0, "No consultar historial sin permiso");
        servicio.consultarMensajesConversacion(10, 1);
        verificar(mensajes.lecturas == 1, "Consultar historial permitido");
        rechazar(() -> servicio.enviarMensaje("Mensaje", 20, 1));
        rechazar(() -> servicio.enviarMensaje("Mensaje", 30, 1));
        rechazar(() -> servicio.enviarMensaje("   ", 10, 1));
        rechazar(() -> servicio.enviarMensaje(null, 10, 1));
        rechazar(() -> servicio.enviarMensaje(new String(new char[1001]).replace('\0', 'x'), 10, 1));
        verificar(mensajes.enviados.isEmpty() && conversaciones.cambios == 0, "Rechazos sin escrituras");
        servicio.enviarMensaje("  Hola\n¿Cómo estás?  ", 10, 1);
        verificar(mensajes.enviados.size() == 1, "Crear un solo mensaje");
        Mensaje enviado = mensajes.enviados.get(0);
        verificar("Hola\n¿Cómo estás?".equals(enviado.getContenido()), "Conservar saltos y quitar espacios exteriores");
        verificar(enviado.getIdUsuario().getIdUsuario().equals(1), "Remitente correcto");
        verificar(enviado.getIdConversacion().getIdConversacion().equals(10), "Conversación correcta");
        verificar(enviado.getFechaHoraEnvio() != null && !enviado.getLeido(), "Fecha y estado inicial reales");
        verificar(conversaciones.cambios == 1 && abierta.getFechaUltimActividad() != null, "Actualizar actividad");
        System.out.println("Chat: " + comprobaciones + " comprobaciones correctas, sin base de datos.");
    }
}
