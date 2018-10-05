package com.github.brunotorrao.mutant.domain;

import com.github.brunotorrao.mutant.helper.DnaHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.join;
import static reactor.core.scheduler.Schedulers.elastic;

@Data
@Document(collection = "mutants")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mutant {
    
    @Id
    private Integer id;
    private List<String> dna;
    private Boolean mutant;
    
    public Boolean isMutant() {
        return mutant;
    }
    
    public void fillId() {
        this.id = join("", this.dna).hashCode();
    }
    
    public Mono<Mutant> identifyMutation() {
        if (mutant == null) {
            return isMutant(dna)
                .map(isMutant -> {
                    mutant = isMutant;
                    return this;
                });
        } else return Mono.just(this);
    }
    
    private Mono<Boolean> isMutant(List<String> dna) {
        return getAllPossibleDnaSequences(dna)
            .parallel()
            .runOn(elastic())
            .filter(DnaHelper::hasFourConsecutiveEqualChars)
            .sequential()
            .collectList()
            .map(this::hasMinimumMatches);
    }
    
    private Flux<String> getAllPossibleDnaSequences(List<String> dna) {
        return Flux.fromIterable(dna)
            .mergeWith(DnaHelper.getSequencesInColumns(dna))
            .mergeWith(DnaHelper.getSequencesInDiagonals(dna));
    }
    
    private Boolean hasMinimumMatches(List<String> matches) {
        return matches.size() >= 2 ? TRUE : FALSE;
    }
    
    
}
