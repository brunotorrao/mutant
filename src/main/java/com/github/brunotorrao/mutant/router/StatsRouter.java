package com.github.brunotorrao.mutant.router;

import com.github.brunotorrao.mutant.handler.StatsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StatsRouter {
    
    @Bean
    public RouterFunction statsRouterFunction(StatsHandler statsHandler) {
        return route(GET("/stats"), statsHandler::stats);
    }
}
