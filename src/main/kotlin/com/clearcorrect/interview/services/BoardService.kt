package com.clearcorrect.interview.services

import org.springframework.stereotype.Service

@Service
class BoardService {
    fun create(): List<List<Char>> {
        return List(15) { List(15) { '.' } }
    }
}