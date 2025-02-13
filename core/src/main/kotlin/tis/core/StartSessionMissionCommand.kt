package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

class StartSessionMissionCommand(
    @TargetAggregateIdentifier
    val sessionMissionId: String,
    val sessionId: String,
    val missionId: String,
    val title: String,
    val description: String,
    val rewardEggCount: Int,
)
