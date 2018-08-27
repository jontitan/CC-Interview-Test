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
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterviewApplicationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun boardController_create() {
        val result = testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isGreaterThan(0)
    }

    @Test
    fun boardController_create_withCustomDimensions() {
        val postResult = testRestTemplate.postForEntity<Long>("/board", Pair(5, 3))

        val result = testRestTemplate.getForEntity("/board?id=" + postResult.body, String::class.java)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body).isEqualTo(
                System.lineSeparator() +
                        "..." + System.lineSeparator() +
                        "..." + System.lineSeparator() +
                        "..." + System.lineSeparator() +
                        "..." + System.lineSeparator() +
                        "..." + System.lineSeparator()
        )
    }

    @Test
    fun boardController_fetch() {
        val postResult = testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())

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
    fun boardController_fetchAll() {
        testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())
        testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())
        testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())
        testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())
        testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())

        val result = testRestTemplate.getForEntity("/board/all", List::class.java)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body?.size).isGreaterThanOrEqualTo(5)
    }

    @Test
    fun playController_play() {
        val postResult = testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())

        var playDTO = PlayDTO(postResult.body!!, Pair(1, 1), Direction.RIGHT, "clear")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(1, 1), Direction.DOWN, "CORRECT")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(6, 1), Direction.RIGHT, "coding")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(7, 1), Direction.RIGHT, "teST")
        val result = testRestTemplate.postForEntity<String>("/play", playDTO)

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
        val postResult = testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())

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
        val postResult = testRestTemplate.postForEntity<Long>("/board", Optional.empty<Any>())

        val playDTO = PlayDTO(postResult.body!!, Pair(0, 14), Direction.RIGHT, "cat")

        val result = testRestTemplate.postForEntity<String>("/play", playDTO)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        then(result.body).isEqualTo("Word Too Long")
    }

    @Test
    fun playController_playHistory() {
        val postResult = testRestTemplate.postForEntity<Long>("/board", Pair(6, 7))

        var playDTO = PlayDTO(postResult.body!!, Pair(0, 0), Direction.RIGHT, "first")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(0, 3), Direction.DOWN, "second")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(0, 4), Direction.DOWN, "third")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        playDTO = PlayDTO(postResult.body!!, Pair(5, 3), Direction.RIGHT, "done")
        testRestTemplate.postForEntity<String>("/play", playDTO)

        val result = testRestTemplate.getForEntity("/play/" + postResult.body + "/all", List::class.java)
        then(result).isNotNull
        then(result.statusCode).isEqualTo(HttpStatus.OK)
        then(result.body?.size).isEqualTo(5)

        then(result.body?.get(0)).isEqualTo(
                System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator()

        )

        then(result.body?.get(1)).isEqualTo(
                System.lineSeparator() +
                        "FIRST.." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator() +
                        "......." + System.lineSeparator()

        )

        then(result.body?.get(2)).isEqualTo(
                System.lineSeparator() +
                        "FIRST.." + System.lineSeparator() +
                        "...E..." + System.lineSeparator() +
                        "...C..." + System.lineSeparator() +
                        "...O..." + System.lineSeparator() +
                        "...N..." + System.lineSeparator() +
                        "...D..." + System.lineSeparator()

        )

        then(result.body?.get(3)).isEqualTo(
                System.lineSeparator() +
                        "FIRST.." + System.lineSeparator() +
                        "...EH.." + System.lineSeparator() +
                        "...CI.." + System.lineSeparator() +
                        "...OR.." + System.lineSeparator() +
                        "...ND.." + System.lineSeparator() +
                        "...D..." + System.lineSeparator()

        )

        then(result.body?.get(4)).isEqualTo(
                System.lineSeparator() +
                        "FIRST.." + System.lineSeparator() +
                        "...EH.." + System.lineSeparator() +
                        "...CI.." + System.lineSeparator() +
                        "...OR.." + System.lineSeparator() +
                        "...ND.." + System.lineSeparator() +
                        "...DONE" + System.lineSeparator()

        )
    }
}
