import os
import re

def replace_coil(file_path):
    with open(file_path, "r") as f:
        content = f.read()

    # Make sure painterResource is imported
    if "import androidx.compose.ui.res.painterResource" not in content:
        content = content.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.res.painterResource")
    if "import androidx.compose.foundation.Image" not in content:
        content = content.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.foundation.Image")

    # Replace angkor_bg in WelcomeScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = R\.drawable\.angkor_bg,\s*contentDescription = (null|".*?"),\s*contentScale = (.*?),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                painter = painterResource(id = R.drawable.angkor_bg),\n                contentDescription = \1,\n                contentScale = \2,\n                modifier = \3\n            )',
        content, flags=re.DOTALL
    )

    # Replace mascot1 in WelcomeScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = R\.drawable\.mascot1,\s*contentDescription = (null|".*?"),\s*contentScale = (.*?),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                    painter = painterResource(id = R.drawable.mascot1),\n                    contentDescription = \1,\n                    contentScale = \2,\n                    modifier = \3\n                )',
        content, flags=re.DOTALL
    )

    # Replace angkor_bg in AngkorWarmBackground
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = R\.drawable\.angkor_bg,\s*contentDescription = (null|".*?"),\s*contentScale = (.*?),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n            painter = painterResource(id = R.drawable.angkor_bg),\n            contentDescription = \1,\n            contentScale = \2,\n            modifier = \3\n        )',
        content, flags=re.DOTALL
    )

    # Replace mascot in HomeScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = com\.example\.R\.drawable\.mascot,\s*contentDescription = (null|".*?"),\s*contentScale = (.*?),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                                painter = painterResource(id = com.example.R.drawable.mascot),\n                                contentDescription = \1,\n                                contentScale = \2,\n                                modifier = \3\n                            )',
        content, flags=re.DOTALL
    )
    # the second mascot in HomeScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = com\.example\.R\.drawable\.mascot,\s*contentDescription = (null|".*?"),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                painter = painterResource(id = com.example.R.drawable.mascot),\n                contentDescription = \1,\n                modifier = \2\n            )',
        content, flags=re.DOTALL
    )

    # Replace mascot in GameScreen
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = com\.example\.R\.drawable\.mascot,\s*contentDescription = (null|".*?"),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                painter = painterResource(id = com.example.R.drawable.mascot),\n                contentDescription = \1,\n                modifier = \2\n            )',
        content, flags=re.DOTALL
    )

    # Replace mascot in AppShellTopBar
    content = re.sub(
        r'SubcomposeAsyncImage\(\s*model = R\.drawable\.mascot,\s*contentDescription = (null|".*?"),\s*modifier = (.*?),.*?loading = \{.*?\}\s*\)',
        r'Image(\n                painter = painterResource(id = R.drawable.mascot),\n                contentDescription = \1,\n                modifier = \2\n            )',
        content, flags=re.DOTALL
    )

    with open(file_path, "w") as f:
        f.write(content)

for root, _, files in os.walk("app/src/main/java/com/example/ui"):
    for file in files:
        if file.endswith(".kt"):
            replace_coil(os.path.join(root, file))

print("Patched!")
