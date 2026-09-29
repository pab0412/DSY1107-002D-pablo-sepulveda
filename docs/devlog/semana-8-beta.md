Mensajería Asíncronas:

&#x20;- Son tareas aparte que se comunican entre si pero de manera asíncrona, sin esperar respuesta de la persona o del servidor.

&#x20;- Funciona como un intermediario entre el un productor y el consumidor de servicios, en donde almacena un mensaje hasta que el consumidor este    listo para procesarlo.

&#x20;- JavaScript Promesas = Programación asíncrona

&#x20;- Se hace mediante un bróker para que el mensaje sea recibido y procesado al consumidor

&#x20;- Análisis de procesos el servicio no se cae simplemente esta procesando la información de manera independiente.



Productor: Crea y envia mensajes al bróker, no conocer y ni se preocupa que le pasara al mensaje

Consumidor: Recibe y procesa los mensajes de la cola, puede ser un o mas consumidores

Mensaje: Paquete de datos que contiene



* Headers/Metadata: Propiedades como tipo/timestamp
* Body: El contenido real del mensaje



Bróker: El servidor/servicio intermediario que entrega, almacena y enruta los mensajes.

Cola: Una estructura de datos tipo FIFO que almacena de manera temporal los mensajes

