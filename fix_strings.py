import re
with open("app/src/main/java/com/example/ui/localization/AppStrings.kt", "r") as f:
    content = f.read()

# Add new strings
content = content.replace('"home" to "Home",', '"home" to "Trang chủ",\n        "flip_board" to "Flip Board",\n        "new_game" to "New Game",')
content = content.replace('"home" to "Trang Chủ",', '"home" to "Trang chủ",\n        "flip_board" to "Xoay bàn",\n        "new_game" to "Ván mới",')
content = content.replace('"home" to "ទំព័រដើម",', '"home" to "ទំព័រដើម",\n        "flip_board" to "បង្វិលក្តារ",\n        "new_game" to "ហ្គេមថ្មី",')
content = content.replace('"home" to "Accueil",', '"home" to "Accueil",\n        "flip_board" to "Tourner",\n        "new_game" to "Nouveau",')

# Add get with fallback
content = content.replace(
    'fun get(language: AppLanguage, key: String): String {',
    'fun get(language: AppLanguage, key: String, fallback: String = key): String {'
)
content = content.replace(
    'return strings[language.name]?.get(key) ?: key',
    'return strings[language.name]?.get(key) ?: strings["ENGLISH"]?.get(key) ?: fallback'
)

with open("app/src/main/java/com/example/ui/localization/AppStrings.kt", "w") as f:
    f.write(content)
