package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.services.BoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/play")
class PlayController {
    @Autowired
    lateinit var boardService: BoardService

    @PostMapping()
    fun playGame(@RequestBody playDTO: PlayDTO): String {
        return boardService.play(playDTO)
    }
}