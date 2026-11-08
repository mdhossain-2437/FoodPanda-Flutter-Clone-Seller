package com.example.foodpanda_seller.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodpanda_seller.R
import com.example.foodpanda_seller.data.model.OrderEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDisabled: Boolean = false,
    isOutlined: Boolean = false,
    tag: String = "button_${text.lowercase().replace(" ", "_")}"
) {
    val primary = MaterialTheme.colorScheme.primary
    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            enabled = !isDisabled,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.5.dp, if (isDisabled) Color.LightGray else primary),
            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .minimumInteractiveComponentSize()
                .testTag(tag)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (isDisabled) Color.Gray else primary
            )
        }
    } else {
        Button(
            onClick = onClick,
            enabled = !isDisabled,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary,
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color(0xFF9E9E9E)
            ),
            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .minimumInteractiveComponentSize()
                .testTag(tag)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    isNumPad: Boolean = false,
    isPassword: Boolean = false,
    errorText: String = "",
    tag: String = "input_${labelText.lowercase().replace(" ", "_")}"
) {
    var obscure by remember { mutableStateOf(isPassword) }
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(labelText) },
            singleLine = true,
            isError = errorText.isNotEmpty(),
            visualTransformation = if (isPassword && obscure) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = when {
                    isNumPad -> KeyboardType.Decimal
                    isPassword -> KeyboardType.Password
                    else -> KeyboardType.Text
                }
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = { obscure = !obscure },
                        modifier = Modifier.testTag("${tag}_toggle_visibility")
                    ) {
                        Icon(
                            imageVector = if (obscure) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (obscure) "Show password" else "Hide password"
                        )
                    }
                }
            } else null,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag)
        )
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun DottedBorderBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    color: Color = Color(0xFF9E9E9E),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = color,
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 14f), 0f)
                ),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
        content()
    }
}

@Composable
fun TextTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color = Color.Transparent,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ConfirmAlertDialog(
    title: String,
    subtitle: String,
    confirmText: String,
    cancelText: String = "Cancel",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF555555)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.testTag("dialog_confirm_button")
            ) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_button")
            ) {
                Text(
                    text = cancelText,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Composable
fun CategoryCard(
    title: String,
    subtitle: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    id: String? = null,
    isPublished: Boolean? = null,
    onPublishedChange: ((Boolean) -> Unit)? = null,
    onEditTap: (() -> Unit)? = null,
    onDeleteTap: (() -> Unit)? = null
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog && onDeleteTap != null) {
        ConfirmAlertDialog(
            title = "Delete Category",
            subtitle = "Are you sure you want to delete \"$title\" and its foods?",
            confirmText = "Delete",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteTap()
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.primary,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
            .testTag("category_card_${id ?: "preview"}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isPublished != null) 128.dp else 104.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 110.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = title.ifBlank { "Category Title" },
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle.ifBlank { "Category subtitle description" },
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (isPublished != null && onPublishedChange != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Switch(
                            checked = isPublished,
                            onCheckedChange = onPublishedChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = Color.White,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.White.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.testTag("switch_category_${id ?: "preview"}")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPublished) "Published" else "Hidden",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.panda_eating),
                contentDescription = "Panda mascot",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
                    .height(84.dp)
            )

            if (onEditTap != null || onDeleteTap != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    if (onEditTap != null) {
                        IconButton(
                            onClick = onEditTap,
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                                .testTag("edit_category_${id ?: ""}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit category",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    if (onDeleteTap != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                                .testTag("delete_category_${id ?: ""}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete category",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(
    title: String,
    subtitle: String,
    price: Double,
    image: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    id: String? = null,
    comparePrice: Double? = null,
    isPublished: Boolean? = null,
    customizationCount: Int = 0,
    onPublishedChange: ((Boolean) -> Unit)? = null,
    onAddCustomizeTap: (() -> Unit)? = null,
    onEditTap: (() -> Unit)? = null,
    onDeleteTap: (() -> Unit)? = null
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog && onDeleteTap != null) {
        ConfirmAlertDialog(
            title = "Delete Food",
            subtitle = "Are you sure you want to delete this food item?",
            confirmText = "Delete",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDeleteTap()
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .testTag("food_card_${id ?: "preview"}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title.ifBlank { "Food Name" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (onEditTap != null || onDeleteTap != null) {
                        Row {
                            if (onEditTap != null) {
                                IconButton(
                                    onClick = onEditTap,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("edit_food_${id ?: ""}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit food",
                                        tint = Color(0xFF707070),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            if (onDeleteTap != null) {
                                IconButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("delete_food_${id ?: ""}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete food",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle.ifBlank { "Food description" },
                    fontSize = 13.sp,
                    color = Color(0xFF757575),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "from $ ${String.format(Locale.US, "%.2f", price)}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    if (comparePrice != null && comparePrice > 0.0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$ ${String.format(Locale.US, "%.2f", comparePrice)}",
                            color = Color(0xFF757575),
                            fontSize = 13.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    if (customizationCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextTag(
                            text = "$customizationCount opts",
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            textColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (isPublished != null && onPublishedChange != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Switch(
                            checked = isPublished,
                            onCheckedChange = onPublishedChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("switch_food_${id ?: "preview"}")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Published",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.size(104.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopStart)
                ) {
                    if (image.isNotBlank()) {
                        AsyncImage(
                            model = image,
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.panda_eating),
                            placeholder = painterResource(id = R.drawable.panda_eating),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.panda_eating),
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        )
                    }
                }

                if (onAddCustomizeTap != null) {
                    IconButton(
                        onClick = onAddCustomizeTap,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .testTag("add_customize_button_${id ?: ""}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add customization",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomizePreview(
    title: String,
    isRequired: Boolean,
    isRadio: Boolean,
    isVariation: Boolean,
    selectAmount: Int,
    listChoiceText: List<String>,
    listPriceText: List<Double>
) {
    var selectedRadio by remember { mutableIntStateOf(0) }
    val checkedStates = remember(listChoiceText.size) {
        mutableStateListOf(*Array(listChoiceText.size) { it == 0 })
    }

    val primary = MaterialTheme.colorScheme.primary
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isRequired) primary.copy(alpha = 0.05f) else Color.White,
        border = BorderStroke(1.dp, if (isRequired) Color(0xFFE0E0E0) else Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = if (isRequired) 15.dp else 4.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isVariation) "Variation" else "Choice of $title",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isRequired) {
                    TextTag(
                        text = "Required",
                        backgroundColor = primary,
                        textColor = Color.White
                    )
                } else {
                    TextTag(
                        text = "Optional",
                        backgroundColor = Color(0xFFE0E0E0),
                        textColor = Color(0xFF757575)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isRadio) "Select one" else "Select $selectAmount",
                fontSize = 13.sp,
                color = if (isRequired) primary else Color(0xFF9E9E9E),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            listChoiceText.forEachIndexed { index, choiceName ->
                val priceVal = listPriceText.getOrElse(index) { 0.0 }
                val priceLabel = if (priceVal != 0.0) {
                    if (isVariation) "$ ${String.format(Locale.US, "%.2f", priceVal)}"
                    else "+ $ ${String.format(Locale.US, "%.2f", priceVal)}"
                } else {
                    "Free"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isRadio) {
                            RadioButton(
                                selected = selectedRadio == index,
                                onClick = { selectedRadio = index },
                                colors = RadioButtonDefaults.colors(selectedColor = primary)
                            )
                        } else {
                            val checked = checkedStates.getOrElse(index) { false }
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    if (index < checkedStates.size) {
                                        checkedStates[index] = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = primary)
                            )
                        }
                        Text(
                            text = choiceName.ifBlank { "Option ${index + 1}" },
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = priceLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: OrderEntity,
    isHistoryOrder: Boolean = false,
    onAcceptOrder: () -> Unit = {},
    onRejectOrder: () -> Unit = {},
    onCompleteOrder: () -> Unit = {}
) {
    val context = LocalContext.current
    val primary = MaterialTheme.colorScheme.primary
    var isViewDetail by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    val foodItems = remember(order.foodOrdersJson) { order.getFoodOrders() }
    val totalQuantity = remember(foodItems) { foodItems.sumOf { it.quantity } }
    val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.US) }

    if (showRejectDialog) {
        ConfirmAlertDialog(
            title = "Rejecting order?",
            subtitle = "Are you sure rejecting this order? This will affect your acceptance rate and also result in bad review.",
            confirmText = "Reject",
            onDismiss = { showRejectDialog = false },
            onConfirm = {
                showRejectDialog = false
                onRejectOrder()
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("order_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "Order receipt",
                    tint = primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Order #${order.id}",
                            fontSize = if (isHistoryOrder) 14.sp else 16.sp,
                            color = primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isHistoryOrder) {
                            val statusText = when {
                                order.isCancelled -> "Cancelled"
                                order.isDelivered -> "Complete"
                                else -> "Progress"
                            }
                            TextTag(
                                text = statusText,
                                backgroundColor = if (order.isDelivered) primary else Color.White,
                                textColor = if (order.isDelivered) Color.White else primary,
                                borderColor = primary
                            )
                        }
                    }
                    Text(
                        text = timeFormat.format(Date(order.time)),
                        fontSize = 12.sp,
                        color = Color(0xFF757575),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Customer: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = order.customerName,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (order.customerPhone.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+${order.customerPhone}"))
                            runCatching { context.startActivity(intent) }
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("call_customer_${order.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call customer",
                        tint = primary
                    )
                }
            }
            Text(
                text = "${order.houseNumber} ${order.street}, ${order.province}",
                fontSize = 13.sp,
                color = Color(0xFF616161)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text("Cutlery: ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(if (order.isCutlery) "Yes" else "No", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("Payment Method: ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(if (order.isPaid) "Credit card" else "Cash", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Amount: ", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("$ ${String.format(Locale.US, "%.2f", order.totalPrice)}", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (order.isPaid) "(Already paid)" else "(Not yet paid)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF757575)
                )
            }

            // Expandable View Detail
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isViewDetail = !isViewDetail }
                    .padding(top = 14.dp, bottom = 8.dp)
                    .testTag("toggle_order_detail_${order.id}")
            ) {
                Text(
                    text = "View details",
                    color = primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "($totalQuantity items)",
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isViewDetail) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand details",
                    tint = primary
                )
            }

            AnimatedVisibility(visible = isViewDetail) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    foodItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${item.quantity}x",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.foodName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                if (item.customize.isNotBlank()) {
                                    Text(
                                        text = item.customize,
                                        fontSize = 12.sp,
                                        color = Color(0xFF616161)
                                    )
                                }
                            }
                            Text(
                                text = "$ ${String.format(Locale.US, "%.2f", item.foodPrice * item.quantity)}",
                                fontSize = 14.sp
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("$ ${String.format(Locale.US, "%.2f", order.totalPrice)}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery fee", fontSize = 14.sp)
                        Text("$ ${String.format(Locale.US, "%.2f", order.deliveryPrice)}", fontSize = 14.sp)
                    }
                    if (order.discountPrice > 0.0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount", fontSize = 14.sp)
                            Text("- $ ${String.format(Locale.US, "%.2f", order.discountPrice)}", fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val finalTotal = order.totalPrice + order.deliveryPrice - order.discountPrice
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total (incl. VAT)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("$ ${String.format(Locale.US, "%.2f", finalTotal)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (order.isPaid) "Paid online (Card)" else "Cash on delivery",
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$ ${String.format(Locale.US, "%.2f", finalTotal)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (!isHistoryOrder) {
                if (!order.isShopAccept) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomTextButton(
                            text = "Reject",
                            onClick = { showRejectDialog = true },
                            isOutlined = true,
                            modifier = Modifier.weight(1f),
                            tag = "reject_order_${order.id}"
                        )
                        CustomTextButton(
                            text = "Accept",
                            onClick = onAcceptOrder,
                            modifier = Modifier.weight(1f),
                            tag = "accept_order_${order.id}"
                        )
                    }
                } else {
                    CustomTextButton(
                        text = "Done (Hand over to Rider)",
                        onClick = onCompleteOrder,
                        tag = "complete_order_${order.id}"
                    )
                }
            }
        }
    }
}
