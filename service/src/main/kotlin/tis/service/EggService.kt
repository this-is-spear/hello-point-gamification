package tis.service

import java.util.concurrent.CompletableFuture
import kotlin.random.Random
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service
import tis.core.AcquireEggCommand
import tis.core.BreakEggsCommand
import tis.service.egg.AcquireEggResponse
import tis.service.egg.BreakEggResponse

@Service
class EggService(
    private val commandGateway: CommandGateway,
) {
    fun breakEgg(sessionId: String): CompletableFuture<BreakEggResponse> {
        val earnedPoints = Random.nextInt(1, 11)

        return commandGateway.send<String>(BreakEggsCommand(sessionId, earnedPoints))
            .thenApply {
                BreakEggResponse(earnedPoints)
            }
    }

    fun acquireEgg(sessionId: String): CompletableFuture<AcquireEggResponse> {
        val acquireEggs = 1
        return commandGateway.send<String>(AcquireEggCommand(sessionId, acquireEggs))
            .thenApply {
                AcquireEggResponse(acquireEggs)
            }
    }
}
