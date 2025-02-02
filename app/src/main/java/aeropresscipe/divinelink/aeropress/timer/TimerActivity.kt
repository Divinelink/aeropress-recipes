package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.FLOW
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.newInstance
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.divinelink.aeropress.recipes.R
import com.divinelink.aeropress.recipes.databinding.ActivityTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.addBackPressCallback
import gr.divinelink.core.util.extensions.getSerializable
import gr.divinelink.core.util.utils.setNavigationBarColor
import gr.divinelink.core.util.viewBinding.activity.viewBinding

@AndroidEntryPoint
class TimerActivity :
    AppCompatActivity(),
    TimerFragment.Callback {
    private val binding: ActivityTimerBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))

        onBackPressedDispatcher.addBackPressCallback(this) { finish() }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val recipe = intent.getSerializable<Recipe>(EXTRA_RECIPE)
        val flow = intent.getSerializable<TimerFlow>(FLOW)

        getFragment(recipe, flow)
    }

    private fun getFragment(recipe: Recipe?, flow: TimerFlow?) {
        val fTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag(TIMER_TAG)
        if (fragment == null) {
            fTransaction.add(R.id.timer_fragment, newInstance(recipe, flow), TIMER_TAG)
        } else { // re-use the old fragment
            fTransaction.replace(R.id.timer_fragment, fragment, TIMER_TAG)
        }
        fTransaction.commitNow()
    }

    override fun onExitTimer() {
        finish()
    }

    companion object {
        private const val EXTRA_RECIPE = "EXTRA_RECIPE"
        private const val TIMER_TAG = "Timer_Fragment"

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
