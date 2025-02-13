package tis.service.mission

class EggMissionDetailResponse(
    val missionId: String,
    val status: MissionStatus,
    val title: String,
    val description: String,
    val rewardEggCount: Int,
)
