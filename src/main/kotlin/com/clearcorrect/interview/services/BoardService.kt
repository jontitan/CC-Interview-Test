package com.clearcorrect.interview.services

import com.clearcorrect.interview.CharacterConflictException
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
        val board = Array(DEFAULT_WIDTH) { CharArray(DEFAULT_WIDTH) { '.' } }
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
        val r = playDTO.coordinate.first
        val c = playDTO.coordinate.second
        var counter = 0
        when (playDTO.direction) {
            Direction.DOWN -> {
                for (i in r until r + playDTO.word.length) {
                    if (board[i][c] != '.' &&
                            board[i][c] != playDTO.word[counter].toUpperCase()) {
                        throw CharacterConflictException()
                    }
                    board[i][c] = playDTO.word[counter++].toUpperCase()
                }
            }
            Direction.RIGHT -> {
                for (i in c until c + playDTO.word.length) {
                    if (board[r][i] != '.' &&
                            board[r][i] != playDTO.word[counter].toUpperCase()) {
                        throw CharacterConflictException()
                    }
                    board[r][i] = playDTO.word[counter++].toUpperCase()
                }
            }
        }

        boardEntity.board = boardTransformer.toDBString(board)
        boardRepository.save(boardEntity)
        return boardTransformer.present(board)
    }
}