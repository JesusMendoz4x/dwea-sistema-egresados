package CDI;
import logneg.MVacante;
import modelo.*;
import java.util.*;
import java.io.Serializable;
import java.text.Normalizer;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
@Named("aVacante")
@ViewScoped
public class AVacante implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final List<String> PUESTOS_BASE = Arrays.asList(
            "Auxiliar administrativo", "Analista de datos", "Auxiliar contable",
            "Desarrollador de software", "Soporte técnico", "Diseñador gráfico",
            "Ejecutivo de ventas", "Supervisor de producción");
    private static final List<String> REQUISITOS_BASE = Arrays.asList(
            "Sin requisitos específicos", "Licenciatura concluida en un área afín",
            "Experiencia mínima de un año en el puesto", "Manejo de Excel",
            "Disponibilidad de horario", "Inglés intermedio");
    private static final List<String> UBICACIONES_OAXACA = Arrays.asList(
            "Oaxaca de Juárez", "Santa Cruz Xoxocotlán", "Santa Lucía del Camino",
            "Santa María Atzompa", "Villa de Zaachila", "Tlacolula de Matamoros",
            "Ocotlán de Morelos", "Zimatlán de Álvarez", "San Pablo Etla",
            "Heroica Ciudad de Huajuapan de León", "Heroica Ciudad de Tlaxiaco",
            "San Juan Bautista Tuxtepec", "Loma Bonita", "Juchitán de Zaragoza",
            "Salina Cruz", "Santo Domingo Tehuantepec", "Ciudad Ixtepec",
            "Puerto Escondido", "Santa María Huatulco", "San Pedro Pochutla",
            "Santiago Pinotepa Nacional", "Huautla de Jiménez", "Miahuatlán de Porfirio Díaz",
            "Ixtlán de Juárez", "Santiago Juxtlahuaca", "Matías Romero Avendaño");

    @EJB private MVacante servicio;
    private Vacante actual = new Vacante();
    private List<Vacante> registros = Collections.emptyList();
    private Integer idEmpresa;
    private Integer idUsuario;
    private List<Empresa> opcionesEmpresa = Collections.emptyList();
    private List<Usuario> opcionesUsuario = Collections.emptyList();
    private List<String> puestos = Collections.emptyList();
    private void error(Exception ex) {
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.WARNING, "Operación fallida", ex);
        Throwable causa = ex;
        while (causa.getCause() != null) causa = causa.getCause();
        String mensaje = causa instanceof IllegalArgumentException ? causa.getMessage() : "No se pudo completar la operación. Revisa los datos y la conexión a la base de datos.";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }
    @PostConstruct public void cargar() {
        try {
            registros = isPublicacion() ? Collections.<Vacante>emptyList() : servicio.consultar();
            if (!isConsulta()) {
                opcionesEmpresa = servicio.consultarEmpresa();
                opcionesUsuario = servicio.consultarUsuario();
                puestos = servicio.sugerirPuestos();
            }
        } catch (Exception ex) { error(ex); }
    }
    public void nuevo() { actual = new Vacante();
        idEmpresa = null;
        idUsuario = null;
    }
    public boolean isConsulta() { return vistaActual("consultar"); }
    public boolean isPublicacion() { return vistaActual("publicar"); }
    public boolean isEdicion() { return vistaActual("editar"); }
    public boolean isRegistroSeleccionado() { return actual.getIdVacante() != null; }
    private boolean vistaActual(String nombre) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        return contexto != null && contexto.getViewRoot() != null
                && ("/vistas/vacantes/" + nombre + ".xhtml").equals(contexto.getViewRoot().getViewId());
    }
    public void editar(Vacante dato) {
        if (!isEdicion()) { error(new IllegalArgumentException("La edición solo está disponible en Editar vacantes.")); return; }
        actual = new Vacante();
        actual.setIdVacante(dato.getIdVacante());
        actual.setPuesto(dato.getPuesto());
        actual.setRequisitos(dato.getRequisitos());
        actual.setDescripcion(dato.getDescripcion());
        actual.setUbicacion(dato.getUbicacion());
        actual.setVigencia(dato.getVigencia());
        actual.setIdEmpresa(dato.getIdEmpresa());
        actual.setIdUsuario(dato.getIdUsuario());

        idEmpresa = dato.getIdEmpresa() == null ? null : dato.getIdEmpresa().getIdEmpresa();
        idUsuario = dato.getIdUsuario() == null ? null : dato.getIdUsuario().getIdUsuario();
    }
    public void guardar() {
        try {
            if (!((isPublicacion() && !isRegistroSeleccionado()) || (isEdicion() && isRegistroSeleccionado()))) {
                throw new IllegalArgumentException("Publica las vacantes desde Publicar vacante o selecciona un registro en Editar vacantes.");
            }
            servicio.guardar(actual, idEmpresa, idUsuario); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro guardado."));
        } catch (Exception ex) { error(ex); }
    }
    public Date getVigenciaMinima() {
        return Date.from(MVacante.primeraVigenciaPermitida().atStartOfDay(MVacante.ZONA_VIGENCIA).toInstant());
    }

    public TimeZone getZonaVigencia() {
        return TimeZone.getTimeZone(MVacante.ZONA_VIGENCIA);
    }

    public void validarVigencia(FacesContext contexto, UIComponent componente, Object valor) {
        if (valor instanceof Date && !MVacante.esVigenciaValida((Date) valor)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    MVacante.MENSAJE_VIGENCIA, null));
        }
    }

    public String formatearVigencia(Date vigencia) {
        return vigencia == null ? "Sin fecha" : new java.text.SimpleDateFormat("dd/MM/yyyy").format(vigencia);
    }

    public Vacante getActual() { return actual; }
    public List<Vacante> getRegistros() { return registros; }
    public Integer getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Integer value) { idEmpresa = value; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer value) { idUsuario = value; }
    public List<Empresa> getOpcionesEmpresa() { return opcionesEmpresa; }
    public List<String> completarPuesto(String consulta) {
        List<String> opciones = new ArrayList<>(PUESTOS_BASE);
        opciones.addAll(puestos);
        return filtrarSugerencias(opciones, consulta);
    }

    public List<String> completarRequisitos(String consulta) {
        List<String> sugerencias = filtrarSugerencias(REQUISITOS_BASE, consulta);
        if (consulta != null) {
            String texto = consulta.trim();
            if (!texto.isEmpty() && texto.length() <= 255
                    && sugerencias.stream().noneMatch(opcion -> normalizar(opcion).equals(normalizar(texto)))) {
                sugerencias.add(texto);
            }
        }
        return sugerencias;
    }

    public List<String> getRequisitosSeleccionados() {
        String requisitos = actual.getRequisitos();
        if (requisitos == null || requisitos.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(requisitos.trim().split("\\s*,\\s*"));
    }

    public void setRequisitosSeleccionados(List<String> requisitos) {
        actual.setRequisitos(requisitos == null || requisitos.isEmpty() ? null : String.join(", ", requisitos));
    }

    public List<String> completarUbicacion(String consulta) {
        return filtrarSugerencias(UBICACIONES_OAXACA, consulta);
    }

    private List<String> filtrarSugerencias(List<String> opciones, String consulta) {
        String texto = consulta == null ? "" : normalizar(consulta.trim());
        Map<String, String> resultados = new LinkedHashMap<>();
        for (String opcion : opciones) {
            String clave = normalizar(opcion.trim());
            if (!clave.isEmpty() && clave.contains(texto)) {
                resultados.putIfAbsent(clave, opcion.trim());
                if (resultados.size() == 10) {
                    break;
                }
            }
        }
        return new ArrayList<>(resultados.values());
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT);
    }

    public List<Usuario> getOpcionesUsuario() { return opcionesUsuario; }
    public void eliminar(Vacante dato) {
        try {
            if (!isEdicion()) throw new IllegalArgumentException("La eliminación solo está disponible en Editar vacantes.");
            servicio.eliminar(dato.getIdVacante()); nuevo(); cargar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro eliminado."));
        } catch (Exception ex) { error(ex); }
    }
}
