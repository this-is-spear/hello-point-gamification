package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class StartGameSessionCommand(
    @TargetAggregateIdentifier
    val sessionId : String,
)
