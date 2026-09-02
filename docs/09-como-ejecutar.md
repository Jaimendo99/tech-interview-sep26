# Compilar, ejecutar y detener

## Lo que necesitas

JDK 17 e IntelliJ IDEA. Ambos ya están instalados en la máquina.

El proyecto usa Gradle como sistema de construcción. No hace falta instalarlo: el repositorio
trae el wrapper (`gradlew`), que es un script que usa la versión correcta de Gradle por ti.

Las dependencias ya están descargadas en la caché local. No necesitas internet para compilar.

## Estructura del proyecto

```
build.gradle                    configuración del proyecto y dependencias
gradlew                         wrapper de Gradle
src/main/java/com/zonaconstru/  todo el código fuente
docs/                           esta documentación
build/                          salida de compilación, se regenera solo
```

`build/` no se toca. Se borra y se vuelve a generar en cada compilación.

## Abrir el proyecto

En IntelliJ, `File > Open` y elige la carpeta raíz del proyecto, la que contiene
`build.gradle`. No abras un archivo suelto ni la carpeta `src`.

La primera vez, IntelliJ va a sincronizar el proyecto con Gradle. Toma unos segundos y aparece
una barra de progreso abajo. Hasta que termine, el IDE marca imports en rojo que en realidad
están bien. Espera a que acabe antes de sacar conclusiones.

Si algo quedó raro después de la sincronización, el botón de recargar de la pestaña Gradle, a
la derecha, vuelve a sincronizar.

## Ejecutar

Abre `Main.java`, busca el método `main` y usa la flecha verde del margen izquierdo, o
`Shift+F10` para correr la última configuración usada.

La salida aparece en la pestaña Run, abajo. Ahí ves lo que imprimas con
`System.out.println` y también los stack traces si algo falla.

## Detener

El programa tiene un bucle infinito. No termina solo.

Se detiene con el cuadrado rojo de la ventana Run, o con `Ctrl+F2`.

Detenerlo y volver a correrlo es parte normal del ciclo de trabajo. Hazlo cada vez que cambies
código, porque el proceso que está corriendo tiene cargada la versión vieja.

## Desde la terminal

Compilar sin ejecutar:

```
./gradlew build
```

Solo compilar las clases, más rápido:

```
./gradlew compileJava
```

Sin red, agrega la bandera:

```
./gradlew build --offline
```

Borrar la salida y empezar de cero:

```
./gradlew clean build
```

Este proyecto no tiene configurada la tarea `run` de Gradle, así que `./gradlew run` no
funciona. Ejecuta desde IntelliJ.

En Windows el comando es `gradlew.bat` en lugar de `./gradlew`.

## Cuando el IDE y el compilador no coinciden

Puede pasar que IntelliJ marque un error que no existe, o que no marque uno que sí. La verdad
la tiene siempre `./gradlew build` en la terminal. Si el IDE dice una cosa y Gradle dice otra,
créele a Gradle.

Si el IDE queda inconsistente, `File > Invalidate Caches > Invalidate and Restart` lo arregla,
aunque tarda.

## Atajos útiles de IntelliJ

| Atajo (macOS) | Atajo (Windows y Linux) | Qué hace |
|---|---|---|
| `Cmd+Click` | `Ctrl+Click` | ir a la declaración de lo que sea |
| `Option+Enter` | `Alt+Enter` | sugerencias sobre el error o la advertencia bajo el cursor |
| `Cmd+P` | `Ctrl+P` | ver los parámetros del método donde está el cursor |
| `Cmd+O` | `Ctrl+N` | buscar una clase por nombre |
| `Cmd+Shift+F` | `Ctrl+Shift+F` | buscar texto en todo el proyecto |
| `Ctrl+R` | `Shift+F10` | ejecutar |
| `Cmd+F2` | `Ctrl+F2` | detener |
| doble `Shift` | doble `Shift` | buscar cualquier cosa |

`Option+Enter` sobre una línea en rojo es el atajo que más te va a servir. Ofrece agregar el
import faltante, crear el método que no existe, o corregir el tipo.
