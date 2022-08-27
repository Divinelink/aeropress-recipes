package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.databinding.ViewTimerProgressBinding
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport

class TimerProgressView : FrameLayout {
    var binding: ViewTimerProgressBinding = ViewTimerProgressBinding.inflate(LayoutInflater.from(context), this, false)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(binding.root)

//        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))

        initUI()
    }

    private fun initUI() {
        // initialize the UI here if needed
    }
}
