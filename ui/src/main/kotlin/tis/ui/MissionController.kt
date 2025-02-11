package tis.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MissionController {
    @GetMapping("/missions")
    fun getMissions(
        @RequestParam sessionId: String,
    ): List<MissionResponse> {
        println("sessionId: $sessionId getMissions")
        return listOf(
            MissionResponse(
                missionId = "1",
                status = MissionStatus.NOT_STARTED,
                title = "링크 누르기 1",
                rewardEggCount = 5,
            ),
            MissionResponse(
                missionId = "2",
                status = MissionStatus.IN_PROGRESS,
                title = "링크 누르기 2",
                rewardEggCount = 10,
            ),
        )
    }

    @GetMapping("/missions/{missionId}")
    fun getMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ): MissionDetailResponse {
        println("sessionId: $sessionId getMission $missionId")
        return MissionDetailResponse(
            missionId = missionId,
            status = MissionStatus.NOT_STARTED,
            title = "Break 10 eggs",
            description = "Break 10 eggs to earn 5 eggs",
            rewardEggCount = 5,
        )
    }

    @PostMapping("/missions/{missionId}/start")
    fun startMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ) {
        println("sessionId: $sessionId startMission $missionId")
        println("userMissionId ${sessionId+missionId}")
    }

    @PostMapping("/missions/{missionId}/complete")
    fun completeMission(
        @RequestParam sessionId: String,
        @PathVariable missionId: String,
    ) {
        println("sessionId: $sessionId completeMission $missionId")
        println("userMissionId ${sessionId+missionId}")
    }

    enum class MissionStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
    }

    class MissionResponse(
        val missionId: String,
        val status: MissionStatus,
        val title: String,
        val rewardEggCount: Int,
    )

    class MissionDetailResponse(
        val missionId: String,
        val status: MissionStatus,
        val title: String,
        val description: String,
        val rewardEggCount: Int,
    )
}
