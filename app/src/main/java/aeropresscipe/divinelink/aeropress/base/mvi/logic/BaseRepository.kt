package aeropresscipe.divinelink.aeropress.base.mvi.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class BaseRepository {
  protected open var coroutineContext: CoroutineContext = Dispatchers.Main

  protected fun performTransaction(
    completionBlock: () -> Unit,
    transaction: suspend () -> Unit,
  ) = MainScope().launch(coroutineContext) {
    runCatching {
      transaction()
    }.onSuccess {
      runCompletionBlock(completionBlock)
    }
  }

  protected fun <T : Any?> performTransaction(
    completionBlock: (T) -> Unit,
    transaction: suspend () -> T,
  ) = MainScope().launch(coroutineContext) {
    runCatching {
      transaction()
    }.onSuccess { response ->
      runCompletionBlock(completionBlock, response)
    }
  }

  protected suspend fun runCompletionBlock(completionBlock: () -> Unit) =
    withContext(coroutineContext) {
      completionBlock()
    }

  protected suspend fun <T : Any?> runCompletionBlock(
    completionBlock: (T) -> Unit,
    response: T,
  ) = withContext(coroutineContext) {
    completionBlock(response)
  }
}
