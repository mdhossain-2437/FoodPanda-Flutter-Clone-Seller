package com.example.foodpanda_seller.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_seller.R
import com.example.foodpanda_seller.data.model.SellerSession
import com.example.foodpanda_seller.ui.components.CustomTextButton
import com.example.foodpanda_seller.ui.components.CustomTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    session: SellerSession,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToNoApprove: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1200)
        if (!session.isSignedIn) {
            onNavigateToAuth()
        } else if (session.isApproved) {
            onNavigateToHome()
        } else {
            onNavigateToNoApprove()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "FoodPanda Seller Splash",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(24.dp)
        )
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 3.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp)
                .size(32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("authentication_screen"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foodpanda_logo),
                    contentDescription = "FoodPanda Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(200.dp)
                )
            }

            Text(
                text = "Elevated your business with us, FoodPanda.",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "Welcome to FoodPanda!",
                color = Color(0xFF9E9E9E),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextButton(
                text = "I'm new here",
                onClick = onNavigateToRegister,
                tag = "auth_register_button"
            )
            CustomTextButton(
                text = "Sign In",
                onClick = onNavigateToLogin,
                isOutlined = true,
                tag = "auth_login_button"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("hovvathana1@gmail.com") }
    var password by remember { mutableStateOf("Panda@2026") }
    var errorEmail by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun validateAndLogin() {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        errorEmail = if (email.isNotBlank() && !emailRegex.matches(email.trim())) {
            "Enter a valid email address"
        } else ""
        errorPassword = if (password.length < 6) {
            "Password has to be at least 6 characters long"
        } else ""

        if (errorEmail.isEmpty() && errorPassword.isEmpty()) {
            onLoginSuccess(email.trim())
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("login_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { validateAndLogin() },
                        enabled = email.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier.testTag("login_top_continue")
                    ) {
                        Text(
                            text = "Continue",
                            color = if (email.isBlank() || password.isBlank()) Color.LightGray
                            else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
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
                .padding(16.dp)
                .testTag("login_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_icon),
                    contentDescription = "Login Icon",
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 16.dp)
                        .width(60.dp)
                )
                Text(
                    text = "Log in with your email",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(36.dp))
                CustomTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorEmail = ""
                    },
                    labelText = "Email",
                    errorText = errorEmail,
                    tag = "login_email_input"
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorPassword = ""
                    },
                    labelText = "Password",
                    isPassword = true,
                    errorText = errorPassword,
                    tag = "login_password_input"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "I forgot my password",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                snackbarHostState.showSnackbar("Password reset link sent to ${email.ifBlank { "your email" }}")
                            }
                        }
                        .padding(vertical = 4.dp)
                        .testTag("forgot_password_button")
                )
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextButton(
                text = "Continue",
                onClick = { validateAndLogin() },
                isDisabled = email.isBlank() || password.isBlank(),
                tag = "login_continue_button"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: (String, String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }

    val hasMinLength = password.length >= 6
    val hasUppercase = password.any { it.isUpperCase() }
    val hasLowercase = password.any { it.isLowerCase() }
    val hasNumeric = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    val isPasswordValid = hasMinLength && hasUppercase && hasLowercase && hasNumeric && hasSpecial

    fun validateAndRegister() {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        errorEmail = if (email.isNotBlank() && !emailRegex.matches(email.trim())) {
            "Enter a valid email address"
        } else ""
        errorPassword = if (!isPasswordValid) {
            "Please complete the requirement."
        } else ""

        if (errorEmail.isEmpty() && errorPassword.isEmpty()) {
            onRegisterSuccess("${firstName.trim()} ${lastName.trim()}", email.trim())
        }
    }

    val isDisabled = firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("register_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { validateAndRegister() },
                        enabled = !isDisabled,
                        modifier = Modifier.testTag("register_top_continue")
                    ) {
                        Text(
                            text = "Continue",
                            color = if (isDisabled) Color.LightGray else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
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
                .padding(16.dp)
                .testTag("register_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 16.dp)
                        .width(60.dp)
                )
                Text(
                    text = "Sign up with your email",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        labelText = "First name",
                        modifier = Modifier.weight(1f),
                        tag = "register_first_name_input"
                    )
                    CustomTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        labelText = "Last name",
                        modifier = Modifier.weight(1f),
                        tag = "register_last_name_input"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorEmail = ""
                    },
                    labelText = "Email",
                    errorText = errorEmail,
                    tag = "register_email_input"
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorPassword = ""
                    },
                    labelText = "Password",
                    isPassword = true,
                    errorText = errorPassword,
                    tag = "register_password_input"
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Password Validator (replicates FlutterPwValidator)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAFA), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PasswordRuleRow("At least 6 characters", hasMinLength)
                    PasswordRuleRow("At least 1 uppercase letter", hasUppercase)
                    PasswordRuleRow("At least 1 lowercase letter", hasLowercase)
                    PasswordRuleRow("At least 1 numeric character", hasNumeric)
                    PasswordRuleRow("At least 1 special character", hasSpecial)
                }
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextButton(
                text = "Continue",
                onClick = { validateAndRegister() },
                isDisabled = isDisabled,
                tag = "register_continue_button"
            )
        }
    }
}

@Composable
private fun PasswordRuleRow(label: String, met: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (met) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (met) Color(0xFF2ECC71) else Color(0xFF9E9E9E),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (met) Color(0xFF2ECC71) else Color(0xFF757575),
            fontWeight = if (met) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendVerificationEmailScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    val context = LocalContext.current
    var alreadySent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
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
                .padding(16.dp)
                .testTag("verification_email_screen")
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email_icon),
                    contentDescription = "Email Verification Icon",
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .width(80.dp)
                )
                Text(
                    text = if (!alreadySent) {
                        "Verify your email address to get started"
                    } else {
                        "We've sent a verification link to your email"
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (!alreadySent) {
                        "This helps us mitigate fraud and keep your personal data safe"
                    } else {
                        "Please click the verification link in your inbox or tap Continue below"
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color(0xFF616161)
                )
            }

            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))
            if (alreadySent) {
                CustomTextButton(
                    text = "Check inbox / Continue to Seller Hub",
                    onClick = {
                        val intent = context.packageManager.getLaunchIntentForPackage("com.google.android.gm")
                        if (intent != null) {
                            runCatching { context.startActivity(intent) }
                        }
                        onVerified()
                    },
                    tag = "check_inbox_button"
                )
            }
            CustomTextButton(
                text = if (!alreadySent) "Send verification email" else "Resend verification link",
                onClick = { alreadySent = true },
                isOutlined = alreadySent,
                tag = "send_verification_button"
            )
        }
    }
}
