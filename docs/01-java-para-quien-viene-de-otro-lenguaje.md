# Java para quien viene de otro lenguaje

## Cómo se organiza el código

Java obliga a que la estructura de carpetas y el código coincidan. No es una convención, el
compilador lo exige.

Una clase pública tiene que vivir en un archivo con exactamente su nombre. La clase `MobItem`
va en `MobItem.java`. Si no coinciden, no compila.

La primera línea de todo archivo declara su paquete, y el paquete tiene que reflejar la ruta
de carpetas desde `src/main/java`:

```java
// archivo: src/main/java/com/zonaconstru/models/MobItem.java
package com.zonaconstru.models;
```

Para usar una clase de otro paquete tienes que importarla:

```java
import com.zonaconstru.models.MobItem;
import java.util.List;
```

Las clases del mismo paquete no necesitan import. Las de `java.lang` (`String`, `Integer`,
`Exception`, `System`, `Thread`) tampoco.

En IntelliJ, `Alt+Enter` sobre un nombre en rojo ofrece agregar el import automáticamente.

## El punto de entrada

Un programa Java arranca en un método con esta firma exacta:

```java
public static void main(String[] args) { }
```

Si cambias algo de esa firma deja de ser el punto de entrada.

## Tipado estático

Toda variable declara su tipo, y ese tipo no cambia nunca.

```java
int cantidad = 5;
String nombre = "hola";
List<String> nombres = new ArrayList<>();
```

Desde Java 10 puedes usar `var` en variables locales. El tipo lo deduce el compilador y sigue
siendo fijo, `var` no es tipado dinámico.

```java
var cantidad = 5;              // es int
var nombres = new ArrayList<String>();
```

`var` solo funciona en variables locales dentro de un método. No en campos de clase, ni en
parámetros, ni en tipos de retorno.

## Primitivos y objetos

Java tiene dos familias de tipos, y la diferencia importa.

Los primitivos se escriben en minúscula y guardan un valor directo. Nunca pueden ser `null`.

| Primitivo | Qué guarda | Valor por defecto |
|---|---|---|
| `int` | entero de 32 bits | `0` |
| `long` | entero de 64 bits | `0L` |
| `double` | decimal | `0.0` |
| `boolean` | `true` o `false` | `false` |
| `char` | un carácter | espacio vacío |

Todo lo demás es un objeto, se escribe con mayúscula inicial y puede ser `null`: `String`,
`List`, `Integer`, `Exception`, y cualquier clase que escribas tú.

Cada primitivo tiene su versión objeto: `int` / `Integer`, `double` / `Double`,
`boolean` / `Boolean`. Java convierte entre las dos automáticamente. Lo relevante es que un
`Integer` puede ser `null` y un `int` no.

## null

`null` es la ausencia de objeto. Llamar a cualquier método sobre `null` lanza
`NullPointerException` en tiempo de ejecución.

Java no tiene tipos que se declaren como "puede ser null" o "no puede ser null" como
TypeScript o Kotlin. Cualquier variable de tipo objeto puede valer `null` y el compilador no
te avisa. Esto compila sin una sola advertencia y explota al ejecutarse:

```java
String texto = null;
int largo = texto.length();   // NullPointerException aquí
```

Cuando un método puede devolver `null`, revísalo antes de usarlo:

```java
if (resultado != null) {
    // usarlo
}
```

## Igualdad

Este es el error clásico de quien llega de otro lenguaje.

`==` compara identidad. En primitivos compara el valor. En objetos compara si son literalmente
el mismo objeto en memoria, no si tienen el mismo contenido.

`.equals(...)` compara contenido, y cada clase define qué significa eso.

```java
int a = 3, b = 3;
a == b                      // true, son primitivos

String x = "hola";
String y = new String("hola");
x == y                      // false, son dos objetos distintos
x.equals(y)                 // true, mismo contenido
```

Para comparar strings usa siempre `.equals(...)`. Con literales cortos `==` a veces da `true`
por una optimización del compilador, y eso hace que el bug aparezca solo a veces.

Si el lado izquierdo puede ser `null`, invierte el orden o usa `Objects.equals(a, b)`:

```java
"esperado".equals(valor)          // seguro aunque valor sea null
Objects.equals(valor, "esperado") // import java.util.Objects
```

## Strings

`String` es inmutable. Ningún método modifica el string original, todos devuelven uno nuevo.

```java
String s = "hola";
s.toUpperCase();          // no cambia s, el resultado se pierde
s = s.toUpperCase();      // ahora sí
```

Métodos que vas a usar:

| Método | Devuelve |
|---|---|
| `length()` | `int`, cantidad de caracteres |
| `equals(Object)` | `boolean`, igualdad de contenido |
| `equalsIgnoreCase(String)` | `boolean`, ignora mayúsculas |
| `compareTo(String)` | `int`, ver [04-algoritmos-de-ordenamiento.md](04-algoritmos-de-ordenamiento.md) |
| `isEmpty()` | `boolean`, true si tiene largo cero |
| `isBlank()` | `boolean`, true si está vacío o solo tiene espacios |
| `contains(CharSequence)` | `boolean` |
| `trim()` | `String` sin espacios en los extremos |
| `substring(int)` y `substring(int, int)` | `String` |
| `split(String)` | arreglo de `String`, el argumento es una expresión regular |
| `String.valueOf(x)` | `String` desde cualquier valor |

Concatenar con `+` funciona y convierte el otro operando a texto automáticamente:

```java
String url = base + "api/mobs";
System.out.println("cantidad: " + items.size());
```

Java no tiene interpolación de strings. Si quieres formato, usa `String.format`, que funciona
como `printf` de C:

```java
String linea = String.format("%s tiene %d items", nombre, cantidad);
```

Java 15 en adelante permite bloques de texto con tres comillas dobles, útil para JSON literal:

```java
String json = """
        {"id": "1"}
        """;
```

## Imprimir en consola

```java
System.out.println(valor);   // imprime y salta de línea
System.out.print(valor);     // sin salto
System.err.println(valor);   // sale en rojo, para errores
```

`println` acepta cualquier tipo y llama internamente a `toString()` del objeto. Si imprimes un
objeto de una clase que no define `toString()`, vas a ver algo como
`com.ejemplo.Cosa@1b6d3586`, que es el nombre de la clase y un número de identidad. Los
`record` sí traen un `toString()` legible de fábrica, ver
[02-records-y-json.md](02-records-y-json.md).

## final

`final` en una variable significa que no se puede reasignar después de darle valor.

```java
private final List<String> items;   // campo, se asigna en el constructor
final int limite = 10;              // variable local
```

No hace inmutable al objeto, solo prohíbe apuntar la variable a otra cosa. Una `List` marcada
`final` sigue aceptando `add`.

## Detalles de sintaxis que muerden

Cada sentencia termina en punto y coma. Los bloques van entre llaves.

Las condiciones de `if` y `while` tienen que ser `boolean`. Java no tiene valores "truthy". Esto
no compila:

```java
if (lista) { }              // error
if (!lista.isEmpty()) { }   // así
```

Los comentarios son `//` de línea y `/* */` de bloque.

Los nombres van en `camelCase` para métodos y variables, `PascalCase` para clases,
`MAYUSCULAS_CON_GUION_BAJO` para constantes.

## Tabla de equivalencias

| Concepto | JavaScript | Python | Java |
|---|---|---|---|
| Declarar variable | `let x = 1` | `x = 1` | `int x = 1;` |
| Lista vacía | `[]` | `[]` | `new ArrayList<>()` |
| Largo de lista | `arr.length` | `len(l)` | `list.size()` |
| Largo de string | `s.length` | `len(s)` | `s.length()` |
| Agregar a lista | `arr.push(x)` | `l.append(x)` | `list.add(x)` |
| Leer posición | `arr[i]` | `l[i]` | `list.get(i)` |
| Escribir posición | `arr[i] = x` | `l[i] = x` | `list.set(i, x)` |
| Recorrer | `for (const x of arr)` | `for x in l:` | `for (String x : list)` |
| Comparar strings | `a === b` | `a == b` | `a.equals(b)` |
| Ausencia de valor | `null` o `undefined` | `None` | `null` |
| Imprimir | `console.log(x)` | `print(x)` | `System.out.println(x)` |
| Diccionario | `{}` o `Map` | `{}` | `new HashMap<>()` |
| Clase | `class A {}` | `class A:` | `public class A {}` |
| Crear objeto | `new A()` | `A()` | `new A()` |
| Manejo de error | `try/catch` | `try/except` | `try/catch` |
