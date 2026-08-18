import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Remove the misplaced imports
content = content.replace("import coil.imageLoader\nimport coil.request.ImageRequest\nimport androidx.compose.runtime.LaunchedEffect\nimport androidx.compose.material3.CircularProgressIndicator\n\n@Composable\nfun MainContent(", "@Composable\nfun MainContent(")

# 2. Add imports at the top
imports = """import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator
import com.example.MainActivity"""

content = content.replace("import com.example.MainActivity", imports)

# 3. Fix the withContext
old_code = """                kotlinx.coroutines.Dispatchers.IO.invoke {
                    val imageLoader = context.imageLoader
                    val imagesToLoad = listOf(
                        com.example.R.drawable.mascot1,
                        com.example.R.drawable.mascot,
                        com.example.R.drawable.angkor_bg,
                        com.example.R.drawable.logo_icon
                    )
                    for (img in imagesToLoad) {
                        val request = coil.request.ImageRequest.Builder(context)
                            .data(img)
                            .build()
                        imageLoader.execute(request)
                    }
                }"""

new_code = """                withContext(Dispatchers.IO) {
                    val imageLoader = context.imageLoader
                    val imagesToLoad = listOf(
                        com.example.R.drawable.mascot1,
                        com.example.R.drawable.mascot,
                        com.example.R.drawable.angkor_bg,
                        com.example.R.drawable.logo_icon
                    )
                    for (img in imagesToLoad) {
                        val request = coil.request.ImageRequest.Builder(context)
                            .data(img)
                            .build()
                        imageLoader.execute(request)
                    }
                }"""
content = content.replace(old_code, new_code)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Patched MainActivity Loading Fix")
