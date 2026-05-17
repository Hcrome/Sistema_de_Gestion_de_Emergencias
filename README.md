# Sistema de Gestión de Emergencias — Exposición Técnica

## Descripción general

El proyecto es una aplicación de escritorio desarrollada en **Java con JavaFX** que simula un sistema de gestión de emergencias ciudadanas. Permite al usuario registrar una emergencia, calcular el hospital más cercano a su posición mediante coordenadas GPS, y simular el envío de una alerta al servicio 112, dejando constancia en archivos de log.

---

## Arquitectura por capas

El código se organiza en cuatro capas con responsabilidades bien diferenciadas:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Presentación | `controller/` | Navegación entre pantallas y recogida de datos del formulario |
| Orquestación | `controller/EmergencyManager` | Coordina la detección y el envío sin contener lógica propia |
| Lógica | `detector/` | Valida la emergencia, resuelve la ubicación y crea el evento |
| Datos y salida | `model/`, `alert/` | Modelos de datos, lectura del JSON y persistencia en disco |

---

## Flujo de ejecución

El sistema sigue un flujo lineal desde la interacción del usuario hasta la escritura en disco:

1. El usuario pulsa **"Iniciar reporte"** en el menú principal.
2. `Main.java` carga el FXML del formulario e instancia `UserDataController`.
3. Al pulsar **"Enviar"**, el controlador recoge los campos y llama a `EmergencyManager.startSystem()`.
4. El manager delega en `EmergencyDetector.processFromGUI()`, que resuelve la ubicación y valida la gravedad.
5. Si la gravedad supera el umbral, se crea un `EmergencyEvent` y se devuelve al manager.
6. El manager llama a `AlertSender.sendAlert()` y a `guardarEnHistorial()` en paralelo.
7. La pantalla cambia al log de resultado.

---

## Clases principales

### `MainMenuController`

Gestiona la navegación entre pantallas mediante dos métodos anotados con `@FXML`:

```java
@FXML
void abrirEmergencia(ActionEvent event) {
    cambiarEscena(event, "/view/formulario_emergencia.fxml", "Registro de Emergencia");
}
```

Toda la lógica de cambio de escena está centralizada en un único método privado `cambiarEscena()`, que obtiene el `Stage` actual a través del evento y reemplaza la escena completa.

**Nota:** existe una inconsistencia entre este controlador y `Main.java`: el archivo `Main` vincula los botones manualmente con `root.lookup()`, ignorando el controlador FXML. En la práctica es `Main.java` el que controla la navegación real.

---

### `UserDataController`

Recoge los datos del formulario usando `root.lookup("#id")` para localizar cada componente:

```java
TextField txtNombre   = (TextField) root.lookup("#txtNombre");
Slider sliderGravedad = (Slider)    root.lookup("#sliderGravedad");
// ...
```

Al pulsar "Enviar" determina el modo de ubicación (dirección manual o coordenadas GPS) y pasa todos los datos al manager. Incluye valores por defecto para cada campo en caso de que `lookup` devuelva `null`.

**Puntos mejorables:**
- El uso de `lookup()` es frágil: un cambio de ID en el FXML provoca un fallo silencioso.
- No hay validación de entrada visible al usuario.
- El controlador mezcla recogida de datos, carga de FXML, ejecución del core y cambio de escena.

---

### `EmergencyManager`

Actúa como **fachada** entre la UI y el core. No contiene lógica propia; instancia las dependencias en el constructor y las coordina en `startSystem()`:

```java
public void startSystem(String tipo, String modoUbic, String dir,
                        String lat, String lon, int grav) {
    EmergencyEvent evento = detector.processFromGUI(...);
    if (evento != null) {
        sender.sendAlert(evento);
        guardarEnHistorial(evento);
    }
}
```

El único punto de decisión es el `if (evento != null)`: si el detector descarta la emergencia por baja gravedad, el manager no realiza ninguna acción.

---

### `EmergencyDetector`

Es la clase con mayor responsabilidad real del sistema. Ejecuta tres tareas en secuencia:

1. **Resolución de ubicación:** si el modo es GPS, instancia `HospitalLoader`, carga el JSON y busca el hospital más cercano usando el algoritmo de Haversine.
2. **Validación de gravedad:** compara el valor del slider contra el umbral configurado (valor `3`).
3. **Creación del evento:** si la gravedad es suficiente, construye y devuelve un `EmergencyEvent`.

**Problema de orden:** el sistema carga el JSON y calcula distancias antes de validar la gravedad. Si el evento resulta ser un falso positivo, ese trabajo se habrá realizado innecesariamente. El orden eficiente sería validar la gravedad primero.

---

### `HospitalLoader`

Lee el archivo JSON de hospitales usando **Jackson** y encuentra el más cercano mediante la API de Streams:

```java
public Hospital encontrarMasCercano(double miLat, double miLon) {
    return listaHospitales.stream()
        .filter(Hospital::tieneCoordenadasValidas)
        .min(Comparator.comparingDouble(h -> h.calcularDistanciaA(miLat, miLon)))
        .orElse(null);
}
```

El filtro `tieneCoordenadasValidas()` descarta hospitales con coordenadas nulas o en formato UTM (valores superiores a 190, que no pueden ser grados decimales válidos), gestionando así datos corruptos del JSON.

**Problema de ruta:** la ruta al JSON está hardcodeada como `new File("src/main/java/resources/...")`, lo que funciona en el IDE pero se rompe al empaquetar en un JAR. Debería usarse `getClass().getResourceAsStream()`.

**Problema de ciclo de vida:** `HospitalLoader` se instancia de nuevo en cada emergencia procesada, leyendo el JSON del disco cada vez. Debería cargarse una sola vez al arrancar la aplicación.

---

### `Hospital` y el algoritmo Haversine

Cada hospital sabe calcular su distancia a unas coordenadas dadas mediante la fórmula de Haversine, que tiene en cuenta la curvatura de la Tierra:

```java
public double calcularDistanciaA(double userLat, double userLon) {
    double earthRadius = 6371; // km
    double dLat = Math.toRadians(hospitalLat - userLat);
    double dLon = Math.toRadians(hospitalLon - userLon);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(userLat)) *
               Math.cos(Math.toRadians(hospitalLat)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;
}
```

El propio código documenta que los campos `latitud` y `longitud` están **invertidos en el JSON de origen**, lo que supone un bug de datos que se gestiona implícitamente pero no se corrige.

---

### `EmergencyEvent`

Modelo inmutable (todos sus campos son `final`) que agrupa los datos de la emergencia. Genera automáticamente un ID basado en la fecha y hora, y asigna una prioridad mediante detección de palabras clave:

| Prioridad | Palabras clave |
|---|---|
| ALTA | fuego, accidente, cardiaco |
| MEDIA | sanitaria, robo |
| BAJA | cualquier otro tipo |

**Limitaciones del sistema de prioridad:**
- Solo reconoce palabras en español y sin variantes (p. ej. "cardíaco" con tilde no activa `ALTA`).
- Cualquier tipo no reconocido cae a `BAJA`, incluyendo errores tipográficos.
- La prioridad y la gravedad numérica son independientes y no se cruzan.

---

### `AlertSender`

Simula el envío al 112 mediante tres acciones:

1. Imprime el paquete de datos por consola.
2. Persiste el evento en `emergency_log.txt` en modo append, para no sobreescribir registros anteriores.
3. Simula una notificación SMS al contacto personal (solo imprime por consola).

**Problema de doble persistencia:** el mismo evento se escribe en dos archivos distintos — `AlertSender` escribe en `emergency_log.txt` y `EmergencyManager` escribe en `historial_emergencias.txt` — con el mismo contenido y sin diferencia documentada de propósito.

---

## Resumen de puntos fuertes

- Separación de responsabilidades razonablemente clara entre capas.
- Uso correcto de Jackson con `FAIL_ON_UNKNOWN_PROPERTIES = false` para tolerar campos extra en el JSON.
- Algoritmo de Haversine correctamente implementado.
- Filtrado defensivo de coordenadas inválidas antes de cualquier cálculo.
- Objetos de evento inmutables (`final`) que garantizan integridad del registro.
- Uso de `try-with-resources` para gestión segura de recursos de I/O.

## Resumen de puntos mejorables

- Rutas a archivos hardcodeadas que rompen al empaquetar en JAR.
- `HospitalLoader` se instancia en cada emergencia en lugar de una sola vez.
- Teléfono de contacto hardcodeado, ignorando los datos reales del usuario.
- `lookup()` frágil en lugar de inyección `@FXML` estándar de JavaFX.
- Doble persistencia del mismo evento sin propósito diferenciado.
- Sistema de prioridad demasiado rígido basado en palabras clave fijas.
- El orden de operaciones en `EmergencyDetector` realiza trabajo innecesario en falsos positivos.
- ID de evento basado en segundos no garantiza unicidad bajo carga.
