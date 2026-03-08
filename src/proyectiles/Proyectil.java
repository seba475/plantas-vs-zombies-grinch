package proyectiles;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
/**
 * Clase Proyectil
 * Representa un proyectil disparado por las plantas.
 * Se mueve hacia la derecha, puede dibujarse en pantalla,
 * detectar colisiones mediante sus bordes y eliminarse
 * cuando sale de la pantalla.
 */
public class Proyectil {
    private double x, y;
    private double velocidad;    // pixeles por tick (hacia la derecha)
    private int daño;            // daño que aplica al zombie
    private double escala;
    private Image img;

    public Proyectil(double xInicial, double yInicial) {
        this.x = xInicial;
        this.y = yInicial;
        this.velocidad = 4.0;     // ajustable
        this.daño = 1;            // cada impacto quita 1 de vida
        this.escala = 0.13;
        this.img = Herramientas.cargarImagen("imagenes/proyectilFuego.png");
    }

    // Avanza hacia la derecha
    public void avanzar() {
        this.x += this.velocidad;
    }

    // Dibuja el proyectil
    public void dibujar(Entorno e) {
        if (img != null) {
            e.dibujarImagen(img, this.x, this.y, 0, this.escala);
        }
    }

    // --- Bordes del proyectil (para colisiones) ---
    public double getBordeIzq() {
        return this.x - (img.getWidth(null) * this.escala) / 2.0;
    }
    public double getBordeDer() {
        return this.x + (img.getWidth(null) * this.escala) / 2.0;
    }
    public double getBordeSup() {
        return this.y - (img.getHeight(null) * this.escala) / 2.0;
    }
    public double getBordeInf() {
        return this.y + (img.getHeight(null) * this.escala) / 2.0;
    }

    // Getters básicos
    public double getX() { return x; }
    public double getY() { return y; }
    public int getDaño() { return daño; }

    // Fuera de pantalla: útil para eliminar el proyectil
    public boolean fueraDePantalla(double anchoPantalla) {
        return this.x > anchoPantalla + 50; // margen extra para evitar cortes visibles
    }
}