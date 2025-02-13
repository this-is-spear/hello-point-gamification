package tis.service.mission

import tis.service.command.MissionStatus

class EggMissionResponse(
    val missionId: String,
    val status: MissionStatus,
    val title: String,
    val rewardEggCount: Int,
)
