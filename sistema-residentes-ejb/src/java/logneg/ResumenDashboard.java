package logneg;

import java.io.Serializable;

/** Resultado de consulta; no representa una tabla de la base de datos. */
public class ResumenDashboard implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int vacantes;
    private final int egresados;
    private final int mensajes;
    private final int administradores;
    private int eventos;
    private int credencialesPendientes;
    public int getEventos(){return eventos;}
    public int getCredencialesPendientes(){return credencialesPendientes;}
    public ResumenDashboard(int v,int e,int m,int a,int eventos,int pendientes){this(v,e,m,a);this.eventos=eventos;this.credencialesPendientes=pendientes;}

    public ResumenDashboard(int vacantes, int egresados, int mensajes, int administradores) {
        this.vacantes = vacantes;
        this.egresados = egresados;
        this.mensajes = mensajes;
        this.administradores = administradores;
    }

    public int getVacantes() { return vacantes; }
    public int getEgresados() { return egresados; }
    public int getMensajes() { return mensajes; }
    public int getAdministradores() { return administradores; }
}
