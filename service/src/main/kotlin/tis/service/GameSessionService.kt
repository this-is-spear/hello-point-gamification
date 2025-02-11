package tis.service

import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service
import tis.core.FindOneGameQuery
import tis.core.StartGameCommand
import tis.service.dto.GameSessionIdResponse
import tis.service.dto.GameSessionResponse
import tis.service.query.GameSessionView

@Service
class GameSessionService(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {
    fun createGameSession(): CompletableFuture<GameSessionIdResponse> {
        val sessionId = UUID.randomUUID().toString()
        val startGameCommand = StartGameCommand(sessionId)
        return commandGateway.send<String>(startGameCommand)
            .thenApply {
                GameSessionIdResponse(sessionId)
            }
    }

    fun getGameSession(sessionId: String): CompletableFuture<GameSessionResponse> {
        return queryGateway.query(FindOneGameQuery(sessionId), GameSessionView::class.java).thenApply {
            GameSessionResponse(it.availableEggs, it.availablePoints)
        }
    }
}
