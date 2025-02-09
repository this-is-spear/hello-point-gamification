package tis.service.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tis.core.AcquireEggCommand;
import tis.core.BreakEggsCommand;
import tis.core.StartGameCommand;
import tis.service.event.EggBroken;
import tis.service.event.EggsAcquired;
import tis.service.event.GameSessionStarted;

@Aggregate
public class MyGameSession {
    @AggregateIdentifier
    private String sessionId;
    private int availableEggs;
    private int availablePoints;

    public MyGameSession() {
    }

    @CommandHandler
    public MyGameSession(StartGameCommand startGameCommand) {
        AggregateLifecycle.apply(new GameSessionStarted(startGameCommand.getSessionId()));
    }

    @CommandHandler
    public void handle(BreakEggsCommand command) {
        AggregateLifecycle.apply(new EggBroken(command.getSessionId(), command.getEarnedPoint()));
    }

    @CommandHandler
    public void handle(AcquireEggCommand command) {
        AggregateLifecycle.apply(new EggsAcquired(command.getSessionId(), command.getAcquireEggs()));
    }

    @EventSourcingHandler
    public void on(GameSessionStarted gameSessionStarted) {
        this.sessionId = gameSessionStarted.getSessionId();
        this.availableEggs = 0;
        this.availablePoints = 0;
    }

    @EventSourcingHandler
    public void on(EggBroken event) {
        sessionId = event.getSessionId();
        availableEggs--;
        availablePoints += event.getEarnedPoints();
    }

    @EventSourcingHandler
    public void on(EggsAcquired event) {
        sessionId = event.getSessionId();
        availableEggs += event.getAcquireEggs();
    }
}
