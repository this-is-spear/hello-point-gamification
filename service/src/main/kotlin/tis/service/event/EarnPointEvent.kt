package tis.service.event

data class EarnPointEvent(
    val sessionId: String,
    val amount: Int,
)
