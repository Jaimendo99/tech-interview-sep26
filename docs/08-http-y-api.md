# HTTP y consumo de la API

## Qué ya está resuelto y qué no

El código HTTP del proyecto ya está escrito. `HttpItemClientImp` sabe hacer el GET y convertir
la respuesta en objetos. `MobWebhookNotifier` sabe serializar un objeto y hacer el POST. No
tienes que escribir peticiones HTTP.

Lo que esas clases no traen son las direcciones. Las URLs entran como texto, y salen de la
información que te entregaron para la prueba. Búscalas ahí.

Antes de escribir una URL en el código, ábrela en el navegador y confirma que responde lo que
esperas. Ahorra media hora de depurar el lugar equivocado.

## Recordatorio de HTTP

Una petición HTTP tiene un método, una URL, encabezados y a veces un cuerpo.

`GET` pide datos y no lleva cuerpo. `POST` envía datos en el cuerpo.

El encabezado `Content-Type: application/json` le avisa al servidor que el cuerpo es JSON.

La respuesta trae un código de estado:

| Código | Significa |
|---|---|
| 200 | salió bien |
| 201 | se creó el recurso |
| 400 | la petición está mal armada, revisa el cuerpo o el formato |
| 404 | la ruta no existe, revisa la URL |
| 405 | la ruta existe pero no acepta ese método, por ejemplo POST donde solo hay GET |
| 500 | el servidor falló procesando la petición |

Cuando el problema es un 404, casi siempre es una URL mal concatenada: una barra de más, una
barra de menos, o un segmento equivocado.

## La API de HttpClient

Está en el paquete `java.net.http` y viene con el JDK, sin dependencias.

```java
HttpClient client = HttpClient.newHttpClient();
```

Construir la petición:

```java
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();
```

```java
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
        .build();
```

Es el patrón builder: cada método devuelve el mismo builder para poder encadenar, y `build()`
cierra y produce el objeto.

Enviar y leer:

```java
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

int codigo = response.statusCode();
String cuerpo = response.body();
```

`send` es bloqueante y lanza `IOException` e `InterruptedException`, las dos checked.

`URI.create(...)` lanza `IllegalArgumentException` si la cadena no es una URL válida. Un string
vacío entra en esa categoría.

## Cómo se arma la URL final

En este proyecto las URLs se construyen concatenando texto. Abre `HttpItemClientImp.getItems()`
y mira la línea donde se forma `url`. Ahí ves qué se le pega a la dirección base y, por lo
tanto, con qué tiene o no tiene que terminar el valor que le pases al constructor.

Es un detalle de una barra que decide entre una respuesta correcta y un 404.

## Inspeccionar un endpoint

Antes de escribir código, mira qué devuelve el servidor de verdad.

Desde el navegador, pega la URL en la barra de direcciones. Para un GET que devuelve JSON, el
navegador te lo muestra directamente. Firefox y Chrome lo formatean y lo hacen navegable.

Desde la terminal, con curl:

```
curl http://ejemplo/ruta
curl -i http://ejemplo/ruta
```

`-i` agrega los encabezados y el código de estado a la salida.

Para un POST de prueba:

```
curl -X POST -H "Content-Type: application/json" -d '{"a":1}' http://ejemplo/ruta
```

Desde el propio código Java, imprime el cuerpo crudo antes de convertirlo:

```java
System.out.println(response.body());
```

Esa línea contesta de una sola vez si el servidor respondió, si respondió lo que creías, y qué
claves trae exactamente el JSON.

## Verificar que el POST llegó

`MobWebhookNotifier` envía la petición pero no revisa el código de estado de la respuesta.
Desde el punto de vista del programa, un webhook rechazado con 400 se ve igual que uno
aceptado con 200.

Si necesitas confirmar que el envío funcionó, tienes tres caminos: mirar los logs del servidor,
consultar el endpoint que muestre lo recibido si es que existe, o imprimir tú el
`statusCode()` de la respuesta.

## Qué se manda en el cuerpo

El notificador recibe un objeto y lo serializa completo con Jackson antes de enviarlo. Lo que
llegue al servidor depende directamente de qué objeto le pases. Una lista de records se
convierte en un arreglo JSON de objetos, con una clave por cada campo del record.

Si el record no tiene todos los campos que el receptor espera, el JSON sale incompleto y el
servidor probablemente responda 400.
