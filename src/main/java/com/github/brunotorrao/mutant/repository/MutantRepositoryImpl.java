package com.github.brunotorrao.mutant.repository;

import com.github.brunotorrao.mutant.domain.Stats;
import com.github.brunotorrao.mutant.domain.StatsAggregate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class MutantRepositoryImpl implements MutantCustomRepository {
    
    private final ReactiveMongoTemplate mongoTemplate;
    
    public MutantRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @SuppressWarnings("unchecked")
    public Mono<Stats> getStats() {
        var aggregation = newAggregation(group("$mutant").count().as("count"));
        
        return mongoTemplate.aggregate(aggregation, "mutants", StatsAggregate.class)
            .collectList()
            .map(this::buildStats);
    }
    
    private Integer getCount(List<StatsAggregate> aggregates, Boolean isMutant) {
        return aggregates
            .stream()
            .filter(aggregate -> aggregate.getMutant() == isMutant)
            .findFirst()
            .orElse(new StatsAggregate())
            .getCount();
    }
    
    private Stats buildStats(List<StatsAggregate> aggregates) {
        return new Stats(getCount(aggregates, FALSE), getCount(aggregates, TRUE));
    }
}

