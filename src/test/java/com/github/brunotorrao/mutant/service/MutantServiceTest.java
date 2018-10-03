package com.github.brunotorrao.mutant.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.test.StepVerifier;

import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutant;
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithHorizontalMatches;
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithLeftDiagonalMatches;
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithRightDiagonalMatches;
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithVerticalMatches;
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validNonMutant;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class MutantServiceTest {
    
    private MutantService service;
    
    @BeforeAll
    void setup() {
        service = new MutantService();
    }
    
    @Test
    void givenMutant_whenIsMutant_thenReturnTrue() {
        var mutant = validMutant();
        
        var isMutant = service.isMutant(mutant.getDna());
    
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertTrue)
            .verifyComplete();
    }
    
    @Test
    void givenNonMutant_whenIsMutant_thenReturnFalse() {
        var mutant = validNonMutant();
        
        var isMutant = service.isMutant(mutant.getDna());
        
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertFalse)
            .verifyComplete();
    }
    
    @Test
    void givenMutantWithOnlyRightDiagonalMatches_whenIsMutant_thenReturnTrue() {
        var mutant = validMutantWithRightDiagonalMatches();
        
        var isMutant = service.isMutant(mutant.getDna());
        
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertTrue)
            .verifyComplete();
    }
    
    @Test
    void givenMutantWithOnlyLeftDiagonalMatches_whenIsMutant_thenReturnTrue() {
        var mutant = validMutantWithLeftDiagonalMatches();
        
        var isMutant = service.isMutant(mutant.getDna());
        
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertTrue)
            .verifyComplete();
    }
    
    @Test
    void givenMutantWithHorizontalMatches_whenIsMutant_thenReturnTrue() {
        var mutant = validMutantWithHorizontalMatches();
        
        var isMutant = service.isMutant(mutant.getDna());
        
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertTrue)
            .verifyComplete();
    }
    
    @Test
    void givenMutantWithVerticalMatches_whenIsMutant_thenReturnTrue() {
        var mutant = validMutantWithVerticalMatches();
        
        var isMutant = service.isMutant(mutant.getDna());
        
        StepVerifier
            .create(isMutant)
            .assertNext(Assertions::assertTrue)
            .verifyComplete();
    }
}
