import dev.fbvictorhugo.pin_bridge_sdk.api.AccessTokenResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.GetBoardsResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.GetPinsResponse
import dev.fbvictorhugo.pin_bridge_sdk.data.PUserAccount
import dev.fbvictorhugo.pin_bridge_sdk.data.inners.CreativeType
import dev.fbvictorhugo.pin_bridge_sdk.data.inners.Privacy
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/v5/user_account")
    fun getUserAccount(
        @Header("Authorization") header: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json"
    ): Call<PUserAccount>

    @GET("/v5/boards")
    fun getListBoards(
        @Header("Authorization") header: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Query("bookmark") bookmark: String? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("privacy") privacy: Privacy? = null
    ): Call<GetBoardsResponse>

    @GET("/v5/boards/{board_id}/pins")
    fun getListPinsOnBoard(
        @Header("Authorization") header: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Path("board_id") boardId: String,
        @Query("bookmark") bookmark: String? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("creative_types") creativeTypes: CreativeType? = null,
        @Query("pin_metrics") pinMetrics: Boolean = false
    ): Call<GetPinsResponse>

}