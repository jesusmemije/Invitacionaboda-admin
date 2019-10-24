package com.example.pice.invitacionabodaadmin.CLASES;

public class Globals {
    private static final Globals ourInstance = new Globals();

    public static Globals getInstance() {
        return ourInstance;
    }
    public int id = 1;
    public int idInvitado = 1;
    public  String usuarioCorreo = "";
    public  String apodo = "";
    public  String nombre = "";
    public  String apePaterno = "";
    public  String apeMaterno = "";
    public  int  mesa = 0;
    public String b[];

    public  boolean cltNumMesa;

    private Globals() {

    }
}
