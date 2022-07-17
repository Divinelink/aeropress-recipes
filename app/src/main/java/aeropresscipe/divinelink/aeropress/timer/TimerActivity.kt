package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.FLOW
import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.newInstance
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.WindowUtil

@AndroidEntryPoint
class TimerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        val mToolBar = findViewById<Toolbar>(R.id.toolbar)
        mToolBar.setNavigationOnClickListener {
            onBackPressed()
        }

        WindowUtil.setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorBackground))

        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as Recipe?
        val flow = intent.getSerializableExtra(FLOW) as TimerFlow

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.timerRoot, newInstance(recipe, flow))
            .commit()
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
