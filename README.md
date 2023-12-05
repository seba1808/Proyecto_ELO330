### Sistema IoT en Aragorn

En este proyecto se diseña un sistema IoT con uso de un Microcontrolador ESP32 y el uso del servidor Aragorn del departamento de Electronica de la Universidad Técnica Federico Santa Maria. En este se genera un codigo en Java el cual genera comunicación remota y monta una pagina Web abierta para ser accedida por cualquiera que cuente con IP y puerto utilizado. 

## Requisitos Previos

# Hardware:

- Contar con un Microcontrolador del tipo ESP32, puede utilizar ESP8266 o versiones actuales de Arduino con modulo WiFi, adaptar el codigo segun esos requermientos.
- Uso de IDE de arduino para subir código a la placa y hacer monitoreo vía comunicación serial.
- Se debe generar los cambios de nombre de WiFi y contraseña respectiva de su red.
- En caso de cambiar de servidor o puerto generar los cambios correspondientes.

- Si no cuenta con el hardware apropiado se puede generar un cliente en lenguaje C o Java que simule este.

# Servidor:
En el caso de trabajar en entorno independiente de manera local o otro servidor:
- JDK para compilar y ejecutar archivos Java.
- GCC para compilar archivos C.
- Usar cualquier tipo puerto disponible

En caso de trabajar en servidor del departamento de Electronica UTFSM aragorn:
- Contar en un directorio todos los archivos y codigos de este Git inclusive .CSS y .HTML.

# Instrucciones de Compilación y Ejecución:
Java:
- Para compilar utilice javac Main.java
- Para ejecutar uttilice java Main

Con esto se monta el servidor en Aragorn, debe luego activar el cliente que envíe los datos, es decir el ESP32 o sustituto.

Para visualizar:
- Ingrese a su navegador de preferencia.
- En la URL ingrese <IP_servidor >: <Puerto_utilizado> en este caso 200.17.1.195:47203 IP de aragorn y puerto libre de este.
- Visualización e interacción en pagina Web.

## Autores
Grupo 1 - ELO330 S2 2023
- Diego Almonacid
- Sebastian Madariaga
- Annette Ramirez
