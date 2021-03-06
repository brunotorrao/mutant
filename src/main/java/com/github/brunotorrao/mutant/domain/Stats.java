package com.github.brunotorrao.mutant.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    @JsonProperty("count_mutant_dna")
    private Integer countMutantDna = 0;
    @JsonProperty("count_human_dna")
    private Integer countHumanDna = 0;
    private Double ratio = 0.0;
}
