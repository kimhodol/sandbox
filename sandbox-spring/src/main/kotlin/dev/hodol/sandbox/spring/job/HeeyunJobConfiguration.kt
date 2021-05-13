package dev.hodol.sandbox.spring.job

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HeeyunJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun heeyunJob() = jobBuilderFactory["heeyunJob"]
        .start(narrationDemo())
        .next(storyBoard())
        .next(printModelingFoundation())
        .next(unitySceneFoundation())
        .build()

    @Bean
    fun narrationDemo(): Step = stepBuilderFactory["narrationDemo"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info("나레이션 데모를 만들자")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun storyBoard(): Step = stepBuilderFactory["storyBoard"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info("스토리보드 콘티를 짜자")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun printModelingFoundation(): Step = stepBuilderFactory["narrationDemo"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info("프린트 모델링 foundation을 공부하자")
            RepeatStatus.FINISHED
        }.build()

    @Bean
    fun unitySceneFoundation(): Step = stepBuilderFactory["narrationDemo"]
        .tasklet { _: StepContribution, _: ChunkContext ->
            log.info("Unity Scene Foundation을 공부하자")
            RepeatStatus.FINISHED
        }.build()

    companion object {
        val log: Logger = LoggerFactory.getLogger(HeeyunJobConfiguration::class.java)
    }
}
