package com.github.brunotorrao.mutant.it

import com.github.brunotorrao.mutant.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import spock.lang.Specification

import static com.github.brunotorrao.mutant.fixture.MutantFixture.validMutant
import static com.github.brunotorrao.mutant.fixture.MutantFixture.validNonMutant
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.FORBIDDEN

@SpringBootTest(classes = Application, webEnvironment = RANDOM_PORT)
@ContextConfiguration(loader = SpringBootContextLoader, classes = Application)
@ActiveProfiles("test")
class MutantIT extends Specification {

    @Autowired
    WebTestClient webTestClient

    def 'given human with mutant dna should return Http status 200 OK'() {
        when:
            def exchange = webTestClient
                .post()
                .uri("/mutant")
                .body(BodyInserters.fromObject(validMutant()))
                .exchange()
        then:
            exchange.expectStatus().isOk()
    }

    def 'given human without mutant dna should return Http status 403 FORBIDDEN'() {
        when:
            def exchange = webTestClient
                .post()
                .uri("/mutant")
                .body(BodyInserters.fromObject(validNonMutant()))
                .exchange()
        then:
            exchange.expectStatus().isEqualTo(FORBIDDEN)
    }
}
