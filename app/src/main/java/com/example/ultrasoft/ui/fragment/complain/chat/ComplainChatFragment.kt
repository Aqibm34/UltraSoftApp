package com.example.ultrasoft.ui.fragment.complain.chat

import android.Manifest
import android.net.Uri
import android.view.View
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.model.complain.Chat
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentComplainChatBinding
import com.example.ultrasoft.ui.adapters.ComplaintChatAdapter
import com.example.ultrasoft.ui.module.popupmenu.MenuEditText
import com.example.ultrasoft.ui.module.popupmenu.SoftKeyBoardPopup
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.MediaUtils
import com.example.ultrasoft.utility.SnackTypes
import com.example.ultrasoft.utility.Utils
import com.example.ultrasoft.utility.contracts.PickFromGalleryActivityContract
import com.example.ultrasoft.utility.createPartFromString
import com.example.ultrasoft.utility.hasCameraPermission
import com.example.ultrasoft.utility.hasWritePermission
import com.example.ultrasoft.utility.loadImageOrVideoPreview
import com.example.ultrasoft.utility.logE
import com.example.ultrasoft.utility.prepareFilePart
import com.example.ultrasoft.utility.showAlertWithButtonConfig
import com.example.ultrasoft.utility.showSnackBar
import com.example.ultrasoft.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class ComplainChatFragment :
    BaseFragment<FragmentComplainChatBinding>(FragmentComplainChatBinding::inflate),
    MenuEditText.PopupListener {

    private val TAG = ComplainChatFragment::class.java.simpleName
    private val args: ComplainChatFragmentArgs by navArgs()
    private val viewModel: ChatComplainViewModel by viewModels()

    lateinit var menuKeyboard: SoftKeyBoardPopup
    private var selectedFileUri: Uri? = null
    private var chatAdapter: ComplaintChatAdapter? = null
    private var chatList: MutableList<Chat> = ArrayList()
    private var isFromPreview = false
    private var engineerReplyType = AppConstants.ComplaintStatus.IN_PROGRESS

    override fun setUpViews() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        showComplaintData()

        if (args.data.status == AppConstants.ComplaintStatus.RESOLVED.name || args.data.status == AppConstants.ComplaintStatus.CLOSED.name) {
            binding.etReply.visibility = View.GONE
            binding.btnSend.visibility = View.GONE
        }

        binding.etReply.popupListener = this
        menuKeyboard = SoftKeyBoardPopup(
            requireContext(), binding.root, binding.etReply, binding.etReply, binding.tvMenu
        ) {
            when (it) {
                "Image" -> {
                    photoIntent()
                }

                "Video" -> {
                    videoIntent()
                }

                "Gallery" -> {
                    if (hasWritePermission()) {
                        galleryPickCallback.launch(MediaUtils.IMAGE_VIDEO_MIME)
                    } else {
                        permissionsResultCallback.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }

        binding.etReply.doOnTextChanged { _, _, _, _ ->
            binding.btnSend.isEnabled = binding.etReply.text.toString().isNotEmpty()
        }

        binding.tvMenu.setOnClickListener {
            toggleMenu()
        }
        binding.btnSend.setOnClickListener {
            toggleMenu()
            prepareFileToUpload()
        }
     }

    private fun showComplaintData() {
        try {
            if (!isFromPreview) {
                chatList = args.data.complaintChats.toMutableList()
            }
            binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
            chatAdapter = ComplaintChatAdapter(
                chatList,
                args.data.createdByCUstomer,
                args.data.assignedToEngineer,
                args.data.assignedByAdmin,
                requireContext()
            ) {
                isFromPreview = true
                findNavController().navigate(
                    ComplainChatFragmentDirections.actionComplainChatFragmentToPreviewFragment(
                        AppConstants.ATTACHMENT_URL + it.attachment
                    )
                )
            }
            binding.rvChats.adapter = chatAdapter
            binding.rvChats.scrollToPosition(chatList.size - 1)
        } catch (e: Exception) {
            logE(TAG, e.message)
        }
    }

    private fun toggleMenu() {
        if (menuKeyboard.isShowing) {
            menuKeyboard.dismiss()
        } else {
            menuKeyboard.show()
        }
    }

    override fun onDestroy() {
        menuKeyboard.clear()
        VideoCompressor.cancel()
        super.onDestroy()
    }

    override fun getPopup(): PopupWindow {
        return menuKeyboard
    }

    private fun photoIntent() {
        if (hasCameraPermission(requireContext())) {
            val fileName = "complain_" + System.currentTimeMillis()
            val photoUriList = Utils.setCaptureFileUri(requireContext(), fileName)
            selectedFileUri = photoUriList?.get(0)
            captureImageCallback.launch(photoUriList?.get(1))
        } else {
            permissionsResultCallback.launch(Manifest.permission.CAMERA)
        }
    }

    private fun videoIntent() {
        if (hasCameraPermission(requireContext())) {
            val fileName = "ymvideo_complain_reply" + System.currentTimeMillis()
            val photoUriList = Utils.setCaptureFileUri(
                requireContext(), fileName, extension = MediaUtils.VIDEO_FORMAT
            )
            //content uri for exposed
            selectedFileUri = photoUriList?.get(1)
            captureVideoCallback.launch(photoUriList?.get(1))
        } else {
            permissionsResultCallback.launch(Manifest.permission.CAMERA)
        }

    }

    private val captureVideoCallback =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { successful ->
            if (successful) {
                selectedFileUri?.let { loadImageOrVideoPreview(it, binding.ivPreview) }
            } else {
                binding.root.showSnackBar(
                    resources.getString(R.string.unable_to_process), SnackTypes.Error
                )
                selectedFileUri = null
            }
        }


    private val captureImageCallback =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
            if (successful) {
                selectedFileUri?.let { loadImageOrVideoPreview(it, binding.ivPreview) }
            } else {
                binding.root.showSnackBar(
                    resources.getString(R.string.unable_to_process), SnackTypes.Error
                )
                selectedFileUri = null
            }
        }

    private val galleryPickCallback =
        registerForActivityResult(PickFromGalleryActivityContract()) { uri ->
            uri?.let {
                loadImageOrVideoPreview(uri, binding.ivPreview)
                selectedFileUri = uri
            }
        }

    private fun prepareFileToUpload() {
        try {
            if (selectedFileUri != null) {
                val mimeType = MediaUtils.getMimeType(requireContext(), selectedFileUri!!)
                if (mimeType != null) {
                    if (mimeType.contains("image")) {
                        val compressedFile = Utils.compressImageFile(
                            File(selectedFileUri?.path!!), requireContext()
                        )
                        callApiReply(compressedFile)
                    } else {
                        startCompression(listOf(selectedFileUri!!))
                    }
                }
            } else {
                callApiReply(null)
//                binding.root.showSnackBar(
//                    resources.getString(R.string.please_attach_file_from_gallery), SnackTypes.Error
//                )
            }
        } catch (e: NullPointerException) {
            binding.root.showSnackBar(
                resources.getString(R.string.please_attach_file_from_gallery), SnackTypes.Error
            )
        } catch (e: Exception) {
            binding.root.showSnackBar(e.message, SnackTypes.Error)
        }
    }

    private fun callApiReply(file: File?) {
        try {
            val filePart = if (file == null) {
                null
            } else {
                requireContext().prepareFilePart("file", file)
            }
            val map: HashMap<String, RequestBody> = HashMap()
            map["remark"] = createPartFromString(binding.etReply.text.toString())
            map["complainId"] = createPartFromString(args.data.complainId)


            val url = when (appPreferences.getRole()) {
                AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_REPLY_COMPLAIN_URL
                AppConstants.UserTypes.CUSTOMER.name -> AppConstants.REPLY_COMPLAIN_URL
                AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_REPLY_COMPLAIN_URL
                else -> ""
            }

            viewModel.callApiReplyComplaint(
                url, appPreferences.getToken(), map, filePart
            )
        } catch (e: Exception) {
            logE(TAG, e.message)
        }
    }

    private fun showProgress() {
        binding.tvMenu.isEnabled = false
        binding.btnSend.visibility = View.INVISIBLE
        binding.pb.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.tvMenu.isEnabled = true
        binding.btnSend.visibility = View.VISIBLE
        binding.pb.visibility = View.GONE
        binding.tvPb.visibility = View.GONE
    }


    private fun startCompression(uriList: List<Uri>) {
        val tag = "Compress Failure"
        try {
            requireContext().toast("Please wait")
            VideoCompressor.start(context = requireContext(),
                uris = uriList,
                isStreamable = true,
                storageConfiguration = MediaUtils.getVideoCompressorStorageConfig("complain.mp4"),
                configureWith = MediaUtils.getVideoCompressorConfig(),
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {
                        requireActivity().runOnUiThread {
                            binding.tvPb.text = percent.toInt().toString()
                        }
                    }

                    override fun onStart(index: Int) {
                        showProgress()
                        binding.tvPb.visibility = View.VISIBLE
                        binding.tvPb.text = "0"
                    }

                    override fun onSuccess(index: Int, size: Long, path: String?) {
                        try {
                            requireActivity().runOnUiThread {
                                selectedFileUri = Uri.fromFile(File(path!!))
                                if (selectedFileUri != null) {
                                    callApiReply(File(selectedFileUri!!.path!!))
                                } else {
                                    hideProgress()
                                    binding.root.showSnackBar(
                                        resources.getString(R.string.something_went_wrong_unable_to_process_video),
                                        SnackTypes.Error
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            logE(tag, e.message)
                        }
                    }

                    override fun onFailure(index: Int, failureMessage: String) {
                        try {
                            requireActivity().runOnUiThread {
                                hideProgress()
                            }
                        } catch (e: Exception) {
                            logE(tag, failureMessage)
                        }
                    }

                    override fun onCancelled(index: Int) {
                        requireActivity().runOnUiThread {
                            hideProgress()
                        }
                    }

                })
        } catch (e: Exception) {
            logE(tag, e.message)
        }
    }

    private fun resetViews() {
        selectedFileUri = null
        binding.etReply.setText("")
        binding.ivPreview.visibility = View.GONE
        binding.ivPreview.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.image_placeholder
            )
        )
    }


    override fun observeView() {
        viewModel.replyResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    showProgress()
                }

                Resource.Status.SUCCESS -> {
                    if (it.data?.status_code == 1) {
                        val url =
                            when (appPreferences.getRole()) {
                                AppConstants.UserTypes.ENGINEER.name -> AppConstants.ENG_COMPLAIN_BY_ID_URL
                                AppConstants.UserTypes.CUSTOMER.name -> AppConstants.COMPLAIN_BY_ID_URL
                                AppConstants.UserTypes.ADMIN.name -> AppConstants.ADMIN_GET_COMPLAIN_BY_ID
                                else -> ""
                            }


                        viewModel.callApiGetComplaintById(
                            url + args.data.complainId, appPreferences.getToken()
                        )
                        binding.root.showSnackBar(it.data.message, SnackTypes.Success)
                    } else {
                        hideProgress()
                        resetViews()
                        binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                    }
                }

                Resource.Status.ERROR -> {
                    hideProgress()
                    binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                }
            }
        }

        viewModel.singleComplainResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {}
                Resource.Status.SUCCESS -> {
                    hideProgress()
                    if (it.data?.status_code == 1) {
                        resetViews()
                        chatList.add(it.data.data.complaintChats[it.data.data.complaintChats.size - 1])
                        chatAdapter?.updateList(chatList)
                        binding.rvChats.scrollToPosition(chatList.size - 1)
                    } else {
                        binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                    }
                }

                Resource.Status.ERROR -> {
                    hideProgress()
                    binding.root.showSnackBar(it.data?.message, SnackTypes.Error)
                }
            }
        }

    }

}