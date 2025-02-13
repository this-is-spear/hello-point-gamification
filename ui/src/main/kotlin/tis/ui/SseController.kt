package tis.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import tis.service.game.MessageSenderService

@RestController
@RequestMapping("/api/sse")
class SseController(
    private val messageSenderService: MessageSenderService,
) {
    @GetMapping("/connect")
    fun connect(
        @RequestParam sessionId: String,
    ): SseEmitter {
        val emitter = SseEmitter().apply {
            onTimeout { messageSenderService.deregister(sessionId) }
            onError { messageSenderService.deregister(sessionId) }
            onCompletion { messageSenderService.deregister(sessionId) }
        }
        val sender = SseMessageSender(emitter)
        messageSenderService.register(sessionId, sender)
        return emitter
    }
}
