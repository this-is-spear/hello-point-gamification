package tis.internal

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tis.service.EggMissionService

@RestController
@RequestMapping("/api/internal")
class MissionInternalController(
    private val eggMissionService: EggMissionService,
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/missions")
    fun createMission(
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam rewardEggCount: Int,
    ) {
        log.info("Creating mission: title=$title, description=$description, rewardEggCount=$rewardEggCount")
        eggMissionService.createEggMission(title, description, rewardEggCount)
    }
}
