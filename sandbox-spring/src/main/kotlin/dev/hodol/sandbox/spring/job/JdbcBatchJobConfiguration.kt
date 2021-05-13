package dev.hodol.sandbox.spring.job

import dev.hodol.sandbox.spring.job.domain.Pay
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

@Configuration
class JdbcBatchJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val dataSource: DataSource
) {
    companion object {
        private const val JOB_NAME = "jdbcBatch"
        private const val chunkSize = 10
    }

    @Bean(JOB_NAME)
    fun job(): Job {
        return jobBuilderFactory["jdbcBatchJob"]
            .start(step())
            .build()
    }

    @Bean("${JOB_NAME}_step")
    fun step(): Step {
        return stepBuilderFactory["jdbcBatchStep"]
            .chunk<Pay, Pay>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean
    fun reader(): JdbcCursorItemReader<Pay> {
        return JdbcCursorItemReaderBuilder<Pay>()
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(BeanPropertyRowMapper(Pay::class.java))
            .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
            .name("jdbcBatchReader")
            .build()
    }

    fun processor(): ItemProcessor<Pay, Pay> {
        return ItemProcessor { it.copy(amount = it.amount + 10000) }
    }

    @Bean
    fun writer(): ItemWriter<Pay> {
        return JdbcBatchItemWriterBuilder<Pay>()
            .dataSource(dataSource)
            .sql("INSERT INTO pay2 (amount, tx_name, tx_date_time) VALUES (:amount, :txName, :txDateTime)")
            .beanMapped()
            .build()
    }
}
