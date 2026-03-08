package plantas;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Casilla;
/**
 * Clase WallNut
 * Planta defensiva que no dispara, pero resiste muchos golpes.
 * Puede colocarse en una casilla, recibir daño y bloquear zombies.
 */
public class WallNut {
	private double x, y, escala;
	private Image img;
	private double bordeIzq, bordeDer, bordeSup,bordeInf;
    private int vida;	// Vida de la planta (aguanta más golpes que la RoseBlade)
    private boolean colocada=false; //Indica si ya fue colocada en una casilla del tablero
    private Casilla casillaActual; //Referencia a la casilla donde está ubicada
	
	public WallNut (double x, double y) {
		this.x=x;
		this.y=y;
		this.escala=0.115;
		img=Herramientas.cargarImagen("imagenes/wallNavideña.PNG");
		// Vida inicial más alta (es defensiva)
        this.vida = 150;
        this.colocada = false;
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
	
	//Chequea si el mouse está dentro de los bordes de la planta
	public static boolean wallSeleccionada (WallNut w, Entorno entorno) {
		double cursorX = entorno.mouseX();
		double cursorY = entorno.mouseY();
		return cursorX > w.getBordeIzq() && cursorX < w.getBordeDer() && cursorY >w.getBordeSup()
				&& cursorY < w.getBordeInf();
	}
	
	// Actualiza la posición de la planta para que siga al mouse mientras se arrastra
	public static void arrastrarWall(WallNut w, Entorno entorno) {
		w.x = entorno.mouseX();
		w.y = entorno.mouseY();

	}
	
	// Dibuja la planta en pantalla en su posición actual
	public void dibujarWallNut (Entorno entorno) {
		entorno.dibujarImagen(img, this.x,this.y, 0, this.escala);
	}
	
	// Bordes de la planta, usados para detectar colisiones y selección
	public double getBordeIzq() {
		this.bordeIzq=x-img.getWidth(null)/2*this.escala;
		return bordeIzq;
	}

	// Bordes de la planta, usados para detectar colisiones y selección
	public double getBordeDer() {
		this.bordeDer=x+img.getWidth(null)/2*this.escala;
		return bordeDer;
	}

	public double getBordeSup() {
		this.bordeSup=y-img.getHeight(null)/2*this.escala;

		return bordeSup;
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

	public double getBordeInf() {
		this.bordeInf=y+img.getHeight(null)/2*this.escala;
		return bordeInf;
	}

	// --- Casilla y colocación ---
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
