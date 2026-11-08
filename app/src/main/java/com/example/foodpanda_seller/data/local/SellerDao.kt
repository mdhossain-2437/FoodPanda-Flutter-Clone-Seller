package com.example.foodpanda_seller.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodpanda_seller.data.model.AddressEntity
import com.example.foodpanda_seller.data.model.BannerEntity
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.CustomizationEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.data.model.VoucherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {
    // Categories
    @Query("SELECT * FROM categories ORDER BY title ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Query("UPDATE categories SET isPublished = :isPublished WHERE id = :id")
    suspend fun updateCategoryPublished(id: String, isPublished: Boolean)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategory(id: String)

    // Foods
    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun observeAllFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE categoryId = :categoryId ORDER BY name ASC")
    fun observeFoodsByCategory(categoryId: String): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE id = :foodId LIMIT 1")
    suspend fun getFoodById(foodId: String): FoodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFood(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFoods(foods: List<FoodEntity>)

    @Query("UPDATE foods SET isPublished = :isPublished WHERE id = :id")
    suspend fun updateFoodPublished(id: String, isPublished: Boolean)

    @Query("DELETE FROM foods WHERE id = :id")
    suspend fun deleteFood(id: String)

    @Query("DELETE FROM foods WHERE categoryId = :categoryId")
    suspend fun deleteFoodsByCategory(categoryId: String)

    // Customizations
    @Query("SELECT * FROM customizations WHERE foodId = :foodId")
    fun observeCustomizationsByFood(foodId: String): Flow<List<CustomizationEntity>>

    @Query("SELECT * FROM customizations")
    fun observeAllCustomizations(): Flow<List<CustomizationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomization(customization: CustomizationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCustomizations(customizations: List<CustomizationEntity>)

    @Query("DELETE FROM customizations WHERE id = :id")
    suspend fun deleteCustomization(id: String)

    // Orders
    @Query("SELECT * FROM orders WHERE isRiderAccept = 0 AND isCancelled = 0 AND isDelivered = 0 ORDER BY time DESC")
    fun observeActiveOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY time DESC")
    fun observeAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrders(orders: List<OrderEntity>)

    @Query("UPDATE orders SET isShopAccept = 1 WHERE id = :orderId")
    suspend fun acceptOrder(orderId: String)

    @Query("UPDATE orders SET isCancelled = 1 WHERE id = :orderId")
    suspend fun rejectOrder(orderId: String)

    @Query("UPDATE orders SET isRiderAccept = 1, isDelivered = 1, deliveredTime = :deliveredTime WHERE id = :orderId")
    suspend fun completeOrder(orderId: String, deliveredTime: Long)

    // Banners
    @Query("SELECT * FROM banners ORDER BY createdAt DESC")
    fun observeBanners(): Flow<List<BannerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBanner(banner: BannerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBanners(banners: List<BannerEntity>)

    @Query("UPDATE banners SET isApproved = :isApproved WHERE id = :bannerId")
    suspend fun updateBannerApproval(bannerId: String, isApproved: Boolean)

    @Query("DELETE FROM banners WHERE id = :bannerId")
    suspend fun deleteBanner(bannerId: String)

    // Addresses
    @Query("SELECT * FROM addresses ORDER BY id ASC")
    fun observeAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAddress(address: AddressEntity)

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteAddress(id: String)

    // Vouchers
    @Query("SELECT * FROM vouchers ORDER BY code ASC")
    fun observeVouchers(): Flow<List<VoucherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVoucher(voucher: VoucherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVouchers(vouchers: List<VoucherEntity>)

    @Query("DELETE FROM vouchers WHERE id = :id")
    suspend fun deleteVoucher(id: String)
}
