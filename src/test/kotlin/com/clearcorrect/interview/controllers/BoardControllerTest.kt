package com.clearcorrect.interview.controllers

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
class BoardControllerTest {

    @InjectMocks
    lateinit var subject: BoardController

    @Mock
    private
    lateinit var mockBoardService: BoardService

    private val id = 123L
    private val readableBoard = "readableBoard"

    @Before
    fun setUp() {
        `when`(mockBoardService.create(Optional.empty())).thenReturn(id)
        `when`(mockBoardService.fetch(anyLong())).thenReturn(readableBoard)
    }

    @Test
    fun createGame_callsBoardService() {
        subject.createGame(Optional.empty())
        verify(mockBoardService).create(Optional.empty())
    }

    @Test
    fun createGame_returnsNewBoardId() {
        then(subject.createGame(Optional.empty())).isEqualTo(id)
    }

    @Test
    fun fetchGame_returnsReadableString() {
        then(subject.fetchGame(123L)).isEqualTo(readableBoard)
    }
}