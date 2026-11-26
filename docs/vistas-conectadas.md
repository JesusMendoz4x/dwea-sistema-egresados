# Vistas conectadas al EJB

Todas siguen el flujo XHTML → CDI.AComponente → logneg.MComponente → dao.ComponenteFacade → JPA/GESTION2.

| Vista | Bean WAR | Servicio EJB | Operaciones |
| --- | --- | --- | --- |
| Vacantes: consultar, publicar y editar | AVacante | MVacante | Alta, consulta con filtros, edición y eliminación |
| Empresas, en Administración | AEmpresa | MEmpresa | Alta, consulta y edición |
| Gestión de egresados | AEgresado | MEgresado | Alta, consulta con filtros, edición y eliminación |
| Usuarios y roles, en Administración | AUsuario | MUsuario | Alta, consulta y edición |
| Mensajería | AChat | MMensaje | Conversaciones propias, historial cronológico y envío con usuario autenticado |
| Conversaciones, página independiente | AConversacion | MConversacion | Alta, consulta y edición |
| Avisos dirigidos | ANotificacion | MNotificacion | Alta, consulta, edición y eliminación |

## Uso

Cada página incluye únicamente el componente de su dominio. Egresados permite
seleccionar usuarios existentes, pero no administrarlos; Mensajería presenta un panel de chat con las conversaciones del usuario autenticado,
mensajes cronológicos y envío como participante de la sesión. No permite elegir
otro remitente ni crear o modificar conversaciones. Se actualiza cada diez
segundos sin procesar ni sobrescribir el texto en redacción.
Conversaciones cuenta con su propia página en Comunicación. Eventos, Avisos,
Credenciales, Usuarios y Empresas mantienen sus operaciones dentro de sus páginas.
Los controladores comprueban la ruta y el rol con `AccesoVista` antes de cargar
o modificar información. El enlace de Gestión de egresados solo se muestra al
Administrador, de acuerdo con la protección existente en `AccesoPanel`.

Las páginas de Vacantes tienen funciones separadas: Consultar solo muestra el
listado con filtros; Publicar solo ofrece el formulario de alta; Editar muestra
el listado con acciones de edición y eliminación, y abre el formulario al
seleccionar un registro. El controlador también restringe las escrituras según
la página y exige un registro seleccionado para editar. La gestión de empresas
se realiza exclusivamente desde Administración; Vacantes permite seleccionarlas.

Registrar usuarios y empresas exclusivamente desde Administración. Luego pulsar
«Actualizar registros y opciones» en el formulario que los necesite. Seleccionar
una fila con Editar para cargar sus datos. Nuevo / cancelar edición restablece
el formulario. Los filtros de las columnas buscan dentro de registros reales.

Los controladores tienen alcance de vista y conservan el formulario durante las
peticiones AJAX. Los servicios M resuelven los IDs contra los DAO y rechazan
relaciones que ya no existen. La creación de vacante y su PublicacionVacante es
una operación transaccional. Se conserva la fecha de envío de mensajes editados;
los mensajes nuevos actualizan la actividad de su conversación.

La edición utiliza una copia de los campos para que cancelar no cambie las filas
en memoria. Las eliminaciones requieren confirmación; la base de datos puede
rechazarlas si existen relaciones que impiden borrar el registro.

## Alcance del modelo actual

Evento y Credencial ya tienen entidad, DAO, servicio M, bean A y vistas persistentes.
Credenciales valida las transiciones de estado y genera un folio al emitir.
La vista de impresión permite imprimir o guardar como PDF desde el navegador. Avisos usa Notificacion para un
usuario concreto, no un feed público ni carga de imágenes. No hay campos para
modalidad, cupo o carrera de una vacante; los formularios muestran los campos
reales de las entidades. Las cuentas de acceso están separadas en CuentaAcceso. Usuario permite crear o
restablecer una contraseña de al menos diez caracteres. Se almacena un hash
PBKDF2 con sal aleatoria, no la contraseña. Solo Administrador y Vinculación
entran al panel; Usuarios y roles se limita a Administrador.

## Validación realizada

Compilación del EAR y despliegue local en Payara. Respuesta HTTP 200 del dashboard
y de las seis páginas conectadas. También se envió un formulario JSF vacío y se comprobó que rechaza los campos obligatorios sin guardar. Pruebas de servicios con DAO sustituidos:
publicación de vacante, relaciones inexistentes, eliminación, egresado duplicado,
año inválido y pertenencia del remitente a la conversación. Estas pruebas no
insertaron datos ficticios en la base de datos. No sustituyen una prueba de
escritura real con datos de prueba y transacciones de Payara.
