package tis.service.mission

import java.util.UUID

class EggMission(
    val title: String,
    val description: String,
    val rewardEggCount: Int,
    val missionId: String = UUID.randomUUID().toString(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EggMission) return false

        if (missionId != other.missionId) return false

        return true
    }

    override fun hashCode(): Int {
        return missionId.hashCode()
    }
}
