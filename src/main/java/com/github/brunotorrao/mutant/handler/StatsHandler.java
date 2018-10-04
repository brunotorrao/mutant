package com.github.brunotorrao.mutant.handler;

import com.github.brunotorrao.mutant.domain.Stats;
import com.github.brunotorrao.mutant.repository.MutantRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class StatsHandler {
    
    private MutantRepository mutantRepository;
    
    public StatsHandler(MutantRepository mutantRepository) {
        this.mutantRepository = mutantRepository;
    }
    
    public Mono<ServerResponse> stats(ServerRequest request) {
        return ok().body(mutantRepository.getStats(), Stats.class);
    }
}
