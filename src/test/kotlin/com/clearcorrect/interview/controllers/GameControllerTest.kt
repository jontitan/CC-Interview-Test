package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.services.BoardPresenter
import com.clearcorrect.interview.services.BoardService
import org.assertj.core.api.Java6BDDAssertions.then
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class GameControllerTest {

    @InjectMocks
    lateinit var subject: GameController

    @Mock
    lateinit var mockBoardService: BoardService

    @Mock
    lateinit var mockBoardPresenter: BoardPresenter

    private val board: List<List<Char>> = Collections.emptyList()

    @Before
    fun setUp() {
        `when`(mockBoardService.create()).thenReturn(board)
    }

    @Test
    fun createGame_callsBoardService() {
        subject.createGame()
        verify(mockBoardService).create()
    }

    @Test
    fun createGame_callsBoardPresenter() {
        subject.createGame()
        verify(mockBoardPresenter).present(board)
    }

    @Test
    fun createGame_returnsNewBoard() {
        `when`(mockBoardPresenter.present(anyList())).thenReturn("boardAsString")
        then(subject.createGame()).isSameAs("boardAsString")
    }
}