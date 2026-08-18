with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content = f.read()

# Replace the incorrect structure
fixed_content = content.replace("""            }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),""", """            }
        } // End of Mascot Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),""")

fixed_content = fixed_content + "\n}\n"

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write(fixed_content)
