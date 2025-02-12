package tis.service.event

class GameSessionUpdated(
    val sessionId: String,
    val availableEggs: Int,
    val availablePoints: Int,
)
