package tis.service

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import tis.core.EarnPointCommand
import java.util.concurrent.CompletableFuture

@Service
class PointService(
    private val commandGateway: CommandGateway,
) {
    fun earn(
        sessionId: String, amount: Int,
    ): CompletableFuture<String> {
        val earnPointCommand = EarnPointCommand(
            sessionId = sessionId,
            amount = amount,
        )
        return commandGateway.send(earnPointCommand)
    }
}
