package com.petid.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.petid.petid.BuildConfig
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.R
import com.petid.petid.databinding.ActivitySocialAuthBinding
import com.petid.petid.type.PlatformType
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.state.LoginResult
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.ui.view.main.MainActivity
import com.petid.petid.util.TAG
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.auth.SocialAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class SocialAuthActivity : BaseActivity() {
    private lateinit var binding: ActivitySocialAuthBinding
    private val viewModel: SocialAuthViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth

    private var socialAccessToken : String? = null

    /* kakao Login callback*/
    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            showErrorMessage("KaKaoLoginError: ${error.message}")
        } else if (token != null) {
            socialAccessToken = token.accessToken
            requestKakaoUserInfo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFcm()
        observesLoginResultState()
        observesDoRestoreResultState()
        initLoginComponent()
    }

    /*
     * initializing login component
     */
    private fun initLoginComponent() {
        with(binding) {
            buttonKakaoAuth
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.platform = PlatformType.kakao
                    handleKakaoLogin()
                }
                .launchIn(lifecycleScope)

            buttonNaverAuth
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.platform = PlatformType.naver
                    handleNaverLogin()
                }
                .launchIn(lifecycleScope)

            buttonGoogleAuth
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.platform = PlatformType.google
                    handleGoogleLogin()
                }
                .launchIn(lifecycleScope)
        }
    }

    /**
     * 카카오 로그인
     */
    private fun handleKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(applicationContext) { token, error ->
                if (error != null) {
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        showErrorMessage("KaKaoLoginError: ${error.message}")
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = kakaoLoginCallback)
                }/*else if (token != null) {
                    loginWithSocialToken(token.accessToken)
                }*/

            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = kakaoLoginCallback)
        }
    }

    /**
     * 카카오 유저 정보 요청
     */
    private fun requestKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 사용자 정보 요청 실패 처리
                showErrorMessage("KaKaoLoginError: ${error.message}")
            } else if (user != null) {
                val userId = user.id
                loginWithSocialToken(userId.toString())
            }
        }
    }

    /**
     * 네이버 로그인
     */
    private fun handleNaverLogin() {
        NaverIdLoginSDK.initialize(applicationContext,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            getString(R.string.social_login_info_naver_client_name))

        NaverIdLoginSDK.authenticate(this, oauthNaverLoginCallback)
    }

    /**
     * 네이버 로그인 콜백
     */
    private val oauthNaverLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            socialAccessToken = NaverIdLoginSDK.getAccessToken()

            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onError(errorCode: Int, message: String) {
                    showErrorMessage("NaverLoginError: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    showErrorMessage("NaverLoginError: $httpStatus\n$message")
                }

                override fun onSuccess(result: NidProfileResponse) {
                    val id = result.profile?.id.toString()
                    loginWithSocialToken(id)
                }
            })
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

            showErrorMessage("$message\n$errorCode $errorDescription")
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
            showErrorMessage(message)
        }
    }


    /**
     * 구글 로그인
     */
    private suspend fun handleGoogleLogin() {
        val credentialManager = CredentialManager.create(getGlobalContext())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(resources.getString(R.string.default_web_client_id))
            .build()
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        runCatching {
            credentialManager.getCredential(
                request = credentialRequest,
                context = this@SocialAuthActivity
            )
        }.onSuccess {
            when(val credential = it.credential) {
                is CustomCredential -> {
                    if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        runCatching {
                            GoogleIdTokenCredential.createFrom(credential.data)
                        }.onSuccess { googleIdTokenCredential ->
                            socialAccessToken = googleIdTokenCredential.idToken
                            firebaseAuthWithGoogle(socialAccessToken!!)
                        }.onFailure { ex ->
                            showErrorMessage("GoogleLoginError: ${ex.message}")
                        }
                    }
                }
            }
        }.onFailure {
            showErrorMessage("GoogleLoginError: ${it.message}")
        }
    }


    /**
     * init FCM setting
     */
    private fun initFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            viewModel.fcmToken = token
            Log.d(TAG, "FCM TOKEN: $token")
        })
    }

    /**
     * init Google Login
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, idToken)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.providerData?.get(1)?.uid?.apply {
                        loginWithSocialToken(this)//user?.uid.toString())
                    } ?: showErrorMessage("사용자 uid를 가져오는데 실패했습니다.")
                } else {
//                    Toast.makeText(this, "Firebase 인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * 각 social 로그인 성공 후 login 시도
     */
    private fun loginWithSocialToken(subValue: String) {
        with(viewModel) {
            this.subValue = subValue
            login()
        }
    }

    /**
     * login 결과 값에 따른
     */
    private fun observesLoginResultState() {
        lifecycleScope.launch {
            viewModel.loginResult.collectLatest { result ->
                if (result !is LoginResult.Loading)
                    hideLoading()

                when (result) {
                    is LoginResult.Success -> {
                        goMainActivity()
                        Log.d(TAG, "Login successful: ${result.data}")
                    }
                    is LoginResult.NeedToSignUp -> goTermsActivity()
                    is LoginResult.TryToRestore -> showRestoreDialog()
                    is LoginResult.Error -> showErrorMessage(result.message.toString())
                    is LoginResult.Loading -> showLoading()
                }
            }
        }
    }

    /**
     * viewModel.doRestore()
     */
    private fun observesDoRestoreResultState() {
        lifecycleScope.launch {
            viewModel.restoreResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        Toast.makeText(
                            getGlobalContext(),
                            getString(R.string.success_restore), Toast.LENGTH_LONG).show()
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 이용 약관 페이지로 이동
     */
    private fun goTermsActivity() {
        val platform = viewModel.platform.toString()
        val sub = viewModel.subValue
        val fcmToken = viewModel.fcmToken

        if (platform.isNotEmpty() && sub != null && fcmToken != null) {
            val target = Intent(this, TermsActivity::class.java).apply {
                putExtra("platform", platform)
                putExtra("token", socialAccessToken)
                putExtra("fcmToken", fcmToken)
            }
            startActivity(target)
        } else {
            showErrorMessage("platform, token, fcmToken null error")
        }
    }

    /**
     * show 계정 복구 dialog
     */
    private fun showRestoreDialog() {
        CustomDialogCommon(
            boldTitle = getString(R.string.restore_dialog_title),
            title = getString(R.string.restore_dialog_desc),
            singleButtonText = getString(R.string.restore_dialog_yes),
            yesButtonClick = {
                viewModel.doRestore()
            }
        ).show(this.supportFragmentManager, null)
    }

    /**
     * 메인 이동
     */
    private fun goMainActivity() {
        val target = Intent(getGlobalContext(), MainActivity::class.java)
        startActivity(target)
        finish()
    }
}
