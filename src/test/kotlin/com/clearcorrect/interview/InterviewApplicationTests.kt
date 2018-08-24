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

        var playDTO = PlayDTO(postResult.body!!, Pair(1, 1), Direction.RIGHT, "clear")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(1, 1), Direction.DOWN, "CORRECT")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(6, 1), Direction.RIGHT, "coding")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(7, 1), Direction.RIGHT, "teST")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        val result = testRestTemplate.getForEntity("/board?id=" + postResult.body, String::class.java)

        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isEqualTo(
                System.lineSeparator() +
                        "..............." + System.lineSeparator() +
                        ".CLEAR........." + System.lineSeparator() +
                        ".O............." + System.lineSeparator() +
                        ".R............." + System.lineSeparator() +
                        ".R............." + System.lineSeparator() +
                        ".E............." + System.lineSeparator() +
                        ".CODING........" + System.lineSeparator() +
                        ".TEST.........." + System.lineSeparator() +
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
    fun boardController_play_whenWordConflicts_returnsBadRequest() {
        val postResult = testRestTemplate.postForEntity<Long>("/board")

        val playDTO1 = PlayDTO(postResult.body!!, Pair(1, 1), Direction.RIGHT, "dog")

        testRestTemplate.postForEntity<String>("/play", playDTO1)

        val playDTO2 = PlayDTO(postResult.body!!, Pair(1, 3), Direction.DOWN, "cat")

        val result = testRestTemplate.postForEntity<String>("/play", playDTO2)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        then(result.body).isEqualTo("Character Conflict")
    }

    @Test
    fun boardController_play_whenWordTooLong_returnsBadRequest() {
        val postResult = testRestTemplate.postForEntity<Long>("/board")

        val playDTO = PlayDTO(postResult.body!!, Pair(0, 14), Direction.RIGHT, "cat")

        val result = testRestTemplate.postForEntity<String>("/play", playDTO)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        then(result.body).isEqualTo("Word Too Long")
    }
}
