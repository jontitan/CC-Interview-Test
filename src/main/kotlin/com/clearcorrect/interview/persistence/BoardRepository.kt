package com.clearcorrect.interview.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : JpaRepository<BoardEntity, Long>, RevisionRepository<BoardEntity, Long, Long>