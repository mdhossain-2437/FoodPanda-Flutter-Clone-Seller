package com.example.foodpanda_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.ui.screens.AddBannerScreen
import com.example.foodpanda_seller.ui.screens.AddCategoryScreen
import com.example.foodpanda_seller.ui.screens.AddCustomizationScreen
import com.example.foodpanda_seller.ui.screens.AddFoodScreen
import com.example.foodpanda_seller.ui.screens.AddressScreen
import com.example.foodpanda_seller.ui.screens.AuthenticationScreen
import com.example.foodpanda_seller.ui.screens.BannerScreen
import com.example.foodpanda_seller.ui.screens.FoodScreen
import com.example.foodpanda_seller.ui.screens.HomeScreen
import com.example.foodpanda_seller.ui.screens.HomeScreenNoApprove
import com.example.foodpanda_seller.ui.screens.LoginScreen
import com.example.foodpanda_seller.ui.screens.MenuScreen
import com.example.foodpanda_seller.ui.screens.OrderHistoryScreen
import com.example.foodpanda_seller.ui.screens.OrderScreen
import com.example.foodpanda_seller.ui.screens.RegisterScreen
import com.example.foodpanda_seller.ui.screens.RegisterShopScreen
import com.example.foodpanda_seller.ui.screens.SearchAddressManualScreen
import com.example.foodpanda_seller.ui.screens.SendVerificationEmailScreen
import com.example.foodpanda_seller.ui.screens.SplashScreen
import com.example.foodpanda_seller.ui.theme.FoodPandaSellerTheme
import com.example.foodpanda_seller.ui.viewmodel.SellerViewModel
import kotlinx.serialization.Serializable

// Type-safe Navigation Routes matching original lib/router.dart
@Serializable object SplashRoute
@Serializable object AuthenticationRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object SendVerificationEmailRoute
@Serializable object HomeRoute
@Serializable object HomeNoApproveRoute
@Serializable object MenuRoute
@Serializable data class AddCategoryRoute(
    val id: String = "",
    val title: String = "",
    val subtitle: String = ""
)
@Serializable data class FoodRoute(
    val categoryId: String,
    val categoryTitle: String = ""
)
@Serializable data class AddFoodRoute(
    val categoryId: String,
    val foodId: String? = null
)
@Serializable data class AddCustomizationRoute(
    val categoryId: String,
    val foodId: String
)
@Serializable object OrdersRoute
@Serializable object OrderHistoryRoute
@Serializable object BannersRoute
@Serializable object AddBannerRoute
@Serializable object RegisterShopRoute
@Serializable object SearchAddressManualRoute
@Serializable object AddressRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodPandaSellerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    FoodPandaSellerApp()
                }
            }
        }
    }
}

@Composable
fun FoodPandaSellerApp(
    startOnHome: Boolean = false
) {
    val context = LocalContext.current
    val viewModel: SellerViewModel = viewModel(
        factory = SellerViewModel.provideFactory(context)
    )
    val navController = rememberNavController()

    val session by viewModel.session.collectAsStateWithLifecycle()
    val shopProfile by viewModel.shopProfile.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val allFoods by viewModel.allFoods.collectAsStateWithLifecycle()
    val allCustomizations by viewModel.allCustomizations.collectAsStateWithLifecycle()
    val activeOrders by viewModel.activeOrders.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val banners by viewModel.banners.collectAsStateWithLifecycle()
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val vouchers by viewModel.vouchers.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (startOnHome) HomeRoute else SplashRoute
    ) {
        composable<SplashRoute> {
            SplashScreen(
                session = session,
                onNavigateToAuth = {
                    navController.navigate(AuthenticationRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToNoApprove = {
                    navController.navigate(HomeNoApproveRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<AuthenticationRoute> {
            AuthenticationScreen(
                onNavigateToRegister = { navController.navigate(RegisterRoute) },
                onNavigateToLogin = { navController.navigate(LoginRoute) }
            )
        }

        composable<LoginRoute> {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { email ->
                    viewModel.signIn(email)
                    navController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { name, email ->
                    viewModel.registerSeller(name, email)
                    navController.navigate(SendVerificationEmailRoute)
                }
            )
        }

        composable<SendVerificationEmailRoute> {
            SendVerificationEmailScreen(
                onBack = { navController.popBackStack() },
                onVerified = {
                    viewModel.markEmailVerified()
                    navController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<HomeNoApproveRoute> {
            HomeScreenNoApprove(
                onApproveAccount = {
                    viewModel.setApprovalStatus(true)
                    navController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                session = session,
                shopProfile = shopProfile,
                categories = categories,
                allFoods = allFoods,
                activeOrders = activeOrders,
                allOrders = allOrders,
                vouchers = vouchers,
                onNavigateToMenu = { navController.navigate(MenuRoute) },
                onNavigateToOrders = { navController.navigate(OrdersRoute) },
                onNavigateToOrderHistory = { navController.navigate(OrderHistoryRoute) },
                onNavigateToBanners = { navController.navigate(BannersRoute) },
                onNavigateToRegisterShop = { navController.navigate(RegisterShopRoute) },
                onNavigateToAddresses = { navController.navigate(AddressRoute) },
                onAddVoucher = { code, discount, minOrder ->
                    viewModel.addVoucher(code, discount, minOrder)
                },
                onDeleteVoucher = { id -> viewModel.deleteVoucher(id) },
                onLogout = {
                    viewModel.signOut()
                    navController.navigate(AuthenticationRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<MenuRoute> {
            MenuScreen(
                categories = categories,
                onBack = { navController.popBackStack() },
                onAddCategory = {
                    navController.navigate(AddCategoryRoute())
                },
                onEditCategory = { cat ->
                    navController.navigate(
                        AddCategoryRoute(
                            id = cat.id,
                            title = cat.title,
                            subtitle = cat.subtitle
                        )
                    )
                },
                onDeleteCategory = { id -> viewModel.deleteCategory(id) },
                onPublishedChange = { id, published ->
                    viewModel.updateCategoryPublished(id, published)
                },
                onSelectCategory = { cat ->
                    navController.navigate(
                        FoodRoute(categoryId = cat.id, categoryTitle = cat.title)
                    )
                }
            )
        }

        composable<AddCategoryRoute> { backStackEntry ->
            val route: AddCategoryRoute = backStackEntry.toRoute()
            AddCategoryScreen(
                initialId = route.id,
                initialTitle = route.title,
                initialSubtitle = route.subtitle,
                onBack = { navController.popBackStack() },
                onSave = { title, subtitle, id ->
                    viewModel.saveCategory(title, subtitle, id)
                    navController.popBackStack()
                }
            )
        }

        composable<FoodRoute> { backStackEntry ->
            val route: FoodRoute = backStackEntry.toRoute()
            val foodsInCategory = allFoods.filter { it.categoryId == route.categoryId }
            FoodScreen(
                categoryTitle = route.categoryTitle,
                foods = foodsInCategory,
                customizations = allCustomizations,
                onBack = { navController.popBackStack() },
                onAddFood = {
                    navController.navigate(AddFoodRoute(categoryId = route.categoryId))
                },
                onEditFood = { food ->
                    navController.navigate(
                        AddFoodRoute(categoryId = route.categoryId, foodId = food.id)
                    )
                },
                onDeleteFood = { foodId -> viewModel.deleteFood(foodId) },
                onPublishedChange = { foodId, published ->
                    viewModel.updateFoodPublished(foodId, published)
                },
                onAddCustomize = { food ->
                    navController.navigate(
                        AddCustomizationRoute(categoryId = route.categoryId, foodId = food.id)
                    )
                }
            )
        }

        composable<AddFoodRoute> { backStackEntry ->
            val route: AddFoodRoute = backStackEntry.toRoute()
            val existingFood by produceState<FoodEntity?>(initialValue = null, route.foodId) {
                value = route.foodId?.let { viewModel.getFoodById(it) }
            }
            AddFoodScreen(
                existingFood = existingFood,
                onBack = { navController.popBackStack() },
                onSave = { name, desc, price, comparePrice, image ->
                    viewModel.saveFood(
                        categoryId = route.categoryId,
                        foodId = route.foodId,
                        name = name,
                        description = desc,
                        price = price,
                        comparePrice = comparePrice,
                        image = image
                    )
                    navController.popBackStack()
                }
            )
        }

        composable<AddCustomizationRoute> { backStackEntry ->
            val route: AddCustomizationRoute = backStackEntry.toRoute()
            AddCustomizationScreen(
                onBack = { navController.popBackStack() },
                onSave = { title, isReq, isRadio, isVar, selectAmt, choices ->
                    viewModel.addCustomization(
                        categoryId = route.categoryId,
                        foodId = route.foodId,
                        title = title,
                        isRequired = isReq,
                        isRadio = isRadio,
                        isVariation = isVar,
                        selectAmount = selectAmt,
                        choices = choices
                    )
                    navController.popBackStack()
                }
            )
        }

        composable<OrdersRoute> {
            OrderScreen(
                orders = activeOrders,
                onBack = { navController.popBackStack() },
                onAcceptOrder = { id -> viewModel.acceptOrder(id) },
                onRejectOrder = { id -> viewModel.rejectOrder(id) },
                onCompleteOrder = { id -> viewModel.completeOrder(id) },
                onSimulateIncomingOrder = { viewModel.simulateIncomingOrder() }
            )
        }

        composable<OrderHistoryRoute> {
            OrderHistoryScreen(
                orders = allOrders,
                onBack = { navController.popBackStack() }
            )
        }

        composable<BannersRoute> {
            BannerScreen(
                banners = banners,
                onBack = { navController.popBackStack() },
                onAddBanner = { navController.navigate(AddBannerRoute) },
                onToggleApproval = { id, approved ->
                    viewModel.toggleBannerApproval(id, approved)
                },
                onDeleteBanner = { id -> viewModel.deleteBanner(id) }
            )
        }

        composable<AddBannerRoute> {
            AddBannerScreen(
                onBack = { navController.popBackStack() },
                onSave = { imageUrl, description ->
                    viewModel.addBanner(imageUrl, description)
                    navController.popBackStack()
                }
            )
        }

        composable<RegisterShopRoute> {
            RegisterShopScreen(
                shopProfile = shopProfile,
                onBack = { navController.popBackStack() },
                onChooseLocation = { navController.navigate(SearchAddressManualRoute) },
                onSaveShop = { name, desc, img, house, street, area, prov, floor, lat, lng ->
                    viewModel.registerShop(name, desc, img, house, street, area, prov, floor, lat, lng)
                    navController.popBackStack()
                }
            )
        }

        composable<SearchAddressManualRoute> {
            SearchAddressManualScreen(
                initialShopProfile = shopProfile,
                onSearchPlaces = { q -> viewModel.searchPlaces(q) },
                onBack = { navController.popBackStack() },
                onConfirmLocation = { place ->
                    viewModel.saveAddress(
                        id = addresses.firstOrNull()?.id,
                        houseNumber = place.houseNumber,
                        street = place.street,
                        area = place.area,
                        province = place.province,
                        floor = shopProfile.floor,
                        latitude = place.latitude,
                        longitude = place.longitude
                    )
                    navController.popBackStack()
                }
            )
        }

        composable<AddressRoute> {
            AddressScreen(
                shopProfile = shopProfile,
                addresses = addresses,
                onBack = { navController.popBackStack() },
                onOpenManualSearch = { navController.navigate(SearchAddressManualRoute) },
                onSaveAddress = { id, house, street, area, prov, floor, lat, lng ->
                    viewModel.saveAddress(id, house, street, area, prov, floor, lat, lng)
                    navController.popBackStack()
                }
            )
        }
    }
}
