import dev.fbvictorhugo.pin_bridge_sdk.data.PUserAccount
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PinterestApi {
    @FormUrlEncoded
    @POST("/v5/oauth/token")
    fun getAccessToken(
        @Header("Authorization") authHeader: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String?,
        @Field("redirect_uri") redirectUri: String
    ): Call<AccessTokenResponse>

    data class AccessTokenResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: Long,
        val scope: String,
        val refresh_token: String?,
        val refresh_token_expires_in: Long?
    )

    @GET("/v5/user_account")
    fun getUserAccount(
        @Header("Authorization") header: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json"
    ): Call<PUserAccount>

}