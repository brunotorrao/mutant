package com.github.brunotorrao.mutant.router;

import com.github.brunotorrao.mutant.handler.MutantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MutantRouter {
    
    @Bean
    RouterFunction routerFunction(MutantHandler mutantHandler) {
        return route(POST("/mutant").and(accept(APPLICATION_JSON)), mutantHandler::isMutant);
    }
}


