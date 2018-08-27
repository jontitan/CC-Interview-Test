package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.services.BoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/board")
class BoardController {
    @Autowired
    lateinit var boardService: BoardService

    @PostMapping()
    fun createGame(@RequestBody optionalDimensions: Optional<Pair<Int, Int>>): Long {
        return boardService.create(optionalDimensions)
    }

    @GetMapping()
    fun fetchGame(@RequestParam id: Long): String {
        return boardService.fetch(id)
    }
}