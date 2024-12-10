package com.android.petid.ui.view.auth

import android.content.Intent
import android.os.Bundle
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.ActivityPermissionBinding
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.util.PreferencesControl
import com.android.petid.util.Utils.setStyleSpan

class PermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding
    private val TAG = "PermissionActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initComponent()
    }

    private fun initComponent() {
        with(binding) {
            permissionTitle1.text =
                setStyleSpan(getGlobalContext(),
                    getString(R.string.permission_title_1),
                    getString(R.string.permission_title_1_span),
                    R.color.dark_red)

            buttonConfirm.setOnClickListener{
                // TODO 권한 요청하기
                PreferencesControl(getGlobalContext())
                    .saveBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, false)
                val intent = Intent(getGlobalContext(), SocialAuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


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