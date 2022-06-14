package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class SavedRecipesRepository(
    override var coroutineContext: CoroutineContext = Dispatchers.Main,
    private val dbRemote: ISavedRecipesServices = SavedRecipesServicesImpl()
) : BaseRepository() {

    fun getListsFromDB(
        context: Context,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getRecipesFromDB(context) }

    fun deleteRecipe(
        recipe: SavedRecipeDomain,
        context: Context,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.deleteRecipe(recipe, context) }

    fun startBrew(
        recipe: SavedRecipeDomain,
        context: Context,
        completionBlock: (SavedRecipeDomain?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getSingleRecipe(recipe, context) }

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
