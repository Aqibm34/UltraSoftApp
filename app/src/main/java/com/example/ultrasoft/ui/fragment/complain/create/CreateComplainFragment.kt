package com.example.ultrasoft.ui.fragment.complain.create

import android.Manifest
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.bumptech.glide.Glide
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.data.network.Resource
import com.example.ultrasoft.databinding.FragmentCreateComplainBinding
import com.example.ultrasoft.utility.*
import com.example.ultrasoft.utility.contracts.PickFromGalleryActivityContract
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class CreateComplainFragment :
    BaseFragment<FragmentCreateComplainBinding>(FragmentCreateComplainBinding::inflate) {

    private var fileUri: Uri? = null
    private val viewModel: CreateComplainViewModel by viewModels()

    override fun setUpViews() {
        binding.tb.setUpToolbar("Create Complain")

        binding.fileGroup.setOnCheckedChangeListener { _, checkedId ->
            fileUri = null
            when (checkedId) {
                R.id.rbImage -> {
                    binding.ivPreview.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.image_placeholder
                        )
                    )
                }

                R.id.rbVideo -> {
                    binding.ivPreview.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.video_placeholder
                        )
                    )
                }
            }
        }

        binding.tvCamera.setOnClickListener {
            if (binding.rbVideo.isChecked) {
                videoIntent()
            } else {
                photoIntent()
            }
        }

        binding.tvGallery.setOnClickListener {
            if (hasWritePermission()) {
                if (binding.rbImage.isChecked) {
                    galleryPickCallback.launch(MediaUtils.IMAGE_MIME)
                } else {
                    galleryPickCallback.launch(MediaUtils.VIDEO_MIME)
                }
            } else {
                permissionsResultCallback.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (validateFile()) {
                prepareFileToUpload()
            }
        }

        validation()
        viewModel.callApiGetAllAssetsCategory()

    }

    private fun validation() {
        binding.tilCategory.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateCategory()) {
                binding.tilCategory.clearError()
                enableButton()
            } else {
                binding.tilCategory.error = resources.getString(R.string.enter_valid_remarks)
                binding.btnSubmit.isEnabled = false
            }
        }

        binding.tilRemarks.editText?.doOnTextChanged { _, _, _, _ ->
            if (validateRemarks()) {
                binding.tilRemarks.clearError()
                enableButton()
            } else {
                binding.tilRemarks.error = resources.getString(R.string.enter_valid_remarks)
                binding.btnSubmit.isEnabled = false
            }
        }
    }

    private fun validateCategory() = binding.tilCategory.editText?.text.toString().isNotEmpty()
    private fun validateRemarks() = binding.tilRemarks.editText?.text.toString().isNotEmpty()

    private fun enableButton() {
        binding.btnSubmit.isEnabled = validateCategory() && validateRemarks()
    }

    private fun validateFile(): Boolean {
        return if (fileUri != null) {
            binding.tvHelper.visibility = View.GONE
            true
        } else {
            binding.tvHelper.visibility = View.VISIBLE
            binding.tvHelper.text = resources.getString(R.string.please_select_a_file_to_upload)
            false
        }
    }

    private fun videoIntent() {
        if (hasCameraPermission(requireContext())) {
            val fileName = "video_complain" + System.currentTimeMillis()
            val photoUriList = Utils.setCaptureFileUri(
                requireContext(),
                fileName,
                extension = MediaUtils.VIDEO_FORMAT
            )
            //content uri for exposed
            fileUri = photoUriList?.get(1)
            captureVideoCallback.launch(photoUriList?.get(1))
        } else {
            permissionsResultCallback.launch(Manifest.permission.CAMERA)
        }
    }

    private fun photoIntent() {
        if (hasCameraPermission(requireContext())) {
            val fileName = "complain_" + System.currentTimeMillis()
            val photoUriList = Utils.setCaptureFileUri(requireContext(), fileName)
            fileUri = photoUriList?.get(0)
            captureImageCallback.launch(photoUriList?.get(1))
        } else {
            permissionsResultCallback.launch(Manifest.permission.CAMERA)
        }
    }

    private val captureImageCallback =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
            if (successful) {
                fileUri?.let { loadImageOrVideoPreview(it, binding.ivPreview) }
            } else {
                binding.root.showSnackBar(
                    resources.getString(R.string.unable_to_process),
                    SnackTypes.Error
                )
                fileUri = null
            }
        }

    private val captureVideoCallback =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { successful ->
            if (successful) {
                fileUri?.let { loadImageOrVideoPreview(it, binding.ivPreview) }
            } else {
                binding.root.showSnackBar(
                    resources.getString(R.string.unable_to_process),
                    SnackTypes.Error
                )
                fileUri = null
            }
        }


    private val galleryPickCallback =
        registerForActivityResult(PickFromGalleryActivityContract()) { uri ->
            uri?.let {
                loadImageOrVideoPreview(uri, binding.ivPreview)
                fileUri = uri
            }
        }


    private fun prepareFileToUpload() {
        try {
            if (fileUri != null) {
                if (binding.rbImage.isChecked) {
                    val compressedFile =
                        Utils.compressImageFile(File(fileUri?.path!!), requireContext())
                    callApiCreateComplain(compressedFile)
                } else {
                    startCompression(listOf(fileUri!!))
                }
            }
        } catch (e: Exception) {
            binding.root.showSnackBar(e.message, SnackTypes.Error)
        }
    }

    private fun startCompression(uriList: List<Uri>) {
        val tag = "Compress Failure"
        try {
            requireContext().toast("Please wait")
            VideoCompressor.start(
                context = requireContext(),
                uris = uriList,
                isStreamable = true,
                storageConfiguration = MediaUtils.getVideoCompressorStorageConfig("complain.mp4"),
                configureWith = MediaUtils.getVideoCompressorConfig(),
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {
                        requireActivity().runOnUiThread {
                            binding.videoPb.progressBar.progress = percent.toInt()
                            binding.videoPb.tvProgress.text = percent.toInt().toString()
                        }
                    }

                    override fun onStart(index: Int) {
                        binding.tvHelper.visibility = View.GONE
                        binding.videoPb.progressBar.progress = 0
                        binding.videoPb.clProgress.visibility = View.VISIBLE
                    }

                    override fun onSuccess(index: Int, size: Long, path: String?) {
                        try {
                            requireActivity().runOnUiThread {
                                fileUri = Uri.fromFile(File(path!!))
                                Glide.with(requireContext()).load(path).into(binding.ivPreview)
                                binding.tvHelper.visibility = View.GONE
                                binding.videoPb.clProgress.visibility = View.GONE
                                callApiCreateComplain(File(fileUri!!.path!!))
                            }

                        } catch (e: Exception) {
                            logE(tag, e.message)
                        }
                    }

                    override fun onFailure(index: Int, failureMessage: String) {
                        try {
                            requireActivity().runOnUiThread {
                                binding.videoPb.clProgress.visibility = View.GONE
                                binding.tvVideoName.visibility = View.GONE
                                binding.tvHelper.visibility = View.VISIBLE
                                binding.tvHelper.text =
                                    failureMessage
                            }
                        } catch (e: Exception) {
                            logE(tag, failureMessage)
                        }
                    }

                    override fun onCancelled(index: Int) {
                        requireActivity().runOnUiThread {
                            binding.videoPb.clProgress.visibility = View.GONE
                        }
                    }

                }
            )
        } catch (e: Exception) {
            logE(tag, e.message)
        }
    }

    private fun callApiCreateComplain(file: File) {
        try {
            val filePart = requireContext().prepareFilePart("file", file)
            val map: HashMap<String, RequestBody> = HashMap()
            map["remark"] = createPartFromString(binding.tilRemarks.editText?.text.toString())
            map["complainCategory"] =
                createPartFromString(binding.tilCategory.editText?.text.toString())

            viewModel.callApiCreateComplaint(
                appPreferences.getToken(),
                map,
                filePart
            )
        } catch (e: Exception) {
            requireContext().toast(e.message.toString())
        }
    }

    override fun observeView() {
        viewModel.createComplainResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (it.data?.status_code == 1) {
                        showAlert(it.message, AppConstants.AlertType.SUCCESS) {
                            findNavController().popBackStack()
                        }
                    } else {
                        showAlert(it.data?.message, AppConstants.AlertType.ERROR) {}
                    }
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    showAlert(it.message, AppConstants.AlertType.ERROR) {}
                }
            }
        }

        viewModel.allAssetCategoryResponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.LOADING -> showLoading()
                Resource.Status.SUCCESS -> {
                    hideLoading()
                    if (response.data?.status_code == 1) {
                        val array = response.data.data.map { it.assetCategoryName }.toTypedArray()
                        (binding.tilCategory.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
                            array
                        )
                    } else {
                        showAlert(response.data?.message, AppConstants.AlertType.ERROR) {}
                    }
                }

                Resource.Status.ERROR -> {
                    hideLoading()
                    showAlert(response.message, AppConstants.AlertType.ERROR) {}
                }
            }
        }
    }
}