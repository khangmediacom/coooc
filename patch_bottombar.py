import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

bottom_bar_replacement = """fun ElegantBottomBar(
    currentScreen: CurrentScreen,
    language: AppLanguage,
    onNavigate: (CurrentScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F1E2)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = AppStrings.get(language, "home"),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                selected = currentScreen is CurrentScreen.Home,
                onClick = { onNavigate(CurrentScreen.Home) },
                tag = "nav_home"
            )
            BottomNavItem(
                label = AppStrings.get(language, "play"),
                selectedIcon = Icons.Filled.SportsEsports,
                unselectedIcon = Icons.Outlined.SportsEsports,                
                selected = currentScreen is CurrentScreen.OnlineLobby,
                onClick = { onNavigate(CurrentScreen.OnlineLobby) },
                tag = "nav_play"
            )
            BottomNavItem(
                label = AppStrings.get(language, "learn"),
                selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
                unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
                selected = currentScreen is CurrentScreen.Tactics,
                onClick = { onNavigate(CurrentScreen.Tactics) },
                tag = "nav_learn"
            )
            BottomNavItem(
                label = AppStrings.get(language, "ranks"),
                selectedIcon = Icons.Filled.EmojiEvents,
                unselectedIcon = Icons.Outlined.EmojiEvents,
                selected = currentScreen is CurrentScreen.Leaderboard,
                onClick = { onNavigate(CurrentScreen.Leaderboard) },
                tag = "nav_ranks"
            )
            BottomNavItem(
                label = AppStrings.get(language, "settings"),
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                selected = currentScreen is CurrentScreen.Customization,
                onClick = { onNavigate(CurrentScreen.Customization) },
                tag = "nav_settings"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    val navModifier = if (selected) {
        Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFEEDDBC))
    } else {
        Modifier.clip(RoundedCornerShape(24.dp))
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = navModifier
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = if (selected) Color(0xFF8B5E34) else Color(0xFF78716C),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color(0xFF8B5E34) else Color(0xFF78716C)
        )
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .background(Color(0xFF8B5E34))
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}"""
content = re.sub(
    r'fun ElegantBottomBar\(.*?\}\n\n@Composable\nfun MainContent',
    bottom_bar_replacement + '\n\n@Composable\nfun MainContent',
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
