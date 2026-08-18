with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()
content = content.replace("            )\n            }\n    }\n}", "            )\n        }\n    }\n}")
with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
