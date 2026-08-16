package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_history")
data class MatchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameMode: String, // AI, LOCAL_2P, ONLINE_MATCH
    val opponentName: String,
    val aiDifficulty: String?,
    val playerColor: String, // WHITE, BLACK
    val winner: String?, // WHITE, BLACK, DRAW
    val endReason: String, // CHECKMATE, RESIGNED, TIMEOUT, DRAW_BY_COUNT
    val totalMoves: Int,
    val movesNotation: String, // JSON or comma separated e.g. "e3-e4,e6-e5,..."
    val movesDetailed: String, // Serialized fromRow,fromCol,toRow,toCol,pieceType,color
    val durationSeconds: Long,
    val eloChange: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: String = "primary_user",
    val isLoggedIn: Boolean = false,
    val email: String = "",
    val username: String = "Guest",
    val avatarUrl: String = "",
    val elo: Int = 1520,
    val level: Int = 12,
    val exp: Int = 2450,
    val tier: String = "Grandmaster (Gold)",
    val totalMatches: Int = 80,
    val wins: Int = 48,
    val losses: Int = 32,
    val draws: Int = 0,
    val winStreak: Int = 4,
    val maxStreak: Int = 8,
    val selectedTheme: String = "traditional_wood",
    val selectedPieceStyle: String = "sculpted",
    val language: String = "en",
    val isDarkMode: Boolean = false,
    val soundEnabled: Boolean = true,
    val soundVolume: Float = 0.85f,
    val musicEnabled: Boolean = true,
    val musicVolume: Float = 0.65f
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val completed: Boolean = false,
    val stars: Int = 0,
    val completedAt: Long = 0L
)
