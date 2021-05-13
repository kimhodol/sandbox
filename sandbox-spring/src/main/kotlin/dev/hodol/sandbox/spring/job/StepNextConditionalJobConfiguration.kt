package dev.hodol.sandbox.spring.job

import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.listener.StepExecutionListenerSupport
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class StepNextConditionalJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun stepNextConditionalJob() = jobBuilderFactory["stepNextConditionalJob"]
        .start(conditionalJobStep1())
            .on("FAILED")
            .to(conditionalJobStep3())
            .on("*")
            .end()
        .from(conditionalJobStep1())
            .on("COMPLETED WITH SKIPS")
            .to(errorPrint1())
            .on("*")
            .end()
        .from(conditionalJobStep1())
            .on("*")
            .to(conditionalJobStep2())
            .next(conditionalJobStep3())
            .on("*")
            .end()
        .end()
        .build()

    @Bean
    fun errorPrint1(): Step = stepBuilderFactory["errorPrint1"]
        .tasklet { _: StepContribution, _: ChunkContext ->
           log.error("************* Error 1")
           RepeatStatus.FINISHED
        }.build()


    @Bean
    fun conditionalJobStep1(): Step = stepBuilderFactory["conditionalJobStep1"]
        .tasklet { contribution: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>>>> Step 1")
            contribution.exitStatus = ExitStatus.FAILED
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun conditionalJobStep2(): Step = stepBuilderFactory["conditionalJobStep2"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>>>> Step 2")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun conditionalJobStep3(): Step = stepBuilderFactory["conditionalJobStep3"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info(">>>>>>>>>>>> Step 3")
            RepeatStatus.FINISHED
        }.build()

    companion object {
        private val log = LoggerFactory.getLogger(StepNextConditionalJobConfiguration::class.java)
    }
}

@Component
class SkipCheckingListener: StepExecutionListenerSupport() {
    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        val exitCode = stepExecution.exitStatus.exitCode
        if (exitCode != ExitStatus.FAILED.exitCode && stepExecution.skipCount > 0) {
            return ExitStatus("COMPLETED WITH SKIPS")
        }
        return null
    }
}
