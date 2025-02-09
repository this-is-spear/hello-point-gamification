package tis.service.query

class GameSessionView(
    val sessionId: String,
    val availableEggs: Int,
    val availablePoints: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameSessionView) return false

        if (sessionId != other.sessionId) return false

        return true
    }

    override fun hashCode(): Int {
        return sessionId.hashCode()
    }
}
