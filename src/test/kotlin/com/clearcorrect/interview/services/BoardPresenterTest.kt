package com.clearcorrect.interview.services

import org.assertj.core.api.BDDAssertions.then
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BoardPresenterTest {

    @InjectMocks
    private lateinit var subject: BoardPresenter

    @Test
    fun create_returns15by15Array() {
        val board: List<List<Char>> = List(5) { List(5) { '.' } }
        then(subject.present(board)).isEqualTo(
                System.lineSeparator() +
                        "....." + System.lineSeparator() +
                        "....." + System.lineSeparator() +
                        "....." + System.lineSeparator() +
                        "....." + System.lineSeparator() +
                        "....." + System.lineSeparator()
        )
    }
}