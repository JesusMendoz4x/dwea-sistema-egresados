# Datos iniciales, acceso y pantallas completas

## Fuente y flujo

El código se escribe en `sistema-residentes-ejb/src/java` y en los controladores
`CDI` del WAR. El flujo es vista → AComponente → MComponente → DAO → JPA.
Los servicios de Eventos, Credenciales, Catálogos y Acceso respetan este flujo.
Se agregaron las entidades Evento, Credencial, Catalogo y CuentaAcceso con sus DAO.
El procesamiento del metamodelo JPA se desactivó porque no se utiliza en el código.

## Inicio automático

`logneg.DatosIniciales` es un EJB `@Singleton @Startup`. Al desplegar:

- Agrega solo los valores ausentes de roles, carreras, estados de usuario,
  tipos de evento y estados de credencial.
- La primera vez prepara usuarios administrativos y de ejemplo, empresa,
  egresado, vacante y publicación, evento, conversación, mensaje, aviso y
  solicitud de credencial de ejemplo.
- Marca la carga con el catálogo interno `sistema/datos-iniciales-v1`. Los
  ejemplos no se vuelven a insertar en cada despliegue ni reaparecen después
  de borrarlos. Los catálogos sí se completan si les falta algún valor base.
- No elimina ni sobrescribe los datos de ejemplo existentes. La contraseña del
  administrador inicial se sincroniza con la clave fija en cada despliegue.

La unidad JPA existente utiliza creación de esquema y crea las tablas nuevas
al desplegar. No se agregó ninguna operación DROP ni recreación de datos.

## Acceso inicial

URL: http://localhost:8080/sistema-residentes-war/login.xhtml

Correo inicial: `admin@residentes.local`.

La contraseña fija del administrador es `2026`. Al desplegar, la cuenta inicial
se crea o se actualiza para utilizar esa contraseña, incluso si los datos de
ejemplo ya estaban cargados. No se genera ni se consulta `acceso-inicial.txt`.
Si se cambia la contraseña del administrador desde Usuarios y roles, el siguiente
despliegue vuelve a establecer `2026`. Los demás usuarios conservan sus contraseñas.
La regla de mínimo diez caracteres sigue aplicándose a las contraseñas asignadas
desde Usuarios y roles; la clave fija inicial es una excepción.

Las contraseñas se guardan como PBKDF2-HMAC-SHA256 con 120000 iteraciones y sal
aleatoria. El filtro protege `/vistas/*`, admite Administrador y Vinculación,
y restringe Administración y Gestión de egresados a Administrador. Cerrar sesión
invalida la sesión. Las peticiones AJAX con sesión vencida se redirigen al login.

## Credenciales

Cada egresado tiene una solicitud. Comienza Pendiente; puede pasar a Requiere
corrección (con motivo) o Aprobada, luego Emitida y finalmente Entregada.
Al emitir se asignan fecha y folio único. Solo se eliminan solicitudes pendientes.
La acción Imprimir abre una credencial imprimible; el navegador permite guardarla
como PDF. No se simula fotografía ni documentos que no se hayan cargado.

## Datos de ejemplo y validación

Los ejemplos están identificados en su nombre. La credencial del egresado de
ejemplo se aprobó y emitió durante la prueba funcional y quedó disponible para
probar la impresión. Un evento temporal de prueba se creó y eliminó.

Se comprobó el despliegue, login real, protección de rutas, once vistas, catálogos,
altas y eliminación de evento por JSF/AJAX, rechazo de un salto inválido de estado,
aprobación y emisión de credencial. Los ejemplos no representan personas reales.

## Pruebas reproducibles

Ejecutar `python3 tests/panel_funcional.py` desde la raíz, con Payara levantado
y los datos iniciales disponibles. Usa solo la biblioteca estándar de Python.
Usa la clave fija del administrador, crea y elimina un evento
y una cuenta temporales y, si corresponde, emite la credencial de ejemplo.
Se verifican restricciones por rol, login, logout, combos y las rutas principales.
No ejecutar contra una instancia sin los datos de ejemplo esperados.
