with open("app/src/main/java/com/example/ui/localization/AppStrings.kt", "r") as f:
    content = f.read()

content = content.replace("fun get(language: AppLanguage, key: String): String {", "fun get(language: AppLanguage, key: String, fallback: String = key): String {")
content = content.replace("return strings[language.name]?.get(key) ?: key", 'return strings[language.name]?.get(key) ?: strings["ENGLISH"]?.get(key) ?: fallback')

with open("app/src/main/java/com/example/ui/localization/AppStrings.kt", "w") as f:
    f.write(content)
