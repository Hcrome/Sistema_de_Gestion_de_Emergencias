# Sistema de Gestión de Emergencias
### Proyecto Final — Programación en Java con JavaFX

---

## ¿Qué hace la aplicación?

Aplicación de escritorio desarrollada en **Java con JavaFX** que simula un sistema de gestión de emergencias ciudadanas. El usuario puede registrar una emergencia indicando sus datos personales, el tipo de incidente y su ubicación (dirección física o coordenadas GPS). El sistema valida la gravedad, localiza el hospital más cercano si se usan coordenadas, y simula el envío de una alerta al 112 guardando un registro en disco.

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17+ | Lenguaje principal |
| JavaFX 25 | Interfaz gráfica |
| Jackson | Deserialización de JSON |
| Haversine | Cálculo de distancias geográficas |
| FXML | Definición declarativa de pantallas |

---

## Estructura del proyecto

```
src/main/
├── java/com/emergencias/
│   ├── main/
│   │   └── Main.java                    ← Punto de entrada
│   ├── controller/
│   │   ├── MainMenuController.java      ← Navegación entre pantallas
│   │   ├── UserDataController.java      ← Formulario de emergencia
│   │   ├── EmergencyListController.java ← Pantalla de instrucciones
│   │   ├── EmergencyLogController.java  ← Pantalla de resultado
│   │   └── EmergencyManager.java        ← Orquestador del core
│   ├── detector/
│   │   └── EmergencyDetector.java       ← Lógica de validación y ubicación
│   ├── alert/
│   │   └── AlertSender.java             ← Simulación de envío al 112
│   └── model/
│       ├── EmergencyEvent.java          ← Modelo del evento (inmutable)
│       ├── UserData.java                ← Datos personales del usuario
│       ├── Hospital.java                ← Modelo hospital + Haversine
│       ├── HospitalLoader.java          ← Lectura JSON de hospitales
│       └── Instruction.java             ← Modelo de instrucción de emergencia
└── resources/
    ├── Main_menu.fxml
    ├── user_data_form.fxml
    ├── emergency_list.fxml
    ├── emergency_log.fxml
    ├── Hospital_list.json
    └── instrucciones.json
```

---

## Flujo de la aplicación

```
Main
 └── carga Main_menu.fxml
      │
      ├── [Botón: Instrucciones]
      │    └── carga emergency_list.fxml
      │         └── EmergencyListController.initialize()
      │              └── lee instrucciones.json
      │                   └── Jackson → List<Instruction> → TextArea
      │
      └── [Botón: Iniciar Emergencia]
           └── carga user_data_form.fxml
                └── UserDataController.initialize()
                     ├── gestiona SplitMenuButton (físico ↔ coordenadas GPS)
                     └── [Botón: Enviar] → enviarEmergencia()
                          ├── recoge campos del formulario
                          └── EmergencyManager.startSystem()
                               └── EmergencyDetector.processFromGUI()
                                    ├── [Modo GPS]
                                    │    └── HospitalLoader.cargarDatos()
                                    │         └── Jackson → List<Hospital>
                                    │              └── encontrarMasCercano()
                                    │                   └── Hospital.calcularDistanciaA()
                                    │                        └── Fórmula Haversine
                                    │
                                    └── [Si gravedad ≥ umbral]
                                         └── new EmergencyEvent(...)
                                              └── asignarPrioridad() → ALTA / MEDIA / BAJA
                                                   └── devuelve evento a EmergencyManager
                                                        ├── AlertSender.sendAlert()
                                                        │    ├── persiste en emergency_log.txt
                                                        │    └── simula SMS al contacto
                                                        └── guardarEnHistorial()
                                                             └── persiste en historial_emergencias.txt
```

---

## Descripción de cada clase

### `Main`
Punto de entrada de la aplicación. Carga el FXML del menú principal y muestra la ventana. No contiene lógica de negocio.

### `MainMenuController`
Gestiona la navegación entre pantallas mediante métodos `@FXML` conectados a los botones del menú. Usa un método genérico `cambiarEscena()` que obtiene el `Stage` actual a través del evento y reemplaza la escena.

### `UserDataController`
Controlador del formulario de emergencia. Gestiona el `SplitMenuButton` para alternar entre dirección física y coordenadas GPS, y el método `enviarEmergencia()` que recoge todos los campos y lanza el core del sistema.

### `EmergencyListController`
Carga el archivo `instrucciones.json` usando Jackson, convierte cada objeto `Instruction` a texto y lo muestra en el `TextArea`. Si no encuentra el archivo, muestra un mensaje de error.

### `EmergencyLogController`
Recibe el `EmergencyEvent` resultante y formatea la información en pantalla. Si el modo fue GPS muestra el hospital más cercano; si fue dirección física, omite ese campo.

### `EmergencyManager`
Fachada que orquesta el core sin contener lógica propia. Instancia `EmergencyDetector` y `AlertSender`, ejecuta el flujo y devuelve el evento al controlador.

### `EmergencyDetector`
Clase con la mayor responsabilidad del sistema. Decide el modo de ubicación, instancia `HospitalLoader` si es GPS, valida la gravedad contra el umbral y construye el `EmergencyEvent` si procede.

### `HospitalLoader`
Lee `Hospital_list.json` con Jackson y encuentra el hospital más cercano usando la API de Streams con filtro de coordenadas válidas.

### `Hospital`
Modelo de datos con el algoritmo de **Haversine** implementado en `calcularDistanciaA()` para calcular la distancia en kilómetros entre el hospital y el usuario. Incluye `tieneCoordenadasValidas()` para filtrar datos corruptos del JSON.

### `EmergencyEvent`
Objeto **inmutable** (todos los campos son `final`) que agrupa los datos del incidente: ID generado por timestamp, tipo, ubicación resuelta, usuario, prioridad y modo de ubicación. La prioridad se asigna automáticamente por palabras clave en el tipo de emergencia.

### `UserData`
POJO con los datos personales del usuario: nombre, teléfono de contacto e información médica.

### `AlertSender`
Simula el envío de la alerta al 112. Persiste el evento en `emergency_log.txt` en modo append (sin sobreescribir registros anteriores) y simula una notificación SMS al contacto personal.

### `Instruction`
POJO que representa una instrucción de emergencia leída del JSON, con campos `id`, `titulo` y `descripcion`.

---
Adrian Andres Garcia Bernabeu
