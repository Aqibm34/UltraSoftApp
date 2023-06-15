package com.example.ultrasoft.ui.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseFragment
import com.example.ultrasoft.databinding.FragmentPreviewBinding
import com.example.ultrasoft.utility.AppConstants.Companion.EMP_IMAGES_URL


private const val URL = "url"

class PreviewFragment : BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate) {

    private val args: PreviewFragmentArgs by navArgs()
    private var videoPosition = 0
    private var Video_STATE_POSITION = 0
    private var isVideo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getInt("pos")
        }
    }

    override fun setUpViews() {
        binding.ivBack.setOnClickListener {
            goBack()
        }

        if (args.url.contains(".mp4")
            || args.url.contains(".mkv")
            || args.url.contains(".mov")
            || args.url.contains(".webm")
            || args.url.contains((".3gp"))
        ) {
            isVideo = true
        }

        if (isVideo) {
            binding.videoView.visibility = View.VISIBLE
            binding.pb.visibility = View.VISIBLE
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    @SuppressLint("SourceLockedOrientationActivity")
                    override fun handleOnBackPressed() {
                        goBack()
                    }

                })
            playVideo()
        } else {
            binding.ivZoom.visibility = View.VISIBLE
            Glide.with(requireContext()).load(EMP_IMAGES_URL + args.url)
                .placeholder(R.drawable.image_placeholder)
                .into(binding.ivZoom)
        }
    }

    private fun goBack() {
        if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            requireActivity().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            findNavController().popBackStack()
        }
    }

    private fun playVideo() {
        binding.videoView.setVideoURI(Uri.parse(args.url))
        val mediaController = MediaController(requireContext())

        binding.ivRotate.setOnClickListener {
            requireActivity().requestedOrientation =
                if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    binding.videoView.layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                } else {
                    binding.videoView.layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
        }
        binding.videoView.setOnPreparedListener { mp: MediaPlayer ->
            binding.pb.visibility = View.GONE
            mp.setOnVideoSizeChangedListener { _: MediaPlayer?, _: Int, _: Int ->
                binding.videoView.setMediaController(mediaController)
                mediaController.setAnchorView(binding.videoView)
                binding.ivRotate.visibility = View.VISIBLE
            }
            if (videoPosition > 0) {
                binding.videoView.seekTo(videoPosition)
            } else {
                binding.videoView.seekTo(1)
            }
            binding.videoView.start()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isVideo) {
            outState.putInt("pos", Video_STATE_POSITION)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isVideo) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                binding.videoView.pause()
            }
            Video_STATE_POSITION = binding.videoView.currentPosition
        }
    }

    override fun onStop() {
        super.onStop()
        if (isVideo) {
            binding.videoView.stopPlayback()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setPlayerViewLayoutParamsForPortrait()
        }

    }


    private fun setPlayerViewLayoutParamsForPortrait() {
        val display: Display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val doubleHeight = width / 1.5
        val height = doubleHeight.toInt()
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, height
        )
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
        layoutParams.endToEnd = ConstraintSet.PARENT_ID
        layoutParams.startToStart = ConstraintSet.PARENT_ID
        layoutParams.topToTop = ConstraintSet.PARENT_ID
        binding.videoView.layoutParams = layoutParams

//        val ivLayoutParams = ConstraintLayout.LayoutParams(24,24)
//        ivLayoutParams.endToEnd = binding.videoView.id
//        ivLayoutParams.topToTop = binding.videoView.id
//        binding.ivRotate.layoutParams = ivLayoutParams
    }
}