# Enums, switch y la interfaz Result

## Enums

Un `enum` es un tipo con un conjunto cerrado de valores.

```java
public enum Estado {
    ACTIVO, INACTIVO, PENDIENTE
}
```

Se usan con el nombre del tipo por delante:

```java
Estado e = Estado.ACTIVO;
```

Un enum es un tipo real, no un alias de string ni de número. No puedes pasarle `"ACTIVO"` a un
parámetro de tipo `Estado`.

| Expresión | Devuelve |
|---|---|
| `Estado.values()` | arreglo con todos los valores |
| `Estado.valueOf("ACTIVO")` | el valor con ese nombre, lanza excepción si no existe |
| `e.name()` | `String` con el nombre de la constante |
| `e.ordinal()` | `int` con la posición en que fue declarada |

Los enums se comparan con `==` sin problema, porque cada constante existe una sola vez en
memoria.

## switch como sentencia y como expresión

Java tiene dos formas de `switch`. La vieja ejecuta código. La nueva **devuelve un valor**, y es
la que usa este proyecto.

Forma clásica, con `case ... :` y `break` obligatorio para no caer al siguiente caso:

```java
switch (estado) {
    case ACTIVO:
        System.out.println("activo");
        break;
    default:
        System.out.println("otro");
}
```

Forma de expresión, con flecha. No hay caída entre casos, así que no lleva `break`:

```java
String texto = switch (estado) {
    case ACTIVO -> "activo";
    case INACTIVO -> "inactivo";
    case PENDIENTE -> "pendiente";
};
```

Fíjate en el punto y coma final. Es una expresión asignada a una variable, no un bloque.

Cuando un caso necesita varias líneas, se abre un bloque y el valor se devuelve con `yield`:

```java
String texto = switch (estado) {
    case ACTIVO -> {
        String base = calcularAlgo();
        yield base + " activo";
    }
    case INACTIVO -> "inactivo";
    case PENDIENTE -> "pendiente";
};
```

`yield` es a un bloque de `switch` lo que `return` es a un método. `return` dentro de un
`switch` expresión no compila.

Un `switch` expresión sobre un enum tiene que ser **exhaustivo**: o cubre todas las constantes,
o tiene `default`. Si dejas una fuera, el compilador dice
`the switch expression does not cover all possible input values`. Esa exhaustividad es útil,
porque si alguien agrega una constante al enum, el compilador te obliga a decidir qué hacer con
ella.

Puedes agrupar casos con coma:

```java
case ACTIVO, PENDIENTE -> "en uso";
```

## Genéricos

Los genéricos son parámetros de tipo. `List<String>` significa "una lista cuyos elementos son
String". El compilador verifica que solo entren y salgan `String`.

Una clase puede declarar los suyos:

```java
public interface Caja<T> {
    T contenido();
}
```

`T` es un nombre cualquiera. La convención usa letras sueltas: `T` de tipo, `E` de elemento,
`K` y `V` de clave y valor. Con dos parámetros se escribe `<T, E>` y al usar el tipo se dan los
dos: `Caja<String>`, `Resultado<Integer, String>`.

Para leer un tipo genérico anidado, ve de afuera hacia adentro. `Result<List<MobItem>, Exception>`
es un `Result` cuyo primer parámetro es `List<MobItem>` y cuyo segundo es `Exception`.

## Interfaces selladas

Una interfaz `sealed` limita quién puede implementarla. La lista va en `permits`.

```java
public sealed interface Figura permits Circulo, Cuadrado {
    record Circulo(double radio) implements Figura { }
    record Cuadrado(double lado) implements Figura { }
}
```

Los records ahí dentro son clases anidadas. Se referencian con punto desde fuera:
`Figura.Circulo`.

La ventaja es que el conjunto de implementaciones es conocido y cerrado. Nadie puede agregar
una tercera figura desde otro archivo.

## Pattern matching con instanceof

`instanceof` pregunta si un objeto es de cierto tipo. Desde Java 16 puede declarar una variable
al mismo tiempo, ya convertida al tipo:

```java
if (figura instanceof Figura.Circulo c) {
    double area = Math.PI * c.radio() * c.radio();
}
```

Sin esa forma tendrías que preguntar el tipo y después hacer un cast a mano. Con ella, `c`
existe solo dentro del `if` y ya viene con el tipo correcto.

## El patrón Result de este proyecto

`Result<T, E>` es una interfaz sellada con dos implementaciones: `Ok`, que envuelve un valor de
éxito, y `Err`, que envuelve un error.

Existe como alternativa a lanzar excepciones. En lugar de que un método reviente, devuelve un
objeto que representa el éxito o el fallo, y quien llama decide qué hacer. Es el mismo patrón
de Rust, del `Either` de otros lenguajes, o de devolver `[error, data]` en Go.

Un método que devuelve `Result<List<MobItem>, Exception>` te está diciendo dos cosas: si sale
bien vas a recibir un `Result.Ok` con una `List<MobItem>` adentro, y si sale mal un
`Result.Err` con una `Exception` adentro. En ningún caso lanza.

Para trabajar con el valor primero hay que averiguar cuál de los dos es, y ahí entra el
`instanceof` con patrón que ya viste. `Result.Ok` expone su contenido con `value()` y
`Result.Err` con `error()`, porque son records y esos son los nombres de sus campos.

El código de `ItemServiceImp` ya está escrito con este patrón. Léelo antes de agregar cosas, es
el ejemplo más directo de cómo se consume.
