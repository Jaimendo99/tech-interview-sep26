# Listas y colecciones

## List es una interfaz, ArrayList es una implementación

```java
List<String> nombres = new ArrayList<>();
```

`List<String>` es el tipo declarado, un contrato. `ArrayList` es la clase concreta que hace el
trabajo. La convención en Java es declarar por la interfaz y construir con la implementación.

El `<>` vacío del lado derecho se llama diamante. El compilador deduce el tipo del lado
izquierdo, no hace falta repetirlo.

Una `List` solo guarda objetos, nunca primitivos. `List<int>` no existe, se escribe
`List<Integer>`.

## Las tres formas de crear una lista

```java
List<String> a = new ArrayList<>();              // vacía y modificable
List<String> b = new ArrayList<>(otraLista);     // copia modificable de otra lista
List<String> c = List.of("x", "y", "z");         // inmutable
```

`List.of(...)` devuelve una lista **inmutable**. Llamar a `add`, `remove`, `set` o `clear`
sobre ella lanza `UnsupportedOperationException` al ejecutar. `List.of()` sin argumentos crea
una lista inmutable vacía, útil cuando un constructor pide una lista pero no tienes datos
todavía.

La segunda forma, `new ArrayList<>(otraLista)`, es la manera de obtener una copia que sí puedes
modificar. Es una copia superficial: la lista nueva es independiente, pero los objetos que
contiene son los mismos. Como los `record` son inmutables, en la práctica eso no genera
problemas aquí.

## Métodos de List

| Método | Devuelve | Qué hace |
|---|---|---|
| `size()` | `int` | cantidad de elementos |
| `isEmpty()` | `boolean` | true si no hay elementos |
| `get(int i)` | el elemento | lee la posición `i`, empezando en 0 |
| `set(int i, E x)` | el elemento anterior | reemplaza la posición `i` |
| `add(E x)` | `boolean` | agrega al final |
| `add(int i, E x)` | `void` | inserta en la posición `i` |
| `remove(int i)` | el elemento quitado | quita por posición |
| `remove(Object x)` | `boolean` | quita la primera coincidencia por `equals` |
| `removeIf(Predicate)` | `boolean` | quita todos los que cumplan la condición |
| `contains(Object x)` | `boolean` | busca por `equals` |
| `indexOf(Object x)` | `int` | posición o `-1` si no está |
| `clear()` | `void` | vacía la lista |
| `addAll(Collection)` | `boolean` | agrega todos los elementos de otra colección |
| `sort(Comparator)` | `void` | ordena la lista en el sitio |
| `stream()` | `Stream<E>` | abre la API de streams |

`get` con un índice fuera de rango lanza `IndexOutOfBoundsException`. Los índices válidos van
de `0` a `size() - 1`.

## Recorrer una lista

Bucle clásico, cuando necesitas el índice:

```java
for (int i = 0; i < lista.size(); i++) {
    String actual = lista.get(i);
}
```

Bucle for-each, cuando solo necesitas los elementos:

```java
for (String actual : lista) {
    System.out.println(actual);
}
```

El for-each es más limpio pero no te da el índice ni te deja usar `set`. Y hay una regla
importante: no puedes agregar ni quitar elementos de una lista mientras la recorres con
for-each. Si lo haces, lanza `ConcurrentModificationException`. Para eso está `removeIf`, o
recorrer con índices hacia atrás.

## Streams, en resumen

Los streams son la API funcional de colecciones. Aparecen en este proyecto, así que conviene
poder leerlos aunque no los uses.

```java
lista.stream()
     .filter(x -> x.length() > 3)      // se queda con los que cumplen
     .map(x -> x.toUpperCase())        // transforma cada elemento
     .findFirst()                      // devuelve un Optional con el primero
     .orElse(null);                    // saca el valor, o null si no había
```

`x -> expresion` es una lambda, una función anónima. `x` es el parámetro y su tipo lo deduce
el compilador.

| Operación | Devuelve |
|---|---|
| `filter(Predicate)` | stream con los que cumplen |
| `map(Function)` | stream con cada elemento transformado |
| `sorted(Comparator)` | stream ordenado |
| `findFirst()` | `Optional<E>` |
| `anyMatch(Predicate)` | `boolean` |
| `count()` | `long` |
| `toList()` | `List<E>` inmutable |
| `collect(Collectors.toList())` | `List<E>` |

`Optional<E>` es un contenedor que puede tener valor o no. `orElse(otro)` saca el valor o
devuelve el reemplazo si está vacío. Existe para evitar devolver `null`, aunque en este
proyecto verás las dos convenciones mezcladas.

## Map, por si lo necesitas

```java
Map<String, Producto> porCodigo = new HashMap<>();
porCodigo.put("A-1", producto);
Producto p = porCodigo.get("A-1");     // null si la clave no existe
boolean existe = porCodigo.containsKey("A-1");
```

| Método | Devuelve |
|---|---|
| `put(K, V)` | el valor anterior o `null` |
| `get(K)` | el valor o `null` |
| `getOrDefault(K, V)` | el valor o el reemplazo |
| `containsKey(K)` | `boolean` |
| `remove(K)` | el valor quitado |
| `keySet()` | `Set<K>` |
| `values()` | `Collection<V>` |
| `entrySet()` | `Set<Map.Entry<K,V>>`, para recorrer clave y valor a la vez |
