package org.gonzalomonzon.bean;

public class Usuarios {
    private int codigoUsuario;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String idUsuario;
    private String contrasenia;

    public Usuarios() {
    }

    public Usuarios(int codigoUsuario, String nombresUsuario, String apellidosUsuario, String idUsuario, String contrasenia) {
        this.codigoUsuario = codigoUsuario;
        this.nombresUsuario = nombresUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.idUsuario = idUsuario;
        this.contrasenia = contrasenia;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosUsuario(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    } 
}
