package tis.ui

import java.util.concurrent.CompletableFuture
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tis.service.EggService
import tis.service.GameSessionService
import tis.service.dto.MyGameSessionIdResponse

@RestController
@RequestMapping("/api")
class GameController(
    private val gameSessionService: GameSessionService,
    private val eggService: EggService,
) {
    @PostMapping("/games")
    fun startGame(): CompletableFuture<MyGameSessionIdResponse> = gameSessionService.createGameSession()

    @GetMapping("/games")
    fun getGame(
        @RequestParam sessionId: String,
    ) = gameSessionService.getGameSession(sessionId)

    @PostMapping("/eggs/acquire")
    fun acquireEggs(
        @RequestParam sessionId: String,
    ) = eggService.acquireEgg(sessionId)

    @PostMapping("/eggs/break")
    fun breakEgg(
        @RequestParam sessionId: String,
    ) = eggService.breakEgg(sessionId)
}
