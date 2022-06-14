package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import android.content.Context
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

class SavedRecipesRepository(
    override var coroutineContext: CoroutineContext = Dispatchers.Main,
    private val dbRemote: ISavedRecipesServices = SavedRecipesServicesImpl()
) : BaseRepository() {

    fun getListsFromDB(
        context: Context?,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getListsFromDB(context) }



//        getFromDbOrWeb(
//            dbCompletionBlock = completionBlock,
//            webCompletionBlock = { domainResponse: ProductsResponse ->
//                val products = domainResponse.products
//                if (products?.isNotEmpty() == true) {
//                    products.map {
//                        val cartProduct = KodeInDB.kodeIn?.findFirstWhere<ProductModel> { cartProduct ->
//                            cartProduct.sku == it.sku && cartProduct.collectionTypes?.contains(collectionType) == true
//                        }
//                        val isExpress = UserStore.selectedDeliveryService == DeliveryService.EXPRESS
//                        it.isExpress = isExpress
//                        it.collectionTypes = collectionType
//                        it.cartQuantity = cartProduct?.cartQuantity ?: 0
//                    }
//                    completionBlock.invoke(products)
//                }
//            },
//            webErrorBlock = errorBlock,
//            conditionToCheckWeb = { dbResponse -> dbResponse?.isEmpty() == true },
//            dbTransaction = { dbRemote.getProducts() },
//            apiRequest = { webRemote.getProducts(collectionType, updatedAt) }
//        )

//        performTransaction(
//            completionBlock = { it ->
//                if (it?.isEmpty() == true) {
//                    performRequest(
//                        { domainResponse: ProductsResponse ->
//                            val products = domainResponse.products
//                            if (products?.isNotEmpty() == true) {
//                                products.map {
//                                    val cartProduct = KodeInDB.kodeIn?.findFirstWhere<ProductModel> { cartProduct ->
//                                        cartProduct.sku == it.sku && cartProduct.collectionTypes?.contains(collectionType) == true
//                                    }
//                                    val isExpress = UserStore.selectedDeliveryService == DeliveryService.EXPRESS
//                                    it.isExpress = isExpress
//                                    it.collectionTypes = collectionType
//                                    it.cartQuantity = cartProduct?.cartQuantity ?: 0
//                                }
//                                completionBlock.invoke(products)
//                            }
//                        },
//                        errorBlock,
//                        { webRemote.getProducts(collectionType, updatedAt) }
//                    )
//                } else {
//                    completionBlock(it)
//                }
//            },
//            transaction = { KodeInDB.kodeIn?.findAll<ProductModel>() }
//        )
//    }

//    fun getProductWithId(
//        id: String,
//        completionBlock: (ProductModel?) -> Unit
//    ) = performTransaction(completionBlock) { dbRemote.getProductWithId(id) }
//
//    fun getProductsWhere(
//        where: (ProductModel) -> Boolean,
//        completionBlock: (ArrayList<ProductModel>?) -> Unit
//    ) = performTransaction(completionBlock) { dbRemote.getProductsWhere(where) }
//
//    fun removeProduct(
//        id: String,
//        completionBlock: () -> Unit
//    ) = performTransaction(completionBlock) { dbRemote.removeProduct(id) }
}

