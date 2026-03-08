package juego;
import entorno.Entorno;
import java.awt.Image;
import entorno.Herramientas;
import regalos.Regalo;
/**
 * Clase Tablero
 * Representa el conjunto completo de casillas donde se desarrolla el juego.
 * Se encarga de crear la grilla, manejar márgenes (HUD y franja izquierda)
 * y dibujar el área jugable en pantalla.
 */

public class Tablero {
    // Atributos principales
    private int filas;
    private int columnas;
    private Casilla[][] celdas;

    // Constantes de ventana
    private double anchoVentana = 800;
    private double altoVentana = 600;

    // Márgenes del área jugable (origen del tablero en x=margenIzquierdo, y=margenSuperior)
    private double margenSuperior;   // HUD arriba
    private double margenIzquierdo;  // franja izquierda para fondo

    // Tamaños de celda
    private double altoCelda;
    private double anchoCelda;
    
    //Texturas del pasto
    private Image pastoClaro;
    private Image pastoOscuro;
    
    private Image fondoNavidad;
    
    // Regalos en la primera columna
    private Regalo[] regalos;

    // Constructor
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;

        this.margenSuperior  = 105;
        this.margenIzquierdo = 90;

        // Calcula la altura de cada celda dividiendo el espacio vertical disponible
        // (alto total de la ventana menos el margen superior reservado para el HUD)
        // entre la cantidad de filas del tablero
        this.altoCelda  = (altoVentana - margenSuperior) / filas;
        
        // Calcula el ancho de cada celda dividiendo el espacio horizontal disponible
        // (ancho total de la ventana menos el margen izquierdo reservado para el fondo)
        // entre la cantidad de columnas del tablero
        this.anchoCelda = (anchoVentana - margenIzquierdo) / columnas;
        
        //carga las imágenes de pasto
        this.pastoClaro = Herramientas.cargarImagen("imagenes/pastoVerdeClaro.png");
        this.pastoOscuro = Herramientas.cargarImagen("imagenes/pastoVerdeOscuro.png");
        this.fondoNavidad = Herramientas.cargarImagen("imagenes/fondoNavidad.png");
        
        // Crear la matriz de casillas
        this.celdas = new Casilla[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Casilla(i, j, anchoCelda, altoCelda, margenSuperior, margenIzquierdo);
            }
        }    
    
 // Crear los regalos en la primera columna
    this.regalos = new Regalo[filas];
    for (int i = 0; i < filas; i++) {
        double x = celdas[i][0].getX();
        double y = celdas[i][0].getY();
        regalos[i] = new Regalo(x, y);
    }
}    

    // Métodos lógicos básicos del tablero

    // Verifica si una posición (fila, columna) está dentro del tablero
    private boolean dentroDeGrilla(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    // Indica si una casilla está libre
    public boolean estaLibre(int fila, int columna) {
        if (!dentroDeGrilla(fila, columna)) {
            return false; // fuera de los límites del tablero
        }
        return celdas[fila][columna].estaLibre();
    }

    // Convierte coordenadas del mouse (x, y) a una posición de la grilla (fila, columna)
    // Restamos márgenes para convertir coordenadas de pantalla a celda
    public int[] coordenadasAFilaColumna(int x, int y) {
        int columna = (int)((x - margenIzquierdo) / anchoCelda);
        int fila = (int)((y - margenSuperior) / altoCelda);

        if (!dentroDeGrilla(fila, columna)) {
            return new int[] { -1, -1 }; // click fuera del área jugable
        }
        return new int[] { fila, columna };
    }
    
    // Movimiento entre casillas en el tablero
    // Devuelve la casilla de abajo (o null si no existe)
    public Casilla casillaAbajo(Casilla casillaActual) {
    		int fila=casillaActual.getFila();
    		int columna= casillaActual.getColumna();
    		if (dentroDeGrilla(fila+1,columna)) {
    			return celdas [fila+1][columna];		
    		}
    		return null;
   	 
   }
    
    // Devuelve la casilla de arriba (o null si no existe)
    public Casilla casillaArriba(Casilla casillaActual) {
		int fila=casillaActual.getFila();
		int columna= casillaActual.getColumna();
		if (dentroDeGrilla(fila-1,columna)) {
			return celdas [fila-1][columna];		
		}
		return null; 
    }
    
    // Devuelve la casilla de la izquierda (o null si no existe)
    public Casilla casillaIzquierda(Casilla casillaActual) {
		int fila=casillaActual.getFila();
		int columna= casillaActual.getColumna();
		if (dentroDeGrilla(fila,columna-1)) {
			return celdas [fila][columna-1];		
		}
		return null;
	 }
    
    // Devuelve la casilla de la derecha (o null si no existe)
    public Casilla casillaDerecha(Casilla casillaActual) {
		int fila=casillaActual.getFila();
		int columna= casillaActual.getColumna();
		if (dentroDeGrilla(fila,columna+1)) {
			return celdas [fila][columna+1];		
		}
		return null;
	 }
    
    // Dibujo del tablero con texturas y regalos
    public void dibujar(Entorno e) {
        // --- Fondo navideño en el margen izquierdo ---
        double xFondo = margenIzquierdo / 2.0;   // centro horizontal del margen (0–90 px)
        double yFondo = altoVentana / 2.0;       // centro vertical de la ventana
        double escala = altoVentana / fondoNavidad.getHeight(null);
        e.dibujarImagen(fondoNavidad, xFondo, yFondo, 0, escala);

        // Dibuja las casillas con texturas
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Coordenadas del centro de la casilla
                double x = celdas[i][j].getX();
                double y = celdas[i][j].getY();

                // Elegir textura según la paridad de fila+columna
                Image textura = ((i + j) % 2 == 0) ? pastoClaro : pastoOscuro;

                // Escala para que la textura cubra exactamente la celda
                double escala2 = altoCelda / textura.getHeight(null);

                // Dibujar la textura en la posición de la casilla
                e.dibujarImagen(textura, x, y, 0, escala2);  
            }
        }
        
     // Dibuja los regalos (solo si no fueron robados)
        for (int i = 0; i < regalos.length; i++) {
            Regalo r = regalos[i];
            if (!r.estaRobado()) {
                r.dibujar(e);
            }
        }
    }

    // Devuelve el regalo de la fila indicada
    public Regalo getRegaloEnFila(int fila) {
        return regalos[fila];
    }
    
    // Devuelve la casilla correspondiente a coordenadas de pantalla (x,y)
    public Casilla obtenerCasilla(int x, int y) {
    	int[] coordenadas = this.coordenadasAFilaColumna(x, y);
    	int fila = coordenadas[0];
    	int columna = coordenadas[1];
    	if (fila == -1 || columna == -1) {
    		return null;
    	}
    	return celdas[fila][columna];
    }
    
    
    // Devuelve la coordenada Y del centro de una fila
    public double centroDeFila(int fila) {
        return margenSuperior + fila * altoCelda + altoCelda / 2;
    }

    // Getters
    public double getMargenSuperior() {
    	return margenSuperior; 
    	}
    public double getMargenIzquierdo() {
    	return margenIzquierdo; 
    	}
    public double getAltoCelda() {
    	return altoCelda; 
    	}
    public double getAnchoCelda() {
    	return anchoCelda; 
    	}
    public int getFilas() {
    	return filas; 
    	}
    public int getColumnas() {
    	return columnas; 
    	}
}
