package hud;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
/**
 * Clase Menu
 * Representa un menú de pantalla con una imagen de fondo,
 * un mensaje y un botón central para iniciar o continuar el juego.
 */
public class Menu {
	private double x;
	private double y;
	private Image image;
	private Image borde;
	private String mensaje;
	private Rectangle boton;
	private double escala;
	
	public Menu(String imagen, double escala, String mensaje) {
		this.x=400;
		this.y=300;
		this.image= Herramientas.cargarImagen(imagen);
		this.borde=Herramientas.cargarImagen("imagenes/lucesMarco.gif");
		this.escala=escala;
		this.mensaje= mensaje;
		this.boton = new Rectangle(400 - 100, 300 - 50, 200, 100);


	}
	
	public void dibujarMenu (Entorno entorno) {
		entorno.dibujarRectangulo(0, 0, 1600, 1200, 0, Color.black); //tapa el tablero
		entorno.dibujarImagen(image, this.x, this.y, 0,this.escala);
		entorno.dibujarImagen(borde, 395, 315, 0, 1.2);
		entorno.cambiarFont("Impact", 30, Color.white,entorno.NEGRITA);
		entorno.escribirTexto(this.mensaje, 260, 270);
	}
	
	public void dibujarBoton (Entorno entorno) {
		entorno.dibujarRectangulo(400, 350, 190, 90, 0, Color.red);
		entorno.cambiarFont("Impact", 50, Color.white);
		entorno.escribirTexto("JUGAR", 334, 364);
	}
	
	public boolean presionoBoton (Entorno entorno) {
		 double ejeX = entorno.mouseX();
		    double ejeY = entorno.mouseY();

		    return ejeX > this.getBordeIzq() && ejeX < this.getBordeDer()
		        && ejeY > this.getBordeSup() && ejeY < this.getBordeInf();
		
	}
	
	public double getBordeIzq() {
	    return boton.getX(); // esquina izquierda
	}

	public double getBordeDer() {
	    return boton.getX() + boton.getWidth(); // esquina derecha
	}

	public double getBordeSup() {
	    return boton.getY(); // parte superior
	}

	public double getBordeInf() {
	    return boton.getY() + boton.getHeight(); // parte inferior
	}

	// Coordenadas del menu en pantalla
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
