package com.forsythe.reviewservice.api;

import com.forsythe.reviewservice.domain.Client;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientWebService {

    @Autowired
    private Vertx vertx;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateClient(@RequestBody Client client) {
        vertx.eventBus().send("CLIENT_REVIEW_SERVICE", Json.encode(client));
    }
}
