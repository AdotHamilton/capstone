# myGarage 🚗
#### The networking and business site for the automotive enthusiast.

myGarage is made by enthusiasts, for enthusiasts.


![GitHub](https://img.shields.io/github/license/AdotHamilton/capstone?style=flat-square) **Version 1.0.0**

## Demo: 


![ProjectDemo](https://user-images.githubusercontent.com/76780774/165186819-cbac6659-8803-4bfc-930e-aeb042464781.gif)

## Codebase:
### Front End: React
The client view of myGarage is powered by React.JS
 - utilizes the **Axios** node module to request page data from the backend. 
 - utilizes **@Reach/Router** for front-end routing
 - utilizes **Material UI** for some UI components and Icons.
 - implements **Google Maps Javascript API** via the official Google Maps node module
 - App-level state management with **React-Redux**
### Backend: Spring Boot REST API
Backend management and database interaction is managed by Spring Boot.

Features:  
 - **Bcrypt** for password encryption
 - Apache **Tomcat** Web Servlet
 - Spring Data **JPA** and Hibernate for database queries
 - Paginated Requests with JPARepository
 - Controllers are annotated by **@RestController**
 - Responses are handled with **ResponseEntity**
 - Custom file upload utility

### Database Schema: 
------------------------------------------------

![ER_Diagram](https://user-images.githubusercontent.com/76780774/165187207-9898e20e-11e2-48cc-aa2e-c879c5b00d10.PNG)

