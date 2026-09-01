# Interfaces, clases y composición de objetos

## Interfaz

Una interfaz declara qué métodos existen, sin decir cómo funcionan.

```java
public interface Notificador {
    void enviar(String mensaje);
}
```

Los métodos de una interfaz son públicos y abstractos por defecto. No llevan cuerpo, terminan
en punto y coma.

Una interfaz no se puede instanciar. `new Notificador()` no compila. Lo que instancias es una
clase que la implemente.

## Implementación

```java
public class NotificadorPorCorreo implements Notificador {
    @Override
    public void enviar(String mensaje) {
        // aquí sí hay cuerpo
    }
}
```

`implements` declara el compromiso. Si no escribes todos los métodos de la interfaz, la clase
no compila.

`@Override` es una anotación opcional pero recomendable. Le dice al compilador "esto se supone
que sobrescribe algo de la interfaz". Si te equivocas en el nombre o en los parámetros, el
error aparece en la línea correcta en vez de aparecer como un método nuevo que nadie llama.

## Por qué separar interfaz de implementación

Porque el resto del código depende del contrato y no de la implementación concreta. Un campo
declarado así:

```java
private final Notificador notificador;
```

acepta cualquier implementación. Puedes pasarle la que habla por HTTP en producción y una falsa
que solo imprime en consola durante pruebas, sin tocar una línea de la clase que la usa.

Cuando declares una variable, usa el tipo más general que te sirva. La interfaz del lado
izquierdo, la clase concreta del lado derecho:

```java
Notificador n = new NotificadorPorCorreo();
List<String> l = new ArrayList<>();
```

## Constructores

Un constructor es un método sin tipo de retorno que se llama igual que la clase. Corre una sola
vez, cuando creas el objeto con `new`.

```java
public class Inventario {
    private final Notificador notificador;
    private final int limite;

    public Inventario(Notificador notificador, int limite) {
        this.notificador = notificador;
        this.limite = limite;
    }
}
```

`this.campo = parametro` es el patrón normal cuando el parámetro se llama igual que el campo.
`this` desambigua: a la izquierda el campo del objeto, a la derecha el parámetro.

Los campos `final` tienen que quedar asignados al terminar el constructor. Después ya no se
pueden reasignar.

Si una clase no declara ningún constructor, Java le da uno vacío sin parámetros. En el momento
en que declaras uno, ese regalo desaparece.

## Composición

Una clase recibe por constructor los objetos que necesita, en lugar de crearlos por dentro. Eso
es inyección de dependencias, y no requiere ningún framework.

```java
Notificador notificador = new NotificadorPorCorreo(configuracion);
Inventario inventario = new Inventario(notificador, 100);
```

O todo en una expresión, construyendo de adentro hacia afuera:

```java
Inventario inventario = new Inventario(new NotificadorPorCorreo(configuracion), 100);
```

Las dos formas son idénticas. La primera es más fácil de leer cuando hay varios niveles.

Cuando las dependencias se encadenan, hay un orden obligatorio: para construir un objeto
necesitas antes todos los que van en su constructor. Empieza por los que no dependen de nada y
avanza hacia afuera.

## Cómo leer un constructor

Abre la clase y busca el método que se llama igual que ella. Sus parámetros son exactamente lo
que necesitas tener listo antes de escribir el `new`.

```java
public MobItemInMemoryDB(List<MobItem> items) {
```

Ese constructor pide una `List<MobItem>`. Nada más.

En IntelliJ hay dos atajos que valen oro aquí. `Ctrl+Click` sobre un nombre de clase salta a su
declaración. Y con el cursor dentro de los paréntesis de un `new`, `Ctrl+P` muestra los
parámetros que espera.

Cada clase del proyecto que implementa una interfaz declara su propio constructor. Ábrelas y
míralas, ahí está escrito qué necesita cada una.

## El problema de pasar null

Esto compila sin ningún reclamo:

```java
Inventario inventario = new Inventario(null, 100);
```

`null` es un valor válido para cualquier parámetro de tipo objeto. El compilador queda
contento. El programa explota más tarde, en la primera línea que intente usar ese campo, con un
`NullPointerException` que apunta a un lugar distinto de donde está el verdadero error.

Es un caso donde el mensaje de error te miente sobre la ubicación del problema. Cuando veas un
NPE, revisa no solo la línea que falló sino también dónde se construyó el objeto involucrado.

## Clases estáticas y métodos de fábrica

Un método `static` pertenece a la clase, no a una instancia. Se llama con el nombre de la clase:

```java
List.of("a", "b")
Integer.parseInt("42")
HttpClient.newHttpClient()
```

Varias clases de la librería estándar no se construyen con `new` sino con un método estático de
fábrica. `HttpClient` es una de ellas: `HttpClient.newHttpClient()` te devuelve un cliente con
configuración por defecto, listo para usar. `new HttpClient()` no existe.

`main` es `static` por la misma razón. Java lo llama sin haber creado ninguna instancia todavía.
