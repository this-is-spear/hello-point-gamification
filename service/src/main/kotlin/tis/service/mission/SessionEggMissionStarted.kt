package tis.service.mission

import tis.service.command.MissionStatus

class SessionEggMissionStarted(
    val sessionMissionId: String,
    val sessionId: String,
    val missionId: String,
    val title: String,
    val description: String,
    val rewardEggCount: Int,
    val status: MissionStatus,
)
