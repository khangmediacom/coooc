import re

filepath = "app/src/main/java/com/example/ui/components/HanumanCoachComponent.kt"
with open(filepath, "r") as f:
    content = f.read()

imports = """import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
"""
if "import coil.compose.SubcomposeAsyncImage" not in content:
    content = content.replace("import androidx.compose.runtime.Composable", imports + "import androidx.compose.runtime.Composable")

old_image = r'androidx\.compose\.foundation\.Image\(\s*painter\s*=\s*androidx\.compose\.ui\.res\.painterResource\(\s*id\s*=\s*com\.example\.R\.drawable\.mascot\s*\)'

new_image = '''SubcomposeAsyncImage(
            model = com.example.R.drawable.mascot,
            loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } }'''

content = re.sub(old_image, new_image, content)

with open(filepath, "w") as f:
    f.write(content)
print("Patched coach")
