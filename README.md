# Film Repository

Sample application developed with Spring Boot with a login pogin and upon
successful login, you redirected to the homepage where you can create new films,
view, update or delete existing films.

## Install & Run

Before you run the project, you must first initialize the PostgreSQL database.

### 1. Initialize the Database

Assuming PostgreSQL is running on your system. If not, you can run it with

`# systemctl start postgresql`

Login into user postgres

`# su - postgres`

Login into PostgreSQL interactive terminal

`$ psql`

Create the database

`postgres=# create database karsldb;`

Create the user

`postgres=# create user karsl with password 'karsl';`

Give all rights to the user on the database

`postgres=# grant all privileges on database karsldb to karsl;`

We're done with the postgres user. Logout from the PostgreSQL interactive terminal

`postgres=# exit`

Logout from login shell

`# exit`

Log into to PostgreSQL interactive terminal on the database

`$ psql -U karsl -d karsldb`

Set search path to the schema.

`karsldb=>set search_path to filmrepository;`

Execute schema script

`karsldb=>\i schema-postgresql.sql`

Execute data script

`karsldb=>\i data-postgresql.sql`

### 1.1 Troubleshooting

If you can't log into the newly created user, try creating a new user with your
username (`echo $USER`).

### 2. Run the Project

You can use Maven wrapper to run the project.

`./mvnw spring-boot:run`

Then visit 'localhost:8080' with your browser.

## Diagrams

You can find the ER diagram in the directory 'diagrams' with name 'er.pdf'.

### Build the ER Diagram

Assuming you have `latexmk`, `pdflatex` and required TeX packages installed, just run

`latexmk -pdf diagrams/er.tex`

## Notes

To build and execute OpenJDK has been  used. Web interface has been tested with Chromium 87.