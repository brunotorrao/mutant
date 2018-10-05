package com.github.brunotorrao.mutant.helper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static java.util.regex.Pattern.compile;
import static reactor.core.scheduler.Schedulers.elastic;

public class DnaHelper {
    
    private static final Pattern PATTERN = compile("(\\w)\\1{3}");
    
    public static Boolean hasFourConsecutiveEqualChars(String dnaSequence) {
        return PATTERN.matcher(dnaSequence).find();
    }
    
    public static Flux<String> getSequencesInColumns(List<String> dna) {
        return Flux.range(0, dna.size())
            .parallel()
            .runOn(elastic())
            .flatMap(x -> getColumnSequence(x, dna))
            .sequential();
    }
    
    public static Flux<String> getSequencesInDiagonals(List<String> dna) {
        var loopRange = (dna.size()*2)-1;
        
        var rightDiagonal = Flux.range(0-dna.size(), loopRange)
            .parallel()
            .runOn(elastic())
            .flatMap(x -> getDiagonalSequence(x, dna, DnaHelper::getCellAndIncrement))
            .sequential();
        
        var leftDiagonal = Flux.range(0, loopRange)
            .parallel()
            .runOn(elastic())
            .flatMap(x -> getDiagonalSequence(x, dna, DnaHelper::getCellAndDecrement))
            .sequential();
        
        return rightDiagonal.mergeWith(leftDiagonal);
    }
    
    private static Mono<String> getColumnSequence(Integer columnIndex, List<String> dna) {
        return Flux.fromIterable(dna)
            .map(dnaSequence -> dnaSequence.substring(columnIndex, columnIndex+1))
            .collectList()
            .map(cells -> join("", cells));
    }
    
    private static Mono<String> getDiagonalSequence(Integer columnIndex, List<String> dna, BiFunction<AtomicInteger, String, Mono<String>> getCellAndMoveIndex) {
        var index = new AtomicInteger(columnIndex);
        
        return Flux.fromIterable(dna)
            .flatMap(dnaSequence -> getCellAndMoveIndex.apply(index, dnaSequence))
            .collectList()
            .map(cells -> join("", cells))
            .filter(dnaSequence -> dnaSequence.length() >= 4);
    }
    
    private static Mono<String> getCellAndDecrement(AtomicInteger index, String dnaSequence) {
        var cell = getCell(index, dnaSequence);
        index.getAndDecrement();
        return cell;
    }
    
    private static Mono<String> getCellAndIncrement(AtomicInteger index, String dnaSequence) {
        var cell = getCell(index, dnaSequence);
        index.getAndIncrement();
        return cell;
    }
    
    private static Mono<String> getCell(AtomicInteger index, String dnaSequence) {
        if (index.get() >= 0 && index.get() < dnaSequence.length()) {
            var value = dnaSequence.substring(index.get(), index.get() + 1);
            return Mono.just(value);
        } else {
            return Mono.empty();
        }
    }
}
