package dev.hodol.sandbox.spring.job

import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.random.Random

@Configuration
class DecideJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun deciderJob() = jobBuilderFactory["deciderJob"]
        .start(startStep())
        .next(decider())
        .from(decider())
            .on("ODD")
            .to(oddStep())
        .from(decider())
            .on("EVEN")
            .to(evenStep())
        .end()
        .build()

    @Bean
    fun decider(): JobExecutionDecider = OddDecider()

    class OddDecider: JobExecutionDecider {
        override fun decide(jobExecution: JobExecution, stepExecution: StepExecution?): FlowExecutionStatus {
            val randomNumber = Random.nextInt(50) + 1
            log.info(">>>>>>>>> randomNumber: $randomNumber")
            if (randomNumber % 2 == 0) {
                return FlowExecutionStatus("EVEN")
            }
            return FlowExecutionStatus("ODD")
        }
    }

    @Bean
    fun startStep(): Step = stepBuilderFactory["startStep"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Start Step")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun evenStep(): Step = stepBuilderFactory["evenStep"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Even")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun oddStep(): Step = stepBuilderFactory["oddStep"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>> Odd")
            RepeatStatus.FINISHED
        }.build()

    companion object {
        private val log = LoggerFactory.getLogger(DecideJobConfiguration::class.java)
    }
}
