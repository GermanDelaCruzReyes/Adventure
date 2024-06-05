package network

import retrofit2.Response
import retrofit2.http.*
interface ApiServiceSalesPerson {
    @GET("personterritory/{businessEntityId}")
    suspend fun getSalesPersonById(@Path("businessEntityId") businessEntityId: Int): Response<SalesPerson>

    @POST("personterritory")
    suspend fun createSalesPerson(@Body salesPerson: SalesPerson): Response<SalesPerson>

    @PUT("personterritory/{businessEntityId}")
    suspend fun updateSalesPerson(@Path("businessEntityId") businessEntityId: Int, @Body salesPerson: SalesPerson): Response<SalesPerson>

    @DELETE("personterritory/{businessEntityId}")
    suspend fun deleteSalesPerson(@Path("businessEntityId") businessEntityId: Int): Response<Void>
}
