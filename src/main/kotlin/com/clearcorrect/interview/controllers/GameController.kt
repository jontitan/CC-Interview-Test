package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.persistence.BoardEntity
import com.clearcorrect.interview.persistence.BoardRepository
import com.clearcorrect.interview.services.BoardService
import com.clearcorrect.interview.services.BoardTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController {
    @Autowired
    lateinit var boardService: BoardService

    @Autowired
    lateinit var boardTransformer: BoardTransformer

    @Autowired
    lateinit var boardRepository: BoardRepository

    val DEFAULT_WIDTH = 15


    @PostMapping()
    fun createGame(): Long {
        val board = boardService.create()
        val boardEntity = BoardEntity()
        boardEntity.board = boardTransformer.toDBString(board)
        boardEntity.width = DEFAULT_WIDTH
        return boardRepository.save(boardEntity).id
    }

    @GetMapping()
    fun fetchGame(@RequestParam id: Long): String {
        return boardRepository.findById(id).map {
            boardTransformer.fromDBString(it.board, DEFAULT_WIDTH)
        }.get()
    }
}