package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.TimerExitEvent
import aeropresscipe.divinelink.aeropress.databinding.ActivityTimerBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.FLOW
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.newInstance
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.setNavigationBarColor
import gr.divinelink.core.util.viewBinding.activity.viewBinding
import org.greenrobot.eventbus.EventBus


@AndroidEntryPoint
class TimerActivity :
    AppCompatActivity(),
    TimerFragment.Callback {
    private val binding: ActivityTimerBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as Recipe?
        val flow = intent.getSerializableExtra(FLOW) as TimerFlow

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.timerRoot, newInstance(recipe, flow))
            .commit()
    }

    override fun onExitTimer() {
        EventBus.getDefault().post(TimerExitEvent())
        finish()
    }

    companion object {
        private const val EXTRA_RECIPE = "EXTRA_RECIPE"

        @JvmStatic
        fun newIntent(
            context: Context?,
            recipe: Recipe?,
            flow: TimerFlow? = TimerFlow.START
        ): Intent {
            val intent = Intent(context, TimerActivity::class.java)
            intent.putExtra(EXTRA_RECIPE, recipe)
            intent.putExtra(FLOW, flow)
            return intent
        }
    }
}
