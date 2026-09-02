# Errores comunes traducidos

Primero ubica en qué etapa falló. Un error de compilación aparece subrayado en el editor y en la
pestaña Build, y el programa nunca arranca. Un error de ejecución aparece en la pestaña Run,
como stack trace, después de que el programa ya estaba corriendo.

Son problemas distintos y se buscan en lugares distintos.

## Errores de compilación

**`cannot find symbol`**

El nombre que escribiste no existe en ese contexto. Puede ser un error de tipeo, un import que
falta, un método que no existe en esa clase, o una variable declarada dentro de otro bloque. El
mensaje incluye `symbol:` y `location:`, que te dicen qué buscaba y dónde.

Si es una clase de otro paquete, `Option+Enter` sobre el nombre ofrece el import.

**`incompatible types: X cannot be converted to Y`**

Estás asignando un valor de un tipo a una variable de otro. Los casos frecuentes: confundir
`int` con `String`, olvidar que un método devuelve `Optional` en vez del valor directo, o
asignar un `List<A>` a un `List<B>`.

**`constructor X in class X cannot be applied to given types`**

Le pasaste al `new` una cantidad o un tipo de argumentos que no coincide con ningún constructor.
El mensaje muestra `required:` con lo que espera y `found:` con lo que le diste. Compáralos de
izquierda a derecha.

Este error también aparece cuando cambias los campos de un record y no actualizas los `new`.

**`missing return statement`**

Hay un camino de ejecución que llega al final del método sin devolver nada. Suele faltar el
`return` de un `else` o del final del método.

**`the switch expression does not cover all possible input values`**

Al `switch` le falta un caso del enum, o le falta `default`. Ver
[05-enums-switch-y-result.md](05-enums-switch-y-result.md).

**`unreported exception X; must be caught or declared to be thrown`**

Llamaste a un método que lanza una excepción checked. Envuélvelo en `try`/`catch` o agrega
`throws` a tu firma. Ver [07-excepciones.md](07-excepciones.md).

**`class X is public, should be declared in a file named X.java`**

El nombre de la clase y el del archivo no coinciden. Renombra uno de los dos.

**`variable X might not have been initialized`**

Declaraste una variable local sin valor y la usas en un camino donde puede no habérselo
asignado. Dale un valor inicial en la declaración.

**`cannot assign a value to final variable X`**

Estás reasignando algo declarado `final`. O quitas el `final`, o usas otra variable.

**`non-static method cannot be referenced from a static context`**

Desde `main`, que es `static`, estás llamando a un método de instancia sin tener una instancia.
Crea el objeto primero.

**`incompatible types: bad return type in lambda expression`**

La lambda devuelve algo distinto de lo que espera el método. Revisa la firma de la interfaz
funcional que estás implementando.

## Errores de ejecución

**`NullPointerException: Cannot invoke "..." because "..." is null`**

Java 17 te dice exactamente qué expresión valía `null`. Léela, es literal. Las dos causas
típicas son un objeto que se construyó pasando `null` a su constructor, y un método que
devolvió `null` porque no encontró lo que buscaba.

Ojo con la ubicación: el NPE explota donde se usa el valor, no donde se creó. El origen del
problema puede estar muchas líneas antes.

**`ConnectException: Connection refused`**

Nadie está escuchando en esa dirección y puerto. El servidor no está corriendo, el puerto está
mal, o la dirección apunta a otro lado. Pruébala en el navegador antes de seguir buscando en el
código.

**`UnrecognizedPropertyException: Unrecognized field "algo"`**

El JSON trae una clave que tu record no declara. Jackson te da el nombre exacto entre comillas.
Ver [02-records-y-json.md](02-records-y-json.md).

**`MismatchedInputException` o `InvalidFormatException`**

El tipo declarado no calza con el valor del JSON. Por ejemplo un campo `int` recibiendo texto,
o un campo simple recibiendo un objeto. El mensaje indica la ruta del campo problemático.

**`InvalidDefinitionException: cannot construct instance`**

Jackson no sabe cómo crear ese tipo. Con records normalmente no pasa. Si aparece, revisa que
estés apuntando al tipo correcto en `readValue`.

**`UnsupportedOperationException`**

Intentaste modificar una colección inmutable. Casi siempre es una lista creada con `List.of(...)`
o devuelta por `stream().toList()`. Haz una copia con `new ArrayList<>(lista)` y modifica esa.

**`IndexOutOfBoundsException: Index 5 out of bounds for length 5`**

Índice fuera de rango. Los válidos van de `0` a `size() - 1`. Un `<=` donde debía ir `<` en la
condición del bucle es la causa habitual.

**`ConcurrentModificationException`**

Agregaste o quitaste elementos de una lista mientras la recorrías con for-each. Usa `removeIf`,
recorre con índices, o recorre una copia.

**`IllegalArgumentException` desde `URI.create`**

La cadena no es una URL válida. Un string vacío califica. Imprímela antes de usarla y mira qué
tiene realmente.

**`NumberFormatException`**

`Integer.parseInt` recibió algo que no es un número. Revisa espacios en blanco y cadenas vacías.

## El programa corre pero hace algo raro

Aquí no hay mensaje de error, así que toca depurar.

Imprime. `System.out.println` en los puntos clave sigue siendo la forma más rápida de ver qué
está pasando. Imprime objetos enteros, no solo campos sueltos, que los records ya traen un
`toString()` legible.

Verifica el tamaño de las colecciones. Una lista vacía hace que los bucles no ejecuten ninguna
iteración y el programa quede en silencio, sin fallar.

Confirma que estás corriendo el código nuevo. Si el proceso anterior sigue vivo, detenlo. Es
más común de lo que parece perder diez minutos ahí.

Si quieres usar el depurador de IntelliJ, un click en el margen izquierdo pone un breakpoint, y
el ícono del insecto corre el programa deteniéndose ahí. `F8` avanza una línea, `F9` continúa
hasta el siguiente breakpoint. La ventana de variables te muestra todo lo que hay en memoria en
ese instante.
