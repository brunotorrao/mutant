package com.github.brunotorrao.mutant.it

import com.github.brunotorrao.mutant.Application
import com.github.brunotorrao.mutant.domain.Mutant
import com.github.brunotorrao.mutant.domain.Stats
import com.github.brunotorrao.mutant.repository.MutantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutant
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithLeftDiagonalMatches
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutantWithRightDiagonalMatches
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validNonMutant
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(classes = Application, webEnvironment = RANDOM_PORT)
@ContextConfiguration(loader = SpringBootContextLoader, classes = Application)
@ActiveProfiles("test")
class StatsIT extends Specification {

    @Autowired
    private MutantRepository repository

    void setup() {
        def create = Flux.fromIterable(
            [validMutant(),
             validNonMutant(),
             validMutantWithRightDiagonalMatches(),
             validMutantWithLeftDiagonalMatches()
            ]
        )
        .flatMap { it.identifyMutation() }
        .flatMap {
            it.fillId()
            repository.save(it)
        }
        .doOnNext {
            println "saved $it"
        }

        repository
            .deleteAll()
            .thenMany(create)
            .collectList()
            .block()
    }

    @Autowired
    WebTestClient webTestClient

    def 'should return stats given database'() {
        when:
            def exchange = webTestClient
                .get()
                .uri("/stats")
                .exchange()
        then:
            exchange
                .expectBody(Stats)
                .isEqualTo(new Stats(3, 1, 3.0))
    }


}
