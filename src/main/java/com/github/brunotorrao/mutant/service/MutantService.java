package com.github.brunotorrao.mutant.service;

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

@Service
public class MutantService {
    
    private static final Pattern PATTERN = compile("(\\w)\\1{3}");
    
    
    public Mono<Boolean> isMutant(List<String> dna) {
        return Flux.fromIterable(dna)
            .mergeWith(getColums(dna))
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
    
    private Flux<String> getColums(List<String> dna) {
        return Flux.range(0, dna.size())
            .flatMap(x -> getColumn(x, dna));
    }
    
    private Mono<String> getColumn(Integer columnIndex, List<String> dna) {
        return Flux.fromIterable(dna)
            .map(dnaSequence -> dnaSequence.substring(columnIndex, columnIndex+1))
            .collectList()
            .map(list -> join("", list));
    }
    
    private Flux<String> getDiagonals(List<String> dna) {
        var rightDiagonal = Flux.range(0-dna.size(), (dna.size()*2)-1)
            .flatMap(x -> getDiagonal(x, dna, this::getCellAndIncrement));
        
        var leftDiagonal = Flux.range(0, (dna.size()*2)-1)
            .flatMap(x -> getDiagonal(x, dna, this::getCellAndDecrement));
        
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
