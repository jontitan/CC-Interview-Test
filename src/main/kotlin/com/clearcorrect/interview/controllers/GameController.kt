package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.services.BoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController {
    @Autowired
    lateinit var boardService: BoardService


    @PostMapping("/create")
    fun createGame() {
        boardService.create()
    }
}