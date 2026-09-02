# Algoritmos de ordenamiento

Este archivo cubre dos cosas: cómo se compara en Java, y cómo funcionan los algoritmos de
ordenamiento clásicos. Los algoritmos van en pseudocódigo a propósito. La idea es que entiendas
el mecanismo y lo escribas tú en Java.

## Comparar es la base de todo

Cualquier ordenamiento necesita responder una pregunta: dados dos elementos, cuál va primero.
En Java esa pregunta se responde con un `int` cuyo signo es lo único que importa.

### compareTo

`String` implementa `compareTo`, que compara en orden lexicográfico:

```java
int r = a.compareTo(b);
```

| Signo de `r` | Significa |
|---|---|
| negativo | `a` va antes que `b` |
| cero | son iguales para efectos de orden |
| positivo | `a` va después que `b` |

El valor exacto no importa, solo el signo. Nunca compares el resultado contra un número
específico, compáralo contra cero.

```java
if (a.compareTo(b) > 0) { }     // a va después que b
if (a.compareTo(b) < 0) { }     // a va antes que b
```

Dos detalles del orden lexicográfico de Java. Compara por valor Unicode, así que todas las
mayúsculas van antes que todas las minúsculas: `"Zeta"` va antes que `"alfa"`. Y no entiende
tildes ni la letra ñ, que quedan después de la z. Si necesitas ignorar mayúsculas existe
`compareToIgnoreCase`.

Los números también se comparan así:

```java
Integer.compare(a, b)      // para int
Double.compare(a, b)       // para double
```

Nunca uses `a - b` para comparar enteros, se desborda con valores grandes.

### Comparator

Un `Comparator<T>` es un objeto que sabe comparar dos elementos de tipo `T`. Su único método es:

```java
int compare(T o1, T o2)
```

Con el mismo contrato de signo. Puedes construirlo con una lambda o con las fábricas de la
clase:

| Expresión | Qué produce |
|---|---|
| `(a, b) -> ...` | un comparador escrito a mano |
| `Comparator.naturalOrder()` | el orden natural del tipo, usa su `compareTo` |
| `Comparator.reverseOrder()` | el orden natural invertido |
| `Comparator.comparing(f)` | compara según lo que devuelva la función `f` |
| `unComparador.reversed()` | invierte cualquier comparador |
| `unComparador.thenComparing(f)` | desempata con un segundo criterio |

`Comparator.comparing` recibe una función que extrae la clave de comparación. Se escribe con
lambda o con referencia a método:

```java
Comparator.comparing(p -> p.nombre())     // lambda
Comparator.comparing(Producto::nombre)    // referencia a método, equivalente
```

`Clase::metodo` es una referencia a método. Es azúcar sintáctico para una lambda que solo
llama a ese método.

### Ordenar con la librería estándar

| Llamada | Efecto |
|---|---|
| `lista.sort(comparador)` | ordena la lista en el sitio |
| `Collections.sort(lista)` | ordena en el sitio usando el orden natural |
| `Collections.sort(lista, comparador)` | igual, con comparador |
| `lista.stream().sorted(comparador).toList()` | devuelve una lista nueva ordenada |

Las dos primeras modifican la lista original, así que fallan con
`UnsupportedOperationException` si la lista es inmutable, por ejemplo una creada con
`List.of(...)` o devuelta por `stream().toList()`.

`Collections` está en `java.util.Collections` y `Comparator` en `java.util.Comparator`.

Internamente Java usa TimSort para objetos, un híbrido de merge sort e insertion sort. Es
estable y su peor caso es O(n log n).

## Los algoritmos clásicos

### Vocabulario

**Complejidad temporal.** Cuántas comparaciones hace el algoritmo según el tamaño de la
entrada. O(n²) significa que si duplicas los elementos, el trabajo se cuadruplica. O(n log n)
crece mucho más despacio.

**En el sitio.** El algoritmo reordena la estructura original sin crear otra. Usa memoria
constante.

**Estable.** Si dos elementos son iguales según el criterio de orden, quedan en el mismo orden
relativo que tenían antes. Importa cuando ordenas por un campo y ya venía ordenado por otro.

### Selection sort

La idea: recorre la lista buscando el elemento más pequeño de lo que falta por ordenar y lo
pone en la posición actual.

```
para i desde 0 hasta n-1:
    minimo = i
    para j desde i+1 hasta n-1:
        si elemento[j] < elemento[minimo]:
            minimo = j
    intercambiar elemento[i] con elemento[minimo]
```

Siempre hace la misma cantidad de comparaciones, esté ordenado o no. Hace pocos intercambios,
como máximo uno por posición.

O(n²) en todos los casos. En el sitio. No es estable en su versión con intercambio.

### Bubble sort

La idea: recorre la lista comparando cada par de vecinos y los intercambia si están en el orden
equivocado. Repite hasta que una pasada completa no haga ningún intercambio.

```
repetir:
    huboIntercambio = falso
    para i desde 0 hasta n-2:
        si elemento[i] > elemento[i+1]:
            intercambiar elemento[i] con elemento[i+1]
            huboIntercambio = verdadero
mientras huboIntercambio
```

Con la bandera de intercambio detecta una lista ya ordenada en una sola pasada, O(n). En el
peor caso es O(n²) y hace muchos más intercambios que selection sort.

O(n²) peor caso, O(n) mejor caso. En el sitio. Estable.

### Insertion sort

La idea: toma cada elemento y lo inserta en su lugar dentro de la parte que ya está ordenada,
corriendo hacia la derecha los que sean mayores. Es lo que hace la gente al ordenar cartas en
la mano.

```
para i desde 1 hasta n-1:
    actual = elemento[i]
    j = i - 1
    mientras j >= 0 y elemento[j] > actual:
        elemento[j+1] = elemento[j]
        j = j - 1
    elemento[j+1] = actual
```

Es el más rápido de los tres en listas pequeñas o casi ordenadas. Por eso las librerías reales
lo usan para los tramos cortos dentro de algoritmos más grandes.

O(n²) peor caso, O(n) si ya está casi ordenada. En el sitio. Estable.

### Merge sort

La idea: divide la lista a la mitad, ordena cada mitad por recursión, y luego mezcla las dos
mitades ordenadas recorriéndolas en paralelo y tomando siempre el menor de los dos frentes.

```
ordenar(lista):
    si tamaño(lista) <= 1:
        devolver lista
    mitad = tamaño(lista) / 2
    izquierda = ordenar(lista[0..mitad])
    derecha   = ordenar(lista[mitad..fin])
    devolver mezclar(izquierda, derecha)

mezclar(a, b):
    resultado = lista vacía
    i = 0, j = 0
    mientras i < tamaño(a) y j < tamaño(b):
        si a[i] <= b[j]:
            agregar a[i] a resultado; i = i + 1
        sino:
            agregar b[j] a resultado; j = j + 1
    agregar lo que quede de a y de b a resultado
    devolver resultado
```

O(n log n) garantizado en todos los casos. Necesita memoria extra proporcional a n. Estable, y
esa es la razón de que Java lo prefiera para ordenar objetos.

### Quicksort

La idea: elige un elemento como pivote, reacomoda la lista dejando a la izquierda los menores
al pivote y a la derecha los mayores, y aplica lo mismo a cada lado.

```
ordenar(lista, inicio, fin):
    si inicio >= fin:
        terminar
    p = particionar(lista, inicio, fin)
    ordenar(lista, inicio, p-1)
    ordenar(lista, p+1, fin)
```

O(n log n) en promedio, O(n²) si el pivote se elige mal de forma sistemática, por ejemplo
tomando siempre el primer elemento en una lista ya ordenada. En el sitio. No es estable.

### Cuál usar

| Algoritmo | Peor caso | Estable | Memoria extra |
|---|---|---|---|
| Selection | O(n²) | no | no |
| Bubble | O(n²) | sí | no |
| Insertion | O(n²) | sí | no |
| Merge | O(n log n) | sí | sí |
| Quick | O(n²) | no | no |
| `Collections.sort` | O(n log n) | sí | sí |

En código de producción se usa la librería estándar. Escribir el algoritmo a mano tiene sentido
cuando el punto es demostrar que entiendes el mecanismo.

## Intercambiar dos posiciones de una lista

Java no tiene desestructuración como `a, b = b, a` de Python. Hay que usar una variable
temporal, o `Collections.swap(lista, i, j)`, que hace exactamente eso.
