package com.clearcorrect.interview.controllers

import com.clearcorrect.interview.persistence.BoardEntity
import com.clearcorrect.interview.persistence.BoardRepository
import com.clearcorrect.interview.services.BoardService
import com.clearcorrect.interview.services.BoardTransformer
import com.nhaarman.mockito_kotlin.argumentCaptor
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
    private
    lateinit var mockBoardService: BoardService

    @Mock
    private
    lateinit var mockBoardTransformer: BoardTransformer

    @Mock
    private
    lateinit var mockBoardRepository: BoardRepository

    private val board: List<List<Char>> = Collections.emptyList()
    private val id = 123L
    private val boardDBString = "boardDBString"
    private val readableBoard = "readableBoard"

    @Before
    fun setUp() {
        `when`(mockBoardService.create()).thenReturn(board)
        val boardEntity = BoardEntity()
        `when`(mockBoardTransformer.toDBString(anyList())).thenReturn(boardDBString)
        boardEntity.id = id
        `when`(mockBoardRepository.save(any(BoardEntity::class.java))).thenReturn(boardEntity)
        boardEntity.board = boardDBString
        `when`(mockBoardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity))
        `when`(mockBoardTransformer.fromDBString(anyString(), anyInt())).thenReturn(readableBoard)
    }

    @Test
    fun createGame_callsBoardService() {
        subject.createGame()
        verify(mockBoardService).create()
    }

    @Test
    fun createGame_callsBoardTransformer() {
        subject.createGame()
        verify(mockBoardTransformer).toDBString(board)
    }

    @Test
    fun createGame_callsBoardRepository() {
        subject.createGame()
        val boardEntity = BoardEntity()
        boardEntity.board = boardDBString
        argumentCaptor<BoardEntity>().apply {
            verify(mockBoardRepository).save(capture())
            then(firstValue.board).isEqualTo(boardDBString)
            then(firstValue.width).isEqualTo(15)
        }
    }

    @Test
    fun createGame_returnsNewBoardId() {
        then(subject.createGame()).isEqualTo(id)
    }

    @Test
    fun fetchGame_callsBoardRepository() {
        subject.fetchGame(123L)
        verify(mockBoardRepository).findById(123L)
    }

    @Test
    fun fetchGame_callsBoardTransformer() {
        subject.fetchGame(123L)
        verify(mockBoardTransformer).fromDBString(boardDBString, 15)
    }

    @Test
    fun fetchGame_returnsReadableString() {
        then(subject.fetchGame(123L)).isEqualTo(readableBoard)
    }
}