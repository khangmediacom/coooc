import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports = """package com.example

import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator
"""

content = content.replace("package com.example", imports)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Fixed imports")
