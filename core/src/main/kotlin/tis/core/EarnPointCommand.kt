package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class EarnPointCommand(
    @TargetAggregateIdentifier
    val sessionId : String,
    val amount: Int,
)
