package tis.core

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class AcquireEggCommand(
    @TargetAggregateIdentifier
    val sessionId: String,
    val acquireEggs: Int,
)
