package com.github.brunotorrao.mutant.repository;

import com.github.brunotorrao.mutant.domain.Stats;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class MutantRepositoryImpl implements MutantCustomRepository {
    
    private final ReactiveMongoTemplate mongoTemplate;
    
    public MutantRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public Mono<Stats> getStats() {
        
        var condMutant = when(where("mutant").is(true)).then(1).otherwise(0);
        var condNonMutant = when(where("mutant").is(false)).then(1).otherwise(0);
        
        Aggregation aggregation = newAggregation(
            project()
                .and(condMutant).as("countMutantDna")
                .and(condNonMutant).as("countHumanDna")
                .and("1").as("aux"),
            group("$aux")
                .sum("countHumanDna").as("countHumanDna")
                .sum("countMutantDna").as("countMutantDna"),
            project("countHumanDna", "countMutantDna")
                .and("countMutantDna").divide("countHumanDna").as("ratio")
        );
    
        return mongoTemplate.aggregate(aggregation, "mutants", Stats.class)
            .collectList()
            .map(x -> x.get(0));
    }
}

