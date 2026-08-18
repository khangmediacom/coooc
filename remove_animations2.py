import re
import os

def disable_infinite(filepath):
    if not os.path.exists(filepath): return
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Replace all infinite floats with 0f
    content = re.sub(
        r'val \w+ by infiniteTransition\.animateFloat\([\s\S]*?label = "[^"]+"\n\s*\)',
        'val \\g<0>_dummy = 0f', # this breaks if we use the same name, so let's just do a regex that extracts the val name
        content
    )
    # Actually it's easier to just do it precisely or replace infiniteRepeatable with snap()
    content = content.replace('infiniteRepeatable(', 'androidx.compose.animation.core.snap(')
    
    with open(filepath, 'w') as f:
        f.write(content)

disable_infinite('app/src/main/java/com/example/ui/components/ChessBoardView.kt')
disable_infinite('app/src/main/java/com/example/ui/components/KhmerMascotIcons.kt')
disable_infinite('app/src/main/java/com/example/ui/components/HanumanCoachComponent.kt')

