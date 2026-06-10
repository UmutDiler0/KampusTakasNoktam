package com.takasr.kampstakasnoktam.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.takasr.kampstakasnoktam.R

@Composable
fun ProfileScreen(
    onTabSelected: (BottomNavTab) -> Unit,
    onChatClick: () -> Unit,
    onBasketClick: () -> Unit,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    MainTabScaffold(
        selectedTab = BottomNavTab.Profile,
        titleRes = R.string.nav_profile,
        onTabSelected = onTabSelected,
        onChatClick = onChatClick,
        onBasketClick = onBasketClick,
        showTopBar = false,
        modifier = modifier
    ) {
        ProfileContent(settingsViewModel = settingsViewModel)
    }
}

@Composable
fun ProfileContent(
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    // States for toggles
    val isDarkThemePref by settingsViewModel.isDarkTheme.collectAsState()
    val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
    val isDarkTheme = isDarkThemePref ?: isSystemDark

    val selectedLanguageCode by settingsViewModel.languageCode.collectAsState()
    val selectedLanguage = if (selectedLanguageCode == "tr") "Turkish" else "English"
    var showLanguageDropdown by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var isNotificationEnabled by remember { 
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                androidx.core.content.ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isNotificationEnabled = isGranted
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.nav_profile),
                    modifier = Modifier
                        .size(108.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = stringResource(id = R.string.profile_full_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(id = R.string.profile_email),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
        }

        item {
            // Preferences Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_section_preferences),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        // Theme Toggle
                        ProfileMenuSwitchRow(
                            icon = Icons.Default.DarkMode,
                            title = stringResource(id = R.string.menu_theme),
                            isChecked = isDarkTheme,
                            onCheckedChange = { settingsViewModel.setDarkTheme(it) }
                        )

                        // Language Selection
                        Box {
                            ProfileMenuRow(
                                icon = Icons.Default.Language,
                                title = "${stringResource(id = R.string.menu_languages)} ($selectedLanguage)",
                                onClick = { showLanguageDropdown = true }
                            )
                            DropdownMenu(
                                expanded = showLanguageDropdown,
                                onDismissRequest = { showLanguageDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("English") },
                                    onClick = {
                                        settingsViewModel.setLanguage("en")
                                        showLanguageDropdown = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Turkish") },
                                    onClick = {
                                        settingsViewModel.setLanguage("tr")
                                        showLanguageDropdown = false
                                    }
                                )
                            }
                        }

                        // Notifications Toggle
                        ProfileMenuSwitchRow(
                            icon = Icons.Default.Notifications,
                            title = stringResource(id = R.string.menu_notifications),
                            isChecked = isNotificationEnabled,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        isNotificationEnabled = true
                                    }
                                } else {
                                    isNotificationEnabled = false
                                }
                            }
                        )
                    }
                }
            }
        }

        item {
            // Support / Account Actions Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.profile_section_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        ProfileMenuRow(
                            icon = Icons.Default.Person,
                            title = stringResource(id = R.string.menu_account_info),
                            onClick = { }
                        )
                        ProfileMenuRow(
                            icon = Icons.Default.Security,
                            title = stringResource(id = R.string.menu_privacy),
                            onClick = { }
                        )
                        ProfileMenuRow(
                            icon = Icons.Default.HelpOutline,
                            title = stringResource(id = R.string.menu_help),
                            onClick = { }
                        )
                        ProfileMenuRow(
                            icon = Icons.Default.Logout,
                            title = stringResource(id = R.string.menu_sign_out),
                            titleColor = MaterialTheme.colorScheme.error,
                            iconColor = MaterialTheme.colorScheme.error,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = titleColor
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ProfileMenuSwitchRow(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
