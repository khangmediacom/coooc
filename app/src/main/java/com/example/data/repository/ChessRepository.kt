package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.MatchHistoryEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.BoardTheme
import com.example.data.model.LeaderboardEntry
import com.example.data.model.PieceStyle
import com.example.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChessRepository(private val database: AppDatabase) {

    val matchHistory: Flow<List<MatchHistoryEntity>> = database.matchHistoryDao().getAllMatches()
    val lessonProgress: Flow<List<LessonProgressEntity>> = database.lessonProgressDao().getAllProgress()

    val userProfile: Flow<UserProfileEntity> = database.userProfileDao().getUserProfile().map { profile ->
        profile ?: UserProfileEntity()
    }

    val userPreferences: Flow<UserPreferences> = userProfile.map { profile ->
        UserPreferences(
            boardTheme = BoardTheme.fromId(profile.selectedTheme),
            pieceStyle = PieceStyle.fromId(profile.selectedPieceStyle),
            language = AppLanguage.fromCode(profile.language),
            isDarkMode = profile.isDarkMode,
            soundEnabled = profile.soundEnabled,
            soundVolume = profile.soundVolume,
            musicEnabled = profile.musicEnabled,
            musicVolume = profile.musicVolume
        )
    }

    suspend fun initializeIfNeeded() {
        val existing = database.userProfileDao().getUserProfileOnce()
        if (existing == null) {
            // Default is guest or initialized state
            database.userProfileDao().insertOrUpdateProfile(
                UserProfileEntity(
                    id = "primary_user",
                    isLoggedIn = false,
                    email = "",
                    username = "Guest",
                    elo = 1520,
                    level = 12,
                    exp = 2450,
                    tier = "Grandmaster (Gold)",
                    totalMatches = 80,
                    wins = 48,
                    losses = 32,
                    draws = 0,
                    winStreak = 4,
                    maxStreak = 8
                )
            )
        }
    }

    suspend fun signInWithGoogle(email: String, displayName: String) {
        val current = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        database.userProfileDao().insertOrUpdateProfile(
            current.copy(
                isLoggedIn = true,
                email = email,
                username = displayName,
                avatarUrl = "avatar_user"
            )
        )
    }

    suspend fun signOut() {
        val current = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        database.userProfileDao().insertOrUpdateProfile(
            current.copy(
                isLoggedIn = false,
                email = "",
                username = "Guest"
            )
        )
    }

    suspend fun recordMatch(
        gameMode: String,
        opponentName: String,
        aiDifficulty: String?,
        playerColor: String,
        winner: String?,
        endReason: String,
        totalMoves: Int,
        movesNotation: String,
        movesDetailed: String,
        durationSeconds: Long,
        eloChange: Int
    ) {
        val entity = MatchHistoryEntity(
            gameMode = gameMode,
            opponentName = opponentName,
            aiDifficulty = aiDifficulty,
            playerColor = playerColor,
            winner = winner,
            endReason = endReason,
            totalMoves = totalMoves,
            movesNotation = movesNotation,
            movesDetailed = movesDetailed,
            durationSeconds = durationSeconds,
            eloChange = eloChange
        )
        database.matchHistoryDao().insertMatch(entity)

        // Update user stats
        val currentProfile = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        val isWin = winner == playerColor
        val isDraw = winner == "DRAW"
        val newWins = currentProfile.wins + if (isWin) 1 else 0
        val newLosses = currentProfile.losses + if (!isWin && !isDraw) 1 else 0
        val newDraws = currentProfile.draws + if (isDraw) 1 else 0
        val newStreak = if (isWin) currentProfile.winStreak + 1 else if (isDraw) currentProfile.winStreak else 0
        val newMaxStreak = maxOf(currentProfile.maxStreak, newStreak)
        val newElo = (currentProfile.elo + eloChange).coerceAtLeast(400)

        val newTier = when {
            newElo >= 2100 -> "Đại Kiện Tướng Angkor"
            newElo >= 1800 -> "Kiện Tướng (Diamond)"
            newElo >= 1500 -> "Dũng Sĩ (Gold)"
            newElo >= 1200 -> "Chiến Binh (Silver)"
            else -> "Tập Sự (Bronze)"
        }

        database.userProfileDao().insertOrUpdateProfile(
            currentProfile.copy(
                totalMatches = currentProfile.totalMatches + 1,
                wins = newWins,
                losses = newLosses,
                draws = newDraws,
                winStreak = newStreak,
                maxStreak = newMaxStreak,
                elo = newElo,
                tier = newTier
            )
        )
    }

    suspend fun updatePreferences(prefs: UserPreferences) {
        val current = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        database.userProfileDao().insertOrUpdateProfile(
            current.copy(
                selectedTheme = prefs.boardTheme.id,
                selectedPieceStyle = prefs.pieceStyle.id,
                language = prefs.language.code,
                isDarkMode = prefs.isDarkMode,
                soundEnabled = prefs.soundEnabled,
                soundVolume = prefs.soundVolume,
                musicEnabled = prefs.musicEnabled,
                musicVolume = prefs.musicVolume
            )
        )
    }

    suspend fun updateUsername(newName: String) {
        val current = database.userProfileDao().getUserProfileOnce() ?: UserProfileEntity()
        database.userProfileDao().insertOrUpdateProfile(
            current.copy(username = newName.trim().ifEmpty { "Kỳ thủ Angkor" })
        )
    }

    suspend fun saveLessonCompletion(lessonId: String, stars: Int = 3) {
        database.lessonProgressDao().saveProgress(
            LessonProgressEntity(
                lessonId = lessonId,
                completed = true,
                stars = stars,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getMatchById(id: Long): MatchHistoryEntity? {
        return database.matchHistoryDao().getMatchById(id)
    }

    fun getGlobalLeaderboard(currentUserProfile: UserProfileEntity): List<LeaderboardEntry> {
        val baseList = mutableListOf(
            LeaderboardEntry(1, "kh_101", "Sovan Dara (ភ្នំពេញ)", "avatar_1", "🇰🇭", 2340, "Đại Kiện Tướng", 210, 18, 92),
            LeaderboardEntry(2, "vn_202", "Trần Minh Quang (Hà Nội)", "avatar_2", "🇻🇳", 2280, "Đại Kiện Tướng", 195, 24, 89),
            LeaderboardEntry(3, "th_303", "Somchai Prasert (Bangkok)", "avatar_3", "🇹🇭", 2210, "Đại Kiện Tướng", 182, 31, 85),
            LeaderboardEntry(4, "kh_104", "Chea Rithy (Siem Reap)", "avatar_4", "🇰🇭", 2150, "Đại Kiện Tướng", 164, 29, 85),
            LeaderboardEntry(5, "vn_205", "Nguyễn Hoàng Nam (TP.HCM)", "avatar_5", "🇻🇳", 2090, "Kiện Tướng", 152, 33, 82),
            LeaderboardEntry(6, "fr_406", "Jean-Luc Dubois (Paris)", "avatar_6", "🇫🇷", 1980, "Kiện Tướng", 140, 42, 77),
            LeaderboardEntry(7, "us_507", "David Miller (California)", "avatar_7", "🇺🇸", 1870, "Kiện Tướng", 125, 48, 72),
            LeaderboardEntry(8, "kh_108", "Vannak Meas (Battambang)", "avatar_8", "🇰🇭", 1790, "Dũng Sĩ", 112, 50, 69)
        )

        // Insert current user in ranked position
        val userEntry = LeaderboardEntry(
            rank = 0,
            playerId = currentUserProfile.id,
            playerName = currentUserProfile.username,
            avatarId = "avatar_user",
            countryFlag = "🇰🇭",
            elo = currentUserProfile.elo,
            tier = currentUserProfile.tier,
            wins = currentUserProfile.wins,
            losses = currentUserProfile.losses,
            winRate = if (currentUserProfile.totalMatches > 0) (currentUserProfile.wins * 100 / currentUserProfile.totalMatches) else 0,
            isCurrentUser = true
        )

        baseList.add(userEntry)
        baseList.sortByDescending { it.elo }

        return baseList.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
    }
}
