# TP-AyDS2

**Trabajo Práctico de la materia Análisis y Diseño de Sistemas 2**  


---

## 📌 Descripción

Este proyecto consiste en el desarrollo de un **sistema de mensajería instantánea**, orientado a la práctica y comprensión de conceptos de análisis y diseño de sistemas.  
Implementado completamente en **Java**, permite la gestión de usuarios, contactos y conversaciones, incorporando mecanismos de **persistencia** y opciones de **encriptación/configuración**.

---

## ✨ Características principales

- 🔐 **Registro e inicio de sesión de usuarios**  
  Los usuarios pueden registrarse y autenticarse en el sistema, interactuando con un servidor por medio de una API.

- 👥 **Gestión de contactos**  
  Permite agregar, visualizar y seleccionar contactos para iniciar conversaciones.  
  Se previene la duplicación y se notifican errores ante intentos de agregar contactos existentes.

- 💬 **Sistema de conversaciones**  
  Los usuarios pueden iniciar conversaciones, enviar y recibir mensajes, y visualizar el historial en un formato tipo chat.

- 💾 **Persistencia de datos**  
  La información de usuarios, contactos y conversaciones se guarda en archivos persistentes, soportando distintos formatos: **JSON, XML y TXT**.  
  Al iniciar sesión, los datos se cargan automáticamente o se inicializan si no existen.

- 🔧 **Configuración y encriptación**  
  El usuario puede elegir la técnica de encriptación y definir una clave para proteger su información personal y conversacional.

---


## 🛡️ Redundancia Pasiva y Alta Disponibilidad

El sistema incorpora una arquitectura tolerante a fallos para garantizar alta disponibilidad:

- 🖧 **Varios servidores:** un servidor activo gestiona las solicitudes, mientras que otros quedan en espera (standby).
- 🔍 **Monitorización:** un componente *monitor* realiza chequeos periódicos (ping/echo) sobre el servidor activo.
- 🔁 **Failover automático:** si el servidor activo cae, el monitor promueve automáticamente un servidor standby como nuevo activo.
- 🔄 **Sincronización entre servidores:** los standby se mantienen sincronizados con el activo, garantizando continuidad.
- 📈 **Alta disponibilidad:** asegura la prestación del servicio incluso ante fallas de hardware o red, minimizando interrupciones.

---

## 🛠️ Tecnologías utilizadas

- Java 
- Swing (para UI)
- Jackson (serialización JSON/XML)
- TXT personalizado
- Criptografía simétrica/asimétrica
- Sockets TCP (cliente-servidor)
- Threads y manejo de concurrencia

---
## 🚀 Ejecución

Para utilizar el sistema de mensajería con **redundancia pasiva y alta disponibilidad**, es necesario ejecutar los siguientes componentes, cada uno como un proceso independiente:

---

### 1. 🖥️ Servidor

El sistema soporta múltiples servidores para redundancia.  
Al menos uno debe estar **activo** y los demás funcionarán como **servidores secundarios sincronizados**.

```bash
java -jar Servidor.jar
```
Podés iniciar tantos servidores secundarios como desees (se debe especificar cada puerto en el archivo .properties).

---

### 2. 🧠 Monitor
El monitor es responsable de:
- Verificar la disponibilidad del servidor activo y los secundarios (ping/echo)
- Ejecutar el failover automático si el servidor activo cae

```bash
java -jar Monitor.jar
```
☝️ El monitor debe iniciarse después de que al menos un servidor esté corriendo.

---

### 3. 👤 Cliente
Cada usuario ejecuta un cliente para acceder al sistema, registrarse, iniciar sesión, gestionar contactos y enviar mensajes.

```bash
java -jar Cliente.jar
```
Podés ejecutar múltiples clientes en diferentes máquinas o terminales según sea necesario.

### 📌 Notas
- Asegurate de que los archivos .jar estén generados y disponibles en el mismo directorio, o especificá la ruta completa al ejecutarlos.
- Revisá los archivos de configuración para el servidor y monitor: puertos y estado.
- Orden recomendado de inicio:
  - Servidores
  - Monitor
  - Clientes

