package com.clearcorrect.interview.services

import com.clearcorrect.interview.CharacterConflictException
import com.clearcorrect.interview.dtos.Direction
import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.persistence.BoardEntity
import com.clearcorrect.interview.persistence.BoardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class BoardService {

    @Autowired
    lateinit var boardTransformer: BoardTransformer

    @Autowired
    lateinit var boardRepository: BoardRepository

    val DEFAULT_WIDTH = 15

    fun create(optionalDimensions: Optional<Pair<Int, Int>>): Long {
        val board = optionalDimensions.map<Array<CharArray>> { dimension ->
            val rows = dimension.first
            val columns = dimension.second
            Array(rows) { _ -> CharArray(columns) { '.' } }
        }.orElseGet {
            Array(DEFAULT_WIDTH) { _ -> CharArray(DEFAULT_WIDTH) { '.' } }
        }

        val boardEntity = BoardEntity()
        boardEntity.board = boardTransformer.toDBString(board)
        boardEntity.rows = board.size
        boardEntity.columns = board[0].size
        return boardRepository.save(boardEntity).id
    }

    fun fetch(id: Long): String {
        return boardRepository.findById(id).map {
            boardTransformer.present(
                    boardTransformer.fromDBString(it.board, it.rows, it.columns)
            )
        }.get()
    }

    fun play(playDTO: PlayDTO): String {
        val boardEntity = boardRepository.findById(playDTO.id).get()
        val board = boardTransformer.fromDBString(boardEntity.board, boardEntity.rows, boardEntity.columns)
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