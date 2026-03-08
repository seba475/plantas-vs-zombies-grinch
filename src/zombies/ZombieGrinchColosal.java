package zombies;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Tablero;
import plantas.RoseBlade;
import plantas.WallNut; 
import proyectiles.Proyectil;
/**
 * Clase ZombieGrinchColosal
 * Representa al jefe final del juego: un zombie gigante con mucha vida.
 * Avanza desde la derecha, ignora filas (colisiona por eje X) y requiere
 * muchos impactos para ser derrotado.
 */
public class ZombieGrinchColosal {
	// Fila y posición del zombie en el tablero
    private int fila;
    private double x, y;
    
    // Velocidad de movimiento y vida del zombie
    private double velocidad; // pixeles por tick
    private int vida;         // impactos necesarios para morir

    // Imagen del zombie
    private Image imgZombie;
    
    private double escala; //permite variar tamaño de zombies

    public ZombieGrinchColosal(int fila, double xInicio, double velocidad, int vida, double escala, Tablero tablero) {
        this.fila = fila;
        this.x = xInicio;
        // Se ubica centrado en la fila correspondiente
        this.y = tablero.centroDeFila(fila);
        this.velocidad = velocidad;
        this.vida = vida;
        this.escala = escala;

        // Carga la imagen del zombie
        this.imgZombie = Herramientas.cargarImagen("imagenes/grinchColosal.png");
    }

    	// Avanza hacia la izquierda
    public void avanzar() {
        this.x -= this.velocidad;
    }
    
    // Dibuja el zombie en pantalla
    public void dibujar(Entorno e) {
        if (this.imgZombie != null) {
            // El último parámetro es la escala
        		e.dibujarImagen(this.imgZombie, this.x, this.y, 0, this.escala);
        } 
    }
    // Devuelve true si el zombie sigue con vida
    public boolean estaVivo() {
        return this.vida > 0; 
    }
    // Resta vida al zombie
    public void recibirDaño(int d) { 
        this.vida -= d; 
    }
    
    //Bordes del zombie
    public double getBordeIzq() {
        return this.x - (imgZombie.getWidth(null) * this.escala) / 2.0;
    }

    public double getBordeDer() {
        return this.x + (imgZombie.getWidth(null) * this.escala) / 2.0;
    }

    public double getBordeSup() {
        return this.y - (imgZombie.getHeight(null) * this.escala) / 2.0;
    }

    public double getBordeInf() {
        return this.y + (imgZombie.getHeight(null) * this.escala) / 2.0;
    }

    // Devuelve true si el zombie llegó a la primera columna (condición de derrota)
    public boolean llegoALaPrimeraColumna(Tablero t) {
        int[] fc = t.coordenadasAFilaColumna((int) this.x, (int) this.y);
        int col = fc[1];
        return col == 0;
    }
    
 // Detecta colisión con una RoseBlade (ignora la fila, mira solo el eje X)
    public boolean colisionaConPlanta(RoseBlade p) {
        if (p == null) return false;
        return !( this.getBordeDer() < p.getBordeIzq() || this.getBordeIzq() > p.getBordeDer() );
    }

    // Detecta colisión con un WallNut (ignora la fila, mira solo el eje X)
    public boolean colisionaConPlanta(WallNut w) {
        if (w == null) return false;
        return !( this.getBordeDer() < w.getBordeIzq() || this.getBordeIzq() > w.getBordeDer() );
    }

    
    public boolean colisionaConProyectil(Proyectil p) {
        return !(p.getBordeDer() < this.getBordeIzq() ||
                 p.getBordeIzq() > this.getBordeDer() ||
                 p.getBordeInf() < this.getBordeSup() ||
                 p.getBordeSup() > this.getBordeInf());
    }
    // Getters básicos
    public int getFila() {
        return fila; 
    }
    public double getX() {
        return x; 
    }
    public double getY() {
        return y; 
    }
}