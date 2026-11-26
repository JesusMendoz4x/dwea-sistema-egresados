# Conexión entre WAR y EJB

La aplicación se ejecuta como `sistema-residentes.ear`, que contiene el WAR, el
JAR EJB y PrimeFaces. En NetBeans se ejecuta el proyecto principal
`sistema-residentes`. El servicio EJB es local: no necesita una API HTTP ni una
interfaz remota porque ambos módulos pertenecen a la misma aplicación.

## Código escrito en Source Packages

- EJB, `src/java/modelo`: entidades JPA existentes; representan tablas.
- EJB, `src/java/dao`: Facade existentes; administran el acceso JPA.
- EJB, `src/java/logneg/MAdministrador.java`: servicio `@Stateless` que recibe cuatro
  Facade con `@EJB` y consulta sus métodos `count()`.
- EJB, `src/java/logneg/ResumenDashboard.java`: objeto de resultado con los cuatro
  totales. No es una entidad y no crea una tabla.
- WAR, `src/java/CDI/ADashboard.java`: controlador `@Named("aDashboard")`, con
  alcance de petición. Recibe `MAdministrador` con `@EJB` y carga el resumen en
  `@PostConstruct`, una vez por petición que utiliza el controlador.
- WAR, `web/vistas/dashboard.xhtml`: muestra las propiedades del resultado con
  expresiones JSF, por ejemplo `#{aDashboard.resumen.vacantes}`.

Flujo: XHTML → ADashboard → MAdministrador → Facade → EntityManager → GESTION2.

El WAR ya tenía la referencia de compilación al EJB en su configuración NetBeans.
Su manifiesto declara `Class-Path: sistema-residentes-ejb.jar` para referenciar
el JAR hermano dentro del EAR. El EJB no se duplica en `WEB-INF/lib`.

## Datos y errores

El resumen cuenta registros de Vacante, Egresado, Mensaje, Administrador y Evento,
y solicitudes de Credencial pendientes. Los formularios conectados se describen
en `vistas-conectadas.md`; el acceso y la carga inicial en
`inicializacion-y-acceso.md`. No se muestran actividades ficticias como datos reales.

Si una consulta EJB falla, el controlador registra la excepción y muestra un
aviso en lugar de presentar ceros como si la consulta hubiera funcionado.
`MAdministrador` centraliza las operaciones administrativas mediante los DAO.
`ADashboard` y `AAdministradores` lo reciben con `@EJB`; los controladores del
WAR no inyectan DAO directamente. La consulta de administradores se conserva.

## Ejecución

1. Tener disponible en Payara el recurso JDBC `GESTION2`, utilizado por la unidad
   existente `sistema-residentes-ejbPU`, con acceso a la base de datos del proyecto.
2. Compilar y ejecutar el proyecto principal `sistema-residentes` como EAR.
3. Abrir el dashboard y comparar los totales con los registros de las tablas.

No se modificó `persistence.xml` ni la estructura de tablas. Su configuración
actual solicita generación de esquema con `create`; se debe tener presente al
desplegar. La compilación no consulta ni modifica la base de datos.

`build/generated-sources` es una salida automática del procesamiento de
anotaciones, no un paquete fuente escrito manualmente. No contiene la lógica
de esta conexión y no es necesario editarlo.

## Si el login indica que aAcceso es null

Revisar el registro de Payara. Si aparece `Type logneg.MAcceso not found`, el WAR
se desplegó por separado y no puede cargar sus servicios locales. En ese caso CDI
descarta `AAcceso` y los otros controladores, aunque tengan `@Named` correcto.

Ejecutar el proyecto principal `sistema-residentes`, que empaqueta ambos módulos
en el EAR. El WAR tiene desactivado el despliegue al guardar y su target
`-pre-run-deploy` rechaza la ejecución independiente con un mensaje explicativo.
La compilación del WAR para incluirlo en el EAR sigue disponible mediante `dist-ear`.
