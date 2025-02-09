package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class BreakEggsCommand(
    @TargetAggregateIdentifier
    val sessionId: String,
    val earnedPoint: Int,
)
