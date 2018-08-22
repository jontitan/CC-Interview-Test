package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.services.BoardService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameControllerTest {

    @InjectMocks
    lateinit var subject: GameController

    @Mock
    lateinit var mockBoardService: BoardService

    @Test
    fun createGame_callsBoardService() {
        subject.createGame()
        verify(mockBoardService).create()
    }
}