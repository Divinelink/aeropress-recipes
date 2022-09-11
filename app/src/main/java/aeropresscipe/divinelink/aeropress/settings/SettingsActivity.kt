package aeropresscipe.divinelink.aeropress.settings

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ActivitySettingsBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.WindowUtil.setNavigationBarColor
import java.lang.ref.WeakReference
import javax.inject.Inject


@AndroidEntryPoint
class SettingsActivity :
    AppCompatActivity(),
    ISettingsViewModel,
    SettingsStateHandler {
    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var assistedFactory: SettingsViewModelAssistedFactory
    private lateinit var viewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorSurface2))

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModelFactory = SettingsViewModelFactory(assistedFactory, WeakReference<ISettingsViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    override fun updateState(state: SettingsState) {
        when (state) {
            is SettingsState.ErrorState -> {}
            is SettingsState.InitialState -> {}
            is SettingsState.LoadingState -> {}
        }
    }

    override fun handleInitialState() {
//        TODO("Not yet implemented")
    }

    override fun handleLoadingState() {
//        TODO("Not yet implemented")
    }

    override fun handleErrorState() {
//        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}