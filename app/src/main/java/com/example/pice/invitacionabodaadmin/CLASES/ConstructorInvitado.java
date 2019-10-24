package com.example.pice.invitacionabodaadmin.CLASES;

public class ConstructorInvitado {

    private int id;
    private String apodo;
    private String nombre;
    private String apellido;
    private String mesa;


    public ConstructorInvitado(int id, String apodo, String mesa, String nombre, String apellido) {
        this.id = id;
        this.apodo = apodo;
        this.mesa = mesa;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) { this.apodo = apodo; }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
