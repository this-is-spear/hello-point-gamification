package tis.service.mission

import tis.service.command.MissionStatus

class EggMissionDetailResponse(
    val missionId: String,
    val status: MissionStatus,
    val title: String,
    val description: String,
    val rewardEggCount: Int,
)
