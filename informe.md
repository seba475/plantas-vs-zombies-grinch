# La Invasión de los Zombies Grinch  
### Trabajo práctico – Programación I (UNGS)  
**Implementación en Java usando el entorno gráfico provisto por la cátedra**

---

## 📘 Descripción general del proyecto
Este proyecto es una recreación del clásico *Plants vs Zombies*, adaptado a una temática navideña donde el jugador debe defender los regalos del ataque de los **Zombies Grinch**.

Fue desarrollado como trabajo práctico final de la materia **Programación I**, utilizando **Java** y el entorno gráfico provisto por las clases `Entorno` y `Herramientas`.

El objetivo del juego es evitar que los zombies lleguen a los regalos, colocando plantas con distintas habilidades, moviéndolas estratégicamente y administrando los tiempos de recarga de cada carta.

---

## ⭐ Características principales del juego

### 🟦 Tablero y HUD
- Tablero de **5 filas** con casillas donde se colocan las plantas.  
- Regalos ubicados en la primera columna (objetivo a defender).  
- HUD superior con:
  - cartas de plantas  
  - zombies eliminados  
  - zombies restantes  
  - tiempo de juego  

### 🌱 Plantación de plantas
- Selección con click izquierdo y arrastre hasta una casilla libre.  
- Cada carta tiene **tiempo de recarga** visible.  
- No se puede plantar sobre casillas ocupadas ni sobre regalos.

### 🔄 Movimiento de plantas
- Solo una planta puede estar seleccionada a la vez.  
- Movimiento con **W, A, S, D**.  
- No pueden salir del tablero ni moverse a casillas ocupadas.  
- Click derecho para deseleccionar.

---

## 🌿 Tipos de plantas

### **RoseBlade**
- Planta ofensiva que dispara proyectiles hacia la derecha.  
- Tiene cooldown.

### **WallNut**
- Planta defensiva con mucha resistencia.

---

## 🧟 Zombies Grinch
- Aparecen desde la derecha en filas aleatorias.  
- Avanzan hacia los regalos.  
- Requieren **2 o más impactos** para ser eliminados.  
- Variantes implementadas:
  - estándar  
  - rápido  
  - resistente  
  - colosal (jefe final)

---

## 💥 Proyectiles
- Avanzan hacia la derecha.  
- Se eliminan al impactar o salir de pantalla.

---

## 🏁 Condiciones del juego
- **Victoria:** eliminar la cantidad total de zombies (50 en esta versión).  
- **Derrota:** un zombie llega a los regalos.  
- Máximo de **1 a 15 zombies simultáneos**.

---

## 🧱 Arquitectura del código

### 🟩 Clase principal: `Juego`
Coordina todo el funcionamiento del juego. Maneja:
- tablero  
- plantas  
- zombies  
- proyectiles  
- HUD  
- menú inicial  

El método central es `tick()`, llamado en cada frame por el entorno.

### 🌱 Plantas
- `RoseBlade`: ofensiva, dispara proyectiles.  
- `WallNut`: defensiva, mucha vida.  

Ambas comparten:
- posición  
- imagen  
- detección de clics  
- colisiones  
- casilla actual  

### 🧟 Zombies
Clases con velocidad, vida, imagen y comportamiento propio.

Métodos comunes:
- `avanzar()`  
- `recibirDaño()`  
- `estaVivo()`  
- `dibujar()`  

### 💥 Proyectiles
- Avanzan hacia la derecha.  
- Detectan colisiones con zombies.  
- Se eliminan al impactar o salir de pantalla.

### 🎁 Regalos
- Objetos a proteger.  
- Si un zombie los alcanza → derrota.

### 🧰 HUD y Cartas
- Cartas con cooldown visible.  
- Barra de recarga.  
- Información del estado del juego.

---

## 🧪 Tecnologías utilizadas
- Java  
- Entorno gráfico provisto por la cátedra (`Entorno`, `Herramientas`)  
- Arrays como única estructura de datos (requisito obligatorio)  
- Eclipse como IDE  

---

## 🎓 Aprendizajes y conclusiones
Este proyecto nos permitió:

- aplicar programación orientada a objetos  
- trabajar con múltiples clases y responsabilidades  
- manejar colisiones, animaciones y lógica de juego  
- organizar código en paquetes  
- trabajar en equipo y resolver conflictos  
- usar GitHub para versionado y colaboración  

El resultado final fue un juego funcional, estable y fiel a la consigna, que nos permitió consolidar los conceptos de la materia.
