package com.clearcorrect.interview.services

import org.springframework.stereotype.Service

@Service
class BoardPresenter {
    fun present(board: List<List<Char>>): String {
        var boardAsString = System.lineSeparator()
        for (row: List<Char> in board) {
            for (cell: Char in row) {
                boardAsString += cell
            }
            boardAsString += System.lineSeparator()
        }
        return boardAsString
    }
}