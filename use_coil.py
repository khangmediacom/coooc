import re
import os

files_to_check = [
    "app/src/main/java/com/example/ui/screens/WelcomeScreen.kt",
    "app/src/main/java/com/example/ui/screens/HomeScreen.kt",
    "app/src/main/java/com/example/ui/screens/GameScreen.kt",
    "app/src/main/java/com/example/ui/components/AppShellTopBar.kt",
    "app/src/main/java/com/example/ui/components/AngkorWarmBackground.kt"
]

def patch_file(filepath):
    if not os.path.exists(filepath):
        return
        
    with open(filepath, "r") as f:
        content = f.read()

    original_content = content
    
    # Imports to add
    imports = """import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
"""
    if "import coil.compose.SubcomposeAsyncImage" not in content:
        content = content.replace("import androidx.compose.runtime.Composable", imports + "import androidx.compose.runtime.Composable")

    # Replace Image(painter = painterResource(id = R.drawable...)) -> SubcomposeAsyncImage(...)
    # Match: androidx.compose.foundation.Image( painter = painterResource(id = R.drawable.angkor_bg), contentDescription = null, contentScale = androidx.compose.ui.layout.ContentScale.Crop, modifier = Modifier.fillMaxSize() )
    # And other variations.

    # 1. WelcomeScreen angkor_bg
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*painterResource\(\s*id\s*=\s*R\.drawable\.angkor_bg\s*\),\s*contentDescription\s*=\s*null,\s*contentScale\s*=\s*androidx\.compose\.ui\.layout\.ContentScale\.Crop,\s*modifier\s*=\s*Modifier\.fillMaxSize\(\)\s*\)',
        r'''SubcomposeAsyncImage(
                model = R.drawable.angkor_bg,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GoldAmber) } }
            )''',
        content
    )
    
    # 2. WelcomeScreen mascot1
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*painterResource\(\s*id\s*=\s*R\.drawable\.mascot1\s*\),\s*contentDescription\s*=\s*"Hanuman Mascot",\s*contentScale\s*=\s*androidx\.compose\.ui\.layout\.ContentScale\.Fit,\s*modifier\s*=\s*Modifier([^)]+)\)',
        r'''SubcomposeAsyncImage(
                    model = R.drawable.mascot1,
                    contentDescription = "Hanuman Mascot",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    modifier = Modifier\1,
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GoldAmber) } }
                )''',
        content
    )

    # 3. HomeScreen mascot
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*androidx\.compose\.ui\.res\.painterResource\(\s*id\s*=\s*com\.example\.R\.drawable\.mascot\s*\)',
        r'''SubcomposeAsyncImage(
                                model = com.example.R.drawable.mascot,
                                loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } }''',
        content
    )

    # 4. GameScreen mascot
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*androidx\.compose\.ui\.res\.painterResource\(\s*id\s*=\s*com\.example\.R\.drawable\.mascot\s*\)',
        r'''SubcomposeAsyncImage(
                    model = com.example.R.drawable.mascot,
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } }''',
        content
    )
    
    # 5. AppShellTopBar mascot
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*painterResource\(\s*id\s*=\s*R\.drawable\.mascot\s*\)',
        r'''SubcomposeAsyncImage(
                    model = R.drawable.mascot,
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } }''',
        content
    )

    # 6. AngkorWarmBackground angkor_bg
    content = re.sub(
        r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*painterResource\(\s*id\s*=\s*R\.drawable\.angkor_bg\s*\),\s*contentDescription\s*=\s*null,\s*contentScale\s*=\s*androidx\.compose\.ui\.layout\.ContentScale\.Crop,\s*alignment\s*=\s*Alignment\.Center,\s*modifier\s*=\s*Modifier\.fillMaxSize\(\)\s*\)',
        r'''SubcomposeAsyncImage(
                model = R.drawable.angkor_bg,
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
                loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator(color = RoyalGoldDark) } }
            )''',
        content
    )

    if content != original_content:
        with open(filepath, "w") as f:
            f.write(content)
        print(f"Patched {filepath}")

for f in files_to_check:
    patch_file(f)

