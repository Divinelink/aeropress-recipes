package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.timer.TimerFragment.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction

class TimerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        val mToolBar = findViewById<Toolbar>(R.id.toolbar)
        mToolBar.setNavigationOnClickListener {
            onBackPressed()
        }

        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as Recipe?

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.timerRoot, newInstance(recipe))
            .commit()
    }

    companion object {
        private const val EXTRA_RECIPE = "EXTRA_RECIPE"

        @JvmStatic
        fun newIntent(
            context: Context,
            recipe: Recipe
        ): Intent {
            val intent = Intent(context, TimerActivity::class.java)
            intent.putExtra(EXTRA_RECIPE, recipe)
            return intent
        }
    }
}