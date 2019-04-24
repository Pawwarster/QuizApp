package pl.pawwar.quizapp.utils.view

import android.animation.LayoutTransition
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.progress.view.*
import pl.pawwar.quizapp.R
import timber.log.Timber

class LoadingLayout : FrameLayout {

    private val MIN_SHOW_TIME = 200L // ms
    private val MIN_DELAY = 200L // ms
    private val ERROR_SHOW_TIME = 300L
    private val EMPTY_SHOW_TIME = 200L // ms

    internal var mStartTime: Long = -1

    internal var mPostedHide = false

    internal var mPostedShow = false

    internal var mDismissed = false

    private var retryCallback: (() -> Unit)? = null
    private var emptyMessage: String = ""

    private val mDelayedError = Runnable {
        Timber.d("showError delayed")
        removeCallbacks()
        mPostedHide = false
        mPostedShow = false
        mStartTime = -1
        progressView.visibility = View.VISIBLE
        progressView.isClickable = true
        progressBar.clearAnimation()
        progressBar.visibility = View.GONE
        error.visibility = View.VISIBLE
        val callback = retryCallback
        if (callback != null) {
            retry.visibility = View.VISIBLE
            retry.setOnClickListener { callback() }
        } else {
            retry.visibility = View.GONE
        }
    }

    private val mDelayedEmpty = Runnable {
        removeCallbacks()
        mPostedHide = false
        mPostedShow = false
        mStartTime = -1
        progressView.isClickable = false
        progressView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        progressBar.clearAnimation()
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        empty.text = emptyMessage
    }

    private val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        progressView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        error.visibility = View.GONE
        empty.visibility = View.GONE
    }

    private val mDelayedShow = Runnable {
        mPostedShow = false
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            progressView.isClickable = true
            progressView.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            error.visibility = View.GONE
            empty.visibility = View.GONE
        }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private lateinit var progressView: View


    fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        progressView = LayoutInflater.from(context).inflate(R.layout.progress, null)
        progressView.visibility = View.GONE
        val lt = LayoutTransition()
        layoutTransition = lt
    }

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(progressView)
    }


    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        removeCallbacks()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
        removeCallbacks(mDelayedError)
        removeCallbacks(mDelayedEmpty)
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum showLoading time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    fun hideLoading() {
        mDismissed = true
        removeCallbacks(mDelayedShow)
        mPostedShow = false
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            progressView.visibility = View.GONE
            progressBar.clearAnimation()
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hideLoading it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hideLoading() is called, the view is never made visible.
     */
    fun showLoading() {
        Timber.d("showLoading() $mPostedShow")
        // Reset the start time.
        mStartTime = -1
        mDismissed = false
        removeCallbacks(mDelayedHide)
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY)
            mPostedShow = true
        }
    }


    fun showError(retryCallback: (() -> Unit)? = null) {
        Timber.d("showError()")
        removeCallbacks()
        postDelayed(mDelayedError, ERROR_SHOW_TIME)
        this.retryCallback = retryCallback
    }


    fun showEmpty(emptyMessage: String) {
        postDelayed(mDelayedEmpty, EMPTY_SHOW_TIME)
        this.emptyMessage = emptyMessage
    }


    fun setLoading(isLoading: Boolean) =
            if (isLoading)
                showLoading()
            else
                hideLoading()
}