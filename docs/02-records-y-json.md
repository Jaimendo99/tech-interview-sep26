# Records y JSON

## Qué es un record

Un `record` es una clase inmutable de solo datos. Declaras los campos entre paréntesis y Java
genera el resto.

```java
public record Producto(
        String codigo,
        String nombre,
        int stock
) {}
```

Con esas cinco líneas obtienes:

- Un constructor que recibe los campos en el orden declarado, llamado constructor canónico.
- Un método de lectura por cada campo.
- `equals` y `hashCode` que comparan campo por campo.
- Un `toString` legible.

Todos los campos son `final`. No hay setters y no los puedes agregar. Un record creado no se
modifica nunca. Si necesitas una versión distinta, construyes otro objeto.

## Los accesores no llevan get

Esta es la diferencia que más confunde viniendo de otros lenguajes. El método de lectura se
llama igual que el campo, sin prefijo, y lleva paréntesis porque es un método, no una propiedad.

```java
Producto p = new Producto("A-1", "Cemento", 40);

p.nombre()      // "Cemento"   correcto
p.getNombre()   // no existe, error de compilación
p.nombre        // no compila desde fuera de la clase
```

## Crear un record

```java
Producto p = new Producto("A-1", "Cemento", 40);
```

El orden de los argumentos es el orden en que declaraste los campos. Si dos campos son del
mismo tipo y los intercambias, el compilador no dice nada y el bug aparece después. Si agregas
o quitas un campo del record, toda llamada a `new` con la cantidad vieja de argumentos deja de
compilar.

## Imprimir un record

`toString()` sale gratis y muestra todos los campos:

```java
System.out.println(p);
// Producto[codigo=A-1, nombre=Cemento, stock=40]
```

Es la forma más rápida de ver qué trae un objeto realmente.

## De JSON a objetos con Jackson

El proyecto usa Jackson, la librería estándar de JSON en Java. La clase central es
`ObjectMapper`.

```java
ObjectMapper mapper = new ObjectMapper();
```

| Método | Qué hace |
|---|---|
| `mapper.readValue(String json, Class<T> tipo)` | convierte texto JSON en un objeto de ese tipo |
| `mapper.writeValueAsString(Object obj)` | convierte un objeto en texto JSON |

```java
Producto p = mapper.readValue(textoJson, Producto.class);
String json = mapper.writeValueAsString(p);
```

`Producto.class` es un literal de clase. Así le dices a Jackson en qué tipo quieres el
resultado.

Ambos métodos lanzan excepciones que hay que capturar o declarar, ver
[07-excepciones.md](07-excepciones.md).

## Cómo Jackson decide qué va en cada campo

Jackson mapea **por nombre**. Toma cada clave del JSON y busca un campo del record que se llame
igual. El orden en que aparecen las claves en el JSON no importa.

De ahí salen dos comportamientos que conviene tener claros:

**Si el JSON trae una clave que el record no tiene**, Jackson falla. Por defecto lanza
`UnrecognizedPropertyException` con un mensaje del estilo `Unrecognized field "algo"`. No es
un error de compilación, aparece recién al ejecutar.

**Si el record tiene un campo que el JSON no trae**, Jackson no falla. Deja el valor por
defecto: `null` si es un objeto, `0` si es `int`, `false` si es `boolean`. Es un caso peor que
el anterior, porque el programa sigue corriendo con datos incompletos.

Un record anidado o una `List` de records se resuelven solos, siempre que los tipos calcen:

```java
public record Catalogo(int version, List<Producto> productos) {}
```

Jackson va a leer el arreglo JSON `productos` y construir un `Producto` por elemento.

## Correspondencia de tipos

| JSON | Java |
|---|---|
| `"texto"` | `String` |
| `42` | `int` o `long` |
| `3.14` | `double` |
| `true` / `false` | `boolean` |
| `["a", "b"]` | `List<String>` |
| `[{...}, {...}]` | `List<OtroRecord>` |
| `{...}` | otro record o clase |
| `null` | `null` en tipos objeto, error si el destino es un primitivo |

Si el tipo no calza, Jackson lanza `InvalidFormatException` o
`MismatchedInputException` indicando qué campo falló.

## La anotación JsonPropertyOrder

```java
@JsonPropertyOrder({ "id", "nombre", "stock" })
public record Producto(...) {}
```

Esta anotación controla el orden de las claves cuando Jackson **escribe** JSON. Al leer no hace
nada, porque leer va por nombre.

En la práctica sirve como documentación del contrato: deja escrito qué claves espera y produce
este tipo.

## Depurar un mapeo

Cuando algo del JSON no llega como esperas, el paso útil es mirar el texto crudo antes de que
Jackson lo toque. Imprime el body de la respuesta con `System.out.println(...)` y compáralo,
clave por clave, contra los campos del record.

Después imprime el objeto ya construido. Un campo en `null` o en `0` que debería tener valor te
dice que el nombre del campo no coincide con la clave del JSON.
