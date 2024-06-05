package network
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceSalesTerritory {
    @GET("salesterritory")
    fun getSalesTerritories(): Call<List<SalesTerritory>>

    @GET("salesterritory/{territoryId}")
    suspend fun getSalesTerritoryById(@Path("territoryId") territoryId: Int): Response<SalesTerritory>

    @POST("salesterritory")
    suspend fun createSalesTerritory(@Body salesTerritory: SalesTerritory): Response<SalesTerritory>

    @PUT("salesterritory/{territoryId}")
    suspend fun updateSalesTerritory(@Path("territoryId") territoryId: Int, @Body salesTerritory: SalesTerritory): Response<SalesTerritory>

    @DELETE("salesterritory/{territoryId}")
    suspend fun deleteSalesTerritory(@Path("territoryId") territoryId: Int): Response<Void>
}