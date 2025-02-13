package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

class CompleteSessionMissionCommand(
    @TargetAggregateIdentifier
    val sessionMissionId: String,
    val sessionId: String,
    val missionId: String,
)
