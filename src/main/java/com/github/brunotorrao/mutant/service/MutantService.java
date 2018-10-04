package com.github.brunotorrao.mutant.service;

import com.github.brunotorrao.mutant.domain.Mutant;
import com.github.brunotorrao.mutant.repository.MutantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.join;
import static java.util.regex.Pattern.compile;
import static reactor.core.scheduler.Schedulers.elastic;

@Service
public class MutantService {
    
    private static final Pattern PATTERN = compile("(\\w)\\1{3}");
    
    private MutantRepository repository;
    
    public MutantService(MutantRepository repository) {
        this.repository = repository;
    }
    
    public Mono<Mutant> findOrCreate(Mutant mutant) {
        return findByDna(mutant.getDna())
            .switchIfEmpty(Mono.just(mutant))
            .flatMap(this::identifyMutant)
            .doOnNext(Mutant::fillId)
            .flatMap(repository::save);
    }
    
    private Mono<Mutant> identifyMutant(Mutant mutant) {
        if (mutant.isMutant() == null) {
            return isMutant(mutant.getDna())
                .map(isMutant -> {
                    mutant.setMutant(isMutant);
                    return mutant;
                });
        } else return Mono.just(mutant);
    }
    
    private Mono<Mutant> findByDna(List<String> dna) {
        return repository.findById(join("", dna).hashCode());
    }
    
     Mono<Boolean> isMutant(List<String> dna) {
        return Flux.fromIterable(dna)
            .mergeWith(getColumns(dna))
            .mergeWith(getDiagonals(dna))
            .filter(this::hasFourEqualLettersInSequence)
            .collectList()
            .map(this::hasMinimumMatches);
    }
    
    private Boolean hasFourEqualLettersInSequence(String dnaSequence) {
        return PATTERN.matcher(dnaSequence).find();
    }
    
    private Boolean hasMinimumMatches(List<String> matches) {
        return matches.size() >= 2 ? TRUE : FALSE;
    }
    
    private Flux<String> getColumns(List<String> dna) {
        return Flux.range(0, dna.size())
            .parallel()
            .runOn(elastic())
            .flatMap(x -> getColumn(x, dna))
            .sequential();
    }
    
    private Mono<String> getColumn(Integer columnIndex, List<String> dna) {
        return Flux.fromIterable(dna)
            .map(dnaSequence -> dnaSequence.substring(columnIndex, columnIndex+1))
            .collectList()
            .map(list -> join("", list));
    }
    
    private Flux<String> getDiagonals(List<String> dna) {
        var loopRange = (dna.size()*2)-1;
        
        var rightDiagonal = Flux.range(0-dna.size(), loopRange)
            .parallel(loopRange)
            .runOn(elastic())
            .flatMap(x -> getDiagonal(x, dna, this::getCellAndIncrement))
            .sequential();
        
        var leftDiagonal = Flux.range(0, loopRange)
            .parallel(loopRange)
            .runOn(elastic())
            .flatMap(x -> getDiagonal(x, dna, this::getCellAndDecrement))
            .sequential();
        
        return rightDiagonal.mergeWith(leftDiagonal);
    }
    
    private Mono<String> getDiagonal(Integer columnIndex, List<String> dna, BiFunction<AtomicInteger, String, Mono<String>> getCellAndMoveIndex) {
        var index = new AtomicInteger(columnIndex);
        
        return Flux.fromIterable(dna)
            .flatMap(dnaSequence -> getCellAndMoveIndex.apply(index, dnaSequence))
            .collectList()
            .map(cells -> join("", cells));
    }
    
    private Mono<String> getCellAndDecrement(AtomicInteger index, String dnaSequence) {
        var cell = getCell(index, dnaSequence);
        index.getAndDecrement();
        return cell;
    }
    
    private Mono<String> getCellAndIncrement(AtomicInteger index, String dnaSequence) {
        var cell = getCell(index, dnaSequence);
        index.getAndIncrement();
        return cell;
    }
    
    private Mono<String> getCell(AtomicInteger index, String dnaSequence) {
        if (index.get() >= 0 && index.get() < dnaSequence.length()) {
            var value = dnaSequence.substring(index.get(), index.get() + 1);
            return Mono.just(value);
        } else {
            return Mono.empty();
        }
    }
    
}
