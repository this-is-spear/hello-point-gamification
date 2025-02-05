package tis.service.query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Repository
import tis.core.FindAllGameSessionsQuery
import tis.core.FindOneGameSessionsQuery
import tis.core.StartGameSessionCommand
import tis.service.command.GameSession
import tis.service.event.EarnPointEvent

@Repository
class GameSessionProjection {
    private val gameSessions = mutableMapOf<String, GameSession>()

    @EventHandler
    fun on(event: StartGameSessionCommand) {
        gameSessions.getOrPut(event.sessionId) { GameSession() }.apply {
            sessionId = event.sessionId
        }
    }

    /**
     * 한 번 포인트를 먼저 적립했더니 gameSessions[sessionId] 값을 조회해서 바로 넣질 못한다...
     * 존재하는지 확인하고 없다면 새로 생성해야 하는 기이한 현상을 유지해야 한다.
     * 이런 경우 이벤트를 삭제해야하는걸까?
     */
    @EventHandler
    fun on(event: EarnPointEvent) {
        gameSessions.getOrPut(event.sessionId) { GameSession() }.apply {
            sessionId = event.sessionId
            points += event.amount
        }
    }

    @QueryHandler
    fun handle(query: FindAllGameSessionsQuery): List<GameSessionView> {
        return gameSessions.values.map {
            GameSessionView(it.sessionId, it.points)
        }.toList()
    }

    @QueryHandler
    fun handle(query: FindOneGameSessionsQuery): GameSessionView {
        val gameSession = gameSessions[query.sessionId] ?: throw IllegalArgumentException("Game session not found")
        return gameSession.let {
            GameSessionView(it.sessionId, it.points)
        }
    }
}
