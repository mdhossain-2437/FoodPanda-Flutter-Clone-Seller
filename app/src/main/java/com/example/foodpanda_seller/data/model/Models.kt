package com.example.foodpanda_seller.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val jsonFormat = Json { ignoreUnknownKeys = true }

@Serializable
data class ChoiceItem(
    val choice: String,
    val price: Double
)

@Serializable
data class FoodOrderItem(
    val foodName: String,
    val quantity: Int,
    val foodPrice: Double,
    val customize: String
)

@Entity(tableName = "categories")
@Serializable
data class CategoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val isPublished: Boolean = true
)

@Entity(tableName = "foods")
@Serializable
data class FoodEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val comparePrice: Double = 0.0,
    val isPublished: Boolean = true
)

@Entity(tableName = "customizations")
@Serializable
data class CustomizationEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val foodId: String,
    val title: String,
    val isRequired: Boolean,
    val isRadio: Boolean,
    val isVariation: Boolean,
    val selectAmount: Int,
    val choicesJson: String
) {
    fun getChoices(): List<ChoiceItem> = try {
        jsonFormat.decodeFromString(choicesJson)
    } catch (e: Exception) {
        emptyList()
    }

    companion object {
        fun encodeChoices(choices: List<ChoiceItem>): String =
            jsonFormat.encodeToString(choices)
    }
}

@Entity(tableName = "orders")
@Serializable
data class OrderEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val houseNumber: String,
    val street: String,
    val area: String,
    val province: String,
    val floor: String,
    val foodOrdersJson: String,
    val totalPrice: Double,
    val deliveryPrice: Double,
    val discountPrice: Double,
    val voucherId: String? = null,
    val isPaid: Boolean = false,
    val isShopAccept: Boolean = false,
    val isRiderAccept: Boolean = false,
    val isPickup: Boolean = false,
    val isNear: Boolean = false,
    val isDelivered: Boolean = false,
    val isCancelled: Boolean = false,
    val isCutlery: Boolean = false,
    val time: Long,
    val deliveredTime: Long? = null
) {
    fun getFoodOrders(): List<FoodOrderItem> = try {
        jsonFormat.decodeFromString(foodOrdersJson)
    } catch (e: Exception) {
        emptyList()
    }

    companion object {
        fun encodeFoodOrders(items: List<FoodOrderItem>): String =
            jsonFormat.encodeToString(items)
    }
}

@Entity(tableName = "banners")
@Serializable
data class BannerEntity(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val description: String,
    val creatorId: String,
    val isApproved: Boolean = false,
    val createdAt: Long
)

@Entity(tableName = "addresses")
@Serializable
data class AddressEntity(
    @PrimaryKey val id: String,
    val houseNumber: String,
    val street: String,
    val area: String,
    val province: String,
    val floor: String,
    val latitude: Double,
    val longitude: Double,
    val deliveryInstruction: String = "",
    val label: String = "Shop"
)

@Entity(tableName = "vouchers")
@Serializable
data class VoucherEntity(
    @PrimaryKey val id: String,
    val code: String,
    val discountAmount: Double,
    val minOrderAmount: Double,
    val isActive: Boolean = true
)

data class SellerSession(
    val isSignedIn: Boolean = true,
    val uid: String = "seller_fp_001",
    val name: String = "Phnom Penh Bistro",
    val email: String = "seller@foodpanda.com.kh",
    val phoneNumber: String = "+85512345678",
    val emailVerified: Boolean = true,
    val isApproved: Boolean = true
)

data class ShopProfile(
    val isRegistered: Boolean = true,
    val shopName: String = "Phnom Penh Street Bistro",
    val shopDescription: String = "Authentic Khmer & Asian Fusion bowls, burgers, and handcrafted iced teas.",
    val shopImage: String = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
    val rating: Double = 4.8,
    val totalRating: Int = 342,
    val houseNumber: String = "320",
    val street: String = "St 320, Boeung Keng Kang 1",
    val area: String = "Chamkarmon",
    val province: String = "Phnom Penh",
    val floor: String = "Ground Floor",
    val latitude: Double = 11.5564,
    val longitude: Double = 104.9282
)

data class PlaceSuggestion(
    val placeId: String,
    val mainText: String,
    val secondaryText: String,
    val latitude: Double,
    val longitude: Double,
    val houseNumber: String,
    val street: String,
    val area: String,
    val province: String
)
