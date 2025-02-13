package tis.service.mission

import java.util.UUID
import org.springframework.stereotype.Repository

@Repository
class EggMissionRepository {
    private val eggMissions = mutableMapOf<String, EggMission>()

    fun createEggMission(title: String, description: String, rewardEggCount: Int) {
        val missionId = UUID.randomUUID().toString()
        eggMissions[missionId] = EggMission(title, description, rewardEggCount, missionId)
    }

    fun findAll() = eggMissions

    fun findOne(missionId: String) = eggMissions[missionId]
}
