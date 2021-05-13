package dev.hodol.sandbox.spring.job

import dev.hodol.sandbox.spring.job.domain.Pay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource


@Configuration
class JdbcJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val dataSource: DataSource
) {
    companion object {
        private const val chunkSize = 10
        private val log: Logger = LoggerFactory.getLogger(JdbcJobConfiguration::class.java)
    }

    @Bean
    fun jdbcCursorItemReaderJob(): Job {
        return jobBuilderFactory["jdbcCursorItemReaderJob"]
            .start(jdbcCursorItemReaderStep())
            .build()
    }

    @Bean
    fun jdbcCursorItemReaderStep(): Step {
        return stepBuilderFactory["jdbcCursorItemReaderStep"]
            .chunk<Pay, Pay>(chunkSize)
            .reader(jdbcCursorItemReader())
            .writer(jdbcCursorItemWriter())
            .build()
    }

    @Bean
    fun jdbcCursorItemWriter(): ItemWriter<Pay> {
        return ItemWriter { list: List<Pay> ->
            for (pay in list) {
                log.info("Current Pay=$pay")
            }
        }
    }

    @Bean
    fun jdbcCursorItemReader(): ItemReader<Pay> {
        return JdbcCursorItemReaderBuilder<Pay>()
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(BeanPropertyRowMapper(Pay::class.java))
            .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
            .name("jdbcCursorItemReader")
            .build()
    }
}


