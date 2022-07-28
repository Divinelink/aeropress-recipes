package aeropresscipe.divinelink.aeropress.finish

import aeropresscipe.divinelink.aeropress.databinding.ActivityFinishBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initUI()
    }

    private fun initUI() {
        // TODO
    }

    companion object {
        private const val EXTRA_RECIPE = "EXTRA_RECIPE"

        @JvmStatic
        fun newIntent(
            context: Context?,
            recipe: Recipe?,
        ): Intent {
            val intent = Intent(context, FinishActivity::class.java)
            intent.putExtra(EXTRA_RECIPE, recipe)
            return intent
        }
    }
}
