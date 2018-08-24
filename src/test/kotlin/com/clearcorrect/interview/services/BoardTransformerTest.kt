package com.clearcorrect.interview.services

import org.assertj.core.api.BDDAssertions.then
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BoardTransformerTest {

    @InjectMocks
    private lateinit var subject: BoardTransformer

    @Test
    fun present() {
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

    @Test
    fun toDBString() {
        val board: MutableList<MutableList<Char>> = MutableList(3) { MutableList(3) { '.' } }
        board[0] = mutableListOf('a', 'b', 'c')
        board[1] = mutableListOf('d', 'e', 'f')
        board[2] = mutableListOf('g', 'h', 'i')

        then(subject.toDBString(board)).isEqualTo("abcdefghi")
    }

    @Test
    fun fromDBString() {
        val board: MutableList<MutableList<Char>> = MutableList(3) { MutableList(3) { '.' } }
        board[0] = mutableListOf('a', 'b', 'c')
        board[1] = mutableListOf('d', 'e', 'f')
        board[2] = mutableListOf('g', 'h', 'i')

        then(subject.fromDBString("abcdefghi", 3)).isEqualTo(board)
    }
}