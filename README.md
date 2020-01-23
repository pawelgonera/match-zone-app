# match-zone-app
Dating app web service.

### Description
This is spring project with angular 8 for frontend, created as dating service. Is using REST API with connection to PostgreSQL database.
Standalone frontend module uses an Angular 8 for retrieving data from endpoints through HTTP connection on 4200 port.

### Technology Stack
* Spring Boot
* Spring Data JPA
* Spring Security
* Spring MVC REST
* JWT
* Maven
* PostgreSQL
* Angular 8
* JUnit
* Lombok
* Bootstrap
* Angular Material

### Functionality 
As dating service, in this app you can register new user and fill your profile with personal data and also upload photo for avatar. 
You can search other registered users, browse their profile and gives them rate star. You can filter the displayed list of users by gender, age,
rating, location and you can sort it.

### How to lunch project
Clone this repo
```
git clone https://github.com/pawelgonera/match-zone-app
```

You must have tomcat server and maven installed, change settings in two files

In maven directory
```
[maven-home-directory]/conf/settings.xml
```
add this in servers section
```
<server>
  <id>TomcatServer</id>
  <username>tomcat</username>
  <password>password</password>
</server>
```

In tomcat directory
```
[tomcat-home-directory]/conf/tomcat-users.xml
```
Replace all tomcat user setting to (remove comment tags)
```
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="tomcat" password="password" roles="manager-gui,manager-script"/>
```

And run server - go to directory where tomcat is installed
```
cd [tomcat-home-directory]/bin/startup
```

Back to main directory of the project and deploy war file on tomcat using maven
```
mvn tomcat7:deploy
```

Now you need to run the front-end, for this you will have to install node.js and npm

Install Angular CLI
```
npm install -g @angular/cli
```

Go to front-end folder in the project
```
cd [your-project-directory]/match-zone-app/src/main/front-end/
```

And install Bootstrap and JQuery
```
npm install bootstrap jquery --save
```

Run Angular client
```
ng serve
```

Go to url
```
http://localhost:4200/
```


#### Coming soon for features and content
* gallery for user profile
* comments and messages in user profile
* private chat
* settings option for logged user
* matching system

### Author
Created by [Paweł Gónera](https://www.linkedin.com/in/pawe%C5%82-g%C3%B3nera-87055aa2/)

