package tis.service.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import tis.core.EarnPointCommand
import tis.core.StartGameSessionCommand
import tis.service.event.EarnPointEvent

@Aggregate
class GameSession {
    @AggregateIdentifier
    final lateinit var sessionId: String
    final var points: Int = 0

    constructor()

    @CommandHandler
    constructor(command: StartGameSessionCommand) {
        AggregateLifecycle.apply(StartGameSessionCommand(command.sessionId))
    }

    @CommandHandler
    fun handle(command: EarnPointCommand) {
        AggregateLifecycle.apply(EarnPointEvent(command.sessionId, command.amount))
    }

    @EventSourcingHandler
    fun on(event: StartGameSessionCommand) {
        sessionId = event.sessionId
    }

    @EventSourcingHandler
    fun on(event: EarnPointEvent) {
        sessionId = event.sessionId
        points += event.amount
    }
}
