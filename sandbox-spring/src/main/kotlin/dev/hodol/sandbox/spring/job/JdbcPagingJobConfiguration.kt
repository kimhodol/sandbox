package dev.hodol.sandbox.spring.job

import dev.hodol.sandbox.spring.job.domain.Pay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.PagingQueryProvider
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource


@Configuration
class JdbcPagingJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val dataSource: DataSource
) {
    companion object {
        private const val chunkSize = 10
        private val log: Logger = LoggerFactory.getLogger(JdbcPagingJobConfiguration::class.java)
    }

    @Bean
    fun jdbcPagingItemReaderJob(): Job {
        return jobBuilderFactory["jdbcPagingItemReaderJob"]
            .start(jdbcPagingItemReaderStep())
            .build()
    }

    @Bean
    fun jdbcPagingItemReaderStep(): Step {
        return stepBuilderFactory["jdbcPagingItemReaderStep"]
            .chunk<Pay, Pay>(chunkSize)
            .reader(jdbcPagingItemReader())
            .writer(jdbcPagingItemWriter())
            .build()
    }

    @Bean
    fun jdbcPagingItemReader(): JdbcPagingItemReader<Pay> = JdbcPagingItemReaderBuilder<Pay>()
        .pageSize(chunkSize)
        .fetchSize(chunkSize)
        .dataSource(dataSource)
        .rowMapper(BeanPropertyRowMapper(Pay::class.java))
        .queryProvider(createQueryProvider())
        .parameterValues(mapOf("amount" to 2000))
        .name("jdbcPagingItemReader")
        .build()

    private fun jdbcPagingItemWriter(): ItemWriter<Pay> {
        return ItemWriter { list: List<Pay> ->
            for (pay in list) {
                log.info("Current Pay=$pay")
            }
        }
    }

    @Bean
    fun createQueryProvider(): PagingQueryProvider = SqlPagingQueryProviderFactoryBean().apply {
        setDataSource(dataSource)
        setSelectClause("id, amount, tx_name, tx_date_time")
        setFromClause("from pay")
        setWhereClause("where amount >= :amount")
        setSortKeys(mapOf("id" to Order.ASCENDING))
    }.`object`
}


