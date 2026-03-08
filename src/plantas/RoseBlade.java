package plantas;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Casilla;

/**
 * Clase RoseBlade
 * Planta que dispara proyectiles a los zombies.
 * Puede colocarse en una casilla, recibir daño y morir.
 */
public class RoseBlade {
	// Posición y escala de dibujo
	private double x, y, escala;
	private Image img;
	
	 // Bordes para detección de selección con el mouse
	private double bordeIzq, bordeDer, bordeSup,bordeInf;
	private boolean colocada=false;
	
	// Estado de la planta
    private int vida; //Cuántos golpes resiste la planta
    private int tiempoRecarga; // ticks entre disparos
    private int contadorDisparo; // contador interno para disparar
    private Casilla casillaActual; //Referencia a la casilla donde está ubicada

    // Constructor
	public RoseBlade (double x, double y) {
		this.x=x;
		this.y=y;
		this.escala=0.05;
		img=Herramientas.cargarImagen("imagenes/roseNavideña.PNG");
        this.vida = 1; // Vida inicial
        this.colocada = false;
        this.tiempoRecarga =60; // 1 segundo
        this.contadorDisparo = 0;
        this.casillaActual = null; // aún no tiene casilla asignada
	}
	
	// Resta vida cuando recibe un golpe
    public void recibirDaño(int d) {
        this.vida -= d;
    }

    // Devuelve true si todavía tiene vida
    public boolean estaViva() {
        return this.vida > 0;
    }
    
    // Chequea si el mouse está dentro de los bordes de la planta
	public static boolean roseSeleccionada (RoseBlade f, Entorno entorno) {
		double cursorX = entorno.mouseX();
		double cursorY = entorno.mouseY();
		return cursorX > f.getBordeIzq() && cursorX < f.getBordeDer() && cursorY >f.getBordeSup()
				&& cursorY < f.getBordeInf();
	}

	// Actualiza la posición de la planta para que siga al mouse mientras se arrastra
	public static void arrastrarRose(RoseBlade f, Entorno entorno) {
		f.x = entorno.mouseX();
		f.y = entorno.mouseY();
	}

	// Dibuja la planta en pantalla en su posición actual
	public void dibujarRose(Entorno entorno) {
		entorno.dibujarImagen(img, this.x, this.y, 0, this.escala);
	}
	
	// --- Métodos de disparo ---
	
	// Incrementa el contador de ticks desde el último disparo
    public void incrementarContador() {
        this.contadorDisparo++;
    }

    // Devuelve true si ya pasó el tiempo de recarga y la planta puede disparar
    public boolean puedeDisparar() {
        return this.contadorDisparo >= this.tiempoRecarga;
    }
    
    // Reinicia el contador de disparo (se llama justo después de disparar)
    public void resetearContador() {
        this.contadorDisparo = 0;
    }
	
	// Bordes de la planta, usados para detectar colisiones y selección
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

	// Getters y setters de posición
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	// --- Casilla y colocación ---
    public int getFila() {
        if (this.casillaActual != null) {
            return this.casillaActual.getFila(); // usa la fila de la casilla
        }
        return -1; // valor por defecto si aún no está colocada
    }

	public boolean estaColocada() {
		return colocada;
	}

	public void setColocada(boolean colocada) {
		this.colocada = colocada;
	}
	
	// Asigna y devuelve la casilla donde está ubicada la planta
	public void setCasilla(Casilla c) {
        this.casillaActual = c;
    }

    public Casilla getCasilla() {
        return this.casillaActual;
    }
}