package com.github.brunotorrao.mutant.fixture;

import com.github.brunotorrao.mutant.domain.Mutant;

import java.util.List;

public class MutantFixture {
    
    public static Mutant validMutant() {
        return Mutant
            .builder()
            .dna(List.of("EBCDEF", "HGBCDC", "TRPBCC", "AHOLBC", "AHOLBC", "FGHIJK"))
            .build();
    }
    
    public static Mutant validMutantWithLeftDiagonalMatches() {
        return Mutant
            .builder()
            .dna(List.of(
                "ALKJIH",
                "BLJEHG",
                "CJAZXV",
                "JQBAVU",
                "EPOVML",
                "FGVIJK"))
            .build();
    }
    
    public static Mutant validMutantWithRightDiagonalMatches() {
        return Mutant
            .builder()
            .dna(List.of(
                "ALUJIH",
                "BLLUHG",
                "CJALUV",
                "JQBALU",
                "EPOVML",
                "FGVIJK"))
            .build();
    }
    
    public static Mutant validNonMutant() {
        return Mutant
            .builder()
            .dna(List.of(
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGABGG",
                "CBCCTA",
                "TCACTG"))
            .build();
    }
    
    public static Mutant validMutantWithHorizontalMatches() {
        return Mutant
            .builder()
            .dna(List.of(
                "ATAAAA",
                "CAGTGC",
                "TTATGT",
                "AAGGGG",
                "CBCCTA",
                "TCACTG"))
            .build();
    }
    
    public static Mutant validMutantWithVerticalMatches() {
        return Mutant
            .builder()
            .dna(List.of(
                "AAAPLO",
                "CAOKOO",
                "TAAWDO",
                "AAJNMO",
                "CBPLRJ",
                "RFGHWD"))
            .build();
    }
}
