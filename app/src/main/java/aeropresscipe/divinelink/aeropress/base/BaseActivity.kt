package aeropresscipe.divinelink.aeropress.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(), IActivityLauncher {

    private var activityLauncher: ActivityResultLauncher<Intent>? = null
    private var onActivityResultAction: ((result: ActivityResult) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityLauncher()
    }

    override fun launchActivity(intent: Intent?, onResult: ((result: ActivityResult) -> Unit)?) {
        onActivityResultAction = onResult
        if (intent != null) {
            activityLauncher?.launch(intent)
        }
    }

    private fun initActivityLauncher() {
        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                onActivityResultAction?.invoke(it)
            }
        }
    }

}

interface IActivityLauncher {
    fun launchActivity(
        intent: Intent?,
        onResult: ((result: ActivityResult) -> Unit)?
    )
}
