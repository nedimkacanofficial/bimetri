package com.bimetri.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Bimetri App", version = "1.0.0", description = "This is a bimetri app swagger documentation."), servers = {@Server(url = "http://localhost:8080", description = "Backend Server URL")})
public class BimetriApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BimetriApplication.class, args);
    }

}
