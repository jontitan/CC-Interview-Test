package com.clearcorrect.interview.services

import com.clearcorrect.interview.dtos.Direction
import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.persistence.BoardEntity
import com.clearcorrect.interview.persistence.BoardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BoardService {

    @Autowired
    lateinit var boardTransformer: BoardTransformer

    @Autowired
    lateinit var boardRepository: BoardRepository

    val DEFAULT_WIDTH = 15

    fun create(): Long {
        val board = List(DEFAULT_WIDTH) { List(DEFAULT_WIDTH) { '.' } }
        val boardEntity = BoardEntity()
        boardEntity.board = boardTransformer.toDBString(board)
        boardEntity.width = DEFAULT_WIDTH
        return boardRepository.save(boardEntity).id
    }

    fun fetch(id: Long): String {
        return boardRepository.findById(id).map {
            boardTransformer.present(
                    boardTransformer.fromDBString(it.board, DEFAULT_WIDTH)
            )
        }.get()
    }

    fun play(playDTO: PlayDTO): String {
        val boardEntity = boardRepository.findById(playDTO.id).get()
        val board = boardTransformer.fromDBString(boardEntity.board, DEFAULT_WIDTH)
        val x = playDTO.coordinate.first
        val y = playDTO.coordinate.second
        var counter = 0
        when (playDTO.direction) {
            Direction.DOWN -> {
                for (i in x until x + playDTO.word.length) {
                    board[i][y] = playDTO.word[counter++].toUpperCase()
                }
            }
            Direction.RIGHT -> {
                for (i in y until y + playDTO.word.length) {
                    board[x][i] = playDTO.word[counter++].toUpperCase()
                }
            }
        }

        boardEntity.board = boardTransformer.toDBString(board)
        boardRepository.save(boardEntity)
        return boardTransformer.present(board)
    }
}