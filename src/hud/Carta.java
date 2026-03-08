package hud;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
/**
 * Clase Carta
 * Representa una carta de planta en la interfaz.
 * Permite seleccionar la planta con el mouse, controla el cooldown
 * y dibuja una barra de progreso mientras no está disponible.
 */
public class Carta {
	private Image img;
	private double cooldown; 		// tiempo de espera entre usos
	private double tiempoUltimoUso; // instante en que se usó por última vez
	private double x;
	private double y;
	private double escala;
	private double bordeIzq, bordeDer, bordeSup, bordeInf;
	
	public Carta (String tipoDePlanta, String nombreImg, double cooldown, double tiempoUltimoUso, double x, double y) {
		this.img=Herramientas.cargarImagen(nombreImg);
		this.cooldown = cooldown;
		this.tiempoUltimoUso = -cooldown; //se inicializa en -cooldown para que la carta esté disponible desde el inicio
		this.x = x;
		this.y = y;
		this.escala=0.22;
	}

	// Devuelve true si el mouse está dentro de los límites de la carta
	public boolean estaSeleccionada(Entorno entorno) {
	    double ejeX = entorno.mouseX();
	    double ejeY = entorno.mouseY();

	    return ejeX > this.getBordeIzq() && ejeX < this.getBordeDer()
	        && ejeY > this.getBordeSup() && ejeY < this.getBordeInf();
	}
	
	//Indica si la carta se puede utilizar o no.
		public boolean estaDisponible(Entorno entorno) {
			double tiempoActual = entorno.tiempo();
			return tiempoActual - this.tiempoUltimoUso >= this.cooldown;
		}
	
	// Dibuja la carta y, encima, la barra de cooldown si corresponde
	public void dibujarCarta(Entorno entorno) {
		entorno.dibujarImagen(img, this.x, this.y, 0, this.escala);
		this.dibujarBarraCooldown(entorno);
	}
	
	// La barra cubre la carta y se reduce a medida que el cooldown se completa
	public void dibujarBarraCooldown(Entorno entorno) {
		Color color = new Color(50, 50, 50, 100);
		
		if (!estaDisponible(entorno)) {
			
			double tiempoActual = entorno.tiempo();
			double tiempoTranscurrido = tiempoActual - this.tiempoUltimoUso;
			
			// Fracción del cooldown que ya pasó (0: recién usada, 1: lista)
			double tiempoCompletado = tiempoTranscurrido / this.cooldown;
			
			if (tiempoCompletado > 1) {
				tiempoCompletado = 1;
			}
			
			double altoCarta = this.getBordeInf() - this.getBordeSup();
			double anchoCarta = this.getBordeDer() - this.getBordeIzq();
			
			//La barra se va achicando a medida que pasa el tiempo
			double altoProgreso = altoCarta * (1 - tiempoCompletado);
			
			entorno.dibujarRectangulo(this.x, this.y, anchoCarta, altoProgreso, 0, color);
		}
	}
	
	// Registra el instante en que se usó la carta (para reiniciar el cooldown)
	public void usarCarta(Entorno entorno) {
		this.tiempoUltimoUso = entorno.tiempo();
	}
	// Reinicia el cooldown como si la carta nunca se hubiera usado
	public void resetearCooldown() {
		this.tiempoUltimoUso=-this.cooldown;
	}
	
	// Bordes de la carta (usados para detección de selección con el mouse)
	public double getBordeIzq() {
		this.bordeIzq=x-img.getWidth(null)/2*this.escala;
		return bordeIzq;
	}

	public double getBordeDer() {
		this.bordeDer=x+img.getWidth(null)/2*this.escala;
		return bordeDer;
	}

	public double getBordeSup() {
		this.bordeSup=y-img.getHeight(null)/2*this.escala;

		return bordeSup;
	}

	public double getBordeInf() {
		this.bordeInf=y+img.getHeight(null)/2*this.escala;
		return bordeInf;
	}

	// Coordenadas de la carta en pantalla
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
}