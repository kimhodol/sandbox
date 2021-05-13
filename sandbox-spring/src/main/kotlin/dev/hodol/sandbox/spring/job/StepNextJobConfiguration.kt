package dev.hodol.sandbox.spring.job

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StepNextJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun stepNextJob(): Job = jobBuilderFactory["stepNextJob"]
        .start(step1())
        .next(step2())
        .next(step3())
        .build()

    @Bean
    fun step1(): Step = stepBuilderFactory["step1"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Step 1")
            RepeatStatus.FINISHED
        }
        .build()

    @Bean
    fun step2(): Step = stepBuilderFactory["step2"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Step 2")
            RepeatStatus.FINISHED
        }
        .build()

    @Bean
    fun step3(): Step = stepBuilderFactory["step3"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Step 3")
            RepeatStatus.FINISHED
        }
        .build()

    companion object {
        private val log = LoggerFactory.getLogger(StepNextJobConfiguration::class.java)
    }
}
