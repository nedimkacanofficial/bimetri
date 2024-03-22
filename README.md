# Bimetri Simple School App Documentation

This project is a student management project repository. It is a basic application using spring boot for students and courses.





## Run Project

Clone project

```bash
  git clone https://github.com/nedimkacanofficial/bimetri.git
```

Run containers for the PostgreSQL database

```bash
  docker-compose -f docker_compose.yml up -d
```

After running the Spring Boot project, the Swagger interface address will be ready for testing at

```bash
  http://localhost:8080/bimetri-ui
```

After starting the project, you need to add a default data. To do this, simply run the default-data.sql file in the resource and it will add the default records to start with:
```bash
  default-data.sql
```

After all these procedures, you can send a request to the addresses specified on swagger and get your results. Finally, the necessary configurations have been made to create the war file of the project, you only need to deploy from the maven menu and it will give you the relevant output.