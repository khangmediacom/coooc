package com.example.data.model

object TacticsData {
    val lessons = listOf(
        TacticsLesson(
            id = "lesson_1",
            chapter = 1,
            titleVi = "1. Nhập môn & Nước đi của Tốt (Trey)",
            titleEn = "1. Introduction & Pawn (Trey) Moves",
            titleKm = "១. ការចាប់ផ្តើម និងការដើររបស់ត្រី",
            titleFr = "1. Introduction et déplacement du Pion (Trey)",
            descriptionVi = "Trong cờ Ốc, tất cả Tốt (Trey) đứng ở hàng 3 và hàng 6, gần nhau hơn so với cờ vua phương Tây!",
            descriptionEn = "In Khmer Chess, all Pawns (Trey) start on rank 3 and 6, meeting in battle much earlier!",
            descriptionKm = "នៅក្នុងអុកខ្មែរ ត្រីទាំងអស់ឈរនៅជួរទី ៣ និង ៦ ដែលជួបគ្នានៅសមរភូមិលឿនជាង!",
            descriptionFr = "Aux échecs khmers, tous les pions débutent aux rangs 3 et 6, se confrontant rapidement !",
            fenPieces = listOf(
                Triple(Position(5, 3), PieceType.PAWN, PieceColor.WHITE),
                Triple(Position(2, 4), PieceType.PAWN, PieceColor.BLACK),
                Triple(Position(7, 3), PieceType.KING, PieceColor.WHITE),
                Triple(Position(0, 4), PieceType.KING, PieceColor.BLACK)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(5, 3) to Position(4, 3)
            ),
            explanationVi = "Tiến Tốt d3 lên d4 để kiểm soát trung tâm bàn cờ ngay từ sớm!",
            explanationEn = "Advance Pawn d3 to d4 to control the central board early!",
            explanationKm = "រុញត្រី d3 ទៅ d4 ដើម្បីគ្រប់គ្រងកណ្តាលក្តារ!",
            explanationFr = "Avancez le pion de d3 à d4 pour contrôler le centre de l'échiquier !",
            difficulty = "Cơ bản"
        ),
        TacticsLesson(
            id = "lesson_2",
            chapter = 2,
            titleVi = "2. Phong Cấp: Tốt Lật (Trey Bompong)",
            titleEn = "2. Promotion: Promoted Pawn (Trey Bompong)",
            titleKm = "២. ការឡើងស័ក្តិ: ត្រីបំពង",
            titleFr = "2. Promotion : Pion Promu (Trey Bompong)",
            descriptionVi = "Khi Tốt trắng tiến đến hàng 6 (hàng 2 theo chỉ số 0-7), Tốt sẽ tự động lật thành Trey Bompong đi chéo 1 ô!",
            descriptionEn = "When White Pawn reaches rank 6, it instantly promotes to Trey Bompong moving 1 step diagonally!",
            descriptionKm = "នៅពេលត្រីសដើរដល់ជួរទី ៦ វានឹងប្រែជាត្រីបំពងដើរតាមអង្កត់ទ្រូង ១ ក្រឡា!",
            descriptionFr = "Quand le pion atteint le rang 6, il est promu en Trey Bompong se déplaçant en diagonale !",
            fenPieces = listOf(
                Triple(Position(3, 3), PieceType.PAWN, PieceColor.WHITE),
                Triple(Position(7, 3), PieceType.KING, PieceColor.WHITE),
                Triple(Position(0, 4), PieceType.KING, PieceColor.BLACK)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(3, 3) to Position(2, 3)
            ),
            explanationVi = "Tiến Tốt lên hàng 6 của đối thủ. Tốt lập tức hóa thành Trey Bompong uy lực!",
            explanationEn = "Advance Pawn to opponent's 6th rank. It immediately becomes Trey Bompong!",
            explanationKm = "រុញត្រីទៅជួរទី ៦ របស់គូប្រកួត វានឹងក្លាយជាត្រីបំពងភ្លាមៗ!",
            explanationFr = "Avancez le pion au rang 6 adverse. Il devient immédiatement un Trey Bompong !",
            difficulty = "Cơ bản"
        ),
        TacticsLesson(
            id = "lesson_3",
            chapter = 3,
            titleVi = "3. Sức Mạnh Của Tượng Koul",
            titleEn = "3. Power of the Nobleman Bishop (Koul)",
            titleKm = "៣. អំណាចរបស់គោល",
            titleFr = "3. Pouvoir du Fou Noble (Koul)",
            descriptionVi = "Tượng Koul có thể đi 1 ô thẳng phía trước hoặc 1 ô theo 4 hướng chéo (tổng 5 hướng).",
            descriptionEn = "Bishop Koul moves 1 step forward or 1 step diagonally in all 4 directions (5 directions total).",
            descriptionKm = "គោលអាចដើរទៅមុខត្រង់ ១ ក្រឡា ឬដើរអង្កត់ទ្រូង ៤ ទិស (សរុប ៥ ទិស)។",
            descriptionFr = "Le Fou Koul avance d'une case tout droit ou d'une case en diagonale (5 directions au total).",
            fenPieces = listOf(
                Triple(Position(4, 4), PieceType.BISHOP, PieceColor.WHITE),
                Triple(Position(3, 4), PieceType.PAWN, PieceColor.BLACK),
                Triple(Position(7, 3), PieceType.KING, PieceColor.WHITE),
                Triple(Position(0, 4), PieceType.KING, PieceColor.BLACK)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(4, 4) to Position(3, 4)
            ),
            explanationVi = "Tượng Koul bắt quân Tốt đen đứng thẳng phía trước - điểm độc đáo của Cờ Ốc!",
            explanationEn = "Bishop Koul captures the black pawn straight ahead - a unique Khmer chess feature!",
            explanationKm = "គោលស៊ីត្រីខ្មៅដែលនៅចំពីមុខ - ជាលក្ខណៈពិសេសនៃអុកខ្មែរ!",
            explanationFr = "Le Fou Koul capture le pion noir droit devant lui - une règle propre aux échecs khmers !",
            difficulty = "Chiến thuật"
        ),
        TacticsLesson(
            id = "lesson_4",
            chapter = 4,
            titleVi = "4. Nước Nhảy Đầu Tiên Của Vua (Ang)",
            titleEn = "4. King (Ang) First Move Leap",
            titleKm = "៤. ការលោតដំបូងរបស់ស្តេច",
            titleFr = "4. Saut Initial du Roi (Ang)",
            descriptionVi = "Ở nước đi đầu tiên (khi chưa di chuyển và không bị chiếu), Vua Ang có thể nhảy như Mã đến ô trống!",
            descriptionEn = "On its very first move, the King Ang can leap like a Knight to any empty square!",
            descriptionKm = "នៅជំហានដំបូង (នៅពេលមិនទាន់ដើរ និងមិនត្រូវគំរាម) ស្តេចអាចលោតដូចសេះទៅក្រឡាទទេ!",
            descriptionFr = "À son tout premier coup, le Roi Ang peut sauter comme un Cavalier vers une case vide !",
            fenPieces = listOf(
                Triple(Position(7, 3), PieceType.KING, PieceColor.WHITE),
                Triple(Position(5, 3), PieceType.PAWN, PieceColor.WHITE),
                Triple(Position(0, 4), PieceType.KING, PieceColor.BLACK)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(7, 3) to Position(5, 2)
            ),
            explanationVi = "Vua nhảy nước Mã thoát khỏi hàng sau và tìm vị trí an toàn!",
            explanationEn = "King leaps like a Knight to escape the baseline and find safe shelter!",
            explanationKm = "ស្តេចលោតដូចសេះចេញពីជួរក្រោយស្វែងរកសុវត្ថិភាព!",
            explanationFr = "Le Roi saute comme un Cavalier pour quitter la rangée arrière et se mettre en sécurité !",
            difficulty = "Đặc biệt"
        ),
        TacticsLesson(
            id = "lesson_5",
            chapter = 5,
            titleVi = "5. Đòn Phối Hợp Mã (Ses) & Tượng (Koul)",
            titleEn = "5. Knight & Bishop Checkmate Coordination",
            titleKm = "៥. ការសម្របសម្រួលសេះ និងគោល",
            titleFr = "5. Coordination Cavalier et Fou Mat",
            descriptionVi = "Sử dụng Mã Ses chiếu tướng phối hợp cùng Tượng Koul bịt đường thoát của Vua đối phương!",
            descriptionEn = "Coordinate Knight and Bishop to deliver a decisive checkmate!",
            descriptionKm = "ប្រើសេះ និងគោលដើម្បីអុកសម្លាប់ស្តេចគូប្រកួត!",
            descriptionFr = "Coordonnez le Cavalier et le Fou pour infliger un échec et mat décisif !",
            fenPieces = listOf(
                Triple(Position(0, 0), PieceType.KING, PieceColor.BLACK),
                Triple(Position(2, 2), PieceType.KNIGHT, PieceColor.WHITE),
                Triple(Position(2, 1), PieceType.BISHOP, PieceColor.WHITE),
                Triple(Position(4, 3), PieceType.KING, PieceColor.WHITE)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(2, 2) to Position(1, 0)
            ),
            explanationVi = "Mã nhảy vào a7 (1,0) chiếu tướng! Vua đen không còn ô thoát và bị chiếu bí!",
            explanationEn = "Knight leaps to a7 (1,0) for checkmate! Black king has no escape square!",
            explanationKm = "សេះលោតទៅ a7 អុកសម្លាប់! ស្តេចខ្មៅគ្មានផ្លូវរត់!",
            explanationFr = "Le Cavalier saute en a7 pour faire échec et mat ! Le Roi noir n'a plus d'échappatoire !",
            difficulty = "Cao cấp"
        ),
        TacticsLesson(
            id = "lesson_6",
            chapter = 6,
            titleVi = "6. Xe (Tuok) Chiếu Bí Hàng Đáy",
            titleEn = "6. Back-rank Checkmate with Boat (Tuok)",
            titleKm = "៦. ការសម្លាប់ដោយទូកនៅជួរក្រោម",
            titleFr = "6. Mat du Couloir avec la Tour / Bateau (Tuok)",
            descriptionVi = "Xe Tuok di chuyển ngang dọc bất kỳ ô nào, là quân tấn công chủ lực trên bàn cờ!",
            descriptionEn = "Rook Tuok moves any empty squares orthogonally and is the primary heavy attacker!",
            descriptionKm = "ទូកដើរតាមជួរដេក និងឈរប៉ុន្មានក្រឡាក៏បាន ជាកម្លាំងវាយប្រហារដ៏ខ្លាំង!",
            descriptionFr = "La Tour Tuok se déplace orthogonalement sur toute case libre, pièce maîtresse de l'attaque !",
            fenPieces = listOf(
                Triple(Position(0, 7), PieceType.KING, PieceColor.BLACK),
                Triple(Position(1, 6), PieceType.PAWN, PieceColor.BLACK),
                Triple(Position(1, 7), PieceType.PAWN, PieceColor.BLACK),
                Triple(Position(7, 0), PieceType.ROOK, PieceColor.WHITE),
                Triple(Position(6, 4), PieceType.KING, PieceColor.WHITE)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(7, 0) to Position(0, 0)
            ),
            explanationVi = "Xe phi thẳng lên hàng 8 chiếu bí vì Vua đen bị chặn bởi chính các quân Tốt của mình!",
            explanationEn = "Rook surges to rank 8 for back-rank mate as black king is trapped behind pawns!",
            explanationKm = "ទូកឡើងទៅជួរទី ៨ អុកសម្លាប់ដោយស្តេចខ្មៅជាប់នឹងត្រីខ្លួនឯង!",
            explanationFr = "La Tour fonce au rang 8 pour un mat du couloir, le Roi noir étant bloqué par ses propres pions !",
            difficulty = "Chiến thuật"
        ),
        TacticsLesson(
            id = "lesson_7",
            chapter = 7,
            titleVi = "7. Luật Đếm Cờ Ốc: Cầm Hòa Thoát Hiểm",
            titleEn = "7. Khmer Counting Rule: Defending for Draw",
            titleKm = "៧. ច្បាប់រាប់អុក: ការការពារដើម្បីស្មើ",
            titleFr = "7. Règle du Compte Khmer : Défense pour la Nulle",
            descriptionVi = "Khi đối phương chỉ còn 1 Xe, bạn chỉ cần phòng thủ trong 16 nước là trận đấu hòa!",
            descriptionEn = "When opponent has only 1 Rook left, survive 16 moves to claim a Draw!",
            descriptionKm = "នៅពេលគូប្រកួតនៅសល់តែទូក ១ អ្នកគ្រាន់តែការពារ ១៦ ជំហានដើម្បីស្មើ!",
            descriptionFr = "Quand l'adversaire n'a plus qu'une Tour, survivez 16 coups pour obtenir la nulle !",
            fenPieces = listOf(
                Triple(Position(3, 3), PieceType.KING, PieceColor.WHITE),
                Triple(Position(1, 1), PieceType.ROOK, PieceColor.BLACK),
                Triple(Position(5, 5), PieceType.KING, PieceColor.BLACK)
            ),
            turn = PieceColor.WHITE,
            expectedMoves = listOf(
                Position(3, 3) to Position(4, 3)
            ),
            explanationVi = "Vua trắng chủ động né xa góc bàn cờ và giữ vị trí trung tâm để kéo dài số nước đếm hòa!",
            explanationEn = "White King stays away from corners and maneuvers in center to count down safely!",
            explanationKm = "ស្តេចសចៀសវាងជ្រុងក្តារ ហើយនៅចំកណ្តាលដើម្បីរាប់ជំហានស្មើដោយសុវត្ថិភាព!",
            explanationFr = "Le Roi blanc évite les coins et manœuvre au centre pour décompter les coups en toute sécurité !",
            difficulty = "Tàn cuộc"
        )
    )
}
