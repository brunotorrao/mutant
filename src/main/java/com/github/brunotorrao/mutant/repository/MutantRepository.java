package com.github.brunotorrao.mutant.repository;

import com.github.brunotorrao.mutant.domain.Mutant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MutantRepository extends ReactiveMongoRepository<Mutant, String>, MutantCustomRepository {
    
    Mono<Mutant> findById(Integer id);
    
    
}
