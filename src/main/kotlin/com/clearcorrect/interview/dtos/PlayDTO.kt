package com.clearcorrect.interview.dtos

data class PlayDTO(
        val id: Long,
        val coordinate: Pair<Int, Int>,
        val direction: Direction,
        val word: String
)