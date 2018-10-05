package com.github.brunotorrao.mutant.domain


import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutant
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithHorizontalMatches
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithLeftDiagonalMatches
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithRightDiagonalMatches
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validNonMutant
import static java.lang.Boolean.FALSE
import static java.lang.Boolean.TRUE

class MutantTest extends Specification {

    @Unroll
    def "given #human should identify mutation and return #isMutant" () {
        expect:
            StepVerifier.create(human.identifyMutation())
                .assertNext { it.isMutant() == isMutant }
                .verifyComplete()
        where:
            human                                   | isMutant
            validMutant()                           | TRUE
            validMutantWithHorizontalMatches()      | TRUE
            validMutantWithLeftDiagonalMatches()    | TRUE
            validMutantWithRightDiagonalMatches()   | TRUE
            validNonMutant()                        | FALSE
    }
}
