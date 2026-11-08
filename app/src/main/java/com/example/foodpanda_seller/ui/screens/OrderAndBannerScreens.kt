package com.example.foodpanda_seller.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodpanda_seller.R
import com.example.foodpanda_seller.data.model.BannerEntity
import com.example.foodpanda_seller.data.model.OrderEntity
import com.example.foodpanda_seller.ui.components.ConfirmAlertDialog
import com.example.foodpanda_seller.ui.components.CustomTextButton
import com.example.foodpanda_seller.ui.components.CustomTextField
import com.example.foodpanda_seller.ui.components.DottedBorderBox
import com.example.foodpanda_seller.ui.components.OrderCard
import com.example.foodpanda_seller.ui.components.TextTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    orders: List<OrderEntity>,
    onBack: () -> Unit,
    onAcceptOrder: (String) -> Unit,
    onRejectOrder: (String) -> Unit,
    onCompleteOrder: (String) -> Unit,
    onSimulateIncomingOrder: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Orders",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("orders_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSimulateIncomingOrder,
                        modifier = Modifier.testTag("simulate_order_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Simulate New Incoming Order",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
            )
        },
        containerColor = Color(0xFFF6F6F6)
    ) { innerPadding ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .testTag("orders_empty_state"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "All caught up! No active orders right now.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF616161)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    CustomTextButton(
                        text = "Simulate Incoming Order",
                        onClick = onSimulateIncomingOrder,
                        tag = "empty_simulate_order_button"
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 6.dp)
                    .testTag("orders_list")
            ) {
                items(orders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        isHistoryOrder = false,
                        onAcceptOrder = { onAcceptOrder(order.id) },
                        onRejectOrder = { onRejectOrder(order.id) },
                        onCompleteOrder = { onCompleteOrder(order.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    orders: List<OrderEntity>,
    onBack: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orders History",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("order_history_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
            )
        },
        containerColor = Color(0xFFF6F6F6)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 6.dp)
                .testTag("order_history_screen")
        ) {
            items(orders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    isHistoryOrder = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerScreen(
    banners: List<BannerEntity>,
    onBack: () -> Unit,
    onAddBanner: () -> Unit,
    onToggleApproval: (String, Boolean) -> Unit,
    onDeleteBanner: (String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    var bannerToDelete by remember { mutableStateOf<BannerEntity?>(null) }

    bannerToDelete?.let { target ->
        ConfirmAlertDialog(
            title = "Delete",
            subtitle = "Are you sure to delete this banner?",
            confirmText = "Confirm",
            onDismiss = { bannerToDelete = null },
            onConfirm = {
                onDeleteBanner(target.id)
                bannerToDelete = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Banners",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("banner_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onAddBanner,
                        modifier = Modifier.testTag("banner_top_add_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add banner",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
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
                .testTag("banner_screen"),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "All Banners",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            banners.forEach { banner ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .testTag("banner_card_${banner.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = banner.imageUrl,
                            contentDescription = banner.description,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.panda_eating),
                            modifier = Modifier
                                .height(130.dp)
                                .aspectRatio(4f / 5f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = banner.description,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.clickable {
                                        onToggleApproval(banner.id, !banner.isApproved)
                                    }
                                ) {
                                    TextTag(
                                        text = if (banner.isApproved) "Approved" else "Waiting Approval",
                                        backgroundColor = if (banner.isApproved) primary else Color.White,
                                        textColor = if (banner.isApproved) Color.White else primary,
                                        borderColor = primary,
                                        leadingIcon = {
                                            Icon(
                                                imageVector = if (banner.isApproved) {
                                                    Icons.Default.CheckCircleOutline
                                                } else {
                                                    Icons.Default.AccessTime
                                                },
                                                contentDescription = null,
                                                tint = if (banner.isApproved) Color.White else primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { bannerToDelete = banner },
                                    modifier = Modifier.testTag("delete_banner_${banner.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete banner",
                                        tint = primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(15.dp),
                color = Color(0xFFE0E0E0),
                border = BorderStroke(1.dp, primary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable(onClick = onAddBanner)
                    .testTag("banner_bottom_add_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new banner",
                        tint = primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBannerScreen(
    onBack: () -> Unit,
    onSave: (String, String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val sampleBannerImages = remember {
        listOf(
            "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80",
            "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80",
            "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=800&q=80"
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUrl = uri.toString()
        } else if (imageUrl.isBlank()) {
            imageUrl = sampleBannerImages.random()
        }
    }

    val isDisabled = imageUrl.isBlank() || description.isBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Banner",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("add_banner_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { if (!isDisabled) onSave(imageUrl, description) },
                        enabled = !isDisabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Save Banner",
                            tint = if (isDisabled) Color.LightGray else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primary)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp)
                .testTag("add_banner_screen")
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                if (imageUrl.isBlank()) {
                    DottedBorderBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = Color(0xFF424242)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CustomTextButton(
                                    text = "Use Promo Art",
                                    onClick = { imageUrl = sampleBannerImages.random() },
                                    modifier = Modifier.weight(1f),
                                    tag = "banner_sample_photo_button"
                                )
                                CustomTextButton(
                                    text = "Upload a photo",
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    tag = "banner_upload_photo_button"
                                )
                            }
                        }
                    }
                } else {
                    DottedBorderBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(174.dp)
                                .aspectRatio(4f / 5f)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Banner Image",
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.panda_eating),
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { imageUrl = "" },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(30.dp)
                                    .background(Color.White, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove image",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    labelText = "Description",
                    tag = "banner_description_input"
                )
            }

            CustomTextButton(
                text = "Confirm",
                onClick = { onSave(imageUrl, description) },
                isDisabled = isDisabled,
                tag = "confirm_add_banner_button"
            )
        }
    }
}
