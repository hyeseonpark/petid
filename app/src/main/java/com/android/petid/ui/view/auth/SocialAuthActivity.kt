package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.lifecycleScope
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.ActivitySocialAuthBinding
import com.android.petid.enum.PlatformType
import com.android.petid.ui.state.LoginResult
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.ui.view.main.MainActivity
import com.android.petid.util.TAG
import com.android.petid.util.showErrorMessage
import com.android.petid.viewmodel.auth.SocialAuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialAuthActivity : BaseActivity() {
    private lateinit var binding: ActivitySocialAuthBinding
    private val viewModel: SocialAuthViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private var socialAccessToken : String? = null

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google 로그인이 성공하면, Firebase로 인증합니다.
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "구글 로그인 성공")
                socialAccessToken = account.idToken.toString()
                loginWithSocialToken(account.id.toString())
            } catch (e: ApiException) {
                // Google 로그인 실패
                showErrorMessage("KaKaoLoginError: ${e.message}")
            }

        }

    /* kakao Login callback*/
    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            showErrorMessage("KaKaoLoginError: ${error.message}")
        } else if (token != null) {
            requestKakaoUserInfo()
            socialAccessToken = token.accessToken
            /*var idToken = token.idToken
            if (idToken != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token}")
                loginWithSocialToken(idToken)
            }*/
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        initGoogle()
        initFcm()
        setupObservers()
        initLoginComponent()
    }

    /*
     * initializing login component
     */
    private fun initLoginComponent() {
        with(binding) {
            buttonKakaoAuth.setOnClickListener {
                viewModel.platform = PlatformType.kakao
                handleKakaoLogin()
            }
            buttonNaverAuth.setOnClickListener {
                viewModel.platform = PlatformType.naver
                handleNaverLogin()
            }
            buttonGoogleAuth.setOnClickListener {
                viewModel.platform = PlatformType.google

                // TODO 구글로그인 형식 변경
                lifecycleScope.launch {
                    val credentialManager = CredentialManager.create(getGlobalContext())

                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(resources.getString(R.string.default_web_client_id))
                        .build()
                    val credentialRequest = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()
                    try {
                        val googleSignInRequest = credentialManager.getCredential(
                            request = credentialRequest,
                            context = this@SocialAuthActivity
                        )
                        val credential = googleSignInRequest.credential
                        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            Log.d(TAG, "구글 로그인 성공")
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            socialAccessToken = googleIdTokenCredential.idToken
                            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                        }
                    } catch (ex: GetCredentialCancellationException) {
                        Log.e(TAG, "Error getting credentials", ex)

                        showErrorMessage("GoogleLoginError: ${ex.message}")
                    }
                }
                //handleGoogleLogin()
            }
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

        NaverIdLoginSDK.reagreeAuthenticate(this, oauthNaverLoginCallback)
    }


    private val oauthNaverLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            socialAccessToken = NaverIdLoginSDK.getAccessToken()

            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onError(errorCode: Int, message: String) {
                    showErrorMessage("NaverLoginError: $message")
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d(TAG, "$httpStatus\n" + message)
                    showErrorMessage("NaverLoginError: $message")
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
    private fun handleGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
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
     * init Google
     */
    private fun initGoogle() {
        // Firebase Authentication 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance()

        // 이미 로그인되어 있는지 확인
        if (firebaseAuth.currentUser != null) {
            // 이미 로그인된 경우 메인 화면으로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // GoogleSignInOptions를 구성합니다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_LOGIN_CLIENT_ID)
            .requestIdToken(BuildConfig.GOOGLE_LOGIN_CLIENT_ID)
            .requestEmail()
            .build()

        // GoogleSignInClient를 초기화합니다.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    /**
     * init Google Login
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, idToken)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val user = firebaseAuth.currentUser
                    loginWithSocialToken(user?.uid.toString())
//                    Toast.makeText(this, "환영합니다, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                    // 여기서 로그인 후 화면 전환 등의 작업을 수행할 수 있습니다.
                    //startActivity(Intent(this, MainActivity::class.java))
                    //finish()
                } else {
                    // 로그인 실패
//                    Toast.makeText(this, "Firebase 인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * 각 social 로그인 성공 후 login 시도
     */
    private fun loginWithSocialToken(subValue: String) {
        viewModel.subValue = subValue
        viewModel.login()
    }

    /**
     * login 결과 값에 따른
     */
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.loginResult.collectLatest { result ->
                if (result !is LoginResult.Loading)
                    hideLoading()

                when (result) {
                    is LoginResult.Success -> {
                        goMainActivity()
                        Log.d(TAG, "Login successful: ${result.data}")
                    }
                    is LoginResult.NeedToSignUp -> {
                        goTermsActivity()
                        // 회원가입 화면으로 이동
                        Log.d(TAG, "goTermsActivity...")
                    }
                    is LoginResult.Error -> showErrorMessage(result.message.toString())
                    is LoginResult.Loading -> showLoading()
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
                putExtra("sub", socialAccessToken)
                putExtra("fcmToken", fcmToken)
            }
            startActivity(target)
        } else {
            showErrorMessage("platform, sub, fcmToken null error")
        }
    }

    private fun goMainActivity() {
        val target = Intent(getGlobalContext(), MainActivity::class.java)
        startActivity(target)
        finish()
    }

}
