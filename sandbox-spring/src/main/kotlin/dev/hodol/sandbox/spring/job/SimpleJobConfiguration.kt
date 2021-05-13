package dev.hodol.sandbox.spring.job

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import dev.hodol.sandbox.spring.job.SimpleJobConfiguration
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun simpleJob(): Job = jobBuilderFactory["simpleJob"]
        .start(simpleStep1(null))
        .build()

    @Bean
    @JobScope
    fun simpleStep1(
        @Value("#{jobParameters[requestDate]}") requestDate: String?
    ): Step = stepBuilderFactory["simpleStep1"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>>>> Simple Step 1")
            log.info(">>>>>>>>>>>> Request Date: $requestDate")
            RepeatStatus.FINISHED
        }
        .build()

    companion object {
        private val log = LoggerFactory.getLogger(SimpleJobConfiguration::class.java)
    }
}
