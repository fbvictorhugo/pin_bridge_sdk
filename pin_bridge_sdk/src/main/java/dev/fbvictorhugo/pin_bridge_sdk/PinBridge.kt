package dev.fbvictorhugo.pin_bridge_sdk

import PinterestApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import com.google.gson.GsonBuilder
import dev.fbvictorhugo.pin_bridge_sdk.api.GetBoardsResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.GetPinsResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.PApiCallback
import dev.fbvictorhugo.pin_bridge_sdk.api.PResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.scopes.PScope
import dev.fbvictorhugo.pin_bridge_sdk.data.DataStoreManager
import dev.fbvictorhugo.pin_bridge_sdk.data.PAccessToken
import dev.fbvictorhugo.pin_bridge_sdk.data.PUserAccount
import dev.fbvictorhugo.pin_bridge_sdk.data.enums.CreativeType
import dev.fbvictorhugo.pin_bridge_sdk.data.enums.Privacy
import dev.fbvictorhugo.pin_bridge_sdk.utils.DateTypeAdapter
import dev.fbvictorhugo.pin_bridge_sdk.utils.PCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

/**
 * PinBridge is the class responsible for all API access and Library management
 */
object PinBridge {
    private const val API_PINTEREST_URL = "https://api.pinterest.com"
    private const val PINTEREST_OAUTH_URL = "https://www.pinterest.com/oauth/?"

    private lateinit var _clientId: String
    private lateinit var _redirectURI: String
    private val _scope: ArrayList<PScope> = ArrayList()
    private var _accessToken: String? = null
    private var _authorizationCode: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var api: PinterestApi
    private lateinit var dataStoreManager: DataStoreManager

    /**
     * @param[context] to configure [DataStoreManager].
     * @param[clientId] This is the unique ID for your app also referred to as App ID.
     * @param[redirectURI] This must be one of the redirect URIs registered on your app. The value of this parameter must exactly match the registered value.
     * @constructor Configure Instance of [PinBridge]
     */
    fun configureInstance(context: Context, clientId: String, redirectURI: String) {
        _clientId = clientId
        _redirectURI = redirectURI
        configureApiClient()
        dataStoreManager = DataStoreManager(context)

        runBlocking {
            launch {
                _accessToken = dataStoreManager.getToken()
            }
        }

    }

    /**
     * Configure Retrofit and instance PinterestApi Client
     */
    private fun configureApiClient() {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(API_PINTEREST_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(PinterestApi::class.java)
    }

    /**
     * Create string Authorization Header
     * @return ```"Bearer $_accessToken"```
     */
    fun buildAuthorizationHeader(): String {
        return "Bearer $_accessToken"
    }

    /**
     * Build URL OAuth Authorization
     */
    private fun buildAuthorizationUrl(
        clientId: String,
        redirectUri: String,
        scope: String
    ): String {
        return PINTEREST_OAUTH_URL +
                "client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&response_type=code" +
                "&scope=$scope"
    }

    /**
     * Add scopes for requests.
     *
     * @param[scopes] List of desired scopes
     * @see PScope
     */
    fun addScopes(vararg scopes: PScope) {
        _scope.addAll(scopes)
    }

    /**
     * Authenticate a flow based on the OAuth 2.0 authorization framework.
     * @param[context] to start Intent Action View.
     * @return [true] if you have already been authenticated and have a saved token
     * @see [PinBridge.interceptAuthorizationCode]
     */
    fun authenticate(context: Context): Boolean {
        if (_accessToken.isNullOrEmpty()) {
            val scopeString = _scope.joinToString(separator = ",") { it.scope }
            val authorizationUrl = buildAuthorizationUrl(_clientId, _redirectURI, scopeString)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl))
                .addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )

            context.startActivity(intent)
            return false
        } else {
            return true
        }
    }

    /**
     * Request Access Token.
     * When success returns, it automatically stores the token for the next calls.
     *
     * @param [clientSecret] App Secret Key in your Pinterest Apps.
     */
    fun requestAccessToken(clientSecret: String, callback: PApiCallback<PAccessToken>) {
        if (_accessToken == null) {
            val authHeader = "Basic " + Base64.encodeToString(
                ("$_clientId:$clientSecret").toByteArray(), Base64.NO_WRAP
            )
            api.getAccessToken(
                authHeader = authHeader,
                code = _authorizationCode,
                redirectUri = _redirectURI
            ).enqueue(object : Callback<PAccessToken> {

                override fun onResponse(
                    call: Call<PAccessToken>,
                    response: Response<PAccessToken>
                ) {
                    if (response.isSuccessful) {
                        saveToken(response.body()?.accessToken)
                        callback.onSuccessful(PResponse(response))
                    } else {
                        callback.onUnsuccessful(PResponse(response))
                    }
                }

                override fun onFailure(call: Call<PAccessToken>, t: Throwable) {
                    callback.onFailure(t)
                }
            })
        }
    }

    private fun saveToken(accessToken: String?) {
        _accessToken = accessToken

        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveToken(accessToken)
        }
    }

    /**
     * @return token
     */
    fun getToken(): String? {
        return _accessToken
    }

    /**
     * Intercept and setup the authorization code, received from the OAuth request ([PinBridge.authenticate]).
     */
    fun interceptAuthorizationCode(intent: Intent, callback: PCallback) {
        if (intent.action == Intent.ACTION_VIEW && intent.data.toString().contains(_redirectURI)) {
            _authorizationCode = Uri.parse(intent.data.toString()).getQueryParameter("code")

            if (_authorizationCode.isNullOrEmpty()) {
                callback.onUnsuccessful()
            } else {
                callback.onSuccessful()
            }
        } else {
            callback.onUnsuccessful()
        }
    }

    /**
     * Helper function to make an API call and handle its response or failure in a standardized way.
     *
     * @param [call] Retrofit Call object representing the API call to be executed.
     * @param [callback] Callback to be invoked when the API call completes.
     *
     * @param [T] The expected response type of the API call.
     */
    private fun <T> callApi(call: Call<T>, callback: PApiCallback<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onSuccessful(PResponse(response))
                } else {
                    callback.onUnsuccessful(PResponse(response))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    // region Requests

    sealed class Requests {

        sealed class UserAccount {
            companion object {

                /**
                 * Get account information for the "operation user_account".
                 */
                fun getUserAccount(callback: PApiCallback<PUserAccount>) {
                    val call = api.getUserAccount(buildAuthorizationHeader())
                    callApi(call, callback)
                }
            }
        }

        sealed class Boards {
            companion object {

                /**
                 * Get a list of the boards owned by the "operation user_account" +
                 * group boards where this account is a collaborator.
                 *
                 * @param [bookmark] Cursor used to fetch the next page of items
                 * @param [pageSize] Maximum number of items to include in a single page
                 *  of the response. (Default: 25)
                 * @param [privacy] Privacy setting for a board.
                 */
                fun getListBoards(
                    bookmark: String? = null,
                    pageSize: Int? = null,
                    privacy: Privacy? = null,
                    callback: PApiCallback<GetBoardsResponse>
                ) {
                    val call = api.getListBoards(
                        bookmark = bookmark,
                        pageSize = pageSize,
                        privacy = privacy,
                        header = buildAuthorizationHeader()
                    )
                    callApi(call, callback)
                }

                /**
                 * Get a list of the Pins on a board owned by the "operation user_account" -
                 * or on a group board that has been shared with this account.
                 *
                 * @param [boardId] Unique identifier of a board.
                 * @param [bookmark] Cursor used to fetch the next page of items
                 * @param [pageSize] Maximum number of items to include in a single page
                 *  of the response. (Default: 25)
                 * @param [creativeTypes] Pin creative types filter. [CreativeType]
                 */
                fun getListPinsOnBoard(
                    boardId: String,
                    bookmark: String? = null,
                    pageSize: Int? = null,
                    creativeTypes: CreativeType? = null,
                    // pinMetrics: Boolean = false,
                    callback: PApiCallback<GetPinsResponse>
                ) {
                    val call = api.getListPinsOnBoard(
                        boardId = boardId,
                        bookmark = bookmark,
                        pageSize = pageSize,
                        creativeTypes = creativeTypes,
                        // pinMetrics = pinMetrics,
                        header = buildAuthorizationHeader()
                    )
                    callApi(call, callback)
                }
            }
        }
    }

    //endregion

}