# ICE MONTECARLO

Integrantes:

- Luis Manuel Rojas Correa
- Ricardo Andres Chamorro Martinez
- Diego Armando Polanco Lozano
- Oscar Muñoz Ramirez
- Sebastian Erazo Ochoa

### Descripción del Método de Monte Carlo para Estimar π:

El método de Monte Carlo es una técnica probabilística utilizada para resolver problemas numéricos que pueden ser difíciles de abordar de manera directa. Para estimar el valor de π, el enfoque consiste en simular el lanzamiento de puntos aleatorios dentro de un cuadrado de lado 2, centrado en el origen del plano cartesiano. Un círculo de radio 1 está inscrito dentro de este cuadrado.

La idea es lanzar puntos de manera aleatoria en el área del cuadrado y determinar cuántos de esos puntos caen dentro del círculo inscrito. La proporción de puntos que caen dentro del círculo, con respecto al número total de puntos lanzados, se relaciona directamente con π/4. Esta relación surge de la geometría: el área del círculo es π y el área del cuadrado es 4, por lo que la razón entre ambas áreas es π/4.

El valor de π se puede aproximar multiplicando por 4 la proporción de puntos que caen dentro del círculo:

$$\
\pi \approx 4 \times \left( \frac{\text{Número de puntos dentro del círculo}}{\text{Número total de puntos lanzados}} \right)
\$$

### Diseño del Modelo Cliente-Maestro-Trabajadores:

En la implementación distribuida del método de Monte Carlo para la estimación de π, el sistema se organiza siguiendo un modelo Cliente-Maestro-Trabajadores. Este enfoque distribuye la carga computacional entre múltiples trabajadores, haciendo que el sistema sea más eficiente y escalable. A continuación, se describe cada uno de los roles en este modelo:

- **Cliente**: El cliente actúa como el iniciador del proceso. Envía una solicitud al maestro pidiendo la estimación de π basada en un número `N` de puntos aleatorios. Este valor de `N` representa el número total de puntos que se lanzarán para la estimación.

- **Maestro**: El maestro es el coordinador del sistema. Su tarea principal es distribuir la carga de trabajo entre los trabajadores. El maestro divide el total de puntos `N` en `n` subtareas, una para cada trabajador. Cada trabajador será responsable de generar un subconjunto de los puntos aleatorios (es decir, `N/n` puntos) y devolver al maestro el número de puntos que cayeron dentro del círculo.

- **Trabajadores**: Los trabajadores son las unidades de procesamiento. Reciben una cantidad específica de puntos aleatorios que deben generar en el cuadrado, y para cada punto, determinan si cae dentro del círculo. El trabajador devuelve al maestro el número de puntos que cayeron dentro del círculo en su tarea.

### Estrategia de Distribución:

1. El maestro divide el total de puntos `N` entre los `n` trabajadores, asignando a cada uno `N/n` puntos.
2. Cada trabajador genera aleatoriamente los puntos en el cuadrado de lado 2 y verifica cuántos caen dentro del círculo inscrito.
3. Los trabajadores envían sus resultados (el número de puntos dentro del círculo) al maestro.
4. El maestro recibe los resultados de todos los trabajadores, suma los puntos que cayeron dentro del círculo y utiliza la fórmula del método de Monte Carlo para estimar el valor de π:

   
   $$\\pi \approx 4 \times \left( \frac{\text{Total de puntos dentro del círculo}}{\text{Total de puntos lanzados}} \right)\$$

### Implementación con ICE (Internet Communications Engine):

El sistema está implementado utilizando ICE, un middleware que facilita la creación de aplicaciones distribuidas. La implementación puede comenzar con comunicaciones síncronas y luego ser optimizada para comunicaciones asíncronas para mejorar la escalabilidad.

#### Componentes clave en la implementación:

- **Comunicación entre cliente, maestro y trabajadores**: ICE facilita la comunicación entre los componentes del sistema. El cliente solicita el cálculo de π, el maestro distribuye las tareas y los trabajadores responden con los resultados.
  
- **Escalabilidad**: El sistema está diseñado para ser escalable. A medida que aumenta el número de puntos `N` o el número de trabajadores `n`, el sistema puede manejar eficientemente la distribución de tareas entre los trabajadores.

### Pruebas y Evaluación:

Se realizaron varias pruebas con diferentes cantidades de puntos `N` y distintos números de trabajadores `n` para evaluar el rendimiento y precisión del sistema. A medida que se aumentó el número de puntos lanzados, la estimación de π se acercó más al valor real de π.

Además, se midió el tiempo de ejecución con diferentes configuraciones de `n` trabajadores. Aumentando el número de nodos (trabajadores) en la red, se investigó cómo varió el tiempo de ejecución y cómo afectó la escalabilidad del sistema.

#### Resultados Exportados a CSV:

Los resultados de las pruebas, incluyendo el número de puntos lanzados, el número de trabajadores, el tiempo de ejecución, y la estimación obtenida de π, se exportaron a un archivo CSV para su análisis posterior. El formato del CSV contiene las siguientes columnas:

- **N**: Número total de puntos lanzados.
- **Tiempo de ejecución (s)**: Tiempo en segundos que tomó ejecutar la tarea.
- **Estimación de π**: Valor estimado de π basado en la proporción de puntos dentro del círculo.

El archivo CSV permitirá realizar un análisis detallado de cómo varía la precisión de la estimación y el tiempo de ejecución según las configuraciones probadas.

### Flujo básico de implementación:

1. **Cliente**: Envía una petición al maestro para calcular π con `N` puntos.
2. **Maestro**:
   - Divide los `N` puntos en tareas más pequeñas para los `n` trabajadores.
   - Envía las tareas a los trabajadores.
   - Recibe los resultados de los trabajadores.
   - Suma los resultados y calcula la estimación de π.
   - Devuelve el resultado al cliente.
3. **Trabajadores**: 
   - Reciben su tarea, generan puntos aleatorios, y cuentan cuántos caen dentro del círculo.
   - Devuelven el número de puntos dentro del círculo al maestro.

Este diseño permite que el cálculo sea distribuido y paralelo, lo que optimiza el tiempo de ejecución a medida que aumenta la cantidad de puntos `N`. 

### Ejecucion del programa

**Worker:**

Se ejecuta con el comando:

`java -jar worker/build/libs/worker.jar`

**Client:**

Se ejecuta con el comando:

`java -jar Master/build/libs/Master.jar`

**Master:** 

Se ejecuta con el comando:

`java -jar client/build/libs/client.jar`

**Nota:**

- Es necesario poner la ip actual del worker y estar en la misma red.
- El analisis de los datos obtenidos y las instrucciones estaran adjuntos en un informe en la carpeta docs del repositorio, donde se podra informar más.


