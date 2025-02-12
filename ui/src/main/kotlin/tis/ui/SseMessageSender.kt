package tis.ui

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import tis.service.session.MessageSender

class SseMessageSender(
    private val sseEmitter: SseEmitter,
) : MessageSender {
    override fun send(message: String) {
        try {
            sseEmitter.send(
                SseEmitter.event()
                    .data(message)
                    .build()
            )
        } catch (e: Exception) {
            sseEmitter.completeWithError(e)
        }
    }
}
