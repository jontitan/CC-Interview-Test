package com.clearcorrect.interview.services

import org.springframework.stereotype.Service

@Service
class BoardTransformer {
    fun present(board: List<List<Char>>): String {
        var boardAsString = System.lineSeparator()
        board.forEach { row: List<Char> ->
            row.forEach { cell: Char ->
                boardAsString += cell
            }
            boardAsString += System.lineSeparator()
        }
        return boardAsString
    }

    fun toDBString(board: List<List<Char>>): String {
        var dbString = ""
        board.forEach { row: List<Char> ->
            row.forEach { cell: Char ->
                dbString += cell
            }
        }
        return dbString
    }

    fun fromDBString(dbString: String, width: Int): String {
        val board: MutableList<MutableList<Char>> = MutableList(width) { MutableList(width) { '.' } }
        var counter = 0
        for (i in 0 until width) {
            for (j in 0 until width) {
                board[i][j] = dbString[counter++]
            }
        }
        return present(board)
    }
}