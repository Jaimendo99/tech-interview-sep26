# Documentación de referencia

Este material existe porque no vas a tener internet durante la prueba. No es un tutorial ni
resuelve los ejercicios. Es lo que normalmente buscarías en Google o en el Javadoc: sintaxis,
firmas de métodos y comportamiento del lenguaje.

Asumo que ya sabes programar en algún otro lenguaje. No explico qué es una variable, un bucle o
una función. Explico cómo Java escribe esas cosas y en qué se comporta distinto.

## Orden de lectura

Lee el 01 completo antes de tocar código. Es corto y te ahorra la mitad de los errores de
compilación. Los demás úsalos como consulta.

| Archivo | Contenido |
|---|---|
| [01-java-para-quien-viene-de-otro-lenguaje.md](01-java-para-quien-viene-de-otro-lenguaje.md) | Estructura de un proyecto Java, tipos, `null`, strings, igualdad |
| [02-records-y-json.md](02-records-y-json.md) | Qué es un `record` y cómo Jackson convierte JSON en objetos |
| [03-listas-y-colecciones.md](03-listas-y-colecciones.md) | `List`, `ArrayList`, recorrido, streams, `Map` |
| [04-algoritmos-de-ordenamiento.md](04-algoritmos-de-ordenamiento.md) | Comparación en Java y los algoritmos de ordenamiento clásicos |
| [05-enums-switch-y-result.md](05-enums-switch-y-result.md) | Enums, `switch` como expresión, genéricos, la interfaz `Result` |
| [06-interfaces-clases-e-inyeccion.md](06-interfaces-clases-e-inyeccion.md) | Interfaz vs implementación, constructores, composición de objetos |
| [07-excepciones.md](07-excepciones.md) | `try`/`catch`, checked vs unchecked, cómo leer un stack trace |
| [08-http-y-api.md](08-http-y-api.md) | Cómo el proyecto habla con el servidor y cómo inspeccionar un endpoint |
| [09-como-ejecutar.md](09-como-ejecutar.md) | Compilar, ejecutar y detener el programa |
| [10-errores-comunes.md](10-errores-comunes.md) | Mensajes de error de Java traducidos a causas probables |

## El trabajo está marcado en el código

Los puntos que tienes que completar están marcados con comentarios `TODO` en los archivos
fuente. En IntelliJ, la pestaña TODO de la barra inferior los lista todos de una vez.

## Si te trabas

Antes de leer, ubica el error. No es lo mismo un error de compilación que uno en ejecución.

Si el proyecto no compila, el IDE subraya la línea en rojo y nunca llegas a ejecutar nada. Ve
directo a [10-errores-comunes.md](10-errores-comunes.md), sección de compilación.

Si compila pero explota al correr, vas a ver un stack trace en la consola. Aprende a leerlo en
[07-excepciones.md](07-excepciones.md). El nombre de la excepción casi siempre dice qué pasó.

Si compila, corre y no hace lo que esperas, imprime valores intermedios con
`System.out.println(...)`. Java no tiene REPL a mano, así que imprimir es la herramienta rápida.

Y antes de suponer nada sobre una clase del proyecto, ábrela y léela. Son archivos cortos. La
respuesta a "qué recibe este constructor" o "qué devuelve este método" está escrita ahí.
