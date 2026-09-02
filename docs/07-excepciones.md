# Excepciones

## Las dos familias

Java divide las excepciones en dos grupos, y la diferencia la impone el compilador.

**Checked.** El compilador te obliga a hacerte cargo. Si un método puede lanzarlas, o las
capturas o las declaras. Son las que representan fallas del mundo exterior: `IOException`,
`InterruptedException`, todo lo que hereda de `Exception` sin pasar por `RuntimeException`.

**Unchecked.** El compilador las ignora. Heredan de `RuntimeException` y representan errores de
programación: `NullPointerException`, `IndexOutOfBoundsException`,
`IllegalArgumentException`, `IllegalStateException`,
`UnsupportedOperationException`. Puedes capturarlas, pero nadie te obliga.

Si vienes de Python, JavaScript o C#, este segundo grupo es el único que conoces. Las checked
son la parte que se siente extraña al principio.

## Declarar que un método lanza

```java
public List<String> leer() throws IOException, InterruptedException {
```

`throws` en la firma significa "yo no manejo esto, que lo maneje quien me llame". Quien llame a
ese método tiene la misma decisión: capturar o volver a declarar.

Si llamas a un método con `throws IOException` y no haces ninguna de las dos cosas, el error de
compilación es `unreported exception IOException; must be caught or declared to be thrown`.

## try y catch

```java
try {
    List<String> datos = leer();
} catch (IOException e) {
    System.out.println("falló la lectura: " + e.getMessage());
}
```

Varios tipos en un solo bloque, separados por barra vertical:

```java
} catch (IOException | InterruptedException e) {
```

Capturar el tipo padre atrapa también a los hijos. `catch (Exception e)` atrapa casi todo, lo
que es cómodo y peligroso al mismo tiempo, porque esconde errores que no esperabas.

`finally` corre siempre, haya o no excepción. Sirve para liberar recursos.

```java
try {
    // ...
} catch (Exception e) {
    // ...
} finally {
    // esto corre pase lo que pase
}
```

Los bloques `catch` se evalúan en orden. Si pones `catch (Exception e)` antes que uno más
específico, el específico queda inalcanzable y el compilador lo reclama.

## Lanzar

```java
throw new IllegalStateException("mensaje que explica qué pasó");
```

`throw` lanza una instancia. `throws` declara en la firma. Se parecen y hacen cosas distintas.

Para envolver una excepción dentro de otra sin perder la original, pásala como argumento:

```java
throw new RuntimeException(e);
```

Eso es lo que produce las secciones `Caused by` en el stack trace.

## Métodos de una excepción

| Método | Devuelve |
|---|---|
| `getMessage()` | `String` con el mensaje, puede ser `null` |
| `getClass().getSimpleName()` | `String` con el nombre del tipo |
| `getCause()` | la excepción envuelta, o `null` |
| `printStackTrace()` | imprime el rastro completo en la salida de error |

`getMessage()` devolviendo `null` es una fuente clásica de un segundo
`NullPointerException` dentro del `catch`, justo cuando estabas tratando de reportar el
primero.

## Leer un stack trace

Cuando algo revienta en ejecución, la consola imprime algo así:

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke
"com.ejemplo.Repositorio.buscar()" because "this.repositorio" is null
	at com.ejemplo.Servicio.obtener(Servicio.java:24)
	at com.ejemplo.Main.main(Main.java:12)
```

Se lee así:

La primera línea trae el tipo de excepción y el mensaje. Casi siempre es la información más
útil, y en el caso de `NullPointerException` Java te dice exactamente qué expresión valía
`null`.

Las líneas `at ...` son la pila de llamadas, de la más reciente a la más antigua. La primera es
donde reventó. La última es dónde empezó todo.

Busca la primera línea que mencione un paquete tuyo. Ese es tu código y ahí está el problema.
Las líneas de `java.base`, `jackson` o `sun.nio` son librerías, y salvo casos raros no tienen
el bug, solo lo reportan.

En IntelliJ los `Nombre.java:24` son enlaces. Un click te lleva a la línea.

Si aparece `Caused by:` más abajo, esa sección es la excepción original y suele ser la que
importa. La de arriba solo la envolvió.

## Thread.sleep e InterruptedException

```java
try {
    Thread.sleep(1000);   // milisegundos
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

`Thread.sleep` pausa el hilo actual la cantidad de milisegundos que le pases. Lanza
`InterruptedException`, que es checked, así que el `try` es obligatorio.

`InterruptedException` significa que alguien pidió que este hilo se detenga mientras dormía. Al
capturarla, Java borra la marca de interrupción del hilo, y por eso la convención es volver a
ponerla con `Thread.currentThread().interrupt()`. Si no lo haces, cualquier código que después
revise `Thread.currentThread().isInterrupted()` va a creer que nadie pidió detenerse.

Ese patrón ya está escrito en `Main.java`. Es el mecanismo que permite cortar un bucle infinito
de forma ordenada.

## Excepciones que vas a ver en este proyecto

| Excepción | Causa habitual |
|---|---|
| `NullPointerException` | usar un objeto que nunca se construyó o que un método devolvió como `null` |
| `ConnectException` | el servidor no está corriendo, o la dirección o el puerto están mal |
| `UnrecognizedPropertyException` | el JSON trae una clave que el record no declara |
| `MismatchedInputException` | el tipo del campo no calza con el valor del JSON |
| `IllegalArgumentException` | un argumento inválido, por ejemplo una URL mal formada |
| `UnsupportedOperationException` | modificar una lista inmutable |
| `IndexOutOfBoundsException` | índice fuera del rango de la lista |
| `ConcurrentModificationException` | modificar una lista mientras se recorre con for-each |
