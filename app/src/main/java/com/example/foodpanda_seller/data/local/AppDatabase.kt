package com.example.foodpanda_seller.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodpanda_seller.data.model.AddressEntity
import com.example.foodpanda_seller.data.model.BannerEntity
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.CustomizationEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.data.model.VoucherEntity

@Database(
    entities = [
        CategoryEntity::class,
        FoodEntity::class,
        CustomizationEntity::class,
        OrderEntity::class,
        BannerEntity::class,
        AddressEntity::class,
        VoucherEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sellerDao(): SellerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foodpanda_seller.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
