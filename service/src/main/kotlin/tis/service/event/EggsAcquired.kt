package tis.service.event

data class EggsAcquired(
    val sessionId: String,
    val acquireEggs: Int,
)