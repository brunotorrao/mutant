package com.github.brunotorrao.mutant.service;

import com.github.brunotorrao.mutant.domain.Mutant;
import com.github.brunotorrao.mutant.repository.MutantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.join;
import static reactor.core.publisher.Mono.defer;

@Service
public class MutantService {
    
    private MutantRepository repository;
    
    public MutantService(MutantRepository repository) {
        this.repository = repository;
    }
    
    public Mono<Mutant> findOrCreate(Mutant mutant) {
        return Mono.justOrEmpty(mutant)
            .flatMap(it -> findByDna(mutant.getDna()))
            .switchIfEmpty(defer(mutant::identifyMutation))
            .doOnNext(Mutant::fillId)
            .flatMap(repository::save);
    }
    
    private Mono<Mutant> findByDna(List<String> dna) {
        return repository.findById(join("", dna).hashCode());
    }
    
}
