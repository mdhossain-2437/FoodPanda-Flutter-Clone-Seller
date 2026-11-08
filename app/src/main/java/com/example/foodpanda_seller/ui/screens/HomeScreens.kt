package com.example.foodpanda_seller.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.data.model.SellerSession
import com.example.foodpanda_seller.data.model.ShopProfile
import com.example.foodpanda_seller.data.model.VoucherEntity
import com.example.foodpanda_seller.ui.components.ConfirmAlertDialog
import com.example.foodpanda_seller.ui.components.CustomTextButton
import com.example.foodpanda_seller.ui.components.CustomTextField
import com.example.foodpanda_seller.ui.components.TextTag
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    session: SellerSession,
    shopProfile: ShopProfile,
    categories: List<CategoryEntity>,
    allFoods: List<FoodEntity>,
    activeOrders: List<OrderEntity>,
    allOrders: List<OrderEntity>,
    vouchers: List<VoucherEntity>,
    onNavigateToMenu: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToOrderHistory: () -> Unit,
    onNavigateToBanners: () -> Unit,
    onNavigateToRegisterShop: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onAddVoucher: (String, Double, Double) -> Unit,
    onDeleteVoucher: (String) -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val primary = MaterialTheme.colorScheme.primary

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDashboardSheet by remember { mutableStateOf(false) }
    var showCustomersSheet by remember { mutableStateOf(false) }
    var showVoucherSheet by remember { mutableStateOf(false) }
    var infoDialogContent by remember { mutableStateOf<Pair<String, String>?>(null) }

    if (showLogoutDialog) {
        ConfirmAlertDialog(
            title = "Logging out?",
            subtitle = "Thanks for stopping by. See you again soon!",
            confirmText = "Log out",
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    infoDialogContent?.let { (title, message) ->
        AlertDialog(
            onDismissRequest = { infoDialogContent = null },
            containerColor = Color.White,
            title = { Text(title, fontWeight = FontWeight.Bold) },
            text = { Text(message, fontSize = 14.sp) },
            confirmButton = {
                TextButton(onClick = { infoDialogContent = null }) {
                    Text("Close", color = primary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showDashboardSheet) {
        val totalRevenue = allOrders.filter { !it.isCancelled }.sumOf { it.totalPrice }
        val completedCount = allOrders.count { it.isDelivered }
        ModalBottomSheet(
            onDismissRequest = { showDashboardSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text("Seller Performance Dashboard", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${shopProfile.shopName} • Rating ★ ${shopProfile.rating} (${shopProfile.totalRating} reviews)",
                    color = Color(0xFF616161),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricBox(
                        title = "Total Revenue",
                        value = "$ ${String.format(Locale.US, "%.2f", totalRevenue)}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Active Orders",
                        value = "${activeOrders.size}",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricBox(
                        title = "Completed Orders",
                        value = "$completedCount",
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Menu Items",
                        value = "${allFoods.size} (${categories.size} cats)",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showCustomersSheet) {
        val customers = remember(allOrders) {
            allOrders.groupBy { it.customerEmail }.map { (_, list) ->
                val latest = list.first()
                Triple(latest.customerName, latest.customerPhone, list.size to list.sumOf { it.totalPrice })
            }
        }
        ModalBottomSheet(
            onDismissRequest = { showCustomersSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text("Recent Customers", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(customers) { (name, phone, stats) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF9F9F9),
                            border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("+$phone", fontSize = 12.sp, color = Color.Gray)
                                }
                                TextTag(
                                    text = "${stats.first} orders • $${String.format(Locale.US, "%.2f", stats.second)}",
                                    backgroundColor = primary.copy(alpha = 0.1f),
                                    textColor = primary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showVoucherSheet) {
        var code by remember { mutableStateOf("") }
        var discount by remember { mutableStateOf("") }
        var minOrder by remember { mutableStateOf("") }
        ModalBottomSheet(
            onDismissRequest = { showVoucherSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text("Shop Vouchers", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomTextField(
                        value = code,
                        onValueChange = { code = it },
                        labelText = "Code",
                        modifier = Modifier.weight(1.2f)
                    )
                    CustomTextField(
                        value = discount,
                        onValueChange = { discount = it },
                        labelText = "Off ($)",
                        isNumPad = true,
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = minOrder,
                        onValueChange = { minOrder = it },
                        labelText = "Min ($)",
                        isNumPad = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextButton(
                    text = "Add Voucher",
                    isDisabled = code.isBlank() || discount.toDoubleOrNull() == null,
                    onClick = {
                        val d = discount.toDoubleOrNull() ?: 1.0
                        val m = minOrder.toDoubleOrNull() ?: 5.0
                        onAddVoucher(code, d, m)
                        code = ""
                        discount = ""
                        minOrder = ""
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                vouchers.forEach { v ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF9F9F9),
                        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(v.code, fontWeight = FontWeight.Bold, color = primary, fontSize = 15.sp)
                                Text(
                                    text = "$${String.format(Locale.US, "%.2f", v.discountAmount)} off orders over $${String.format(Locale.US, "%.2f", v.minOrderAmount)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                            IconButton(onClick = { onDeleteVoucher(v.id) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete voucher",
                                    tint = primary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header matching original MyDrawer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .background(primary)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val initial = session.name.firstOrNull()?.uppercaseChar()?.toString() ?: "F"
                            Text(
                                text = initial,
                                color = primary,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            modifier = Modifier
                                .clickable {
                                    scope.launch { drawerState.close() }
                                    onNavigateToRegisterShop()
                                }
                                .testTag("drawer_shop_profile")
                        ) {
                            Text(
                                text = session.name.ifBlank { "Foodpanda Seller" },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = shopProfile.shopName,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Orders History", fontSize = 14.sp) },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.LibraryBooks, contentDescription = null, tint = primary) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToOrderHistory()
                    },
                    modifier = Modifier.testTag("drawer_orders_history")
                )
                NavigationDrawerItem(
                    label = { Text("Addresses", fontSize = 14.sp) },
                    selected = false,
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = primary) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToAddresses()
                    },
                    modifier = Modifier.testTag("drawer_addresses")
                )
                NavigationDrawerItem(
                    label = { Text("Register / Edit Shop", fontSize = 14.sp) },
                    selected = false,
                    icon = { Icon(Icons.Default.Storefront, contentDescription = null, tint = primary) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToRegisterShop()
                    },
                    modifier = Modifier.testTag("drawer_register_shop")
                )
                NavigationDrawerItem(
                    label = { Text("Help center", fontSize = 14.sp) },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = primary) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        infoDialogContent = "FoodPanda Seller Help Center" to
                            "Need partner support? Contact our seller hotline or email hovvathana1@gmail.com for immediate assistance with orders, payouts, or menu approvals."
                    },
                    modifier = Modifier.testTag("drawer_help_center")
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    label = { Text("Settings", fontSize = 14.sp) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        infoDialogContent = "Seller App Settings" to
                            "Auto-accept orders: Off\nOrder sound notifications: Enabled\nCurrency: USD ($)\nLocation: ${shopProfile.street}, ${shopProfile.province}"
                    },
                    modifier = Modifier.testTag("drawer_settings")
                )
                NavigationDrawerItem(
                    label = { Text("Terms & Conditions / Privacy", fontSize = 14.sp) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        infoDialogContent = "Terms & Privacy Policy" to
                            "FoodPanda Seller Partner Agreement: Sellers must maintain accurate menu pricing, preparation times, and hygiene standards."
                    },
                    modifier = Modifier.testTag("drawer_terms")
                )
                NavigationDrawerItem(
                    label = { Text("Log out", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showLogoutDialog = true
                    },
                    modifier = Modifier.testTag("drawer_logout")
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "FoodPanda Seller",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primary
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("home_drawer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open menu",
                                tint = primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
                    .testTag("home_screen"),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Welcome ${session.name}",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${shopProfile.shopName} • ${shopProfile.street}, ${shopProfile.province}",
                    fontSize = 13.sp,
                    color = Color(0xFF707070),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CollageItem(
                        icon = Icons.Default.AddChart,
                        title = "Dashboard",
                        onClick = { showDashboardSheet = true },
                        modifier = Modifier.weight(1f),
                        tag = "collage_dashboard"
                    )
                    CollageItem(
                        icon = Icons.Default.Fastfood,
                        title = "Menu",
                        onClick = {
                            if (shopProfile.isRegistered) onNavigateToMenu()
                            else onNavigateToRegisterShop()
                        },
                        modifier = Modifier.weight(1f),
                        tag = "collage_menu"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CollageItem(
                        icon = Icons.Default.PersonOutline,
                        title = "Customers",
                        onClick = { showCustomersSheet = true },
                        modifier = Modifier.weight(1f),
                        tag = "collage_customers"
                    )
                    CollageItem(
                        icon = Icons.Default.ShoppingCart,
                        title = "Orders",
                        alertCount = activeOrders.size,
                        onClick = {
                            if (shopProfile.isRegistered) onNavigateToOrders()
                            else onNavigateToRegisterShop()
                        },
                        modifier = Modifier.weight(1f),
                        tag = "collage_orders"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CollageItem(
                        icon = Icons.Default.PhotoSizeSelectActual,
                        title = "Banners",
                        onClick = {
                            if (shopProfile.isRegistered) onNavigateToBanners()
                            else onNavigateToRegisterShop()
                        },
                        modifier = Modifier.weight(1f),
                        tag = "collage_banners"
                    )
                    CollageItem(
                        icon = Icons.Default.LocalOffer,
                        title = "Voucher",
                        alertCount = vouchers.size,
                        onClick = {
                            if (shopProfile.isRegistered) showVoucherSheet = true
                            else onNavigateToRegisterShop()
                        },
                        modifier = Modifier.weight(1f),
                        tag = "collage_voucher"
                    )
                }
            }
        }
    }
}

@Composable
private fun CollageItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    alertCount: Int = 0,
    tag: String
) {
    val primary = MaterialTheme.colorScheme.primary
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFCFCFC),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = modifier
            .height(138.dp)
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            if (alertCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp)
                        .size(26.dp)
                        .background(primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = alertCount.toString(),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 12.sp, color = Color(0xFF616161))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenNoApprove(
    onApproveAccount: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FoodPanda Seller",
                        fontSize = 16.sp,
                        color = primary,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .testTag("home_no_approve_screen"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Waiting for admin to approve this account. You may contact via email - ",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "hovvathana1@gmail.com",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = primary
            )
            Spacer(modifier = Modifier.height(28.dp))
            CustomTextButton(
                text = "Approve Account Now",
                onClick = onApproveAccount,
                tag = "approve_account_button"
            )
        }
    }
}
