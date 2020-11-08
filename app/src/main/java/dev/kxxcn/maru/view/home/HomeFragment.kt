package dev.kxxcn.maru.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.LruCache
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.HomeFragmentBinding
import dev.kxxcn.maru.util.FileUtils
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.REQUEST_CODE_PERMISSION_STORAGE
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragment
import dev.kxxcn.maru.view.base.Capturable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragment : BaseFragment(), Scrollable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: HomeFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@HomeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupOnboard()
        setupListAdapter()
        setupListener()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CODE_PERMISSION_STORAGE -> {
                    capture()
                }
            }
        } else {
            viewModel.message(R.string.requires_permission)
        }
    }

    override fun onDestroyView() {
        if (::binding.isInitialized) {
            with(binding.contentsList) {
                clearOnScrollListeners()
                adapter = null
            }
        }
        super.onDestroyView()
    }

    override fun scrollToTop() {
        binding.contentsList.smoothScrollToPosition(0)
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupOnboard() {
        if (!PreferenceUtils.shownOnboard) {
            HomeFragmentDirections.actionHomeFragmentToOnboardFragment().also {
                findNavController().navigate(it)
            }
            PreferenceUtils.shownOnboard = !PreferenceUtils.shownOnboard
        }
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            with(binding.contentsList) {
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy == 0) return
                        val activity = requireActivity() as MaruActivity
                        activity.openNavigator(dy < 0)
                    }
                })
                addItemDecoration(LinearSpacingDecoration(50.px), 0)
                val activity = activity ?: return
                adapter = HomeAdapter(activity, viewModel)
            }
        }
    }

    private fun setupListener() {
        viewModel.daysEvent.observe(viewLifecycleOwner, EventObserver {
            HomeFragmentDirections.actionHomeFragmentToDaysFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.shareEvent.observe(viewLifecycleOwner, EventObserver {
            if (checkPermission()) capture()
        })
    }

    private fun handleCaptureSuccess(): () -> Unit {
        return lambda@{
            if (isDetached) return@lambda
            viewModel.isLoading(false)
            viewModel.message(R.string.save_to_gallery)
            showNavigator(true)
        }
    }

    private fun handleCaptureFailure(): () -> Unit {
        return lambda@{
            if (isDetached) return@lambda
            viewModel.isLoading(false)
            viewModel.message(R.string.try_again_later)
        }
    }

    private fun checkPermission(): Boolean {
        return FileUtils.checkPermission(
            activity,
            REQUEST_CODE_PERMISSION_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ) { Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
    }

    private fun capture() {
        viewModel.isLoading(true)
        val context = context ?: return
        showNavigator(false)
        val parent = binding.contentsList
        lifecycleScope.launch(Dispatchers.Default) {
            (parent.adapter as? HomeAdapter)?.let { adapter ->
                parent.suppressLayout(true)

                var height = 0
                // 홀더 간 간격
                val spacingBetweenHolder = 30.px
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                val cacheSize = maxMemory / 8
                val cache = LruCache<String, Bitmap>(cacheSize)

                fun isPassedHolder(viewType: Int): Boolean {
                    return when (viewType) {
                        HomeAdapter.TYPE_WELCOME,
                        HomeAdapter.TYPE_BANNER_AD -> true
                        HomeAdapter.TYPE_DAYS -> adapter.items
                            ?.firstOrNull()
                            ?.content
                            ?.hasDays == false
                        else -> false
                    }
                }

                val widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
                val heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

                val size = adapter.itemCount
                for (position in 0 until size) {
                    val viewType = adapter.getItemViewType(position)
                    if (isPassedHolder(viewType)) continue
                    val holder = withContext(Dispatchers.Main) {
                        with(adapter) {
                            createViewHolder(
                                parent,
                                viewType
                            ).also { holder ->
                                onBindViewHolder(holder, position)
                                (holder as? Capturable)?.capture()

                                holder.itemView.measure(widthMeasureSpec, heightMeasureSpec)
                                holder.itemView.layout(
                                    0,
                                    0,
                                    holder.itemView.measuredWidth,
                                    holder.itemView.measuredHeight
                                )
                            }
                        }
                    }

                    cache.put(position.toString(), holder.itemView.drawToBitmap())
                    height += holder.itemView.measuredHeight + spacingBetweenHolder
                }

                parent.suppressLayout(false)

                val bitmap = createBitmap(parent.measuredWidth, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                val color = if (PreferenceUtils.useDarkMode) {
                    ContextCompat.getColor(context, R.color.colorPrimaryDarkNight)
                } else {
                    Color.WHITE
                }
                canvas.drawColor(color)

                // 캔버스 상 다음 홀더가 그려질 높이(y 좌표)
                var offset = spacingBetweenHolder
                val paint = Paint()

                for (position in 0 until size) {
                    cache[position.toString()]?.let {
                        canvas.drawBitmap(it, 0f, offset.toFloat(), paint)
                        offset += it.height + spacingBetweenHolder
                        it.recycle()
                    }
                }

                FileUtils.saveBitmapToExternalStorage(
                    context,
                    bitmap,
                    "${System.currentTimeMillis()}_share.png",
                    handleCaptureSuccess(),
                    handleCaptureFailure()
                )
            }
        }
    }

    private fun showNavigator(isShowing: Boolean = true) {
        (requireActivity() as? MaruActivity)?.openNavigator(isShowing)
    }
}
