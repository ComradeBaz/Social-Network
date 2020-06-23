# Social-Network
Social Network Implementation

**Introduction**

>This is an implementation of a social network that allows users register with a network, post to a timeline, add friends, send messages and add group events that other users can sign up to attend.
>A separate Event Server (sse) that is an implementation of JAX RS RESTful Web Services which dispatches events that are handled by Social-Network

***Technologies used***
- Spring Boot 2
- Java 1.8
- Tomcat 9.0.26
- Spring Security
- Hibernate 5.3.7
- MySql 5.6
- ActiveMq 5.11.1(J.M.S.)
- JSON
- Mojarra 2.2.10
- PrimeFaces 6.2
- Log4j 1.2.17
- Maven 3.6.3
- jQuery 3.3.1
- Javascript
- CSS
- Apache CXF 3
- Bootstrap 3.3.7

***Implementation***
- The application is developed using Spring Boot 2.1.2.
- Views are rendered on .xhtml pages using predominantly JSF tags. PrimeFaces components are used where standard tags did not provide   needed functionality.
- Pages are styled with CSS.
- Ajax requests and Javascript are used to update views dynamically based on user actions.
- A Javascript Server Sent Event handler listens for events and renders changes to the view in response to changes in state of the application.
- Pages are built on a Bootstrap template and render for desktop and mobile devices. At present views are optimised for viewing from a desktop with Google
Chrome.
- User requests are handled by Controllers decorated with the @Component annotation.
- Business logic is delegated to @Service annotated beans.
- CRUD operations on domain entities are handled by @Repository interfaces using “out of the box”, and custom queries.
- Database entities are mapped using @Entity beans.
- A JMS implementation dispatches messages to a topic from where they are consumed by a listener running as part of the SSE server implementation (see below).

***Security***
- Authentication/login is handled by Spring Security
- Registration and login is performed on custom .xhtml pages.
- A PBKDF2 Hasher is wired to implement password hashing.
