package com.clearcorrect.interview.services

import org.springframework.stereotype.Service

@Service
class BoardTransformer {
    fun present(board: Array<CharArray>): String {
        var boardAsString = System.lineSeparator()
        for (row: CharArray in board) {
            row.forEach { cell: Char ->
                boardAsString += cell
            }
            boardAsString += System.lineSeparator()
        }
        return boardAsString
    }

    fun toDBString(board: Array<CharArray>): String {
        var dbString = ""
        for (row: CharArray in board) {
            row.forEach { cell: Char ->
                dbString += cell
            }
        }
        return dbString
    }

    fun fromDBString(dbString: String, width: Int): Array<CharArray> {
        val board: Array<CharArray> = Array(width) { CharArray(width) { '.' } }
        var counter = 0
        for (i in 0 until width) {
            for (j in 0 until width) {
                board[i][j] = dbString[counter++]
            }
        }
        return board
    }
}