package tis.service.mission

class SessionEggMissionView(
    val sessionMissionId: String,
    val title: String,
    val description: String,
    val rewardEggCount: Int,
    val status: MissionStatus,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SessionEggMissionView) return false

        if (sessionMissionId != other.sessionMissionId) return false

        return true
    }

    override fun hashCode(): Int {
        return sessionMissionId.hashCode()
    }
}
