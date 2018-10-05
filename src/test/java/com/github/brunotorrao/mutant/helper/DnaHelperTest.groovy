package com.github.brunotorrao.mutant.helper

import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Unroll

import static java.lang.Boolean.FALSE
import static java.lang.Boolean.TRUE

class DnaHelperTest extends Specification {

    @Unroll
    def 'given dnaSequence #dnaSequence should return #hasFourConsecutiveEqualChars'() {
        expect:
            DnaHelper.hasFourConsecutiveEqualChars(dnaSequence) == hasFourConsecutiveEqualChars
        where:
            dnaSequence | hasFourConsecutiveEqualChars
            "AAAALP"    | TRUE
            "PLOKIJ"    | FALSE
    }

    @Unroll
    def 'given #dna when getSequencesInColumns should return #result'() {
        expect:
            DnaHelper.getSequencesInColumns(dna).collectList().block().sort() == result.sort()
        where:
            dna                                                         | result
            ["ABCDEF","ABCDEF","ABCDEF","ABCDEF","ABCDEF","ABCDEF"]     | ["AAAAAA", "BBBBBB", "CCCCCC", "DDDDDD", "EEEEEE", "FFFFFF"]
    }

    @Unroll
    def 'given #dna when getSequencesInDiagonals should return #result'() {
        expect:
            DnaHelper.getSequencesInDiagonals(dna).collectList().block().sort() == result.sort()
        where:
            dna                                                         | result
            ["ABCDEF","FABCDE","EFABCD","DEFABC","CDEFAB","ABCDEF"]     | ["AAAAAF", "BBBBB", "CCCC", "DBFC", "DBFD", "ECAEB", "ECAEC", "EEED", "FDBFDA", "FFFFE"]
    }
}
