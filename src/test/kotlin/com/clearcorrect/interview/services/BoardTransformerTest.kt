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
        val board: Array<CharArray> = Array(5) { _ -> CharArray(5) { '.' } }
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
        val board: Array<CharArray> = Array(3) { _ -> CharArray(3) { '.' } }
        board[0] = charArrayOf('a', 'b', 'c')
        board[1] = charArrayOf('d', 'e', 'f')
        board[2] = charArrayOf('g', 'h', 'i')

        then(subject.toDBString(board)).isEqualTo("abcdefghi")
    }

    @Test
    fun fromDBString() {
        val board: Array<CharArray> = Array(3) { _ -> CharArray(3) { '.' } }
        board[0] = charArrayOf('a', 'b', 'c')
        board[1] = charArrayOf('d', 'e', 'f')
        board[2] = charArrayOf('g', 'h', 'i')

        then(subject.fromDBString("abcdefghi", 3, 3)).isEqualTo(board)
    }

    @Test
    fun fromDBString_WhenEowsAndColumnsAreDifferent() {
        val board: Array<CharArray> = Array(4) { _ -> CharArray(3) { '.' } }
        board[0] = charArrayOf('a', 'b', 'c')
        board[1] = charArrayOf('d', 'e', 'f')
        board[2] = charArrayOf('g', 'h', 'i')
        board[3] = charArrayOf('j', 'k', 'l')

        then(subject.fromDBString("abcdefghijkl", 4, 3)).isEqualTo(board)
    }
}