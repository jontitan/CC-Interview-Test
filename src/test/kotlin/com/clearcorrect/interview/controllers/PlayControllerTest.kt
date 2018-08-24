package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.dtos.Direction
import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.services.BoardService
import com.nhaarman.mockito_kotlin.any
import org.assertj.core.api.Java6BDDAssertions.then
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayControllerTest {

    @InjectMocks
    private lateinit var subject: PlayController

    @Mock
    private
    lateinit var mockBoardService: BoardService

    private val playDTO = PlayDTO(123L, Pair(1, 1), Direction.RIGHT, "dog")
    private val readableBoard = "readableBoard"

    @Before
    fun setUp() {
        `when`(mockBoardService.play(any())).thenReturn(readableBoard)
    }

    @Test
    fun playGame_callsBoardService() {
        subject.playGame(playDTO)
        verify(mockBoardService).play(playDTO)
    }

    @Test
    fun playGame_returnsReadableString() {
        then(subject.playGame(playDTO)).isEqualTo(readableBoard)
    }
}