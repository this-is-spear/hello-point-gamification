package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class StartGameCommand(
    @TargetAggregateIdentifier
    val sessionId: String,
)
