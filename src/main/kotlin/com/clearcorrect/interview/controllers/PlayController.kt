package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.CharacterConflictException
import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.services.BoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/play")
class PlayController {
    @Autowired
    lateinit var boardService: BoardService

    @PostMapping()
    fun playGame(@RequestBody playDTO: PlayDTO): String {
        return boardService.play(playDTO)
    }

    @ExceptionHandler(CharacterConflictException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleCharacterConflictException(): String {
        return "Character Conflict"
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleArrayIndexOutOfBoundsException(): String {
        return "Word Too Long"
    }
}