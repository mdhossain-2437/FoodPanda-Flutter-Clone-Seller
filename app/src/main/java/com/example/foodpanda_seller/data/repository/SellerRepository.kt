package com.example.foodpanda_seller.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.foodpanda_seller.data.local.SellerDao
import com.example.foodpanda_seller.data.model.AddressEntity
import com.example.foodpanda_seller.data.model.BannerEntity
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.ChoiceItem
import com.example.foodpanda_seller.data.model.CustomizationEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.data.model.FoodOrderItem
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.data.model.PlaceSuggestion
import com.example.foodpanda_seller.data.model.SellerSession
import com.example.foodpanda_seller.data.model.ShopProfile
import com.example.foodpanda_seller.data.model.VoucherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SellerRepository(
    private val dao: SellerDao,
    context: Context
) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("foodpanda_seller_prefs", Context.MODE_PRIVATE)

    private val _session = MutableStateFlow(loadSession())
    val session: StateFlow<SellerSession> = _session.asStateFlow()

    private val _shopProfile = MutableStateFlow(loadShopProfile())
    val shopProfile: StateFlow<ShopProfile> = _shopProfile.asStateFlow()

    val categories: Flow<List<CategoryEntity>> = dao.observeCategories()
    val allFoods: Flow<List<FoodEntity>> = dao.observeAllFoods()
    val allCustomizations: Flow<List<CustomizationEntity>> = dao.observeAllCustomizations()
    val activeOrders: Flow<List<OrderEntity>> = dao.observeActiveOrders()
    val allOrders: Flow<List<OrderEntity>> = dao.observeAllOrders()
    val banners: Flow<List<BannerEntity>> = dao.observeBanners()
    val addresses: Flow<List<AddressEntity>> = dao.observeAddresses()
    val vouchers: Flow<List<VoucherEntity>> = dao.observeVouchers()

    fun observeFoodsByCategory(categoryId: String): Flow<List<FoodEntity>> =
        dao.observeFoodsByCategory(categoryId)

    private fun loadSession(): SellerSession {
        return SellerSession(
            isSignedIn = prefs.getBoolean("signed_in", true),
            uid = prefs.getString("uid", "seller_fp_001") ?: "seller_fp_001",
            name = prefs.getString("name", "Vathana Hov") ?: "Vathana Hov",
            email = prefs.getString("email", "hovvathana1@gmail.com") ?: "hovvathana1@gmail.com",
            phoneNumber = prefs.getString("phoneNumber", "+85512889900") ?: "+85512889900",
            emailVerified = prefs.getBoolean("emailVerified", true),
            isApproved = prefs.getBoolean("isApproved", true)
        )
    }

    private fun saveSession(session: SellerSession) {
        prefs.edit()
            .putBoolean("signed_in", session.isSignedIn)
            .putString("uid", session.uid)
            .putString("name", session.name)
            .putString("email", session.email)
            .putString("phoneNumber", session.phoneNumber)
            .putBoolean("emailVerified", session.emailVerified)
            .putBoolean("isApproved", session.isApproved)
            .apply()
        _session.value = session
    }

    private fun loadShopProfile(): ShopProfile {
        return ShopProfile(
            isRegistered = prefs.getBoolean("shop_registered", true),
            shopName = prefs.getString("shopName", "Phnom Penh Street Bistro") ?: "Phnom Penh Street Bistro",
            shopDescription = prefs.getString(
                "shopDescription",
                "Authentic Khmer & Asian Fusion bowls, burgers, and handcrafted iced teas."
            ) ?: "",
            shopImage = prefs.getString(
                "shopImage",
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80"
            ) ?: "",
            rating = prefs.getFloat("rating", 4.8f).toDouble(),
            totalRating = prefs.getInt("totalRating", 342),
            houseNumber = prefs.getString("houseNumber", "320") ?: "320",
            street = prefs.getString("street", "St 320, Boeung Keng Kang 1") ?: "St 320, Boeung Keng Kang 1",
            area = prefs.getString("area", "Chamkarmon") ?: "Chamkarmon",
            province = prefs.getString("province", "Phnom Penh") ?: "Phnom Penh",
            floor = prefs.getString("floor", "Ground Floor") ?: "Ground Floor",
            latitude = prefs.getFloat("latitude", 11.5564f).toDouble(),
            longitude = prefs.getFloat("longitude", 104.9282f).toDouble()
        )
    }

    fun updateShopProfile(profile: ShopProfile) {
        prefs.edit()
            .putBoolean("shop_registered", profile.isRegistered)
            .putString("shopName", profile.shopName)
            .putString("shopDescription", profile.shopDescription)
            .putString("shopImage", profile.shopImage)
            .putFloat("rating", profile.rating.toFloat())
            .putInt("totalRating", profile.totalRating)
            .putString("houseNumber", profile.houseNumber)
            .putString("street", profile.street)
            .putString("area", profile.area)
            .putString("province", profile.province)
            .putString("floor", profile.floor)
            .putFloat("latitude", profile.latitude.toFloat())
            .putFloat("longitude", profile.longitude.toFloat())
            .apply()
        _shopProfile.value = profile
    }

    fun signIn(email: String, name: String? = null) {
        val current = _session.value
        val displayName = name ?: current.name.ifBlank {
            email.substringBefore("@").replaceFirstChar { it.uppercase() }
        }
        saveSession(
            current.copy(
                isSignedIn = true,
                email = email,
                name = displayName,
                emailVerified = true,
                isApproved = true
            )
        )
    }

    fun registerSeller(name: String, email: String) {
        saveSession(
            SellerSession(
                isSignedIn = true,
                uid = "seller_${UUID.randomUUID().toString().take(8)}",
                name = name,
                email = email,
                phoneNumber = "+85512889900",
                emailVerified = false,
                isApproved = true
            )
        )
    }

    fun markEmailVerified() {
        saveSession(_session.value.copy(emailVerified = true))
    }

    fun setApprovalStatus(approved: Boolean) {
        saveSession(_session.value.copy(isApproved = approved))
    }

    fun signOut() {
        saveSession(_session.value.copy(isSignedIn = false))
    }

    suspend fun ensureSeedData() {
        if (dao.getCategoryCount() > 0) return

        val categories = listOf(
            CategoryEntity(
                id = "cat_breakfast",
                title = "All Breakfast & Bowls",
                subtitle = "Signature morning rice bowls, noodle soups & iced coffee combos",
                isPublished = true
            ),
            CategoryEntity(
                id = "cat_burgers",
                title = "Artisan Burgers & Wraps",
                subtitle = "Chargrilled smash burgers, crispy chicken tenders & loaded fries",
                isPublished = true
            ),
            CategoryEntity(
                id = "cat_drinks",
                title = "Handcrafted Beverages",
                subtitle = "Freshly brewed Thai milk tea, matcha lattes & tropical fruit teas",
                isPublished = true
            ),
            CategoryEntity(
                id = "cat_desserts",
                title = "Sweet Treats & Sides",
                subtitle = "Mango sticky rice, pandan waffles & crispy spring rolls",
                isPublished = true
            )
        )
        dao.upsertCategories(categories)

        val foods = listOf(
            FoodEntity(
                id = "food_1",
                categoryId = "cat_breakfast",
                name = "Khmer Pork & Egg Rice Bowl",
                description = "Charcoal-grilled marinated pork slices over jasmine rice with pickled radish and sunny-side egg.",
                image = "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=600&q=80",
                price = 4.50,
                comparePrice = 5.50,
                isPublished = true
            ),
            FoodEntity(
                id = "food_2",
                categoryId = "cat_breakfast",
                name = "Phnom Penh Beef Noodle Soup",
                description = "Slow-simmered aromatic beef broth with tender brisket, meatballs, rice noodles, and fresh herbs.",
                image = "https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=600&q=80",
                price = 5.25,
                comparePrice = 6.00,
                isPublished = true
            ),
            FoodEntity(
                id = "food_3",
                categoryId = "cat_burgers",
                name = "Double Smash Wagyu Burger",
                description = "Two seared Wagyu patties, aged cheddar, caramelized onions, house spicy mayo on brioche bun.",
                image = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
                price = 7.90,
                comparePrice = 9.50,
                isPublished = true
            ),
            FoodEntity(
                id = "food_4",
                categoryId = "cat_burgers",
                name = "Crispy Korean Gochujang Chicken",
                description = "Double-fried crispy chicken thigh glazed in sweet-spicy gochujang sauce with Asian slaw.",
                image = "https://images.unsplash.com/photo-1606755962773-d324e0a13086?w=600&q=80",
                price = 6.50,
                comparePrice = 7.50,
                isPublished = true
            ),
            FoodEntity(
                id = "food_5",
                categoryId = "cat_drinks",
                name = "Brown Sugar Pearl Milk Tea",
                description = "Warm caramelized brown sugar tapioca pearls with organic fresh milk and roasted Assam black tea.",
                image = "https://images.unsplash.com/photo-1558857563-b371033873b8?w=600&q=80",
                price = 2.85,
                comparePrice = 3.50,
                isPublished = true
            ),
            FoodEntity(
                id = "food_6",
                categoryId = "cat_desserts",
                name = "Mango Sticky Rice Supreme",
                description = "Sweet ripe Kampot mango served with warm coconut-infused glutinous rice and toasted mung beans.",
                image = "https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=600&q=80",
                price = 3.75,
                comparePrice = 4.25,
                isPublished = true
            )
        )
        dao.upsertFoods(foods)

        val customizations = listOf(
            CustomizationEntity(
                id = "cust_sugar",
                categoryId = "cat_drinks",
                foodId = "food_5",
                title = "Sugar Level",
                isRequired = true,
                isRadio = true,
                isVariation = false,
                selectAmount = 1,
                choicesJson = CustomizationEntity.encodeChoices(
                    listOf(
                        ChoiceItem("100% Normal Sugar", 0.0),
                        ChoiceItem("75% Less Sugar", 0.0),
                        ChoiceItem("50% Half Sugar", 0.0),
                        ChoiceItem("25% Light Sugar", 0.0)
                    )
                )
            ),
            CustomizationEntity(
                id = "cust_toppings",
                categoryId = "cat_breakfast",
                foodId = "food_1",
                title = "Extra Add-Ons",
                isRequired = false,
                isRadio = false,
                isVariation = false,
                selectAmount = 2,
                choicesJson = CustomizationEntity.encodeChoices(
                    listOf(
                        ChoiceItem("Extra Fried Egg", 0.75),
                        ChoiceItem("Extra Grilled Pork", 1.50),
                        ChoiceItem("Iced Lemon Tea Combo", 1.25)
                    )
                )
            )
        )
        dao.upsertCustomizations(customizations)

        val now = System.currentTimeMillis()
        val orders = listOf(
            OrderEntity(
                id = "FP-8942",
                customerName = "Sokha Chan",
                customerEmail = "sokha.chan@gmail.com",
                customerPhone = "85512901122",
                houseNumber = "45",
                street = "St 288, BKK1",
                area = "Chamkarmon",
                province = "Phnom Penh",
                floor = "3rd Floor, Apt 3B",
                foodOrdersJson = OrderEntity.encodeFoodOrders(
                    listOf(
                        FoodOrderItem("Khmer Pork & Egg Rice Bowl", 2, 4.50, "75% of Sugar, Extra Fried Egg"),
                        FoodOrderItem("Brown Sugar Pearl Milk Tea", 2, 2.85, "75% of Sugar")
                    )
                ),
                totalPrice = 14.70,
                deliveryPrice = 1.50,
                discountPrice = 2.00,
                voucherId = "PANDA20",
                isPaid = true,
                isShopAccept = false,
                isRiderAccept = false,
                isDelivered = false,
                isCancelled = false,
                isCutlery = true,
                time = now - 8 * 60 * 1000L
            ),
            OrderEntity(
                id = "FP-8941",
                customerName = "Dara Pich",
                customerEmail = "dara.pich@gmail.com",
                customerPhone = "85598765432",
                houseNumber = "112",
                street = "Norodom Blvd",
                area = "Daun Penh",
                province = "Phnom Penh",
                floor = "Lobby Reception",
                foodOrdersJson = OrderEntity.encodeFoodOrders(
                    listOf(
                        FoodOrderItem("Double Smash Wagyu Burger", 1, 7.90, "Extra Cheddar"),
                        FoodOrderItem("Crispy Korean Gochujang Chicken", 1, 6.50, "Regular")
                    )
                ),
                totalPrice = 14.40,
                deliveryPrice = 1.25,
                discountPrice = 0.0,
                isPaid = false,
                isShopAccept = true,
                isRiderAccept = false,
                isDelivered = false,
                isCancelled = false,
                isCutlery = false,
                time = now - 22 * 60 * 1000L
            ),
            OrderEntity(
                id = "FP-8930",
                customerName = "Maly Seng",
                customerEmail = "maly.seng@gmail.com",
                customerPhone = "85517223344",
                houseNumber = "18",
                street = "St 51, Pasteur",
                area = "BKK1",
                province = "Phnom Penh",
                floor = "2nd Floor",
                foodOrdersJson = OrderEntity.encodeFoodOrders(
                    listOf(
                        FoodOrderItem("Phnom Penh Beef Noodle Soup", 2, 5.25, "Extra Herbs"),
                        FoodOrderItem("Mango Sticky Rice Supreme", 1, 3.75, "Standard")
                    )
                ),
                totalPrice = 14.25,
                deliveryPrice = 1.00,
                discountPrice = 1.25,
                isPaid = true,
                isShopAccept = true,
                isRiderAccept = true,
                isDelivered = true,
                isCancelled = false,
                isCutlery = true,
                time = now - 140 * 60 * 1000L,
                deliveredTime = now - 110 * 60 * 1000L
            ),
            OrderEntity(
                id = "FP-8918",
                customerName = "Rithy Kem",
                customerEmail = "rithy.kem@gmail.com",
                customerPhone = "85511556677",
                houseNumber = "88",
                street = "Russian Confed Blvd",
                area = "Tuol Kork",
                province = "Phnom Penh",
                floor = "1st Floor",
                foodOrdersJson = OrderEntity.encodeFoodOrders(
                    listOf(
                        FoodOrderItem("Brown Sugar Pearl Milk Tea", 3, 2.85, "50% Sugar")
                    )
                ),
                totalPrice = 8.55,
                deliveryPrice = 1.50,
                discountPrice = 0.0,
                isPaid = false,
                isShopAccept = false,
                isRiderAccept = false,
                isDelivered = false,
                isCancelled = true,
                isCutlery = false,
                time = now - 280 * 60 * 1000L
            )
        )
        dao.upsertOrders(orders)

        val banners = listOf(
            BannerEntity(
                id = "banner_1",
                imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80",
                description = "Weekend Feast: 25% OFF All Family Rice Bowls & Burgers",
                creatorId = "seller_fp_001",
                isApproved = true,
                createdAt = now - 86_400_000L
            ),
            BannerEntity(
                id = "banner_2",
                imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80",
                description = "Free Brown Sugar Milk Tea on Orders Over \$12",
                creatorId = "seller_fp_001",
                isApproved = false,
                createdAt = now - 3_600_000L
            )
        )
        dao.upsertBanners(banners)

        dao.upsertAddress(
            AddressEntity(
                id = "addr_main",
                houseNumber = "320",
                street = "St 320",
                area = "Boeung Keng Kang 1",
                province = "Phnom Penh",
                floor = "Ground Floor",
                latitude = 11.5564,
                longitude = 104.9282,
                deliveryInstruction = "Pickup at main restaurant counter",
                label = "Main Restaurant Branch"
            )
        )

        dao.upsertVouchers(
            listOf(
                VoucherEntity(
                    id = "v_panda20",
                    code = "PANDA20",
                    discountAmount = 2.00,
                    minOrderAmount = 10.00,
                    isActive = true
                ),
                VoucherEntity(
                    id = "v_freeship",
                    code = "FREEDEL",
                    discountAmount = 1.50,
                    minOrderAmount = 8.00,
                    isActive = true
                )
            )
        )
    }

    // Category CRUD
    suspend fun saveCategory(title: String, subtitle: String, id: String?) {
        val catId = if (id.isNullOrBlank()) "cat_${UUID.randomUUID().toString().take(8)}" else id
        dao.upsertCategory(
            CategoryEntity(
                id = catId,
                title = title.trim(),
                subtitle = subtitle.trim(),
                isPublished = true
            )
        )
    }

    suspend fun updateCategoryPublished(id: String, isPublished: Boolean) {
        dao.updateCategoryPublished(id, isPublished)
    }

    suspend fun deleteCategory(id: String) {
        dao.deleteFoodsByCategory(id)
        dao.deleteCategory(id)
    }

    // Food CRUD
    suspend fun getFoodById(foodId: String): FoodEntity? = dao.getFoodById(foodId)

    suspend fun saveFood(
        categoryId: String,
        foodId: String?,
        name: String,
        description: String,
        price: Double,
        comparePrice: Double,
        image: String
    ) {
        val resolvedId = if (foodId.isNullOrBlank()) "food_${UUID.randomUUID().toString().take(8)}" else foodId
        dao.upsertFood(
            FoodEntity(
                id = resolvedId,
                categoryId = categoryId,
                name = name.trim(),
                description = description.trim(),
                image = image.ifBlank {
                    "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&q=80"
                },
                price = price,
                comparePrice = comparePrice,
                isPublished = true
            )
        )
    }

    suspend fun updateFoodPublished(id: String, isPublished: Boolean) {
        dao.updateFoodPublished(id, isPublished)
    }

    suspend fun deleteFood(id: String) {
        dao.deleteFood(id)
    }

    // Customization CRUD
    suspend fun addCustomization(
        categoryId: String,
        foodId: String,
        title: String,
        isRequired: Boolean,
        isRadio: Boolean,
        isVariation: Boolean,
        selectAmount: Int,
        choices: List<ChoiceItem>
    ) {
        dao.upsertCustomization(
            CustomizationEntity(
                id = "cust_${UUID.randomUUID().toString().take(8)}",
                categoryId = categoryId,
                foodId = foodId,
                title = title.trim(),
                isRequired = isRequired,
                isRadio = isRadio,
                isVariation = isVariation,
                selectAmount = if (isRadio) 1 else selectAmount,
                choicesJson = CustomizationEntity.encodeChoices(choices)
            )
        )
    }

    suspend fun deleteCustomization(id: String) {
        dao.deleteCustomization(id)
    }

    // Orders
    suspend fun acceptOrder(orderId: String) {
        dao.acceptOrder(orderId)
    }

    suspend fun rejectOrder(orderId: String) {
        dao.rejectOrder(orderId)
    }

    suspend fun completeOrder(orderId: String) {
        dao.completeOrder(orderId, System.currentTimeMillis())
    }

    suspend fun simulateIncomingOrder() {
        val orderNum = (8943..9999).random()
        val customers = listOf(
            Triple("Vannak Lim", "vannak.lim@gmail.com", "85512334455"),
            Triple("Bopha Meas", "bopha.meas@gmail.com", "85516778899"),
            Triple("Narith Chea", "narith.chea@gmail.com", "85592445566")
        )
        val (name, email, phone) = customers.random()
        dao.upsertOrder(
            OrderEntity(
                id = "FP-$orderNum",
                customerName = name,
                customerEmail = email,
                customerPhone = phone,
                houseNumber = "204",
                street = "Monivong Blvd",
                area = "Boeung Keng Kang",
                province = "Phnom Penh",
                floor = "Floor 2",
                foodOrdersJson = OrderEntity.encodeFoodOrders(
                    listOf(
                        FoodOrderItem("Double Smash Wagyu Burger", 1, 7.90, "75% of Sugar"),
                        FoodOrderItem("Brown Sugar Pearl Milk Tea", 2, 2.85, "75% of Sugar")
                    )
                ),
                totalPrice = 13.60,
                deliveryPrice = 1.25,
                discountPrice = 1.00,
                isPaid = true,
                isShopAccept = false,
                isRiderAccept = false,
                isDelivered = false,
                isCancelled = false,
                isCutlery = true,
                time = System.currentTimeMillis()
            )
        )
    }

    // Banners
    suspend fun addBanner(imageUrl: String, description: String) {
        dao.upsertBanner(
            BannerEntity(
                id = "banner_${UUID.randomUUID().toString().take(8)}",
                imageUrl = imageUrl.ifBlank {
                    "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80"
                },
                description = description.trim(),
                creatorId = _session.value.uid,
                isApproved = false,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleBannerApproval(bannerId: String, isApproved: Boolean) {
        dao.updateBannerApproval(bannerId, isApproved)
    }

    suspend fun deleteBanner(bannerId: String) {
        dao.deleteBanner(bannerId)
    }

    // Addresses
    suspend fun saveAddress(
        id: String?,
        houseNumber: String,
        street: String,
        area: String,
        province: String,
        floor: String,
        latitude: Double,
        longitude: Double
    ) {
        val resolvedId = if (id.isNullOrBlank()) "addr_${UUID.randomUUID().toString().take(6)}" else id
        dao.upsertAddress(
            AddressEntity(
                id = resolvedId,
                houseNumber = houseNumber.trim(),
                street = street.trim(),
                area = area.trim(),
                province = province.trim().ifBlank { "Phnom Penh" },
                floor = floor.trim(),
                latitude = latitude,
                longitude = longitude
            )
        )
        // Keep shop profile address synced
        updateShopProfile(
            _shopProfile.value.copy(
                houseNumber = houseNumber.trim(),
                street = street.trim(),
                area = area.trim(),
                province = province.trim().ifBlank { "Phnom Penh" },
                floor = floor.trim(),
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    // Vouchers
    suspend fun addVoucher(code: String, discountAmount: Double, minOrderAmount: Double) {
        dao.upsertVoucher(
            VoucherEntity(
                id = "v_${UUID.randomUUID().toString().take(6)}",
                code = code.trim().uppercase(),
                discountAmount = discountAmount,
                minOrderAmount = minOrderAmount,
                isActive = true
            )
        )
    }

    suspend fun deleteVoucher(id: String) {
        dao.deleteVoucher(id)
    }

    fun searchPlaces(query: String): List<PlaceSuggestion> {
        val allPlaces = listOf(
            PlaceSuggestion(
                placeId = "kh_bkk1",
                mainText = "320 St 320",
                secondaryText = "Boeung Keng Kang 1, Phnom Penh",
                latitude = 11.5564,
                longitude = 104.9282,
                houseNumber = "320",
                street = "St 320",
                area = "Boeung Keng Kang 1",
                province = "Phnom Penh"
            ),
            PlaceSuggestion(
                placeId = "kh_riverside",
                mainText = "108 Sisowath Quay",
                secondaryText = "Riverside, Daun Penh, Phnom Penh",
                latitude = 11.5730,
                longitude = 104.9315,
                houseNumber = "108",
                street = "Sisowath Quay",
                area = "Daun Penh",
                province = "Phnom Penh"
            ),
            PlaceSuggestion(
                placeId = "kh_tk",
                mainText = "45 St 315",
                secondaryText = "Boeung Kak 1, Tuol Kork, Phnom Penh",
                latitude = 11.5812,
                longitude = 104.8984,
                houseNumber = "45",
                street = "St 315",
                area = "Tuol Kork",
                province = "Phnom Penh"
            ),
            PlaceSuggestion(
                placeId = "kh_russian_market",
                mainText = "162 St 155",
                secondaryText = "Toul Tom Poung 1, Chamkarmon, Phnom Penh",
                latitude = 11.5405,
                longitude = 104.9142,
                houseNumber = "162",
                street = "St 155",
                area = "Toul Tom Poung 1",
                province = "Phnom Penh"
            ),
            PlaceSuggestion(
                placeId = "kh_aeon",
                mainText = "132 Samdach Sothearos Blvd",
                secondaryText = "Tonle Bassac, Chamkarmon, Phnom Penh",
                latitude = 11.5478,
                longitude = 104.9336,
                houseNumber = "132",
                street = "Samdach Sothearos Blvd",
                area = "Tonle Bassac",
                province = "Phnom Penh"
            )
        )
        if (query.isBlank()) return allPlaces
        return allPlaces.filter {
            it.mainText.contains(query, ignoreCase = true) ||
                it.secondaryText.contains(query, ignoreCase = true) ||
                it.area.contains(query, ignoreCase = true)
        }.ifEmpty { allPlaces }
    }
}
