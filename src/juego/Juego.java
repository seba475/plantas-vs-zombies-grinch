package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import hud.Carta;
import hud.Interfaz;
import hud.Menu;
import plantas.RoseBlade;
import plantas.WallNut;
import zombies.ZombieGrinch;
import zombies.ZombieGrinchRapido;
import zombies.ZombieGrinchResistente;
import zombies.ZombieGrinchColosal;
import proyectiles.Proyectil;
import regalos.Regalo;

//Clase principal del juego: maneja el entorno, los menús y la lógica general
public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	
	// Tablero de casillas y la interfaz con estadísticas
	private Tablero tablero;
	private Interfaz interfaz;
	
	// Menús del juego
	private Menu menuInicio;    
	private Menu menuGameOver;    
	private Menu menuGanador;      
	private Menu menuPrincipal;
	private String menuActual= "inicio";
	private Boolean estaJugando = false;

	// Cartas para elegir y plantar
	private Carta cartaWall;		
	private Carta cartaRose;		
	
	// Plantas en juego
	private RoseBlade[] generarRose;
	private WallNut [] generarWall;	 
	private int cantidadRose;
	private int cantidadWall;
	
	// Imágenes de los zombies
	private Image imagenZombieNormal;
	private Image imagenZombieRapido;
	private Image imagenZombieResistente;
	private Image imagenZombieColosal;
	
	// Plantas que se están arrastrando o moviendo
	private RoseBlade plantaArrastradaRose;
	private WallNut plantaArrastradaWall;
	private RoseBlade roseSeleccionada=null;	
	private WallNut wallSeleccionada=null;	
	private Casilla casillaAnterior=null;	
	
	// Zombies en juego
	private ZombieGrinch[] zombies;
	private int cantidadZombies;		

	private ZombieGrinchRapido[] zombiesRapidos;
	private int cantidadZombiesRapidos;
	private ZombieGrinchResistente[] zombiesResistentes;
	private int cantidadZombiesResistentes;
	
	// Zombie especial
	private ZombieGrinchColosal zombieColosal;
	private boolean colosalActivo;
	
	// Progreso de la partida
	private int zombiesEliminados;
	private int totalZombiesObjetivo; 
	private double tiempoInicio;
	
	// Proyectiles disparados
	private Proyectil[] proyectiles;
	private int cantidadProyectiles;
	
	// Control del tiempo de aparición de zombies
	private int contadorSpawn = 0;
	private int tiempoSpawn = 120; // cada 2 segundos
	
	// Estados finales
	private boolean derrota;
	private boolean victoria;
	
	// Constructor: prepara la ventana, el tablero, las cartas y los menús
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		this.tablero = new Tablero(5, 10); // 5 filas de juego, 10 columnas
		this.imagenZombieNormal = Herramientas.cargarImagen("imagenes/znormal.png");
		this.imagenZombieColosal = Herramientas.cargarImagen("imagenes/zcolosal.png");
		this.imagenZombieRapido = Herramientas.cargarImagen("imagenes/zmotoquero.png");
		this.imagenZombieResistente = Herramientas.cargarImagen("imagenes/zresistente.png");		
		
		//Se crean las cartas con su imagen, cooldown y posición en pantalla
		cartaRose = new Carta ("RoseBlade","imagenes/roseblade.png",2500,15,225,55);
		cartaWall = new Carta ("WallNut","imagenes/walnutCarta.png",5000,15,125,55);
		
		//Se crean los tipos de menu
		menuInicio= new Menu ("imagenes/mPrincipal.jpg",0.57,"¡Preparate para jugar!");
		menuGameOver= new Menu ("imagenes/mPerdedor.jpg",0.57,"La navidad se arruinó");
		menuGanador= new Menu ("imagenes/mGanador.jpg",0.57,"¡Salvaste la navidad!");

		// Inicia el juego!
		this.reiniciarJuego();
		this.entorno.iniciar();
	}
	
	private void reiniciarJuego() {
		
        this.tablero = new Tablero(5, 10);
        
        // Inicialización de arreglos de plantas
        cantidadRose = 0;
        cantidadWall = 0;
        generarRose = new RoseBlade[100]; 
        generarWall = new WallNut[100];
        
        // Si la partida termino cuando se estaba seleccionando un objeto este se invalida
        plantaArrastradaRose = null;
        plantaArrastradaWall = null;
        roseSeleccionada = null;
        wallSeleccionada = null;
        casillaAnterior = null;
        
        
        // Arreglos de zombies (máximo 15 simultáneos)
        this.zombies = new ZombieGrinch[15];
        this.cantidadZombies = 0;
        this.zombiesRapidos = new ZombieGrinchRapido[15];
        this.cantidadZombiesRapidos = 0;
        this.zombiesResistentes = new ZombieGrinchResistente[15];
        this.cantidadZombiesResistentes = 0;
        this.zombieColosal = null;
        this.colosalActivo = false;

        // Contadores de partida
        this.zombiesEliminados = 0;
        this.totalZombiesObjetivo = 50;
        this.contadorSpawn = 0;
        
        // inicio de proyectiles
        this.proyectiles = new Proyectil[1000];
        this.cantidadProyectiles = 0;
		
        // inicio de la interfaz
        this.interfaz = new Interfaz();
        this.interfaz.actualizarZombiesEliminados(0); 
        this.interfaz.actualizarZombiesTotales(totalZombiesObjetivo);
        this.tiempoInicio = entorno.tiempo(); // guarda el tiempo actual como punto de partida
        
        // Estados iniciales
        this.derrota = false;
        this.victoria = false; 
        estaJugando = false; 
        menuActual = "inicio";
        
        // Reinicio cooldown de plantas
        cartaRose.resetearCooldown(); 
        cartaWall.resetearCooldown();
	}
	
	// Método auxiliar: devuelve true si hay un zombie en la fila indicada
	// Si el colosal está vivo, todas las filas disparan
	private boolean hayZombieEnFila(int fila) {
	    if (zombieColosal != null && zombieColosal.estaVivo()) {
	        return true;
	    }
	    // Zombies normales
	    for (int i = 0; i < cantidadZombies; i++) {
	        ZombieGrinch z = zombies[i];
	        if (z != null && z.estaVivo() && z.getFila() == fila) return true;
	    }
	    // Rápidos
	    for (int i = 0; i < cantidadZombiesRapidos; i++) {
	        ZombieGrinchRapido zr = zombiesRapidos[i];
	        if (zr != null && zr.estaVivo() && zr.getFila() == fila) return true;
	    }
	    // Resistentes
	    for (int i = 0; i < cantidadZombiesResistentes; i++) {
	        ZombieGrinchResistente zr = zombiesResistentes[i];
	        if (zr != null && zr.estaVivo() && zr.getFila() == fila) return true;
	    }
	    return false;
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		
		// Dibuja tablero, HUD y cartas		
		this.tablero.dibujar(entorno);
		int tiempo = ((int) entorno.tiempo() - (int) tiempoInicio) / 1000;
		this.interfaz.dibujar(entorno, tiempo);
		cartaRose.dibujarCarta(entorno);
		cartaWall.dibujarCarta(entorno);
		this.entorno.dibujarImagen(imagenZombieColosal,566,55,0,0.2);
		this.entorno.dibujarImagen(imagenZombieNormal,630,55,0,0.2);
		this.entorno.dibujarImagen(imagenZombieRapido,694,55,0,0.2);
		this.entorno.dibujarImagen(imagenZombieResistente,758,55,0,0.2);
		
		// Dibuja tipo de menu según sea el momento del juego 
		if (estaJugando==false) {
			if (menuActual.equals("inicio")) {
				menuPrincipal=menuInicio;
			}
			if (menuActual.equals("victoria")) {
				menuPrincipal=menuGanador;
			}
			if(menuActual.equals("derrota")) {
				menuPrincipal=menuGameOver;			
			}
			menuPrincipal.dibujarMenu(entorno);
			menuPrincipal.dibujarBoton(entorno);
			
			// Solo si se presiona el boton inicializa el juego
			if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && menuPrincipal.presionoBoton(entorno)){
				System.out.println("Iniciando el juego...");
				this.reiniciarJuego();
				estaJugando = true;
				menuActual = "juego";

			}
		}
		// Lógica principal de juego
		if(estaJugando==true) {
		//Si se hace click izquierdo sobre una carta disponible, se genera la planta correspondiente
		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
			
			if (cantidadRose < generarRose.length 
				    && cartaRose.estaSeleccionada(entorno) // chequea si el mouse está sobre la carta
				    && cartaRose.estaDisponible(entorno)) { // chequea cooldown
				generarRose[cantidadRose] = new RoseBlade(cartaRose.getX(),cartaRose.getY());
				cantidadRose++;
				cartaRose.usarCarta(entorno); // reinicia cooldown
			}
			
			if (cantidadWall < generarWall.length 
				    && cartaWall.estaSeleccionada(entorno)
				    && cartaWall.estaDisponible(entorno)) {			
				generarWall[cantidadWall] = new WallNut(cartaWall.getX(),cartaWall.getY());
				cantidadWall++;
				cartaWall.usarCarta(entorno);
			}
		}
		// Dibujar RoseBlade y disparar si hay zombies en la fila
		for (int j = 0; j < cantidadRose; j++) {
		    RoseBlade r = generarRose[j];
		    if (r != null && r.estaViva()) {
		        r.dibujarRose(entorno);
		        if (hayZombieEnFila(r.getFila())) {
		        r.incrementarContador();
		        if (r.puedeDisparar()) {
		            proyectiles[cantidadProyectiles++] = new Proyectil(r.getX(), r.getY());
		            r.resetearContador();
		        }
		    }
		}
	}
		// Dibujar WallNut (solo defienden, no disparan)
		for (int i = 0; i < cantidadWall; i++) {
	        if (generarWall[i] != null) {
	            generarWall[i].dibujarWallNut(entorno);
	        }
	    }
		
		//Lógica de arrastre de plantas y colocarlas en las casillas
		boolean yaArrastrada=false;  //evita arrastrar más de una a la vez
		
		// --- Manejo de RoseBlade ---
		for (int a=cantidadRose-1; a >=0; a--) { //se recorre de atrás hacia adelante para priorizar la última creada en caso de solapamiento
			RoseBlade nuevaRoseBlade = generarRose[a];
			
			// Si la planta existe, no está colocada y el mouse la está arrastrando
			if (nuevaRoseBlade != null && !nuevaRoseBlade.estaColocada() 
					&& entorno.estaPresionado(entorno.BOTON_IZQUIERDO)
					&& RoseBlade.roseSeleccionada(nuevaRoseBlade, entorno)) {
				
				if (!yaArrastrada) { //Solo una planta puede seguir al mouse
					RoseBlade.arrastrarRose(nuevaRoseBlade, entorno); //actualiza X/Y al mouse
					this.plantaArrastradaRose = nuevaRoseBlade;	 //guarda referencia a la planta arrastrada
		            yaArrastrada=true;
				}		
			}
			
			// Cuando se suelta el botón izquierdo del mouse
			if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
				if (nuevaRoseBlade!=null && !nuevaRoseBlade.estaColocada()) {
					int x= entorno.mouseX();
					int y= entorno.mouseY();
					Casilla casillaFinal=tablero.obtenerCasilla(x, y);// casilla donde se soltó el mouse
					
					// Si la casilla es válida, está libre y es jugable (no columna 0)
					if (casillaFinal != null && casillaFinal.estaLibre() && casillaFinal.esJugable()) {
						this.plantaArrastradaRose.setX(casillaFinal.getX()); 
		                this.plantaArrastradaRose.setY(casillaFinal.getY());
						nuevaRoseBlade.setColocada(true);
						nuevaRoseBlade.setCasilla(casillaFinal);	// guarda referencia a la casilla donde está
						casillaFinal.ocupar();  		// marca la casilla como ocupada
						this.plantaArrastradaRose = null; // ya no se arrastra
					}
					else {
						// Si no se soltó en una casilla válida: se elimina la planta
						int eliminar=-1;
						for (int r=0;r<cantidadRose;r++) {
							if (generarRose[r]==nuevaRoseBlade) {
								eliminar=r;
								this.plantaArrastradaRose=null;
							}
						}
						// Compacta el arreglo para no dejar huecos
						if (eliminar!=-1) {
							for (int j = eliminar; j < cantidadRose - 1; j++) {
		                        generarRose[j] = generarRose[j + 1];
		                    }
		                    generarRose[cantidadRose - 1] = null;
		                    cantidadRose--;
						}
					}
				}
			}
		}
		
		// --- Manejo de WallNut ---
		for (int i = cantidadWall-1; i >=0; i--) {
			WallNut nuevaWallNut= generarWall[i];
			
			// Si la planta existe, no está colocada y el mouse la está arrastrando
			if (nuevaWallNut != null && !nuevaWallNut.estaColocada() 
					&& entorno.estaPresionado(entorno.BOTON_IZQUIERDO)
					&& WallNut.wallSeleccionada(nuevaWallNut, entorno)) {
				
				if (!yaArrastrada) { // solo una planta puede seguir al mouse
					WallNut.arrastrarWall(nuevaWallNut, entorno);
					this.plantaArrastradaWall = nuevaWallNut;
					yaArrastrada=true;
				}			
			}
			
			// Cuando se suelta el botón izquierdo del mouse
			if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
				if(nuevaWallNut!=null && !nuevaWallNut.estaColocada()) {
					int x= entorno.mouseX();
					int y= entorno.mouseY();
					Casilla casillaFinal=tablero.obtenerCasilla(x, y);
					
					// Si la casilla es válida, está libre y es jugable (no columna 0)
					if (casillaFinal != null && casillaFinal.estaLibre() && casillaFinal.esJugable()) {
						this.plantaArrastradaWall.setX(casillaFinal.getX());
						this.plantaArrastradaWall.setY(casillaFinal.getY());
						nuevaWallNut.setColocada(true);
						nuevaWallNut.setCasilla(casillaFinal);
						casillaFinal.ocupar();
						this.plantaArrastradaWall = null;
						}
					else {
						//Si no se soltó en una casilla válida: se elimina la planta
						int eliminar=-1;
						for (int r=0;r<cantidadWall;r++) {
							if (generarWall[r]==nuevaWallNut) {
								eliminar=r;
								this.plantaArrastradaWall=null;
							}
						}
						// Compacta el arreglo
						if (eliminar!=-1) {
							for (int j = eliminar; j < cantidadWall - 1; j++) {
		                        generarWall[j] = generarWall[j + 1];
		                    }
		                    generarWall[cantidadWall - 1] = null;
		                    cantidadWall--;
						}
					}				
				}
			}		
		}
		//---Movimiento de plantas con teclas---
		
		boolean plantaSeleccionada=false;

		// Click izquierdo: seleccionar una planta ya colocada
		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
			
			// --- Selección de RoseBlade ---
			for (int i=cantidadRose-1; i>=0 && !plantaSeleccionada; i--) {
					RoseBlade r =generarRose[i];
					
					//Si la planta existe, ya está colocada y el mouse hace click sobre ella
					if (r!=null && r.estaColocada() && RoseBlade.roseSeleccionada(r, entorno)) {
						roseSeleccionada = r; // guarda referencia a la RoseBlade seleccionada
						wallSeleccionada=null;	// se deselecciona cualquier WallNut
						casillaAnterior = r.getCasilla(); // guarda la casilla actual de la planta
						plantaSeleccionada=true;	// marca que ya se seleccionó una planta
					}
			}
			
			// --- Selección de WallNut ---
			for (int j=cantidadWall-1; j>=0 && !plantaSeleccionada; j--) {
					WallNut w= generarWall[j];
					if (w!=null && w.estaColocada() && WallNut.wallSeleccionada(w, entorno)) {
						wallSeleccionada = w; // guarda referencia a la WallNut seleccionada
						roseSeleccionada=null;	// se deselecciona cualquier RoseBlade
						casillaAnterior = w.getCasilla(); // guarda la casilla actual de la planta
						plantaSeleccionada=true;
					}
			}	
		}	
		
		// --- Movimiento con WASD ---
		
		//Si hay una planta seleccionada y sabemos su casilla actual,
		//se calcula la nueva casilla según la tecla presionada
		
		if (casillaAnterior!=null && (roseSeleccionada!=null || wallSeleccionada!=null)) {

			Casilla nuevaCasilla= null;

			 //Según la tecla, se pide al tablero la casilla vecina
			if(entorno.sePresiono('s')) {
				nuevaCasilla= tablero.casillaAbajo(casillaAnterior);		
			}
			if(entorno.sePresiono('w')) {
				nuevaCasilla= tablero.casillaArriba(casillaAnterior);
			}
			if(entorno.sePresiono('a')) {
				nuevaCasilla= tablero.casillaIzquierda(casillaAnterior);
			}
			if(entorno.sePresiono('d')) {
				nuevaCasilla= tablero.casillaDerecha(casillaAnterior);
			}
			
			// Solo se mueve si la casilla existe, está libre y no es la columna de regalos
			if (nuevaCasilla != null && nuevaCasilla.estaLibre() && nuevaCasilla.esJugable()) { //se verifica que la casilla este libre
				casillaAnterior.liberar(); //se libera la casilla anterior
				if (roseSeleccionada!=null) {
				    roseSeleccionada.setX(nuevaCasilla.getX());
				    roseSeleccionada.setY(nuevaCasilla.getY());
				    roseSeleccionada.setCasilla(nuevaCasilla);	
				}
				else if (wallSeleccionada!=null) {
					wallSeleccionada.setX(nuevaCasilla.getX());
					wallSeleccionada.setY(nuevaCasilla.getY());
					wallSeleccionada.setCasilla(nuevaCasilla);
				}
				
				nuevaCasilla.ocupar(); // marca la nueva casilla como ocupada
				casillaAnterior=nuevaCasilla; // actualiza referencia
			}
			
			// Click derecho: deselecciona la planta
			if (entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {
			    roseSeleccionada = null;
			    wallSeleccionada = null;
			    casillaAnterior = null;
			}					
		}
	
		// --- Spawn automático de zombies ---
		contadorSpawn++; // se incrementa una vez por tick

		// Total de zombies en pantalla (normales + rápidos + resistentes + 1 si el colosal está activo)
		int totalEnPantalla = cantidadZombies + cantidadZombiesRapidos + cantidadZombiesResistentes + (colosalActivo ? 1 : 0);

		// Condiciones para que aparezca un nuevo zombie
		if (estaJugando
		    && !derrota
		    && !victoria
		    && contadorSpawn >= tiempoSpawn
		    && totalEnPantalla < 15
		    && (zombiesEliminados + totalEnPantalla) < totalZombiesObjetivo) {

		    int fila = (int)(Math.random() * 5);
		    int faltantes = totalZombiesObjetivo - (zombiesEliminados + totalEnPantalla);

		    if (faltantes == 1 && !colosalActivo) {
		        // último enemigo: colosal
		        zombieColosal = new ZombieGrinchColosal(2, 800.0, 0.5, 100, 0.83, tablero);
		        colosalActivo = true;
		    } else {
		    		// Según probabilidad, aparece un tipo de zombie
		        double r = Math.random();
		        if (r < 0.4) {
		            if (cantidadZombies < zombies.length) {
		                zombies[cantidadZombies++] = new ZombieGrinch(fila, 800.0, 1, 3, 0.18, tablero);
		            }
		        } else if (r < 0.7) {
		            if (cantidadZombiesRapidos < zombiesRapidos.length) {
		                zombiesRapidos[cantidadZombiesRapidos++] = new ZombieGrinchRapido(fila, 800.0, 1.5, 2, 0.11, tablero);
		            }
		        } else {
		            if (cantidadZombiesResistentes < zombiesResistentes.length) {
		                zombiesResistentes[cantidadZombiesResistentes++] = new ZombieGrinchResistente(fila, 800.0, 0.7, 6, 0.18, tablero);
		            }
		        }
		    }
		    contadorSpawn = 0; // se resetea acá
		}
		// --- Lógica de proyectiles ---
		for (int i = 0; i < cantidadProyectiles; i++) {
		    Proyectil p = proyectiles[i];
		    if (p != null) {
		        p.avanzar();
		        p.dibujar(entorno);

		        // --- Colisión con zombies normales ---
		        for (int j = 0; j < cantidadZombies; j++) {
		            ZombieGrinch z = zombies[j];
		            if (p != null && z != null && z.estaVivo() && z.colisionaConProyectil(p)) {
		                z.recibirDaño(p.getDaño());
		                proyectiles[i] = null;

		                // Si el zombie muere, se elimina y se compacta el arreglo
		                if (!z.estaVivo()) {
		                    for (int k = j; k < cantidadZombies - 1; k++) {
		                        zombies[k] = zombies[k + 1];
		                    }
		                    zombies[cantidadZombies - 1] = null;
		                    cantidadZombies--;
		                    zombiesEliminados++;
		                    this.interfaz.actualizarZombiesEliminados(this.zombiesEliminados);
		                    j--; // ajusta índice tras compactar
		                }
		            }
		        }

		        // --- Colisión con zombies rápidos ---
		        for (int j = 0; j < cantidadZombiesRapidos; j++) {
		            ZombieGrinchRapido zr = zombiesRapidos[j];
		            if (p != null && zr != null && zr.estaVivo() && zr.colisionaConProyectil(p)) {
		                zr.recibirDaño(p.getDaño());
		                proyectiles[i] = null;

		                if (!zr.estaVivo()) {
		                    for (int k = j; k < cantidadZombiesRapidos - 1; k++) {
		                        zombiesRapidos[k] = zombiesRapidos[k + 1];
		                    }
		                    zombiesRapidos[cantidadZombiesRapidos - 1] = null;
		                    cantidadZombiesRapidos--;
		                    zombiesEliminados++;
		                    this.interfaz.actualizarZombiesEliminados(this.zombiesEliminados);
		                    j--;
		                }
		            }
		        }
		    
		        // --- Colisión con zombies resistentes ---
		        for (int j = 0; j < cantidadZombiesResistentes; j++) {
		            ZombieGrinchResistente zr = zombiesResistentes[j];
		            if (p != null && zr != null && zr.estaVivo() && zr.colisionaConProyectil(p)) {
		                zr.recibirDaño(p.getDaño());
		                proyectiles[i] = null;

		                if (!zr.estaVivo()) {
		                    for (int k = j; k < cantidadZombiesResistentes - 1; k++) {
		                        zombiesResistentes[k] = zombiesResistentes[k + 1];
		                    }
		                    zombiesResistentes[cantidadZombiesResistentes - 1] = null;
		                    cantidadZombiesResistentes--;
		                    zombiesEliminados++;
		                    this.interfaz.actualizarZombiesEliminados(this.zombiesEliminados);
		                    j--;
		                }
		            }
		        }

		        // --- Colisión con zombie colosal ---
		        if (colosalActivo && zombieColosal != null && zombieColosal.estaVivo() && p != null) {
		            if (zombieColosal.colisionaConProyectil(p)) {
		                zombieColosal.recibirDaño(p.getDaño());
		                proyectiles[i] = null;

		                if (!zombieColosal.estaVivo()) {
		                    zombieColosal = null;
		                    colosalActivo = false;
		                    zombiesEliminados++;
		                    this.interfaz.actualizarZombiesEliminados(this.zombiesEliminados);
		                }
		            }
		        }
		    }

		    // Si el proyectil salió de la pantalla, se elimina
		    if (p != null && p.fueraDePantalla(entorno.ancho())) {
		        proyectiles[i] = null;
		    }
		}

		// --- Lógica de zombies normales ---
		for (int i = 0; i < cantidadZombies; i++) {
		    ZombieGrinch z = zombies[i];
		    if (z != null && z.estaVivo()) {
		        // Colisiones con RoseBlade y WallNut
		        for (int j = 0; j < cantidadRose; j++) {
		            RoseBlade r = generarRose[j];
		            if (r != null && r.estaViva() && z.colisionaConPlanta(r)) {
		                r.recibirDaño(1);
		                if (!r.estaViva()) {
		                    Casilla c = r.getCasilla();
		                    if (c != null) c.liberar();
		                    if (roseSeleccionada == r) { roseSeleccionada = null; casillaAnterior = null; }
		                    generarRose[j] = null;
		                }
		            }
		        }
		        // Colisión con WallNut
		        boolean frenado = false;
		        for (int j = 0; j < cantidadWall; j++) {
		            WallNut w = generarWall[j];
		            if (w != null && w.estaViva() && z.colisionaConPlanta(w)) {
		                frenado = true;
		                w.recibirDaño(1);
		                if (!w.estaViva()) {
		                    Casilla c = w.getCasilla();
		                    if (c != null) c.liberar();
		                    if (wallSeleccionada == w) { wallSeleccionada = null; casillaAnterior = null; }
		                    generarWall[j] = null;
		                }
		            }
		        }
		        // Si no está frenado por un WallNut, avanza
		        if (!frenado) z.avanzar();
		        z.dibujar(entorno);
		        
		        // Si llega a la primera columna, roba el regalo y termina la partida
		        if (z.llegoALaPrimeraColumna(tablero)) {
		            Regalo regalo = tablero.getRegaloEnFila(z.getFila());
		            if (!regalo.estaRobado()) {
		                regalo.robar();
		                derrota = true;
		                estaJugando = false;
		                menuActual = "derrota";
		            }
		        }
		    }
		}
	        // --- Lógica de zombies rápidos ---
	        for (int i = 0; i < cantidadZombiesRapidos; i++) {
	            ZombieGrinchRapido zr = zombiesRapidos[i];
	            if (zr != null && zr.estaVivo()) {
	                // Colisiones con RoseBlade
	                for (int j = 0; j < cantidadRose; j++) {
	                    RoseBlade r = generarRose[j];
	                    if (r != null && r.estaViva() && zr.colisionaConPlanta(r)) {
	                        r.recibirDaño(1);
	                        if (!r.estaViva()) {
	                            Casilla c = r.getCasilla();
	                            if (c != null) c.liberar();
	                            if (roseSeleccionada == r) { roseSeleccionada = null; casillaAnterior = null; }
	                            generarRose[j] = null;
	                        }
	                    }
	                }
	                // Colisión con WallNut
	                boolean frenado = false;
	                for (int j = 0; j < cantidadWall; j++) {
	                    WallNut w = generarWall[j];
	                    if (w != null && w.estaViva() && zr.colisionaConPlanta(w)) {
	                        frenado = true;
	                        w.recibirDaño(1);
	                        if (!w.estaViva()) {
	                            Casilla c = w.getCasilla();
	                            if (c != null) c.liberar();
	                            if (wallSeleccionada == w) { wallSeleccionada = null; casillaAnterior = null; }
	                            generarWall[j] = null;
	                        }
	                    }
	                }
	                // Si no está frenado, avanza
	                if (!frenado) zr.avanzar();
	                zr.dibujar(entorno);
	                
	                // Si llega a la primera columna, roba el regalo y termina la partida
	                if (zr.llegoALaPrimeraColumna(tablero)) {
	                    Regalo regalo = tablero.getRegaloEnFila(zr.getFila());
	                    if (!regalo.estaRobado()) {
	                        regalo.robar();
	                        derrota = true;
	                        estaJugando = false;
	                        menuActual = "derrota";
	                    }
	                }
	            }
	        }

	        // --- Lógica de zombies resistentes ---
	        for (int i = 0; i < cantidadZombiesResistentes; i++) {
	            ZombieGrinchResistente zr = zombiesResistentes[i];
	            if (zr != null && zr.estaVivo()) {
	                // Colisiones con RoseBlade
	                for (int j = 0; j < cantidadRose; j++) {
	                    RoseBlade r = generarRose[j];
	                    if (r != null && r.estaViva() && zr.colisionaConPlanta(r)) {
	                        r.recibirDaño(1);
	                        if (!r.estaViva()) {
	                            Casilla c = r.getCasilla();
	                            if (c != null) c.liberar();
	                            if (roseSeleccionada == r) { roseSeleccionada = null; casillaAnterior = null; }
	                            generarRose[j] = null;
	                        }
	                    }
	                }
	                // Colisiones con WallNut
	                boolean frenado = false;
	                for (int j = 0; j < cantidadWall; j++) {
	                    WallNut w = generarWall[j];
	                    if (w != null && w.estaViva() && zr.colisionaConPlanta(w)) {
	                        frenado = true;
	                        w.recibirDaño(1);
	                        if (!w.estaViva()) {
	                            Casilla c = w.getCasilla();
	                            if (c != null) c.liberar();
	                            if (wallSeleccionada == w) { wallSeleccionada = null; casillaAnterior = null; }
	                            generarWall[j] = null;
	                        }
	                    }
	                }
	                // Si no está frenado, avanza
	                if (!frenado) zr.avanzar();
	                zr.dibujar(entorno);
	                
	                // Si llega a la primera columna, roba el regalo y termina la partida
	                if (zr.llegoALaPrimeraColumna(tablero)) {
	                    Regalo regalo = tablero.getRegaloEnFila(zr.getFila());
	                    if (!regalo.estaRobado()) {
	                        regalo.robar();
	                        derrota = true;
	                        estaJugando = false;
	                        menuActual = "derrota";
	                    }
	                }
	            }
	        }

	        // --- Lógica del zombie colosal ---
	        if (colosalActivo && zombieColosal != null && zombieColosal.estaVivo()) {
	            // Colisiones con RoseBlade
	            for (int j = 0; j < cantidadRose; j++) {
	                RoseBlade r = generarRose[j];
	                if (r != null && r.estaViva() && zombieColosal.colisionaConPlanta(r)) {
	                    r.recibirDaño(1);
	                    if (!r.estaViva()) {
	                        Casilla c = r.getCasilla();
	                        if (c != null) c.liberar();
	                        if (roseSeleccionada == r) { roseSeleccionada = null; casillaAnterior = null; }
	                        generarRose[j] = null;
	                    }
	                }
	            }
	            // Colisiones con WallNut
	            boolean frenado = false;
	            for (int j = 0; j < cantidadWall; j++) {
	                WallNut w = generarWall[j];
	                if (w != null && w.estaViva() && zombieColosal.colisionaConPlanta(w)) {
	                    frenado = true;
	                    w.recibirDaño(1);
	                    if (!w.estaViva()) {
	                        Casilla c = w.getCasilla();
	                        if (c != null) c.liberar();
	                        if (wallSeleccionada == w) { wallSeleccionada = null; casillaAnterior = null; }
	                        generarWall[j] = null;
	                    }
	                }
	            }
	            // Si no está frenado, avanza
	            if (!frenado) zombieColosal.avanzar();
	            zombieColosal.dibujar(entorno);
	            
	            // Si llega a la primera columna, roba el regalo y termina la partida
	            if (zombieColosal.llegoALaPrimeraColumna(tablero)) {
	                Regalo regalo = tablero.getRegaloEnFila(zombieColosal.getFila());
	                if (!regalo.estaRobado()) {
	                    regalo.robar();
	                    derrota = true;
	                    estaJugando = false;
	                    menuActual = "derrota";
	                }
	            }
	        }
		}


		// --- Chequeo de victoria ---
		if (!derrota && !victoria && zombiesEliminados >= totalZombiesObjetivo) {
		    victoria = true;               
		    estaJugando = false;             //vuelve al menú
		    menuActual = "victoria";
		    System.out.println("¡Ganaste! Eliminaste a todos los zombies.");
		}
	}
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}