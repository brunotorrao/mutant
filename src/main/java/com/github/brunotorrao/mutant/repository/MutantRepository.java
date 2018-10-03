package com.github.brunotorrao.mutant.repository;

import com.github.brunotorrao.mutant.domain.Mutant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MutantRepository extends ReactiveMongoRepository<Mutant, String> {
}
