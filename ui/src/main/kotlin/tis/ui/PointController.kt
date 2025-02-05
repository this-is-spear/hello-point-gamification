package tis.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tis.service.PointService
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/points")
class PointController(
    private val pointService: PointService,
) {
    @GetMapping("/earn")
    fun earn(
        @RequestParam(value = "sessionId", defaultValue = "1") sessionId: String,
        @RequestParam(value = "amount", defaultValue = "1") amount: Int,
    ): CompletableFuture<String> = pointService.earn(sessionId, amount)
}
