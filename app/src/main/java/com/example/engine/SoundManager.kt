package com.example.engine

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Advanced Sound & Traditional Khmer Music Synthesizer for Ouk Chatrang (Khmer Chess).
 * Real-time synthesis of Pinpeat orchestra instruments (Roneat Ek xylophone, Kong Vong gong circle,
 * Tro string drone, Angkor bronze temple bells, and acoustic teakwood pieces).
 */
object SoundManager {

    private const val TAG = "SoundManager"
    private val scope = CoroutineScope(Dispatchers.Default)
    private const val SAMPLE_RATE = 44100
    private val sfxSemaphore = Semaphore(4)

    // Volume & Mute Controls
    @Volatile var isSoundEnabled: Boolean = true
    @Volatile var sfxVolume: Float = 0.90f

    @Volatile var isMusicEnabled: Boolean = true
    @Volatile var musicVolume: Float = 0.65f

    // BGM Streaming Track & Background Coroutine Job
    private var bgmJob: Job? = null
    private var bgmTrack: AudioTrack? = null
    private val bgmLock = Any()

    // Pre-computed PCM audio waveforms for instant zero-latency SFX playback
    private val movePcm: ShortArray by lazy { generateMoveSound() }
    private val capturePcm: ShortArray by lazy { generateCaptureSound() }
    private val checkPcm: ShortArray by lazy { generateCheckGongSound() }
    private val promotionPcm: ShortArray by lazy { generatePromotionSound() }
    private val victoryPcm: ShortArray by lazy { generateVictorySound() }
    private val defeatPcm: ShortArray by lazy { generateDefeatSound() }
    private val kingLeapPcm: ShortArray by lazy { generateKingLeapSound() }
    private val countTickPcm: ShortArray by lazy { generateCountTickSound() }
    private val buttonClickPcm: ShortArray by lazy { generateButtonClickSound() }

    private fun playPcm(pcm: ShortArray, volumeScale: Float = 1.0f) {
        if (!isSoundEnabled) return
        val currentVol = (sfxVolume * volumeScale).coerceIn(0f, 1f)
        if (currentVol <= 0.01f) return

        scope.launch {
            try {
                sfxSemaphore.withPermit {
                    // Apply volume scale to buffer
                    val scaledBuffer = ShortArray(pcm.size)
                    for (i in pcm.indices) {
                        scaledBuffer[i] = (pcm[i] * currentVol).toInt().coerceIn(-32768, 32767).toShort()
                    }

                    val minBufferSize = AudioTrack.getMinBufferSize(
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                    )
                    val bufferSizeBytes = (scaledBuffer.size * 2).coerceAtLeast(minBufferSize.coerceAtLeast(2048))

                    val audioTrack = try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            AudioTrack.Builder()
                                .setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                        .build()
                                )
                                .setAudioFormat(
                                    AudioFormat.Builder()
                                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                        .setSampleRate(SAMPLE_RATE)
                                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                        .build()
                                )
                                .setBufferSizeInBytes(bufferSizeBytes)
                                .setTransferMode(AudioTrack.MODE_STREAM)
                                .build()
                        } else {
                            @Suppress("DEPRECATION")
                            AudioTrack(
                                AudioManager.STREAM_MUSIC,
                                SAMPLE_RATE,
                                AudioFormat.CHANNEL_OUT_MONO,
                                AudioFormat.ENCODING_PCM_16BIT,
                                bufferSizeBytes,
                                AudioTrack.MODE_STREAM
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to create AudioTrack for SFX", e)
                        null
                    }

                    if (audioTrack != null && audioTrack.state == AudioTrack.STATE_INITIALIZED) {
                        audioTrack.play()
                        audioTrack.write(scaledBuffer, 0, scaledBuffer.size)
                        val durationMs = (scaledBuffer.size.toDouble() / SAMPLE_RATE * 1000).toLong() + 50L
                        delay(durationMs)
                        try {
                            audioTrack.stop()
                            audioTrack.release()
                        } catch (_: Throwable) {}
                    } else {
                        try {
                            audioTrack?.release()
                        } catch (_: Throwable) {}
                    }
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Error playing PCM SFX", e)
            }
        }
    }

    // ==========================================
    // TRADITIONAL KHMER SOUND SYNTHESIZERS (SFX)
    // ==========================================

    /**
     * Authentic teakwood piece placement sound (sharp wooden click + warm acoustic body).
     */
    private fun generateMoveSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.085).toInt()
        val buffer = ShortArray(numSamples)
        val f0 = 440.0
        val f1 = 220.0
        val f2 = 880.0
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay = exp(-t * 60.0)
            val sample = (sin(2.0 * PI * f0 * t) * 0.50 +
                    sin(2.0 * PI * f1 * t) * 0.35 +
                    sin(2.0 * PI * f2 * t) * 0.15) * decay
            buffer[i] = (sample * 26000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Heavy piece capture impact (double-wood collision snap + acoustic Roneat chime).
     */
    private fun generateCaptureSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.125).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay1 = exp(-t * 50.0)
            val t2 = (t - 0.018).coerceAtLeast(0.0)
            val decay2 = if (t >= 0.018) exp(-t2 * 40.0) else 0.0
            val wave1 = sin(2.0 * PI * 520.0 * t) * 0.40 * decay1
            val wave2 = sin(2.0 * PI * 260.0 * t2) * 0.60 * decay2
            val snap = sin(2.0 * PI * 1150.0 * t) * 0.30 * decay1
            val roneat = sin(2.0 * PI * 659.25 * t) * 0.20 * decay1
            val sample = (wave1 + wave2 + snap + roneat)
            buffer[i] = (sample * 27000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Traditional Angkor bronze temple bell / resonant gong sound for Check ("Ouk!").
     */
    private fun generateCheckGongSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.500).toInt()
        val buffer = ShortArray(numSamples)
        val f0 = 587.33 // D5 note
        val f1 = 880.00 // A5 fifth
        val f2 = 1174.66 // D6 octave
        val fSub = 293.66 // D4 deep gong resonance
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay = exp(-t * 7.5)
            val shimmer = sin(2.0 * PI * 3.5 * t) * 0.12
            val sample = (sin(2.0 * PI * f0 * t) * 0.45 +
                    sin(2.0 * PI * f1 * t) * 0.25 +
                    sin(2.0 * PI * f2 * t) * 0.12 +
                    sin(2.0 * PI * fSub * t) * 0.35 + shimmer) * decay
            buffer[i] = (sample * 26000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Auspicious ascending glissando for Trey Bompong promotion.
     */
    private fun generatePromotionSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.300).toInt()
        val buffer = ShortArray(numSamples)
        val freqs = listOf(440.0, 523.25, 659.25, 783.99, 1046.50) // A4, C5, E5, G5, C6
        val step = 0.055
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val idx = (t / step).toInt().coerceIn(0, freqs.size - 1)
            val localT = t - (idx * step)
            val decay = exp(-localT * 16.0)
            val freq = freqs[idx]
            val sample = sin(2.0 * PI * freq * localT) * decay * 0.85
            buffer[i] = (sample * 24000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Royal Khmer Pinpeat Victory Fanfare (Triumphant pentatonic palace celebration).
     */
    private fun generateVictorySound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.800).toInt()
        val buffer = ShortArray(numSamples)
        val notes = listOf(523.25, 587.33, 659.25, 783.99, 880.00, 1046.50, 1174.66) // C5, D5, E5, G5, A5, C6, D6
        val noteDur = 0.100
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val noteIdx = (t / noteDur).toInt().coerceIn(0, notes.size - 1)
            val noteTime = t - (noteIdx * noteDur)
            val decay = exp(-noteTime * 9.0)
            val freq = notes[noteIdx]
            val roneatChime = sin(2.0 * PI * freq * noteTime) * 0.65
            val gongHarmony = sin(2.0 * PI * (freq / 2) * noteTime) * 0.25
            val sample = (roneatChime + gongHarmony) * decay
            buffer[i] = (sample * 26000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Traditional Khmer Defeat Melancholic Cadence (Somber descending pentatonic cadence).
     */
    private fun generateDefeatSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.750).toInt()
        val buffer = ShortArray(numSamples)
        val notes = listOf(880.00, 783.99, 659.25, 587.33, 523.25, 440.00) // A5, G5, E5, D5, C5, A4
        val noteDur = 0.110
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val noteIdx = (t / noteDur).toInt().coerceIn(0, notes.size - 1)
            val noteTime = t - (noteIdx * noteDur)
            val decay = exp(-noteTime * 7.5)
            val freq = notes[noteIdx]
            val acousticFlute = sin(2.0 * PI * freq * noteTime) * 0.60
            val subPad = sin(2.0 * PI * (freq * 0.5) * noteTime) * 0.25
            val sample = (acousticFlute + subPad) * decay
            buffer[i] = (sample * 24000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Rapid double-tap for King Knight-Leap first move.
     */
    private fun generateKingLeapSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.120).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val t1 = t
            val decay1 = exp(-t1 * 50.0)
            val click1 = sin(2.0 * PI * 580.0 * t1) * decay1 * 0.5

            val t2 = (t - 0.040).coerceAtLeast(0.0)
            val decay2 = if (t >= 0.040) exp(-t2 * 45.0) else 0.0
            val click2 = if (t >= 0.040) sin(2.0 * PI * 870.0 * t2) * decay2 * 0.7 else 0.0

            val sample = (click1 + click2)
            buffer[i] = (sample * 26000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * Wooden temple clapper countdown tick.
     */
    private fun generateCountTickSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.035).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay = exp(-t * 90.0)
            val sample = sin(2.0 * PI * 920.0 * t) * decay * 0.7
            buffer[i] = (sample * 22000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    /**
     * UI button click (clean soft acoustic bubble).
     */
    private fun generateButtonClickSound(): ShortArray {
        val numSamples = (SAMPLE_RATE * 0.030).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val decay = exp(-t * 110.0)
            val sample = sin(2.0 * PI * 700.0 * t) * decay * 0.6
            buffer[i] = (sample * 20000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    // ==========================================================
    // CONTINUOUS TRADITIONAL KHMER BACKGROUND MUSIC ENGINE (BGM)
    // ==========================================================

    /**
     * Khmer Pinpeat Pentatonic Melodic Loop Generator:
     * Plays a soothing, peaceful traditional Angkor melody combining Roneat xylophone,
     * Kong Vong gong bells, and warm Tro string pads.
     */
    private val bgmNotes = doubleArrayOf(
        293.66, // D4
        329.63, // E4
        392.00, // G4
        440.00, // A4
        523.25, // C5
        587.33, // D5
        659.25, // E5
        783.99  // G5
    )

    // Authentic traditional 16-step melody pattern
    private val melodySequence = intArrayOf(
        0, 2, 4, 3,  2, 4, 5, 4,
        3, 2, 1, 0,  2, 0, 3, 2
    )

    private fun generateBgmLoopChunk(): ShortArray {
        val bpm = 68.0
        val stepSec = 60.0 / (bpm * 2.0) // ~0.44s per step
        val totalSec = stepSec * melodySequence.size
        val totalSamples = (SAMPLE_RATE * totalSec).toInt()
        val buffer = ShortArray(totalSamples)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val stepIdx = (t / stepSec).toInt().coerceIn(0, melodySequence.size - 1)
            val stepTime = t - (stepIdx * stepSec)

            val noteFreq = bgmNotes[melodySequence[stepIdx]]
            val roneatDecay = exp(-stepTime * 5.0)

            // 1. Roneat Ek (Wood Xylophone)
            val roneat = (sin(2.0 * PI * noteFreq * stepTime) * 0.50 +
                    sin(2.0 * PI * (noteFreq * 2.0) * stepTime) * 0.15) * roneatDecay

            // 2. Tro Warm Drone Pad (Continuous harmonic base)
            val droneFreq = 146.83 // D3 root
            val drone = (sin(2.0 * PI * droneFreq * t) * 0.18 +
                    sin(2.0 * PI * (droneFreq * 1.5) * t) * 0.08)

            // 3. Gentle Kong Vong temple bell on beat 0 and 8
            val gongTime = if (stepIdx < 8) t else t - (8 * stepSec)
            val gongDecay = exp(-gongTime * 1.8)
            val gong = if (stepIdx == 0 || stepIdx == 8) {
                sin(2.0 * PI * 293.66 * gongTime) * 0.25 * gongDecay
            } else 0.0

            val mixed = (roneat + drone + gong) * 0.75
            buffer[i] = (mixed * 24000).toInt().coerceIn(-32768, 32767).toShort()
        }
        return buffer
    }

    private val bgmPcmChunk: ShortArray by lazy { generateBgmLoopChunk() }

    fun startBackgroundMusic() {
        synchronized(bgmLock) {
            if (bgmJob?.isActive == true) return
            bgmJob = scope.launch {
                var track: AudioTrack? = null
                try {
                    val minBufSize = AudioTrack.getMinBufferSize(
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                    )
                    val bufferSize = minBufSize.coerceAtLeast(bgmPcmChunk.size * 2).coerceAtLeast(4096)

                    track = try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            AudioTrack.Builder()
                                .setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                        .build()
                                )
                                .setAudioFormat(
                                    AudioFormat.Builder()
                                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                        .setSampleRate(SAMPLE_RATE)
                                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                        .build()
                                )
                                .setBufferSizeInBytes(bufferSize)
                                .setTransferMode(AudioTrack.MODE_STREAM)
                                .build()
                        } else {
                            @Suppress("DEPRECATION")
                            AudioTrack(
                                AudioManager.STREAM_MUSIC,
                                SAMPLE_RATE,
                                AudioFormat.CHANNEL_OUT_MONO,
                                AudioFormat.ENCODING_PCM_16BIT,
                                bufferSize,
                                AudioTrack.MODE_STREAM
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to create BGM AudioTrack", e)
                        null
                    }

                    if (track == null || track.state != AudioTrack.STATE_INITIALIZED) {
                        try { track?.release() } catch (_: Throwable) {}
                        return@launch
                    }

                    bgmTrack = track
                    track.play()

                    val renderChunk = ShortArray(bgmPcmChunk.size)

                    while (isActive) {
                        if (!isMusicEnabled || musicVolume <= 0.01f) {
                            delay(350)
                            continue
                        }

                        val vol = musicVolume.coerceIn(0f, 1f)
                        for (j in bgmPcmChunk.indices) {
                            renderChunk[j] = (bgmPcmChunk[j] * vol).toInt().coerceIn(-32768, 32767).toShort()
                        }

                        val written = track.write(renderChunk, 0, renderChunk.size)
                        if (written < 0) {
                            delay(250)
                        }
                    }
                } catch (_: CancellationException) {
                } catch (e: Throwable) {
                    Log.e(TAG, "BGM loop exception", e)
                } finally {
                    try {
                        track?.stop()
                        track?.release()
                    } catch (_: Throwable) {}
                    bgmTrack = null
                }
            }
        }
    }

    fun stopBackgroundMusic() {
        synchronized(bgmLock) {
            bgmJob?.cancel()
            bgmJob = null
            try {
                bgmTrack?.stop()
                bgmTrack?.release()
                bgmTrack = null
            } catch (_: Throwable) {}
        }
    }

    fun updatePreferences(
        soundEnabled: Boolean,
        soundVol: Float,
        musicEnabled: Boolean,
        musicVol: Float
    ) {
        this.isSoundEnabled = soundEnabled
        this.sfxVolume = soundVol.coerceIn(0f, 1f)
        this.isMusicEnabled = musicEnabled
        this.musicVolume = musicVol.coerceIn(0f, 1f)

        if (musicEnabled && musicVol > 0.01f) {
            startBackgroundMusic()
        } else {
            stopBackgroundMusic()
        }
    }

    // ==========================================
    // PUBLIC EVENT AUDIO DISPATCHERS
    // ==========================================

    fun playMove(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(movePcm)
    }

    fun playCapture(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(capturePcm)
    }

    fun playCheck(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(checkPcm, volumeScale = 1.15f)
    }

    fun playPromotion(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(promotionPcm)
    }

    fun playVictory(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(victoryPcm, volumeScale = 1.2f)
    }

    fun playDefeat(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(defeatPcm, volumeScale = 1.1f)
    }

    fun playKingLeap(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(kingLeapPcm)
    }

    fun playCountTick(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(countTickPcm)
    }

    fun playButtonClick(context: Context, enabled: Boolean = isSoundEnabled) {
        if (!enabled) return
        playPcm(buttonClickPcm)
    }

    fun triggerHaptic(context: Context, isHeavy: Boolean = false) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = if (isHeavy) {
                    VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(if (isHeavy) 45L else 20L)
            }
        } catch (_: Throwable) {}
    }
}
