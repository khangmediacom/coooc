package com.example.engine

import android.content.Context

/**
 * Audio & Haptics bridge forwarding to [SoundManager].
 */
object AudioHaptics {

    fun playMoveSound(context: Context, enabled: Boolean) {
        SoundManager.playMove(context, enabled)
    }

    fun playCaptureSound(context: Context, enabled: Boolean) {
        SoundManager.playCapture(context, enabled)
    }

    fun playCheckSound(context: Context, enabled: Boolean) {
        SoundManager.playCheck(context, enabled)
    }

    fun playPromotionSound(context: Context, enabled: Boolean) {
        SoundManager.playPromotion(context, enabled)
    }

    fun playVictorySound(context: Context, enabled: Boolean) {
        SoundManager.playVictory(context, enabled)
    }

    fun playDefeatSound(context: Context, enabled: Boolean) {
        SoundManager.playDefeat(context, enabled)
    }

    fun playKingLeapSound(context: Context, enabled: Boolean) {
        SoundManager.playKingLeap(context, enabled)
    }

    fun playCountTick(context: Context, enabled: Boolean) {
        SoundManager.playCountTick(context, enabled)
    }

    fun playButtonClick(context: Context, enabled: Boolean) {
        SoundManager.playButtonClick(context, enabled)
    }

    fun triggerHaptic(context: Context, isHeavy: Boolean = false) {
        SoundManager.triggerHaptic(context, isHeavy)
    }

    fun startBgm() {
        SoundManager.startBackgroundMusic()
    }

    fun stopBgm() {
        SoundManager.stopBackgroundMusic()
    }

    fun updatePreferences(
        soundEnabled: Boolean,
        soundVol: Float,
        musicEnabled: Boolean,
        musicVol: Float
    ) {
        SoundManager.updatePreferences(soundEnabled, soundVol, musicEnabled, musicVol)
    }
}
