package hud;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Color;
import java.awt.Image;
/**
 * Clase Interfaz (HUD)
 * Muestra en pantalla información del juego:
 * Imagen de fondo de la interfaz
 * Contador de zombies eliminados
 * Contador de zombies restantes
 */
public class Interfaz {
	private int zombiesEliminados;
	private int zombiesTotales;
	private Image image; 		// imagen de fondo de la interfaz (HUD)
	private double x;			// posición X de la interfaz en pantalla
	private double y;			// posición Y de la interfaz en pantalla
	private double escala;
	
	public Interfaz(){
		this.zombiesEliminados = 0;
		this.zombiesTotales = 0;
        this.x = 400;
        this.y = 53;
        this.escala = 0.55;
        this.image = Herramientas.cargarImagen("imagenes/interfaz.png");

	}
	
	// Incrementa el contador de zombies eliminados
	public void actualizarZombiesEliminados(int n) {
		this.zombiesEliminados = n;
	}
	
	public void actualizarZombiesTotales(int n) {
		this.zombiesTotales = n;
	}

	// Dibuja la interfaz en pantalla
	public void dibujar(Entorno entorno, int tiempo) {
	    entorno.dibujarImagen(image, this.x, this.y, 0, this.escala);
	    entorno.cambiarFont("Arial", 18, Color.WHITE);

	    entorno.escribirTexto("Eliminados: " + this.zombiesEliminados, 350, 27);
	    int restantes = this.zombiesTotales - this.zombiesEliminados;
	    entorno.escribirTexto("Restantes: " + restantes, 350, 65);

	    entorno.escribirTexto("Tiempo: " + tiempo + " seg", 350, 99);
	}
	
	// Devuelve la cantidad de zombies eliminados
	public int getZombiesEliminados() {
		return zombiesEliminados;
	}
}