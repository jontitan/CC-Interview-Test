package com.clearcorrect.interview.services

import com.clearcorrect.interview.CharacterConflictException
import com.clearcorrect.interview.dtos.Direction
import com.clearcorrect.interview.dtos.PlayDTO
import com.clearcorrect.interview.persistence.BoardEntity
import com.clearcorrect.interview.persistence.BoardRepository
import com.nhaarman.mockito_kotlin.anyArray
import com.nhaarman.mockito_kotlin.argumentCaptor
import org.assertj.core.api.BDDAssertions.then
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.history.Revisions
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BoardServiceTest {

    @InjectMocks
    private lateinit var subject: BoardService

    @Mock
    private
    lateinit var mockBoardTransformer: BoardTransformer

    @Mock
    private
    lateinit var mockBoardRepository: BoardRepository

    private val id = 123L
    private val boardDBString = "boardDBString"
    private val board = Array(15) { _ -> CharArray(15) { '.' } }
    private val readableBoard = "readableBoard"
    private var playDTO = PlayDTO(id, Pair(1, 1), Direction.RIGHT, "dog")

    @Before
    fun setUp() {
        val boardEntity = BoardEntity()
        `when`(mockBoardTransformer.toDBString(anyArray())).thenReturn(boardDBString)
        boardEntity.id = id
        boardEntity.board = boardDBString
        boardEntity.rows = 15
        boardEntity.columns = 15
        `when`(mockBoardRepository.save(any(BoardEntity::class.java))).thenReturn(boardEntity)
        `when`(mockBoardRepository.findById(anyLong())).thenReturn(Optional.of(boardEntity))
        `when`(mockBoardTransformer.fromDBString(anyString(), anyInt(), anyInt())).thenReturn(board)
        `when`(mockBoardTransformer.present(anyArray())).thenReturn(readableBoard)
    }

    @Test
    fun create_callsBoardTransformer() {
        subject.create(Optional.empty())
        argumentCaptor<Array<CharArray>>().apply {
            verify(mockBoardTransformer).toDBString(capture())
            then(firstValue.size).isEqualTo(15)

            for (row: CharArray in firstValue) {
                then(row.size).isEqualTo(15)
                row.forEach { cell: Char ->
                    then(cell).isEqualTo('.')
                }
            }
        }
    }

    @Test
    fun create_withOptionalDimensions() {
        subject.create(Optional.of(Pair(2, 3)))
        argumentCaptor<Array<CharArray>>().apply {
            verify(mockBoardTransformer).toDBString(capture())
            then(firstValue.size).isEqualTo(2)

            for (row: CharArray in firstValue) {
                then(row.size).isEqualTo(3)
            }
        }
    }

    @Test
    fun create_callsBoardRepositoryWith15By15Board() {
        subject.create(Optional.empty())
        val boardEntity = BoardEntity()
        boardEntity.board = boardDBString
        argumentCaptor<BoardEntity>().apply {
            verify(mockBoardRepository).save(capture())
            then(firstValue.board).isEqualTo(boardDBString)
            then(firstValue.rows).isEqualTo(15)
            then(firstValue.columns).isEqualTo(15)
        }
    }

    @Test
    fun create_returnsNewBoardId() {
        then(subject.create(Optional.empty())).isEqualTo(id)
    }

    @Test
    fun fetch_callsBoardRepository() {
        subject.fetch(123L)
        verify(mockBoardRepository).findById(123L)
    }

    @Test
    fun fetch_callsBoardTransformer() {
        subject.fetch(123L)
        verify(mockBoardTransformer).fromDBString(boardDBString, 15, 15)
        verify(mockBoardTransformer).present(board)
    }

    @Test
    fun fetch_returnsReadableString() {
        then(subject.fetch(123L)).isEqualTo(readableBoard)
    }

    @Test
    fun play_callsBoardRepositoryFind() {
        subject.play(playDTO)
        verify(mockBoardRepository).findById(id)
    }

    @Test
    fun play_callsBoardTransformer() {
        subject.play(playDTO)
        verify(mockBoardTransformer).fromDBString(boardDBString, 15, 15)
        argumentCaptor<Array<CharArray>>().apply {
            verify(mockBoardTransformer).toDBString(capture())
            then(firstValue[1][1]).isEqualTo('D')
            then(firstValue[1][2]).isEqualTo('O')
            then(firstValue[1][3]).isEqualTo('G')
        }
        verify(mockBoardTransformer).present(board)
    }

    @Test
    fun play_returnsReadableString() {
        then(subject.play(playDTO)).isEqualTo(readableBoard)
    }

    @Test
    fun play_whenDirectionIsDown() {
        playDTO = PlayDTO(id, Pair(1, 1), Direction.DOWN, "dog")
        subject.play(playDTO)
        verify(mockBoardTransformer).fromDBString(boardDBString, 15, 15)
        argumentCaptor<Array<CharArray>>().apply {
            verify(mockBoardTransformer).toDBString(capture())
            then(firstValue[1][1]).isEqualTo('D')
            then(firstValue[2][1]).isEqualTo('O')
            then(firstValue[3][1]).isEqualTo('G')
        }
        verify(mockBoardTransformer).present(board)
    }

    @Test(expected = CharacterConflictException::class)
    fun play_whenCharactersConflicts() {
        playDTO = PlayDTO(id, Pair(1, 1), Direction.DOWN, "dog")
        board[1][1] = 'A'
        `when`(mockBoardTransformer.fromDBString(anyString(), anyInt(), anyInt())).thenReturn(board)
        subject.play(playDTO)
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun play_whenWordTooLong() {
        playDTO = PlayDTO(id, Pair(14, 14), Direction.DOWN, "dog")
        subject.play(playDTO)
    }

    @Test
    fun fetchAll_callsBoardRepository() {
        subject.fetchAll()
        verify(mockBoardRepository).findAll()
    }

    @Test
    fun fetchAll_returnsAListOfBoardsAsString() {
        `when`(mockBoardRepository.findAll()).thenReturn(
                Arrays.asList(
                        BoardEntity(),
                        BoardEntity(),
                        BoardEntity(),
                        BoardEntity(),
                        BoardEntity()
                )
        )
        then(subject.fetchAll().size).isEqualTo(5)
    }

    @Test
    fun fetchAll_callsTransformerOnEachBoard() {
        val board1 = BoardEntity()
        val board2 = BoardEntity()
        val board3 = BoardEntity()
        board1.board = "board1"
        board2.board = "board2"
        board3.board = "board3"

        `when`(mockBoardRepository.findAll()).thenReturn(
                Arrays.asList(
                        board1,
                        board2,
                        board3
                )
        )
        subject.fetchAll()

        verify(mockBoardTransformer).fromDBString("board1", 0, 0)
        verify(mockBoardTransformer).fromDBString("board2", 0, 0)
        verify(mockBoardTransformer).fromDBString("board3", 0, 0)
    }

    @Test
    fun fetchAll_CallsPresentOnEachBoards() {
        `when`(mockBoardRepository.findAll()).thenReturn(
                Arrays.asList(
                        BoardEntity(),
                        BoardEntity(),
                        BoardEntity()
                )
        )
        subject.fetchAll()

        verify(mockBoardTransformer, times(3)).present(board)
    }

    @Test
    fun fetchHistory_CallsRepository() {
        `when`(mockBoardRepository.findRevisions(anyLong())).thenReturn(Revisions.none())
        subject.fetchHistory(123L)

        verify(mockBoardRepository).findRevisions(123L)
    }
}