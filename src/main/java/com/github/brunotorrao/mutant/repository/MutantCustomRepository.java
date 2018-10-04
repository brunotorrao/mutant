package com.github.brunotorrao.mutant.repository;

import com.github.brunotorrao.mutant.domain.Stats;
import reactor.core.publisher.Mono;

public interface MutantCustomRepository {
    
    Mono<Stats> getStats();
}
