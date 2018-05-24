package com.forsythe.reviewservice;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ClientServiceApplication {

    @Autowired
    private ClientServiceVerticle serviceVerticle;

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

    @PostConstruct
    public void deployVerticle() {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(serviceVerticle);
    }
}
