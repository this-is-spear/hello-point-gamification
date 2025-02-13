package tis.service.game

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import tis.core.FindOneGameQuery
import tis.service.event.EggBroken
import tis.service.event.EggsAcquired
import tis.service.event.GameSessionStarted

@Repository
class GameSessionProjection(
    private val sessionMap: MutableMap<String, GameSessionView> = mutableMapOf(),
) {
    private val log: Logger = LoggerFactory.getLogger(GameSessionProjection::class.java)

    @EventHandler
    fun on(gameSessionStarted: GameSessionStarted) {
        log.info("Creating session with id: ${gameSessionStarted.sessionId}")
        sessionMap[gameSessionStarted.sessionId] = GameSessionView(gameSessionStarted.sessionId, 0, 0)
    }

    @EventHandler
    fun on(event: EggBroken) {
        log.info("Breaking egg in session with id: ${event.sessionId}")
        val gameSession = sessionMap[event.sessionId] ?: throw IllegalArgumentException("Session not found")
        val updatedGameSession = GameSessionView(
            sessionId = event.sessionId,
            availableEggs = gameSession.availableEggs - 1,
            availablePoints = gameSession.availablePoints + event.earnedPoints
        )
        sessionMap[event.sessionId] = updatedGameSession
    }

    @EventHandler
    fun on(event: EggsAcquired) {
        log.info("Acquiring egg in session with id: ${event.sessionId}")
        val gameSession = sessionMap[event.sessionId] ?: throw IllegalArgumentException("Session not found")
        val updatedGameSession = GameSessionView(
            sessionId = event.sessionId,
            availableEggs = gameSession.availableEggs + event.acquireEggs,
            availablePoints = gameSession.availablePoints
        )
        sessionMap[event.sessionId] = updatedGameSession
    }

    @QueryHandler
    fun query(query: FindOneGameQuery): GameSessionView {
        log.info("Getting session with id: ${query.sessionId}")
        return sessionMap[query.sessionId] ?: throw IllegalArgumentException("Session not found")
    }
}
