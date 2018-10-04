package com.github.brunotorrao.mutant.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Stats {
    
    public Stats(Integer countHumanDna, Integer countMutantDna) {
        this.countHumanDna = countHumanDna;
        this.countMutantDna = countMutantDna;
        this.ratio = countHumanDna <= 0 ? 0 : countMutantDna.doubleValue() / countHumanDna.doubleValue();
    }
    
    @JsonProperty("count_mutant_dna")
    private Integer countMutantDna;
    @JsonProperty("count_human_dna")
    private Integer countHumanDna;
    private Double ratio;
}
