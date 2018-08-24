package com.clearcorrect.interview

import com.clearcorrect.interview.dtos.Direction
import com.clearcorrect.interview.dtos.PlayDTO
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
    fun boardController_create() {
        val result = testRestTemplate.postForEntity<Number>("/board")
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isNotNull
    }

    @Test
    fun boardController_fetch() {
        val postResult = testRestTemplate.postForEntity<Number>("/board")

        val result = testRestTemplate.getForEntity("/board?id=" + postResult.body, String::class.java)
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

    @Test
    fun boardController_play() {
        val postResult = testRestTemplate.postForEntity<Long>("/board")

        val playDTO = PlayDTO(postResult.body!!, Pair(1, 1), Direction.RIGHT, "dog")

        val result = testRestTemplate.postForEntity<String>("/play", playDTO)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isEqualTo(
                System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        ".DOG..........." + System.lineSeparator() +
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
