package com.github.brunotorrao.mutant.handler;

import com.github.brunotorrao.mutant.domain.Mutant;
import com.github.brunotorrao.mutant.service.MutantService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class MutantHandler {
    
    private MutantService service;
    
    public MutantHandler(MutantService service) {
        this.service = service;
    }
    
    public Mono<ServerResponse> isMutant(ServerRequest request) {
        return request.bodyToMono(Mutant.class)
            .map(Mutant::getDna)
            .flatMap(service::isMutant)
            .flatMap(isMutant -> {
                if (isMutant) {
                    return ok().build();
                } else {
                    return status(FORBIDDEN).build();
                }
            });
    }
}
