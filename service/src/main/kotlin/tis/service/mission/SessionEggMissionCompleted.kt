package tis.service.mission

data class SessionEggMissionCompleted(
    val sessionMissionId: String,
    val sessionId: String,
    val missionId: String,
    val status: MissionStatus,
)
