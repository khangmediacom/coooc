import re

with open("app/src/main/java/com/example/ui/components/HanumanCoachComponent.kt", "r") as f:
    content = f.read()

# Replace the AnimatedHanumanCoach function completely
new_func = """fun AnimatedHanumanCoach(
    modifier: Modifier = Modifier,
    mood: HanumanMood = HanumanMood.IDLE,
    size: Dp = 72.dp,
    showCrown: Boolean = true,
    interactiveOnClick: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    var isTapped by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (isTapped) 1.15f else 1.0f,
        animationSpec = tween(150),
        finishedListener = { isTapped = false },
        label = "hanuman_tap_scale"
    )

    val auraBorderColor = when (mood) {
        HanumanMood.HAPPY -> Color(0xFF10B981)
        HanumanMood.ALERT -> Color(0xFFEF4444)
        HanumanMood.THINKING -> Color(0xFF3B82F6)
        HanumanMood.TALKING -> Color(0xFFD4AF37)
        HanumanMood.MEDITATING -> Color(0xFF8B5CF6)
        HanumanMood.IDLE -> Color(0xFFD4AF37)
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scaleAnim)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, auraBorderColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = interactiveOnClick
            ) {
                isTapped = true
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.mascot),
            contentDescription = "Hanuman Mascot Avatar",
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            modifier = Modifier.fillMaxSize().padding(4.dp).clip(CircleShape)
        )
    }
}"""

pattern = r"fun AnimatedHanumanCoach\((?:.|\n)*?^}\n}"
new_content = re.sub(pattern, new_func, content, flags=re.MULTILINE)

with open("app/src/main/java/com/example/ui/components/HanumanCoachComponent.kt", "w") as f:
    f.write(new_content)
