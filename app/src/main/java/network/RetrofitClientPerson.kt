package network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientPerson {
    private const val BASE_URL = "http://adventureworks.somee.com/api/"

    val instance: ApiServiceSalesPerson by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiServiceSalesPerson::class.java)
    }
}