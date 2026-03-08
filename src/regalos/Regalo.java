package regalos;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
/**
 * Clase Regalo
 * Representa un regalo en el tablero.
 * Puede dibujarse mientras no haya sido robado.
 * Al ser robado, deja de mostrarse en pantalla.
 */
public class Regalo {
    private double x, y;       // posición en el tablero
    private boolean robado;    // estado del regalo
    private Image imagen;

    public Regalo(double x, double y) {
        this.x = x;
        this.y = y;
        this.robado = false;
        this.imagen = Herramientas.cargarImagen("imagenes/regalo.png");
    }

    // Dibuja el regalo solo si no fue robado
    public void dibujar(Entorno e) {
        if (!robado) {
            e.dibujarImagen(imagen, x, y, 0, 0.15);
        }
    }

    // Marcar como robado: deja de dibujarse
    public void robar() {
        this.robado = true;
    }

    public boolean estaRobado() {
        return robado;
    }

    // Getters de posición
    public double getX() {
    	return x; 
    	}
    
    public double getY() {
    	return y; 
    	}
}