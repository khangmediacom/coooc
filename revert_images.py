import re

files_to_check = [
    'app/src/main/java/com/example/ui/screens/WelcomeScreen.kt',
    'app/src/main/java/com/example/ui/components/AppShellTopBar.kt',
    'app/src/main/java/com/example/ui/components/AngkorWarmBackground.kt'
]

for filepath in files_to_check:
    with open(filepath, 'r') as f:
        content = f.read()

    # Revert to file:///android_asset for AsyncImage
    content = content.replace('R.drawable.angkor_hero_img', '"file:///android_asset/images/angkor-hero.jpg"')
    content = content.replace('R.drawable.hanuman_mascot_hero_img', '"file:///android_asset/images/hanuman-mascot-hero.png"')
    content = content.replace('R.drawable.hanuman_mascot_img', '"file:///android_asset/images/hanuman-mascot.png"')
    
    # Check if there are any remaining R.drawable that I changed recently
    content = content.replace('com.example.R.drawable.angkor_hero_img', '"file:///android_asset/images/angkor-hero.jpg"')

    with open(filepath, 'w') as f:
        f.write(content)

