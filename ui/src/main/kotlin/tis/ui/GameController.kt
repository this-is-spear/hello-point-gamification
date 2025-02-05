package tis.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tis.service.GameService
import tis.service.query.GameSessionView
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/games")
class GameController(
    private val gameService: GameService,
) {
    @PostMapping("/start")
    fun start(
        @RequestParam sessionId: String,
    ): CompletableFuture<String> = gameService.start(sessionId)

    @GetMapping("/all")
    fun findAll(): CompletableFuture<List<GameSessionView>> = gameService.findAll()

    @GetMapping("/{sessionId}")
    fun findOne(
        @PathVariable sessionId: String,
    ): CompletableFuture<GameSessionView> = gameService.findOne(sessionId)
}
