package com.example.foodpanda_seller.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodpanda_seller.data.local.AppDatabase
import com.example.foodpanda_seller.data.model.AddressEntity
import com.example.foodpanda_seller.data.model.BannerEntity
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.ChoiceItem
import com.example.foodpanda_seller.data.model.CustomizationEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.data.model.PlaceSuggestion
import com.example.foodpanda_seller.data.model.SellerSession
import com.example.foodpanda_seller.data.model.ShopProfile
import com.example.foodpanda_seller.data.model.VoucherEntity
import com.example.foodpanda_seller.data.repository.SellerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellerViewModel(
    private val repository: SellerRepository
) : ViewModel() {

    val session: StateFlow<SellerSession> = repository.session
    val shopProfile: StateFlow<ShopProfile> = repository.shopProfile

    val categories: StateFlow<List<CategoryEntity>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFoods: StateFlow<List<FoodEntity>> = repository.allFoods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCustomizations: StateFlow<List<CustomizationEntity>> = repository.allCustomizations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeOrders: StateFlow<List<OrderEntity>> = repository.activeOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerEntity>> = repository.banners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val addresses: StateFlow<List<AddressEntity>> = repository.addresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vouchers: StateFlow<List<VoucherEntity>> = repository.vouchers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeedData()
        }
    }

    fun observeFoodsByCategory(categoryId: String): Flow<List<FoodEntity>> =
        repository.observeFoodsByCategory(categoryId)

    // Auth Actions
    fun signIn(email: String, name: String? = null) {
        repository.signIn(email, name)
    }

    fun registerSeller(name: String, email: String) {
        repository.registerSeller(name, email)
    }

    fun markEmailVerified() {
        repository.markEmailVerified()
    }

    fun setApprovalStatus(approved: Boolean) {
        repository.setApprovalStatus(approved)
    }

    fun signOut() {
        repository.signOut()
    }

    // Shop Registration
    fun registerShop(
        shopName: String,
        shopDescription: String,
        shopImage: String,
        houseNumber: String,
        street: String,
        area: String,
        province: String,
        floor: String,
        latitude: Double,
        longitude: Double
    ) {
        val updated = repository.shopProfile.value.copy(
            isRegistered = true,
            shopName = shopName.trim(),
            shopDescription = shopDescription.trim(),
            shopImage = shopImage.ifBlank {
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80"
            },
            houseNumber = houseNumber.trim(),
            street = street.trim(),
            area = area.trim(),
            province = province.trim().ifBlank { "Phnom Penh" },
            floor = floor.trim(),
            latitude = latitude,
            longitude = longitude
        )
        repository.updateShopProfile(updated)
    }

    // Category Actions
    fun saveCategory(title: String, subtitle: String, id: String?) {
        viewModelScope.launch {
            repository.saveCategory(title, subtitle, id)
        }
    }

    fun updateCategoryPublished(id: String, isPublished: Boolean) {
        viewModelScope.launch {
            repository.updateCategoryPublished(id, isPublished)
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            repository.deleteCategory(id)
        }
    }

    // Food Actions
    suspend fun getFoodById(foodId: String): FoodEntity? = repository.getFoodById(foodId)

    fun saveFood(
        categoryId: String,
        foodId: String?,
        name: String,
        description: String,
        price: Double,
        comparePrice: Double,
        image: String
    ) {
        viewModelScope.launch {
            repository.saveFood(categoryId, foodId, name, description, price, comparePrice, image)
        }
    }

    fun updateFoodPublished(id: String, isPublished: Boolean) {
        viewModelScope.launch {
            repository.updateFoodPublished(id, isPublished)
        }
    }

    fun deleteFood(id: String) {
        viewModelScope.launch {
            repository.deleteFood(id)
        }
    }

    // Customization Actions
    fun addCustomization(
        categoryId: String,
        foodId: String,
        title: String,
        isRequired: Boolean,
        isRadio: Boolean,
        isVariation: Boolean,
        selectAmount: Int,
        choices: List<ChoiceItem>
    ) {
        viewModelScope.launch {
            repository.addCustomization(
                categoryId = categoryId,
                foodId = foodId,
                title = title,
                isRequired = isRequired,
                isRadio = isRadio,
                isVariation = isVariation,
                selectAmount = selectAmount,
                choices = choices
            )
        }
    }

    fun deleteCustomization(id: String) {
        viewModelScope.launch {
            repository.deleteCustomization(id)
        }
    }

    // Order Actions
    fun acceptOrder(orderId: String) {
        viewModelScope.launch {
            repository.acceptOrder(orderId)
        }
    }

    fun rejectOrder(orderId: String) {
        viewModelScope.launch {
            repository.rejectOrder(orderId)
        }
    }

    fun completeOrder(orderId: String) {
        viewModelScope.launch {
            repository.completeOrder(orderId)
        }
    }

    fun simulateIncomingOrder() {
        viewModelScope.launch {
            repository.simulateIncomingOrder()
        }
    }

    // Banner Actions
    fun addBanner(imageUrl: String, description: String) {
        viewModelScope.launch {
            repository.addBanner(imageUrl, description)
        }
    }

    fun toggleBannerApproval(bannerId: String, isApproved: Boolean) {
        viewModelScope.launch {
            repository.toggleBannerApproval(bannerId, isApproved)
        }
    }

    fun deleteBanner(bannerId: String) {
        viewModelScope.launch {
            repository.deleteBanner(bannerId)
        }
    }

    // Address Actions
    fun saveAddress(
        id: String?,
        houseNumber: String,
        street: String,
        area: String,
        province: String,
        floor: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            repository.saveAddress(id, houseNumber, street, area, province, floor, latitude, longitude)
        }
    }

    // Voucher Actions
    fun addVoucher(code: String, discountAmount: Double, minOrderAmount: Double) {
        viewModelScope.launch {
            repository.addVoucher(code, discountAmount, minOrderAmount)
        }
    }

    fun deleteVoucher(id: String) {
        viewModelScope.launch {
            repository.deleteVoucher(id)
        }
    }

    fun searchPlaces(query: String): List<PlaceSuggestion> = repository.searchPlaces(query)

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = AppDatabase.getInstance(context)
                    val repo = SellerRepository(db.sellerDao(), context)
                    return SellerViewModel(repo) as T
                }
            }
    }
}
