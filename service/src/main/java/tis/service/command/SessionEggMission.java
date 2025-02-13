package tis.service.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tis.core.CompleteSessionMissionCommand;
import tis.core.StartSessionMissionCommand;
import tis.service.event.EggsAcquired;
import tis.service.mission.SessionEggMissionCompleted;
import tis.service.mission.SessionEggMissionStarted;

@Aggregate
public class SessionEggMission {
    @AggregateIdentifier
    private String sessionMissionId;
    private String sessionId;
    private String missionId;
    private MissionStatus status;
    private String title;
    private String description;
    private Integer rewardEggCount;

    protected SessionEggMission() {
    }

    @CommandHandler
    public SessionEggMission(StartSessionMissionCommand command) {
        AggregateLifecycle.apply(new SessionEggMissionStarted(
                command.getSessionMissionId(),
                command.getSessionId(),
                command.getMissionId(),
                command.getTitle(),
                command.getDescription(),
                command.getRewardEggCount(),
                MissionStatus.IN_PROGRESS
        ));
        AggregateLifecycle.apply(new EggsAcquired(
                command.getSessionId(),
                command.getRewardEggCount()
        ));
    }

    @CommandHandler
    public void handle(CompleteSessionMissionCommand command) {
        AggregateLifecycle.apply(new SessionEggMissionCompleted(
                command.getSessionMissionId(),
                command.getSessionId(),
                command.getMissionId(),
                MissionStatus.COMPLETED
        ));
    }

    @EventSourcingHandler
    public void on(SessionEggMissionStarted sessionEggMissionStarted) {
        this.sessionMissionId = sessionEggMissionStarted.getSessionMissionId();
        this.sessionId = sessionEggMissionStarted.getSessionId();
        this.missionId = sessionEggMissionStarted.getMissionId();
        this.status = sessionEggMissionStarted.getStatus();
        this.title = sessionEggMissionStarted.getTitle();
        this.description = sessionEggMissionStarted.getDescription();
        this.rewardEggCount = sessionEggMissionStarted.getRewardEggCount();
    }

    @EventSourcingHandler
    public void on(SessionEggMissionCompleted sessionEggMissionCompleted) {
        this.sessionMissionId = sessionEggMissionCompleted.getSessionMissionId();
        this.status = sessionEggMissionCompleted.getStatus();
    }
}
