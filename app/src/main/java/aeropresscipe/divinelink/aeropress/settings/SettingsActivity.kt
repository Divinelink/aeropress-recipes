package aeropresscipe.divinelink.aeropress.settings

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ActivitySettingsBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import gr.divinelink.core.util.utils.WindowUtil.setNavigationBarColor


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorSurface2))

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initUI()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initUI() {
        //TODO
    }
}