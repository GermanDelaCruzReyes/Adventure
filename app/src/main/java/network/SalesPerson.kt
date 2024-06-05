package network

data class SalesPerson(
    val businessEntityId: Int,
    val territoryId: Int,
    val salesQuota: Double,
    val bonus: Double,
    val commissionPct: Double
)
