package juego;

import entorno.Entorno;

/**
 * Clase Casilla
 * Representa una celda del tablero donde puede colocarse una planta.
 * Cada casilla conoce su posición en la grilla (fila y columna),
 * su tamaño en píxeles, y su posición dentro de la ventana.
 */
public class Casilla {
    // Posición dentro del tablero
    private int fila;
    private int columna;
 
    // Dimensiones de la casilla en píxeles
    private double ancho;
    private double alto;

    // Coordenadas del centro en pantalla
    private double x;
    private double y;

    // Márgenes del tablero
    private double margenSuperior;  // espacio reservado para el HUD
    private double margenIzquierdo; // espacio para la imagen de fondo

    // Devuelve true si la casilla no tiene planta
    private boolean ocupada;

    // Constructor de Casilla
    public Casilla(int fila, int columna, double ancho, double alto, double margenSuperior, double margenIzquierdo) {
        this.fila = fila;
        this.columna = columna;
        this.ancho = ancho;
        this.alto = alto;
        this.margenSuperior = margenSuperior;
        this.margenIzquierdo = margenIzquierdo;

        // Calcula las coordenadas del centro de la casilla
        this.x = margenIzquierdo + columna * ancho + ancho / 2.0;
        this.y = margenSuperior + fila * alto + alto / 2.0;

        this.ocupada = false;
    }
    // Devuelve true si el mouse está dentro de la casilla
    public boolean estaDentro(Entorno entorno) {
        return contienePunto(entorno.mouseX(), entorno.mouseY());
    }

    // Estado de ocupación
    public boolean estaLibre() {
    	return !ocupada; 
    	}
    public void ocupar() {
    	this.ocupada = true; 
    	}
    public void liberar() {
    	this.ocupada = false; 
    	}
    
    //la primer columna está reservada para los regalos, por lo tanto no es jugable
    public boolean esJugable() {
        return columna > 0;
    }

    // Getters básicos
    public int getFila() {
    	return fila; 
    	}
    public int getColumna() {
    	return columna; 
    	}
    public double getX() {
    	return x; 
    	}
    public double getY() {
    	return y; 
    	}
    public double getAncho() {
    	return ancho; 
    	}
    public double getAlto() {
    	return alto; 
    	}
    public double getMargenSuperior() {
    	return margenSuperior; 
    	}
    public double getMargenIzquierdo() {
    	return margenIzquierdo; 
    	}

    // Punto dentro de la casilla (rectángulo centrado)
    public boolean contienePunto(double mouseX, double mouseY) {
        double mitadAncho = ancho / 2.0;
        double mitadAlto  = alto / 2.0;
        return mouseX >= x - mitadAncho && mouseX <= x + mitadAncho
            && mouseY >= y - mitadAlto  && mouseY <= y + mitadAlto;
    }
    
}