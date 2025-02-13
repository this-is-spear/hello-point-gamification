package tis.service.mission

import tis.service.command.MissionStatus

data class SessionEggMissionCompleted(
    val sessionMissionId: String,
    val sessionId: String,
    val missionId: String,
    val status: MissionStatus,
)
