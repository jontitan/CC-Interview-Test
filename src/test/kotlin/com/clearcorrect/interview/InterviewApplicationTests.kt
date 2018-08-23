package com.clearcorrect.interview

import org.assertj.core.api.BDDAssertions.then
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterviewApplicationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun gameController() {
        val result = testRestTemplate.postForEntity<String>("/create")
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isEqualTo(
                System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        "..............." + System.lineSeparator()
        )
    }

}
