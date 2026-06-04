package com.cypherstealth;

public class Launcher {

    public static void main(String[] args) {
        // Llamamos al main de tu interfaz gráfica desde una clase
        // que NO hereda de Application. ¡Esto evade la restricción!
        MainGUI.main(args);
    }
}