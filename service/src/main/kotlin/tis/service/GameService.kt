package tis.service

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service
import tis.core.FindAllGameSessionsQuery
import tis.core.FindOneGameSessionsQuery
import tis.core.StartGameSessionCommand
import tis.service.query.GameSessionView
import java.util.concurrent.CompletableFuture

@Service
class GameService(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {
    fun start(sessionId: String): CompletableFuture<String> {
        val startGameSessionCommand = StartGameSessionCommand(
            sessionId = sessionId,
        )
        return commandGateway.send(startGameSessionCommand)
    }

    fun findAll(): CompletableFuture<List<GameSessionView>> {
        return queryGateway.query(
            FindAllGameSessionsQuery(),
            ResponseTypes.multipleInstancesOf(GameSessionView::class.java)
        )
    }

    fun findOne(
        sessionId: String,
    ): CompletableFuture<GameSessionView> {
        return queryGateway.query(
            FindOneGameSessionsQuery(sessionId),
            ResponseTypes.instanceOf(GameSessionView::class.java)
        )
    }
}
