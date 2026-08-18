import os
import re

def process_file(file_path):
    with open(file_path, "r") as f:
        content = f.read()
    
    original = content
    
    # Matches SubcomposeAsyncImage with model pointing to a local drawable.
    # It dynamically captures the model, contentDescription, contentScale, and modifier regardless of order, 
    # but the easiest way is to just replace SubcomposeAsyncImage( ... ) where model = ...drawable...
    
    # Alternatively, just use regex to find all SubcomposeAsyncImage block
    
    # HomeScreen 1
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = com\.example\.R\.drawable\.mascot,\s*loading = \{ Box.*?\},\s*contentDescription = (.*?),\s*contentScale = (.*?),\s*modifier = (.*?)\s*\)',
        r'Image(\n                                painter = painterResource(id = com.example.R.drawable.mascot),\n                                contentDescription = \1,\n                                contentScale = \2,\n                                modifier = \3\n                            )',
        content, flags=re.DOTALL
    )

    # GameScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = com\.example\.R\.drawable\.mascot,\s*loading = \{ Box.*?\},\s*contentDescription = (.*?),\s*contentScale = (.*?),\s*modifier = (.*?)\s*\)',
        r'Image(\n                painter = painterResource(id = com.example.R.drawable.mascot),\n                contentDescription = \1,\n                contentScale = \2,\n                modifier = \3\n            )',
        content, flags=re.DOTALL
    )

    # AppShellTopBar
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = R\.drawable\.mascot,\s*loading = \{ Box.*?\},\s*contentDescription = (.*?),\s*modifier = (.*?)\s*\)',
        r'Image(\n                painter = painterResource(id = R.drawable.mascot),\n                contentDescription = \1,\n                modifier = \2\n            )',
        content, flags=re.DOTALL
    )

    if content != original:
        if "import androidx.compose.ui.res.painterResource" not in content:
            content = "import androidx.compose.ui.res.painterResource\nimport androidx.compose.foundation.Image\n" + content
        with open(file_path, "w") as f:
            f.write(content)

for root, _, files in os.walk("app/src/main/java/com/example/ui"):
    for file in files:
        if file.endswith(".kt"):
            process_file(os.path.join(root, file))

print("Patched all!")
