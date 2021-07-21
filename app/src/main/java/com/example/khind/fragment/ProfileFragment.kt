package com.example.khind.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.khind.R
import com.example.khind.RealPathUtil
import com.example.khind.activity.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.properties.Delegates

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    var isChange = false
    lateinit var mUri: Uri
    lateinit var imgAvatar: ImageView
    lateinit var defaultAvatar: ImageView
    private var expired by Delegates.notNull<Long>()
    lateinit var multipartBodyAvt: MultipartBody.Part
    private val homeViewModel = HomeActivity().getViewModelHome()
    private val loginViewModel = HomeActivity().getViewModelLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = "My profile"
        val changePass = view.findViewById<ImageView>(R.id.idChangePass)
        val email = view.findViewById<TextView>(R.id.idEmail)
        imgAvatar = view.findViewById(R.id.idAvatar)
        defaultAvatar = view.findViewById(R.id.defaultAvatar)

        val avatarLink = HomeActivity().getViewModelLogin().getAvatarLink()
        if (avatarLink != null) {
            defaultAvatar.visibility = View.GONE
            loadImage(avatarLink)
        } else defaultAvatar.visibility = View.VISIBLE

        email.text = HomeActivity().getViewModelLogin().getEmailData()
        changePass.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangePassFragment()
            findNavController().navigate(action)
        }

        imgAvatar.setOnClickListener {
            onClickRequestPermission()
        }

        makeObserver()

        return view
    }

    private fun onClickRequestPermission() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestMultiplePermissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                openGallery()
            }
        }


    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"))
    }

    private val mActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent = it.data ?: return@registerForActivityResult
            val uri: Uri? = data.data
            try {
                uri?.let {
                    mUri = if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            uri
                        )
                        imgAvatar.setImageBitmap(bitmap)
                        uri
                    } else {
                        val source = activity?.let { it1 ->
                            ImageDecoder.createSource(
                                it1.contentResolver,
                                uri
                            )
                        }
                        val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                        imgAvatar.setImageBitmap(bitmap)
                        uri
                    }
                    makeApiCall()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun makeApiCall() {
        val strRealPath: String = RealPathUtil.getRealPath(context, mUri)
        val file = File(strRealPath)
        val requestBodyAvt: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        multipartBodyAvt = MultipartBody.Part.createFormData("avatar", file.name, requestBodyAvt)
        expired = loginViewModel.getExpired()
        if (expired * 1000 > System.currentTimeMillis()) homeViewModel.callAPIChangeAvatar(
            loginViewModel.getTokenLogin(),
            multipartBodyAvt
        )
        else {
            isChange = true
            loginViewModel.callAPIRefreshToken(
                loginViewModel.getTokenLogin(),
                loginViewModel.getReTokenLogin()
            )
        }
    }

    private fun makeObserver() {
        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null && isChange) {
                isChange = false
                homeViewModel.callAPIChangeAvatar(loginViewModel.getTokenLogin(), multipartBodyAvt)
            }
        })
        homeViewModel.getChangeAvatarLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                loadImage(it.data.avatar)
            }
        })
    }

    private fun loadImage(avatarLink: String) {
        context?.let {
            Glide.with(it).load(avatarLink).override(300, 300).fitCenter().into(imgAvatar)
        }
    }


    override fun onResume() {
        super.onResume()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val location = homeActivity.findViewById<ConstraintLayout>(R.id.idChangeLocation)
        location.visibility = View.GONE
        botBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val location = homeActivity.findViewById<ConstraintLayout>(R.id.idChangeLocation)
        location.visibility = View.VISIBLE
        botBar.visibility = View.VISIBLE
    }

}