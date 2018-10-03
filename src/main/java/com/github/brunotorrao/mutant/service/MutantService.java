package com.github.brunotorrao.mutant.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.regex.Pattern.compile;

@Service
public class MutantService {
    
    private static final Pattern PATTERN = compile("(\\w)\\1{3}");
    
    
    public Mono<Boolean> isMutant(List<String> dna) {
        return Flux.fromIterable(dna)
            .mergeWith(getColums(dna))
            .mergeWith(getDiagonals(dna))
            .filter(this::hasFourLettersInSequence)
            .collectList()
            .map(this::hasMinimumMatches);
    }
    
    private Boolean hasFourLettersInSequence(String dnaSequence) {
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
            .map(list -> String.join("", list));
    }
    
    private Flux<String> getDiagonals(List<String> dna) {
        var rightDiagonal = Flux.range(0-dna.size(), (dna.size()*2)-1)
            .flatMap(x -> getRightDiagonal(x, dna));
        
        var leftDiagonal = Flux.range(0, (dna.size()*2)-1)
            .flatMap(x -> getLeftDiagonal(x, dna));
        
        return rightDiagonal.mergeWith(leftDiagonal);
    }
    
    private Mono<String> getRightDiagonal(Integer columnIndex, List<String> dna) {
        AtomicInteger index = new AtomicInteger(columnIndex);
    
        return Flux.fromIterable(dna)
            .flatMap(it -> {
                if (index.get() >= 0 && index.get() < it.length()) {
                    String value = it.substring(index.getAndIncrement(), index.get());
                    return Mono.just(value);
                } else {
                    index.getAndIncrement();
                    return Mono.empty();
                }
            })
            .collectList()
            .map(list -> String.join("", list));
    }
    
    private Mono<String> getLeftDiagonal(Integer columnIndex, List<String> dna) {
        AtomicInteger index = new AtomicInteger(columnIndex);
        
        return Flux.fromIterable(dna)
            .flatMap(dnaSequence -> {
                if (index.get() >= 0 && index.get() < dnaSequence.length()) {
                    String value = dnaSequence.substring(index.getAndDecrement(), index.get()+2);
                    return Mono.just(value);
                } else {
                    index.getAndDecrement();
                    return Mono.empty();
                }
            })
            .collectList()
            .map(list -> String.join("", list));
    }
    
}
