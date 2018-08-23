package com.clearcorrect.interview.services

import org.assertj.core.api.BDDAssertions.then
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BoardServiceTest {

    @InjectMocks
    private lateinit var subject: BoardService

    @Test
    fun create_returns15by15Array() {
        val board: List<List<Char>> = subject.create()
        then(board.size).isEqualTo(15)

        board.forEach { row: List<Char> ->
            then(row.size).isEqualTo(15)
            row.forEach { cell: Char ->
                then(cell).isEqualTo('.')
            }
        }
    }
}