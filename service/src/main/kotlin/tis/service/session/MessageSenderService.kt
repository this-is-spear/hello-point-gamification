package tis.service.session

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Service
import tis.service.dto.GameSessionResponse
import tis.service.event.GameSessionUpdated

@Service
class MessageSenderService(
    private val objectMapper: ObjectMapper,
) {
    private val gameSessions: MutableMap<String, MessageSender> = mutableMapOf()

    init {
        objectMapper.registerKotlinModule()
    }

    fun register(sessionId: String, sender: MessageSender) {
        gameSessions[sessionId] = sender
    }

    fun deregister(sessionId: String) {
        gameSessions.remove(sessionId)
    }

    @EventHandler
    fun on(event: GameSessionUpdated) {
        val gameSessionResponse = GameSessionResponse(event.availableEggs, event.availablePoints)
        val message = objectMapper.writeValueAsString(gameSessionResponse)
        gameSessions[event.sessionId]?.send(message)
    }
}
