package aeropresscipe.divinelink.aeropress.delegates

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ActivityLauncher : IActivityLauncher {
    private var onActivityResultAction: ((result: ActivityResult) -> Unit)? = null
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun registerLauncher(activity: AppCompatActivity) {
        launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                onActivityResultAction?.invoke(it)
            }
        }
    }

    override fun launchActivity(intent: Intent?, onResult: ((result: ActivityResult) -> Unit)?) {
        onActivityResultAction = onResult
        if (intent != null) {
            launcher?.launch(intent)
        }
    }
}

interface IActivityLauncher {
    fun registerLauncher(activity: AppCompatActivity)
    fun launchActivity(intent: Intent?, onResult: ((result: ActivityResult) -> Unit)?)
}
