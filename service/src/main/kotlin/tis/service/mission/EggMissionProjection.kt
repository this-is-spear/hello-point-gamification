package tis.service.mission

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Repository
import tis.core.FindAllSessionMissionQuery
import tis.core.FindOneSessionMissionQuery

@Repository
class EggMissionProjection {
    private val usersMissions = mutableMapOf<String, MutableMap<String, SessionEggMissionView>>()

    @EventHandler
    fun startSessionMission(event: SessionEggMissionStarted) {
        val sessionMissionId = event.sessionMissionId
        val sessionEggMissionView = SessionEggMissionView(
            sessionMissionId = sessionMissionId,
            title = event.title,
            description = event.description,
            rewardEggCount = event.rewardEggCount,
            status = event.status,
        )
        usersMissions.getOrPut(event.sessionId) { mutableMapOf() }[sessionMissionId] = sessionEggMissionView
    }

    @EventHandler
    fun completeSessionMission(event: SessionEggMissionCompleted) {
        val sessionMissionId = event.sessionMissionId
        val sessionEggMissionViews = usersMissions.getOrDefault(event.sessionId, emptyMap())[sessionMissionId]
        requireNotNull(sessionEggMissionViews) { "Mission not found" }
        val eggMissionViews = usersMissions[event.sessionId]!!
        requireNotNull(eggMissionViews[sessionMissionId]) { "Mission not found" }
        eggMissionViews[sessionMissionId] = SessionEggMissionView(
            sessionMissionId = sessionMissionId,
            title = sessionEggMissionViews.title,
            description = sessionEggMissionViews.description,
            rewardEggCount = sessionEggMissionViews.rewardEggCount,
            status = event.status,
        )
    }

    @QueryHandler
    fun getSessionEggMissions(query: FindAllSessionMissionQuery): List<SessionEggMissionView> {
        val sessionId = query.sessionId
        val sessionEggMissionViews = usersMissions.getOrDefault(sessionId, emptyMap())
        return sessionEggMissionViews.values.toList()
    }

    @QueryHandler
    fun getSessionEggMission(query: FindOneSessionMissionQuery): SessionEggMissionView? {
        val sessionId = query.sessionId
        val sessionMissionId = query.sessionId + query.missionId
        val sessionEggMissionViews = usersMissions.getOrDefault(sessionId, emptyMap())
        return sessionEggMissionViews[sessionMissionId]
    }
}
