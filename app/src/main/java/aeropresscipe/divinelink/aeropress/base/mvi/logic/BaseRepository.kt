package aeropresscipe.divinelink.aeropress.base.mvi.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class BaseRepository {
  private companion object {
    const val HTTP_STATUS_200 = 200
    const val HTTP_STATUS_401 = 401
  }

  protected open var coroutineContext: CoroutineContext = Dispatchers.Main

//    //region web
//    protected fun <ApiResponse : KmmApiBaseResponse, DomainResponse : KmmBaseResponse> performRequest(
//        completionBlock: (DomainResponse) -> Unit,
//        errorBlock: (KmmErrorWrapperModel) -> Unit,
//        request: suspend () -> ApiResponse
//    ) = MainScope().launch(coroutineContext) {
//        runCatching {
//            request()
//        }.onSuccess { apiResponse ->
//            runCompletionBlock(completionBlock, apiResponse)
//        }.onFailure { error ->
//            runErrorBlock(errorBlock, error)
//        }
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    protected suspend fun <T> runCompletionBlock(
//        completionBlock: (T) -> Unit,
//        apiResponse: KmmApiBaseResponse
//    ) {
//        val response = apiResponse.transform() as T
//        // Return to the UI thread
//        withContext(coroutineContext) {
//            val hasSessionTimeout =
//                (response as? KmmBaseResponse)?.sessionTimeout == true
//            if (hasSessionTimeout) {
//                KmmCommunication.getInterface()?.restartApp()
//            } else {
//                completionBlock(response)
//            }
//        }
//    }
//
//    protected fun runErrorBlock(
//        errorBlock: (KmmErrorWrapperModel) -> Unit,
//        error: Throwable
//    ) = runErrorBlockWithSessionHandling(errorBlock, error)
//
//    private fun runErrorBlockWithSessionHandling(
//        errorBlock: (KmmErrorWrapperModel) -> Unit,
//        error: Throwable
//    ) {
//        val httpStatus = (error as? ResponseException)?.getHttpStatus() ?: HTTP_STATUS_200
//        if (httpStatus == HTTP_STATUS_401) {
//            MainScope().launch(coroutineContext) {
//                KmmCommunication.getInterface()?.restartApp()
//            }
//        } else {
//            val response = KmmErrorWrapperModel(error)
//            // Return to the UI thread
//            MainScope().launch(coroutineContext) {
//                errorBlock(response)
//            }
//        }
//    }
//    //endregion web

  //region db
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

  protected fun performTransaction(
    transaction: suspend () -> Unit,
  ) = MainScope().launch(coroutineContext) {
    runCatching {
      transaction()
    }.onSuccess {
      runCompletionBlock {
        // Do nothing
      }
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

  protected suspend fun runCompletionBlock(
    completionBlock: () -> Unit,
  ) = withContext(coroutineContext) {
    completionBlock()
  }

  protected suspend fun <T : Any?> runCompletionBlock(
    completionBlock: (T) -> Unit,
    response: T,
  ) = withContext(coroutineContext) {
    completionBlock(response)
  }
  //endregion db

  /**
   *      1. tries to fetch data from local DB
   *      2. if (conditionToCheckWeb is TRUE) then fetches data from BE
   *      3. returns results
   */
//    @Suppress("LongParameterList")
//    protected fun <DBResponse : Any?, ApiResponse : KmmApiBaseResponse, DomainResponse : KmmBaseResponse> getFromDbOrWeb(
//        dbCompletionBlock: (DBResponse) -> Unit,
//        webCompletionBlock: (DomainResponse) -> Unit,
//        webErrorBlock: (KmmErrorWrapperModel) -> Unit,
//        conditionToCheckWeb: (DBResponse) -> Boolean,
//        dbTransaction: suspend () -> DBResponse,
//        apiRequest: suspend () -> ApiResponse
//    ) = MainScope().launch(coroutineContext) {
//        runCatching {
//            dbTransaction()
//        }.onSuccess { response ->
//            if (conditionToCheckWeb(response)) performRequest(
//                webCompletionBlock,
//                webErrorBlock
//            ) { apiRequest() }
//            else runCompletionBlock(dbCompletionBlock, response)
//        }
//    }
}

// fun ResponseException.getHttpStatus(): Int = response.status.value
