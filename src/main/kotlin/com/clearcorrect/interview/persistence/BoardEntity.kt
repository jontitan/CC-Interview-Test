package com.clearcorrect.interview.persistence

import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@Audited
@EntityListeners(AuditingEntityListener::class)
class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0
    var board: String = ""
    var rows: Int = 0
    var columns: Int = 0
}