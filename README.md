# Play Book REST API

This is Play scala project for De Gruyter Interview task;
design an API that allows storing and retrieving books.



### Running

You need to download and install sbt for this application to run.

Once you have sbt installed, the following at the command prompt will start up Play in development mode:


``` 
sbt run
```

Play will start up on the HTTP port at <http://localhost:9000/>.
or you can use IntellJ IDEA to run the project.

locally Play version 3.0.4 and Java 11.0.23 we used while developing this project.

### Database and evolutions (Anorm used for database access)
Project is using in memory database H2, and it is using Anorm for database access.
in application.conf there is also configuration for MySQL database, it is workable, but it commented. (also there is  an evolutions/default/1-mysql.sql file for MySQL syntax)

I have added authors as addition to books entity beside it's not required to show the one-to-many relationship.
At the start of the app it is inserting some data into the database. (conf/evolutions/default/1.sql and 2.sql)

(1.sql) - creates 2 tables - book and author with one-to-many relationship between the author and book tables
(2.sql) - some sample data is inserted into the tables (books and authors)

Books table columns:
- id
- title
- subtitle
- ISBN
- published and 
- authorId as foreign key to authors table.

Author table columns:
- id
- name

### Usage



Routes defined in the project are:
```routes
GET /api/books - list all books
GET /api/books/:id - get book by id
POST /api/books - create new book 

GET /api/authors - list all authors
GET /api/authors/:id - get author by id
POST /api/authors - create new author
```

Inside project folder you can find Postman collection file for testing the api with Postman.
(Play De Gruyter.postman_collection.json)

or you can use cUrl to test the api:
```
1. get all books:
curl --location 'http://localhost:9000/api/books'

2. get books by id:
curl --location 'http://localhost:9000/api/books/11'

3. get all authors:
curl --location 'http://localhost:9000/api/authors'

4. get author by id:
curl --location 'http://localhost:9000/api/authors/3'


5. post new author:
curl --location 'http://localhost:9000/api/authors' \
--header 'Content-Type: application/json' \
--data '    {
        "name": "Max Planck"
    }'
    
    
6. post new book:
curl --location 'http://localhost:9000/api/books' \
--header 'Content-Type: application/json' \
--data '{
    "title": "The Origin and Development of the Quantum Theory",
    "ISBN": "0-8108-5420-13",
    "published": "1922-01-10",
    "authorId": 8
    }'
```

Httpie : 
```
http --follow --timeout 3600 GET 'http://localhost:9000/api/books'
http --follow --timeout 3600 GET 'http://localhost:9000/api/authors/'

create author: 
printf '    {
        "name": "Max Planck"
    }'| http  --follow --timeout 3600 POST 'http://localhost:9000/api/authors' \
 Content-Type:'application/json'
 
 create new book:
 
 printf '{
    "title": "The Origin and Development of the Quantum Theory",
    "ISBN": "0-8108-5420-13",
    "published": "1922-01-10",
    "authorId": 8
    }'| http  --follow --timeout 3600 POST 'http://localhost:9000/api/books' \
 Content-Type:'application/json'
 
```

### Running tests
few unit tests are added
```
sbt test
```

### Note
Controllers folder and view folder are added only to show the index page of the beggining of the app, and to list the books at the start of the app.
(after inserting new book we need to refresh the page to see the new book in the list.)

those folders can be removed if not needed together with first line in conf/routes - no impact on api and the functionality of the app as defined in the task



### Desc
String Interpolating Routing DSL is used for api routing
**BookRouter** and **AuthorRouter** are used for routing using sird router
https://www.playframework.com/documentation/3.0.x/ScalaSirdRouter


Dependency injection via Module file inside app folder (Guice)
https://www.playframework.com/documentation/latest/ScalaDependencyInjection


Layered architecture is used for the project

The **BookResourceHandler** and **AuthorResourceHandler** are service classes that handle business logic related to books and authors respectively. 
they are acting like a service layer in the application.
They use the respective **BookRepository** and **AuthorRepository** repository classes to interact with the database and returning Data Transfer Objects (DTOs)
**BookResource** and **AuthorResource** are defined as repositories and are used respectively in BookResourceHandler and AuthorResourceHandler.


###  Possible improvements:
- pagination
- put delete methods
- more and meaningful unit tests and integration tests
- Authentication and Authorization
- Rate Limiting ...