package com.petid.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.petid.petid.R
import com.petid.petid.common.Constants
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.databinding.ActivityPermissionBinding
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.setStyleSpan
import com.petid.petid.util.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class PermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initUI()
    }

    private fun initUI() {
        with(binding) {
            permissionTitle1.text =
                setStyleSpan(getGlobalContext(),
                    getString(R.string.permission_title_1),
                    getString(R.string.permission_title_1_span),
                    R.color.dark_red)

            buttonConfirm
                .clicks()
                .throttleFirst()
                .onEach {
                    // TODO 권한 요청하기
                    getPreferencesControl()
                        .saveBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, false)
                    val target = Intent(getGlobalContext(), IntroActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(target)
                    finish()
                }
                .launchIn(lifecycleScope)

        }
    }


    /*private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "앱 설정 -> Notification 권한 허용 필요", Toast.LENGTH_SHORT).show()
        }
    }*/

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