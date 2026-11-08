package com.example.foodpanda_seller.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodpanda_seller.R
import com.example.foodpanda_seller.data.model.AddressEntity
import com.example.foodpanda_seller.data.model.PlaceSuggestion
import com.example.foodpanda_seller.data.model.ShopProfile
import com.example.foodpanda_seller.ui.components.CustomTextButton
import com.example.foodpanda_seller.ui.components.CustomTextField
import com.example.foodpanda_seller.ui.components.DottedBorderBox
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterShopScreen(
    shopProfile: ShopProfile,
    onBack: () -> Unit,
    onChooseLocation: () -> Unit,
    onSaveShop: (String, String, String, String, String, String, String, String, Double, Double) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    var shopName by remember(shopProfile.shopName) { mutableStateOf(shopProfile.shopName) }
    var shopDescription by remember(shopProfile.shopDescription) { mutableStateOf(shopProfile.shopDescription) }
    var imageUrl by remember(shopProfile.shopImage) { mutableStateOf(shopProfile.shopImage) }

    val sampleShopImages = remember {
        listOf(
            "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
            "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
            "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=800&q=80"
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUrl = uri.toString()
        } else if (imageUrl.isBlank()) {
            imageUrl = sampleShopImages.random()
        }
    }

    val isDisabled = shopName.isBlank() || shopDescription.isBlank() || imageUrl.isBlank()

    fun save() {
        if (isDisabled) return
        onSaveShop(
            shopName,
            shopDescription,
            imageUrl,
            shopProfile.houseNumber,
            shopProfile.street,
            shopProfile.area,
            shopProfile.province,
            shopProfile.floor,
            shopProfile.latitude,
            shopProfile.longitude
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Register Shop",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("register_shop_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { save() },
                        enabled = !isDisabled,
                        modifier = Modifier.testTag("register_shop_top_check")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save Shop",
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
                .verticalScroll(rememberScrollState())
                .testTag("register_shop_screen"),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Register shop",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF757575)
            )
            Text(
                text = "Fill in the informations",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = shopName,
                onValueChange = { shopName = it },
                labelText = "Shop Name",
                tag = "shop_name_input"
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = shopDescription,
                onValueChange = { if (it.length <= 500) shopDescription = it },
                label = { Text("Description") },
                minLines = 3,
                maxLines = 4,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primary,
                    unfocusedBorderColor = Color(0xFF9E9E9E)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shop_description_input")
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Shop location",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            DottedBorderBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChooseLocation)
                    .padding(10.dp)
                    .testTag("choose_shop_location_box")
            ) {
                if (shopProfile.street.isBlank()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.foodpanda_location),
                            contentDescription = "Choose location",
                            modifier = Modifier.width(90.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Choose location", fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        InteractiveMapPreview(
                            latitude = shopProfile.latitude,
                            longitude = shopProfile.longitude,
                            label = "${shopProfile.houseNumber} ${shopProfile.street}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${shopProfile.houseNumber} ${shopProfile.street}".trim(),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${shopProfile.area}, ${shopProfile.province}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF616161)
                                )
                            }
                            Text(
                                text = "Change",
                                color = primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Shop background image",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (imageUrl.isBlank()) {
                DottedBorderBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
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
                                text = "Sample Photo",
                                onClick = { imageUrl = sampleShopImages.random() },
                                modifier = Modifier.weight(1f)
                            )
                            CustomTextButton(
                                text = "Upload a photo",
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.weight(1f)
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
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Shop Image",
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.panda_eating),
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { imageUrl = "" },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(30.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear image",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            CustomTextButton(
                text = "Confirm Shop Details",
                onClick = { save() },
                isDisabled = isDisabled,
                tag = "confirm_register_shop_button"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAddressManualScreen(
    initialShopProfile: ShopProfile,
    onSearchPlaces: (String) -> List<PlaceSuggestion>,
    onBack: () -> Unit,
    onConfirmLocation: (PlaceSuggestion) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    var searchText by remember { mutableStateOf("") }
    val suggestions = remember(searchText) { onSearchPlaces(searchText) }
    var selectedPlace by remember {
        mutableStateOf(
            PlaceSuggestion(
                placeId = "current",
                mainText = "${initialShopProfile.houseNumber} ${initialShopProfile.street}".trim(),
                secondaryText = "${initialShopProfile.area}, ${initialShopProfile.province}",
                latitude = initialShopProfile.latitude,
                longitude = initialShopProfile.longitude,
                houseNumber = initialShopProfile.houseNumber,
                street = initialShopProfile.street,
                area = initialShopProfile.area,
                province = initialShopProfile.province
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Set delivery address", fontSize = 13.sp) },
                        singleLine = true,
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.HighlightOff,
                                            contentDescription = "Clear search",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val first = suggestions.firstOrNull()
                                        if (first != null) {
                                            selectedPlace = first
                                            searchText = "${first.mainText}, ${first.province}"
                                        }
                                    },
                                    modifier = Modifier.testTag("gps_locate_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GpsFixed,
                                        contentDescription = "Use current GPS location",
                                        tint = primary
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .testTag("search_address_input")
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("search_address_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("search_address_manual_screen")
        ) {
            InteractiveMapPreview(
                latitude = selectedPlace.latitude,
                longitude = selectedPlace.longitude,
                label = "${selectedPlace.mainText}, ${selectedPlace.province}",
                modifier = Modifier.fillMaxSize()
            )

            // Suggestions overlay card
            Surface(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    suggestions.take(4).forEach { suggestion ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPlace = suggestion
                                    searchText = "${suggestion.mainText}, ${suggestion.province}"
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (selectedPlace.placeId == suggestion.placeId) primary else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = suggestion.mainText,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = suggestion.secondaryText,
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                CustomTextButton(
                    text = "Confirm",
                    onClick = { onConfirmLocation(selectedPlace) },
                    tag = "confirm_manual_address_button"
                )
            }
        }
    }
}

@Composable
fun AddressScreen(
    shopProfile: ShopProfile,
    addresses: List<AddressEntity>,
    onBack: () -> Unit,
    onOpenManualSearch: () -> Unit,
    onSaveAddress: (String?, String, String, String, String, String, Double, Double) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val currentAddress = addresses.firstOrNull()

    var houseNumber by remember(currentAddress, shopProfile) {
        mutableStateOf(currentAddress?.houseNumber ?: shopProfile.houseNumber)
    }
    var street by remember(currentAddress, shopProfile) {
        mutableStateOf(currentAddress?.street ?: shopProfile.street)
    }
    var area by remember(currentAddress, shopProfile) {
        mutableStateOf(currentAddress?.area ?: shopProfile.area)
    }
    var floor by remember(currentAddress, shopProfile) {
        mutableStateOf(currentAddress?.floor ?: shopProfile.floor)
    }
    val latitude by remember(currentAddress, shopProfile) {
        mutableDoubleStateOf(currentAddress?.latitude ?: shopProfile.latitude)
    }
    val longitude by remember(currentAddress, shopProfile) {
        mutableDoubleStateOf(currentAddress?.longitude ?: shopProfile.longitude)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("address_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Map Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.42f)
            ) {
                InteractiveMapPreview(
                    latitude = latitude,
                    longitude = longitude,
                    label = "$houseNumber $street, ${shopProfile.province}",
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 12.dp)
                        .size(42.dp)
                        .background(Color.White, CircleShape)
                        .testTag("address_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // Bottom Sheet Card matching original AddressScreen
            Surface(
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.58f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(50))
                            .align(Alignment.CenterHorizontally)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Add a new address",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onOpenManualSearch)
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = primary,
                                    modifier = Modifier.size(30.dp)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "$houseNumber $street".trim().ifBlank { "320 St 320" },
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = shopProfile.province.ifBlank { "Phnom Penh" },
                                        fontSize = 13.sp,
                                        color = Color(0xFF616161)
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Pick on map",
                                tint = primary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CustomTextField(
                                value = houseNumber,
                                onValueChange = { houseNumber = it },
                                labelText = "House Number",
                                modifier = Modifier.weight(1f),
                                tag = "address_house_input"
                            )
                            CustomTextField(
                                value = street,
                                onValueChange = { street = it },
                                labelText = "Street",
                                modifier = Modifier.weight(1f),
                                tag = "address_street_input"
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        CustomTextField(
                            value = area,
                            onValueChange = { area = it },
                            labelText = "Area",
                            tag = "address_area_input"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        CustomTextField(
                            value = floor,
                            onValueChange = { floor = it },
                            labelText = "Floor/Unit #",
                            tag = "address_floor_input"
                        )
                    }

                    HorizontalDivider(color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextButton(
                        text = "Continue",
                        onClick = {
                            onSaveAddress(
                                currentAddress?.id,
                                houseNumber,
                                street,
                                area,
                                shopProfile.province,
                                floor,
                                latitude,
                                longitude
                            )
                        },
                        tag = "address_continue_button"
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveMapPreview(
    latitude: Double,
    longitude: Double,
    label: String,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier.background(Color(0xFFEBF2EA)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // River / Mekong stylized channel
            val riverPath = Path().apply {
                moveTo(w * 0.78f, 0f)
                cubicTo(w * 0.72f, h * 0.35f, w * 0.85f, h * 0.65f, w * 0.76f, h)
                lineTo(w, h)
                lineTo(w, 0f)
                close()
            }
            drawPath(riverPath, color = Color(0xFFB3E5FC))

            // Parks
            drawCircle(
                color = Color(0xFFC8E6C9),
                radius = w * 0.16f,
                center = Offset(w * 0.25f, h * 0.3f)
            )

            // City street grid
            val gridColor = Color.White.copy(alpha = 0.85f)
            for (i in 1..5) {
                val y = h * (i / 6f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = if (i == 3) 10f else 5f
                )
            }
            for (j in 1..5) {
                val x = w * (j / 6f)
                drawLine(
                    color = gridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = if (j == 3) 10f else 5f
                )
            }

            // Main Boulevard highlight
            drawLine(
                color = Color(0xFFFFE082),
                start = Offset(0f, h * 0.5f),
                end = Offset(w, h * 0.5f),
                strokeWidth = 8f
            )

            // Accuracy pulse circle around center pin
            drawCircle(
                color = primary.copy(alpha = 0.15f),
                radius = 36.dp.toPx(),
                center = Offset(w / 2f, h / 2f)
            )
            drawCircle(
                color = primary.copy(alpha = 0.35f),
                radius = 36.dp.toPx(),
                center = Offset(w / 2f, h / 2f),
                style = Stroke(width = 2f)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = BorderStroke(1.dp, primary.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "$label (${String.format(Locale.US, "%.4f", latitude)}, ${String.format(Locale.US, "%.4f", longitude)})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Pin",
                tint = primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
