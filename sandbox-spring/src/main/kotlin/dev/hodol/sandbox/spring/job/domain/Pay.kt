package dev.hodol.sandbox.spring.job.domain

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Pay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var amount: Long,
    var txName: String,
    @CreatedDate
    var txDateTime: LocalDateTime?
) {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
    }
}
