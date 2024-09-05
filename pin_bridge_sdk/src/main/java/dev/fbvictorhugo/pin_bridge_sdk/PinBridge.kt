package dev.fbvictorhugo.pin_bridge_sdk

import PinterestApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import com.google.gson.GsonBuilder
import dev.fbvictorhugo.pin_bridge_sdk.api.GetBoardsResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.GetPinsResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.PCallback
import dev.fbvictorhugo.pin_bridge_sdk.api.PResponse
import dev.fbvictorhugo.pin_bridge_sdk.api.scopes.PScope
import dev.fbvictorhugo.pin_bridge_sdk.data.PAccessToken
import dev.fbvictorhugo.pin_bridge_sdk.data.PUserAccount
import dev.fbvictorhugo.pin_bridge_sdk.data.enums.CreativeType
import dev.fbvictorhugo.pin_bridge_sdk.data.enums.Privacy
import dev.fbvictorhugo.pin_bridge_sdk.utils.DateTypeAdapter
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

    private lateinit var _AppContext: Context
    private lateinit var _clientId: String
    private lateinit var _redirectURI: String
    private val _scope: ArrayList<PScope> = ArrayList()
    private var _accessToken: String? = null
    private var _authorizationCode: String? = null
    private lateinit var retrofit: Retrofit
    private lateinit var api: PinterestApi

    /**
     * @param[context] App Context.
     * @param[clientId] This is the unique ID for your app also referred to as App ID.
     * @param[redirectURI] This must be one of the redirect URIs registered on your app. The value of this parameter must exactly match the registered value.
     * @constructor Configure Instance of [PinBridge]
     */
    fun configureInstance(context: Context, clientId: String, redirectURI: String) {
        _AppContext = context
        _clientId = clientId
        _redirectURI = redirectURI
        _accessToken //TODO Restore token
        configureApiClient()
    }

    private fun configureApiClient() {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.pinterest.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(PinterestApi::class.java)
    }

    private fun buildBearerToken(): String {
        return "Bearer $_accessToken"
    }

    /**
     * build URL OAuth Authorization
     */
    private fun buildAuthorizationUrl(
        clientId: String,
        redirectUri: String,
        scope: String
    ): String {
        return "https://www.pinterest.com/oauth/?" +
                "client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&response_type=code" +
                "&scope=$scope"
    }

    /**
     * Add scopes for requests.
     *
     * Default scopes: [PScope.Pins.Read], [PScope.Boards.Read].
     * @param[scopes] List of desired scopes
     */
    fun addScopes(vararg scopes: PScope) {
        _scope.addAll(scopes)
    }

    /**
     * Authenticate a flow based on the OAuth 2.0 authorization framework.
     *
     * @see [PinBridge.interceptAuthorizationCode]
     */
    fun authenticate() {
        if (_accessToken.isNullOrEmpty()) {
            val scopeString = _scope.joinToString(separator = ",") { it.scope }
            val authorizationUrl = buildAuthorizationUrl(_clientId, _redirectURI, scopeString)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl))
                .addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                )

            _AppContext.startActivity(intent)
        }
    }

    /**
     * Request Access Token.
     * When success returns, it automatically stores the token for the next calls.
     *
     * @param [clientSecret] aka App Secret or App Secret Key.
     */
    fun requestAccessToken(clientSecret: String, callback: PCallback<PAccessToken>) {
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
                        _accessToken = response.body()?.accessToken
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

    /**
     * Intercept and setup the authorization code, received from the OAuth request ([PinBridge.authenticate]).
     */
    fun interceptAuthorizationCode(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW && intent.data.toString().contains(_redirectURI)) {
            _authorizationCode = Uri.parse(intent.data.toString()).getQueryParameter("code")
        }
    }

    sealed class Requests {

        sealed class UserAccount {
            companion object {

                fun getUserAccount(callback: PCallback<PUserAccount>) {
                    api.getUserAccount(
                        buildBearerToken()
                    ).enqueue(object : Callback<PUserAccount> {
                        override fun onResponse(
                            call: Call<PUserAccount>,
                            response: Response<PUserAccount>
                        ) {
                            if (response.isSuccessful) {
                                callback.onSuccessful(PResponse(response))
                            } else {
                                callback.onUnsuccessful(PResponse(response))
                            }
                        }

                        override fun onFailure(call: Call<PUserAccount>, t: Throwable) {
                            callback.onFailure(t)
                        }

                    })
                }
            }
        }

        sealed class Boards {
            companion object {

                fun getListBoards(
                    bookmark: String? = null,
                    pageSize: Int? = null,
                    privacy: Privacy? = null,
                    callback: PCallback<GetBoardsResponse>
                ) {
                    api.getListBoards(
                        bookmark = bookmark,
                        pageSize = pageSize,
                        privacy = privacy,
                        header = buildBearerToken()
                    ).enqueue(object : Callback<GetBoardsResponse> {
                        override fun onResponse(
                            call: Call<GetBoardsResponse>,
                            response: Response<GetBoardsResponse>
                        ) {
                            if (response.isSuccessful) {
                                callback.onSuccessful(PResponse(response))
                            } else {
                                callback.onUnsuccessful(PResponse(response))
                            }
                        }

                        override fun onFailure(call: Call<GetBoardsResponse>, t: Throwable) {
                            callback.onFailure(t)
                        }
                    })
                }

                fun getListPinsOnBoard(
                    boardId: String,
                    bookmark: String? = null,
                    pageSize: Int? = null,
                    creativeTypes: CreativeType? = null,
                    // pinMetrics: Boolean = false,
                    callback: PCallback<GetPinsResponse>
                ) {
                    api.getListPinsOnBoard(
                        boardId = boardId,
                        bookmark = bookmark,
                        pageSize = pageSize,
                        creativeTypes = creativeTypes,
                        // pinMetrics = pinMetrics,
                        header = buildBearerToken()
                    ).enqueue(object : Callback<GetPinsResponse> {
                        override fun onResponse(
                            call: Call<GetPinsResponse>,
                            response: Response<GetPinsResponse>
                        ) {
                            if (response.isSuccessful) {
                                callback.onSuccessful(PResponse(response))
                            } else {
                                callback.onUnsuccessful(PResponse(response))
                            }
                        }

                        override fun onFailure(call: Call<GetPinsResponse>, t: Throwable) {
                            callback.onFailure(t)
                        }
                    })
                }
            }
        }
    }

}