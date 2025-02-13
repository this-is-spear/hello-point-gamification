package tis.service.mission

class EggMissionResponse(
    val missionId: String,
    val status: MissionStatus,
    val title: String,
    val rewardEggCount: Int,
)
