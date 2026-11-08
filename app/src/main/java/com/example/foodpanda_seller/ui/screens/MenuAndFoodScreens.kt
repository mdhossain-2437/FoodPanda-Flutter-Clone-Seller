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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.foodpanda_seller.data.model.CategoryEntity
import com.example.foodpanda_seller.data.model.ChoiceItem
import com.example.foodpanda_seller.data.model.CustomizationEntity
import com.example.foodpanda_seller.data.model.FoodEntity
import com.example.foodpanda_seller.ui.components.CategoryCard
import com.example.foodpanda_seller.ui.components.CustomTextButton
import com.example.foodpanda_seller.ui.components.CustomTextField
import com.example.foodpanda_seller.ui.components.CustomizePreview
import com.example.foodpanda_seller.ui.components.DottedBorderBox
import com.example.foodpanda_seller.ui.components.FoodCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    categories: List<CategoryEntity>,
    onBack: () -> Unit,
    onAddCategory: () -> Unit,
    onEditCategory: (CategoryEntity) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onPublishedChange: (String, Boolean) -> Unit,
    onSelectCategory: (CategoryEntity) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("menu_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onAddCategory,
                        modifier = Modifier.testTag("menu_top_add_category")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add category",
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
                .testTag("menu_screen"),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "All Menus",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF757575)
            )
            Text(
                text = "All Categories",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            categories.forEach { category ->
                CategoryCard(
                    title = category.title,
                    subtitle = category.subtitle,
                    id = category.id,
                    isPublished = category.isPublished,
                    onPublishedChange = { onPublishedChange(category.id, it) },
                    onTap = { onSelectCategory(category) },
                    onEditTap = { onEditCategory(category) },
                    onDeleteTap = { onDeleteCategory(category.id) },
                    modifier = Modifier.padding(bottom = 18.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(15.dp),
                color = Color(0xFFE0E0E0),
                border = BorderStroke(1.dp, primary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable(onClick = onAddCategory)
                    .testTag("menu_bottom_add_category")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new category",
                        tint = primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    initialId: String,
    initialTitle: String,
    initialSubtitle: String,
    onBack: () -> Unit,
    onSave: (String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var subtitle by remember { mutableStateOf(initialSubtitle) }
    val primary = MaterialTheme.colorScheme.primary
    val isDisabled = title.isBlank() || subtitle.isBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialId.isBlank()) "Add Category" else "Edit Category",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("add_category_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!isDisabled) {
                                onSave(title, subtitle, initialId.ifBlank { null })
                            }
                        },
                        enabled = !isDisabled,
                        modifier = Modifier.testTag("add_category_top_confirm")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Save Category",
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
                .testTag("add_category_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (initialId.isBlank()) "Add New Category" else "Update Category",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomTextField(
                    value = title,
                    onValueChange = { title = it },
                    labelText = "Title",
                    tag = "category_title_input"
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    labelText = "Subtitle",
                    tag = "category_subtitle_input"
                )
                Spacer(modifier = Modifier.height(36.dp))
                Text(
                    text = "Preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategoryCard(
                    title = title,
                    subtitle = subtitle,
                    onTap = {}
                )
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextButton(
                text = "Confirm",
                onClick = { onSave(title, subtitle, initialId.ifBlank { null }) },
                isDisabled = isDisabled,
                tag = "confirm_add_category_button"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(
    categoryTitle: String,
    foods: List<FoodEntity>,
    customizations: List<CustomizationEntity>,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    onEditFood: (FoodEntity) -> Unit,
    onDeleteFood: (String) -> Unit,
    onPublishedChange: (String, Boolean) -> Unit,
    onAddCustomize: (FoodEntity) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Foods",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("food_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onAddFood,
                        modifier = Modifier.testTag("food_top_add_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add food",
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
                .testTag("food_screen"),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = categoryTitle.ifBlank { "All Breakfast" },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF757575)
            )
            Text(
                text = "All Foods",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (foods.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No food items in this category yet.",
                            color = Color(0xFF757575),
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CustomTextButton(
                            text = "Add First Food Item",
                            onClick = onAddFood,
                            tag = "empty_add_food_button"
                        )
                    }
                }
            } else {
                foods.forEachIndexed { index, food ->
                    val custCount = customizations.count { it.foodId == food.id }
                    FoodCard(
                        title = food.name,
                        subtitle = food.description,
                        price = food.price,
                        comparePrice = if (food.comparePrice == 0.0) null else food.comparePrice,
                        image = food.image,
                        id = food.id,
                        isPublished = food.isPublished,
                        customizationCount = custCount,
                        onPublishedChange = { onPublishedChange(food.id, it) },
                        onTap = { onEditFood(food) },
                        onEditTap = { onEditFood(food) },
                        onDeleteTap = { onDeleteFood(food.id) },
                        onAddCustomizeTap = { onAddCustomize(food) }
                    )
                    if (index != foods.lastIndex) {
                        HorizontalDivider(
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    existingFood: FoodEntity?,
    onBack: () -> Unit,
    onSave: (String, String, Double, Double, String) -> Unit
) {
    var name by remember { mutableStateOf(existingFood?.name ?: "") }
    var description by remember { mutableStateOf(existingFood?.description ?: "") }
    var priceStr by remember {
        mutableStateOf(existingFood?.price?.toString() ?: "")
    }
    var comparePriceStr by remember {
        mutableStateOf(
            if (existingFood != null && existingFood.comparePrice > 0.0)
                existingFood.comparePrice.toString()
            else ""
        )
    }
    var imageUrl by remember { mutableStateOf(existingFood?.image ?: "") }

    LaunchedEffect(existingFood) {
        if (existingFood != null) {
            name = existingFood.name
            description = existingFood.description
            priceStr = existingFood.price.toString()
            comparePriceStr = if (existingFood.comparePrice > 0.0) existingFood.comparePrice.toString() else ""
            imageUrl = existingFood.image
        }
    }

    val sampleFoodImages = remember {
        listOf(
            "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=600&q=80",
            "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
            "https://images.unsplash.com/photo-1558857563-b371033873b8?w=600&q=80",
            "https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=600&q=80"
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUrl = uri.toString()
        } else if (imageUrl.isBlank()) {
            imageUrl = sampleFoodImages.random()
        }
    }

    val priceVal = priceStr.toDoubleOrNull() ?: 0.0
    val comparePriceVal = comparePriceStr.toDoubleOrNull() ?: 0.0
    val isDisabled = name.isBlank() || description.isBlank() || priceVal <= 0.0 || imageUrl.isBlank()
    val primary = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existingFood == null) "Add Food" else "Edit Food",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("add_food_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!isDisabled) {
                                onSave(name, description, priceVal, comparePriceVal, imageUrl)
                            }
                        },
                        enabled = !isDisabled,
                        modifier = Modifier.testTag("add_food_top_confirm")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Save Food",
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
                .testTag("add_food_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (existingFood == null) "Add New Food" else "Update Food",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    labelText = "Name",
                    tag = "food_name_input"
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    labelText = "Description",
                    tag = "food_description_input"
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Price is in dollar USD currency.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        labelText = "Price",
                        isNumPad = true,
                        modifier = Modifier.weight(1f),
                        tag = "food_price_input"
                    )
                    CustomTextField(
                        value = comparePriceStr,
                        onValueChange = { comparePriceStr = it },
                        labelText = "Old Price",
                        isNumPad = true,
                        modifier = Modifier.weight(1f),
                        tag = "food_old_price_input"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Food image",
                    fontSize = 15.sp,
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
                                    text = "Use Sample Photo",
                                    onClick = { imageUrl = sampleFoodImages.random() },
                                    modifier = Modifier.weight(1f),
                                    tag = "food_sample_photo_button"
                                )
                                CustomTextButton(
                                    text = "Upload a photo",
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    tag = "food_upload_photo_button"
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
                                contentDescription = "Selected Food Image",
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.panda_eating),
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { imageUrl = "" },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(32.dp)
                                    .background(Color.White, CircleShape)
                                    .testTag("food_clear_image_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove image",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Preview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FoodCard(
                    title = name,
                    subtitle = description,
                    price = priceVal,
                    comparePrice = if (comparePriceVal == 0.0) null else comparePriceVal,
                    image = imageUrl,
                    onTap = {}
                )
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextButton(
                text = "Confirm",
                onClick = { onSave(name, description, priceVal, comparePriceVal, imageUrl) },
                isDisabled = isDisabled,
                tag = "confirm_add_food_button"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomizationScreen(
    onBack: () -> Unit,
    onSave: (String, Boolean, Boolean, Boolean, Int, List<ChoiceItem>) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    var title by remember { mutableStateOf("") }
    var isRequired by remember { mutableStateOf(true) }
    var isRadio by remember { mutableStateOf(true) }
    var isVariation by remember { mutableStateOf(false) }
    var selectAmount by remember { mutableIntStateOf(1) }

    val choiceTexts = remember { mutableStateListOf("") }
    val priceStrings = remember { mutableStateListOf("") }

    val isDisabled = title.isBlank() || choiceTexts.isEmpty() || choiceTexts.any { it.isBlank() }

    fun submit() {
        if (isDisabled) return
        val choices = choiceTexts.mapIndexed { idx, c ->
            val p = priceStrings.getOrNull(idx)?.toDoubleOrNull() ?: 0.0
            ChoiceItem(c.trim(), p)
        }
        onSave(title, isRequired, isRadio, isVariation, if (isRadio) 1 else selectAmount, choices)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Customize",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("customize_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { submit() },
                        enabled = !isDisabled,
                        modifier = Modifier.testTag("customize_top_check")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save Customization",
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
                .testTag("add_customization_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Add Customize",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Choice of", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = isVariation,
                        onCheckedChange = { isVariation = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = primary)
                    )
                    Text("Variation", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                }
                Text(
                    text = "* \"Choice of\" will use the price set before as the starting price and each choice price will be treated as additional.",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "* \"Variation\" will use the price set for each choice as the starting price.",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = title,
                        onValueChange = { title = it },
                        labelText = "Title",
                        modifier = Modifier.weight(1f),
                        tag = "customize_title_input"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Switch(
                            checked = isRequired,
                            onCheckedChange = { isRequired = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = primary)
                        )
                        Text("Required", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add choices", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    IconButton(
                        onClick = {
                            choiceTexts.add("")
                            priceStrings.add("")
                        },
                        modifier = Modifier.testTag("add_choice_row_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add choice",
                            tint = primary
                        )
                    }
                }

                choiceTexts.forEachIndexed { index, choiceVal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomTextField(
                            value = choiceVal,
                            onValueChange = { choiceTexts[index] = it },
                            labelText = "Choice ${index + 1}",
                            modifier = Modifier.weight(1.4f),
                            tag = "choice_name_input_$index"
                        )
                        CustomTextField(
                            value = priceStrings.getOrElse(index) { "" },
                            onValueChange = { priceStrings[index] = it },
                            labelText = "Price",
                            isNumPad = true,
                            modifier = Modifier.weight(1f),
                            tag = "choice_price_input_$index"
                        )
                        if (choiceTexts.size > 1) {
                            IconButton(
                                onClick = {
                                    choiceTexts.removeAt(index)
                                    priceStrings.removeAt(index)
                                    if (selectAmount > choiceTexts.size) {
                                        selectAmount = choiceTexts.size
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RemoveCircleOutline,
                                    contentDescription = "Remove choice",
                                    tint = primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Checkbox", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isRadio,
                            onCheckedChange = { isRadio = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = primary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Radio", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    if (isRadio) {
                        Text("Can select 1", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Can select ", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            IconButton(
                                onClick = { if (selectAmount > 1) selectAmount-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text("$selectAmount", fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = { if (selectAmount < choiceTexts.size) selectAmount++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Preview", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                CustomizePreview(
                    title = title,
                    isRequired = isRequired,
                    isRadio = isRadio,
                    isVariation = isVariation,
                    selectAmount = if (isRadio) 1 else selectAmount,
                    listChoiceText = choiceTexts.toList(),
                    listPriceText = priceStrings.map { it.toDoubleOrNull() ?: 0.0 }
                )
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextButton(
                text = "Add more",
                onClick = { submit() },
                isDisabled = isDisabled,
                tag = "confirm_add_customize_button"
            )
        }
    }
}
