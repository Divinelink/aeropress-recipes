package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.ui.UIText
import aeropresscipe.divinelink.aeropress.ui.getString
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun SimpleAlertDialog(
    confirmClick: () -> Unit,
    dismissClick: () -> Unit,
    confirmText: UIText,
    dismissText: UIText,
    title: UIText,
    text: UIText,
) {
    AlertDialog(
        onDismissRequest = dismissClick,
        confirmButton = {
            TextButton(
                onClick = confirmClick,
            ) {
                Text(text = confirmText.getString())
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismissClick,
            ) {
                Text(text = dismissText.getString())
            }
        },
        title = {
            Text(text = title.getString())
        },
        text = {
            Text(text = text.getString())
        },
    )
}
