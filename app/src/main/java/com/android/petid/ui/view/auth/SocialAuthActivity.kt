package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.common.Constants.SHARED_VALUE_FCM_TOKEN
import com.android.petid.common.PreferencesControl
import com.android.petid.databinding.ActivitySocialAuthBinding
import com.android.petid.ui.state.LoginResult
import com.android.petid.ui.view.main.MainActivity
import com.android.petid.viewmodel.auth.SocialAuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SocialAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySocialAuthBinding
    private val viewModel: SocialAuthViewModel by viewModels()

    private val TAG = "SocialAuthActivity"

//    private lateinit var splashScreen: SplashScreen

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.d("LOGIN--", task.toString())
            try {
                // Google 로그인이 성공하면, Firebase로 인증합니다.
                val account = task.getResult(ApiException::class.java)!!
                Log.d("LOGIN--22", account.idToken!!)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google 로그인 실패
            }

        }

    // kakao Login
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            loginWithSocialToken(token.accessToken)
        }
    }

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
        initGoogleLogin()
        initFcm()
        setupObservers()


        /*checkProcessing()
        // setContentView하기 전에 installSplashScreen() 필수
        splashScreen = installSplashScreen()
        startSplash()

        val intentIntro = Intent(this, IntroActivity::class.java)
        startActivity(intentIntro)*/

    }

    /*
     * initializing view component
     */
    private fun initComponent() {
        binding.buttonKakaoAuth.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                UserApiClient.instance.loginWithKakaoTalk(applicationContext) { token, error ->
                    if (error != null) {
                        //Log.e(TAG, "카카오톡으로 로그인 실패2", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)

                    } /*else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공2 ${token.accessToken}")
                    }*/

                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
            }
        }
        binding.buttonNaverAuth.setOnClickListener {
            val intent = Intent(this, TermsActivity::class.java)
            startActivity(intent)
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

            // Get new FCM registration token
            val token = task.result

            PreferencesControl.saveStringValue(applicationContext, SHARED_VALUE_FCM_TOKEN, token)
            Log.d(TAG, "FCM TOKEN: $token")
        })
    }

    /**
     * init Google Login
     */
    private fun initGoogleLogin() {
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
            .requestServerAuthCode(getString(R.string.google_login_client_id))
            .requestIdToken(getString(R.string.google_login_client_id))
            .requestEmail()
            .build()

        // GoogleSignInClient를 초기화합니다.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 구글 로그인 버튼 클릭 이벤트 처리
        binding.buttonGoogleAuth.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    /**
     * init Google Login
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("LOGIN--3", idToken)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val user = firebaseAuth.currentUser
//                    Toast.makeText(this, "환영합니다, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                    // 여기서 로그인 후 화면 전환 등의 작업을 수행할 수 있습니다.
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // 로그인 실패
//                    Toast.makeText(this, "Firebase 인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginWithSocialToken(subValue: String) {
        val sub = subValue
        val fcmToken = PreferencesControl.getStringValue(applicationContext, SHARED_VALUE_FCM_TOKEN)
        if (fcmToken != null) {
            viewModel.login(sub, fcmToken)
        }
    }
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.loginResult.collectLatest { result ->
                when (result) {
                    is LoginResult.Success -> {
                        val loginEntity = result.data
                        Log.d(TAG, "Login successful: ${loginEntity}")
                    }
                    is LoginResult.NeedToSignUp -> {
                        // 회원가입 화면으로 이동
                        goTermsActivity()
                        Log.d(TAG, "goTermsActivity...")
                    }
                    is LoginResult.Error -> {
                            // 오류 처리
                        Log.e(TAG, "Login error: ${result.message}")
                    }
                    is LoginResult.Loading -> {
                        // 로딩 상태 처리
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }

    private fun goTermsActivity() {
        // 회원가입 화면으로 이동
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)
    }

    /*
     * splash의 애니메이션 설정
     */
//    private fun startSplash() {
//        splashScreen.setOnExitAnimationListener { splashScreenView ->
//            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 5f, 1f)
//            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 5f, 1f)
//
//            ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, scaleX, scaleY).run {
//                interpolator = AnticipateInterpolator()
//                duration = 1000L
//                doOnEnd {
//                    splashScreenView.remove()
//                }
//                start()
//            }
//        }
//    }

    /**
     *
     * 최초 실행 시 권한 안내 페이지 이동
     *
     */
//    private fun checkProcessing() {
//        val isFirst: Boolean =
//            PreferencesControl.getBooleanValue(
//                applicationContext, Constants.SHARED_VALUE_IS_FIRST, true
//            )
//
//        Log.d(TAG, "[checkProcessing] isFirst: $isFirst")
//
//        if (isFirst) {
//            val intent = Intent(this, PermissionActivity::class.java)
//            startActivity(intent)
//        } else {
//            checkPermission(REQUEST_STORAGE_PERMISSION)
//        }
//    }


    private fun checkPermission(type: Int) {
//        Log.d(TAG, "checkPermission type: $type")
        // goActivity()

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d(TAG, "SDK_INT >= R");
            if (!Environment.isExternalStorageManager()) {
                try {
                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                    startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));

                } catch (Exception e) {
                    startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
                }

            } else {
                Log.d(TAG, "저장공간 접근 권한 허용됨!!!!");
                goActivity();
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "SDK_INT >= M");
            if (type == REQUEST_STORAGE_PERMISSION) {
                if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_STORAGE_PERMISSION);
                    Log.d(TAG, "REQUEST_STORAGE_PERMISSION...");
                } else {
                    Log.d(TAG, "저장공간 접근 권한 허용됨!!!!");
                    goActivity();
                }
            }
        } else {
            goActivity();
        }

         */
    }


    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>?,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
//
//        when (requestCode) {
//            REQUEST_STORAGE_PERMISSION -> {
//                if ((grantResults.size == 2) &&
//                    (grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
//                    (grantResults[1] == PackageManager.PERMISSION_GRANTED)
//                ) {
//                    Log.d(TAG, "저장공간 접근 권한 허용됨!!!!")
////                    goActivity()
//                } else {
//                    Log.d(TAG, "저장공간 접근 권한 필요!!!!")
//
////                    permissionAlert()
//                }
//
//                return
//            }
//        }
//    }

}