package tis.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tis.service.EggMissionService

@RestController
@RequestMapping("/api")
class MissionController(
    private val eggMissionService: EggMissionService,
) {
    @GetMapping("/missions")
    fun getMissions(
        @RequestParam sessionId: String,
    ) = eggMissionService.getSessionEggMissions(sessionId)

    @GetMapping("/missions/{missionId}")
    fun getMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ) = eggMissionService.getEggMission(sessionId, missionId)

    @PostMapping("/missions/{missionId}/start")
    fun startMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ) = eggMissionService.startEggMission(sessionId, missionId)

    @PostMapping("/missions/{missionId}/complete")
    fun completeMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ) = eggMissionService.completeEggMission(sessionId, missionId)
}
