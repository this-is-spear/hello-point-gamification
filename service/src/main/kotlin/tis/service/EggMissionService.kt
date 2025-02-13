package tis.service

import java.util.concurrent.CompletableFuture
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service
import tis.core.AcquireEggCommand
import tis.core.CompleteSessionMissionCommand
import tis.core.FindAllSessionMissionQuery
import tis.core.FindOneSessionMissionQuery
import tis.core.StartSessionMissionCommand
import tis.service.command.MissionStatus
import tis.service.mission.EggMissionDetailResponse
import tis.service.mission.EggMissionRepository
import tis.service.mission.EggMissionResponse
import tis.service.mission.SessionEggMissionView

@Service
class EggMissionService(
    private val missionRepository: EggMissionRepository,
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {
    fun getSessionEggMissions(sessionId: String): CompletableFuture<List<EggMissionResponse>> {
        val eggMissions = missionRepository.findAll()
        val sessionEggMissions = queryGateway.query(
            FindAllSessionMissionQuery(sessionId),
            ResponseTypes.multipleInstancesOf(SessionEggMissionView::class.java)
        ).get()
        return CompletableFuture.completedFuture(
            eggMissions.values.map { eggMission ->
                val sessionEggMission =
                    sessionEggMissions.find { it.sessionMissionId == sessionId + eggMission.missionId }
                EggMissionResponse(
                    eggMission.missionId,
                    sessionEggMission?.status ?: MissionStatus.NOT_STARTED,
                    eggMission.title,
                    eggMission.rewardEggCount,
                )
            }
        )
    }

    fun getEggMission(sessionId: String, missionId: String): CompletableFuture<EggMissionDetailResponse> {
        val eggMission = missionRepository.findOne(missionId) ?: throw IllegalArgumentException("Mission not found")
        val sessionEggMission = queryGateway.query(
            FindOneSessionMissionQuery(sessionId, missionId),
            SessionEggMissionView::class.java
        ).get()
        return CompletableFuture.completedFuture(
            EggMissionDetailResponse(
                missionId,
                sessionEggMission?.status ?: MissionStatus.NOT_STARTED,
                eggMission.title,
                eggMission.description,
                eggMission.rewardEggCount,
            )
        )
    }

    fun startEggMission(sessionId: String, missionId: String): CompletableFuture<String> {
        val eggMission = missionRepository.findOne(missionId) ?: throw IllegalArgumentException("Mission not found")
        val startSessionMissionCommand = StartSessionMissionCommand(
            getSessionMissionId(sessionId, missionId),
            sessionId,
            missionId,
            eggMission.title,
            eggMission.description,
            eggMission.rewardEggCount,
        )
        val sessionMissionId = commandGateway.send<String>(startSessionMissionCommand)
        return sessionMissionId
    }

    fun completeEggMission(sessionId: String, missionId: String): CompletableFuture<String> {
        val sessionMission = queryGateway.query(
            FindOneSessionMissionQuery(sessionId, missionId),
            SessionEggMissionView::class.java
        ).get() ?: throw IllegalArgumentException("Mission not found")
        val completeSessionMissionCommand =
            CompleteSessionMissionCommand(getSessionMissionId(sessionId, missionId), sessionId, missionId)
        val acquireEggCommand = AcquireEggCommand(sessionId, sessionMission.rewardEggCount)

        return commandGateway.send<String>(completeSessionMissionCommand).thenApply {
            commandGateway.send<String>(acquireEggCommand)
            it
        }
    }

    fun createEggMission(title: String, description: String, rewardEggCount: Int) =
        missionRepository.createEggMission(title, description, rewardEggCount)

    private fun getSessionMissionId(sessionId: String, missionId: String) = sessionId + missionId
}
