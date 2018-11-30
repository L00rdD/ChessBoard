import com.github.ajalt.mordant.TermColors

class ChessBoard: IChessBoard {
    companion object {
        val CHESSBOARD_MAX_NUMBER = 8
        val CHESSBOARD_MIN_NUMBER = 1
        val CHESSBOARD_MAX_LETTER = 'H'
        val CHESSBOARD_MIN_LETTER = 'A'
    }

    override val boxes = mutableMapOf(
            //Black side
            Pair(Box.A8, Pawn(PawnType.ROOK, ChessSide.BLACK)),
            Pair(Box.B8, Pawn(PawnType.KNIGHT, ChessSide.BLACK)),
            Pair(Box.C8, Pawn(PawnType.BISHOP, ChessSide.BLACK)),
            Pair(Box.D8, Pawn(PawnType.QUEEN, ChessSide.BLACK)),
            Pair(Box.E8, Pawn(PawnType.KING, ChessSide.BLACK)),
            Pair(Box.F8, Pawn(PawnType.BISHOP, ChessSide.BLACK)),
            Pair(Box.G8, Pawn(PawnType.KNIGHT, ChessSide.BLACK)),
            Pair(Box.H8, Pawn(PawnType.ROOK, ChessSide.BLACK)),
            Pair(Box.A7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.B7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.C7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.D7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.E7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.F7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.G7, Pawn(PawnType.PAWN, ChessSide.BLACK)),
            Pair(Box.H7, Pawn(PawnType.PAWN, ChessSide.BLACK)),

            //Empty boxes
            Pair(Box.A6, null),
            Pair(Box.B6, null),
            Pair(Box.C6, null),
            Pair(Box.D6, null),
            Pair(Box.E6, null),
            Pair(Box.F6, null),
            Pair(Box.G6, null),
            Pair(Box.H6, null),
            Pair(Box.A5, null),
            Pair(Box.B5, null),
            Pair(Box.C5, null),
            Pair(Box.D5, null),
            Pair(Box.E5, null),
            Pair(Box.F5, null),
            Pair(Box.G5, null),
            Pair(Box.H5, null),
            Pair(Box.A4, null),
            Pair(Box.B4, null),
            Pair(Box.C4, null),
            Pair(Box.D4, null),
            Pair(Box.E4, null),
            Pair(Box.F4, null),
            Pair(Box.G4, null),
            Pair(Box.H4, null),
            Pair(Box.A3, null),
            Pair(Box.B3, null),
            Pair(Box.C3, null),
            Pair(Box.D3, null),
            Pair(Box.E3, null),
            Pair(Box.F3, null),
            Pair(Box.G3, null),
            Pair(Box.H3, null),

            //White side
            Pair(Box.A2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.B2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.C2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.D2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.E2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.F2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.G2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.H2, Pawn(PawnType.PAWN, ChessSide.WHITE)),
            Pair(Box.A1, Pawn(PawnType.ROOK, ChessSide.WHITE)),
            Pair(Box.B1, Pawn(PawnType.KNIGHT, ChessSide.WHITE)),
            Pair(Box.C1, Pawn(PawnType.BISHOP, ChessSide.WHITE)),
            Pair(Box.D1, Pawn(PawnType.QUEEN, ChessSide.WHITE)),
            Pair(Box.E1, Pawn(PawnType.KING, ChessSide.WHITE)),
            Pair(Box.F1, Pawn(PawnType.BISHOP, ChessSide.WHITE)),
            Pair(Box.G1, Pawn(PawnType.KNIGHT, ChessSide.WHITE)),
            Pair(Box.H1, Pawn(PawnType.ROOK, ChessSide.WHITE))
    )
    override var sidePlaying = ChessSide.WHITE
    override var playCount = 0
    override var playsHistoric: ArrayList<Move> = arrayListOf()
    override var rooksAvailable = listOf(ChessSide.WHITE, ChessSide.BLACK)

    override fun getSideHistorical(side: ChessSide): ArrayList<Move>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun move(from: Box, to: Box): Boolean {
        val pawn = boxes[from] ?: return false
        val possibilities = getMovePossibilities(from)
        if (possibilities == null || possibilities.isEmpty() || !possibilities.contains(to)) return false
        movePawn(from, to)
        switchSidePlaying()
        this.playsHistoric.add(Move(pawn, from, to))
        return true
    }

    fun getKingStatus(): KingStatus {
        return getKingStatus(sidePlaying)
    }

    override fun getKingStatus(side: ChessSide): KingStatus {
        val box = boxes.asIterable().first { it.value != null && it.value!!.side == side && it.value!!.type == PawnType.KING }
        val king = box.value

        if (king == null ||!getAllMovePossibilities(getOppositeSide(side)).contains(box.key)) {
            return KingStatus.FREE
        }

        val possibilities = getKingMovePossibilities(king, box.key)?.toMutableList() ?: return KingStatus.MAT

        var previousBox = box.key
        var previousContent = king
        possibilities.removeIf {
            movePawn(previousBox, box.key)
            boxes[previousBox] = previousContent
            previousBox = it
            previousContent = getPawn(it)
            movePawn(box.key, it)
            (getAllMovePossibilities(getOppositeSide(side)).contains(it))
        }

        movePawn(previousBox, box.key)
        boxes[previousBox] = previousContent

        return if(possibilities.isEmpty()) KingStatus.MAT else KingStatus.CHECKED
    }

    fun getPawn(box: Box): Pawn? {
        return boxes[box]
    }

    private fun getBox(pawn: Pawn): Box? {
        return boxes.asIterable().firstOrNull { it.value === pawn }?.key
    }

    private fun getMovePossibilities(box: Box): List<Box>? {
        val pawn = getPawn(box) ?: return null
        return when (pawn.type) {
            PawnType.PAWN -> getPawnMovePossibilities(pawn ,box)
            PawnType.KNIGHT -> getKnightMovePossibilities(pawn, box)
            PawnType.QUEEN -> getQueenMovePossibilities(pawn, box)
            PawnType.ROOK -> getRookMovePossibilities(pawn, box)
            PawnType.BISHOP -> getBishopMovePossibilities(pawn, box)
            PawnType.KING -> getKingMovePossibilities(pawn, box)
        }
    }

    fun getMovesAvailable(box: Box): List<Box> {
        val pawn = getPawn(box) ?: return emptyList()
        val moves = getMovePossibilities(box)?.toMutableList() ?: return emptyList()

        var previousBox = box
        var previousContent = getPawn(box)
        moves.removeIf {
            movePawn(previousBox, box)
            boxes[previousBox] = previousContent
            previousBox = it
            previousContent = getPawn(it)
            movePawn(box, it)
            (getAllMovePossibilities(getOppositeSide(pawn.side)).contains(getKing(pawn.side)) || getKingStatus() == KingStatus.CHECKED)
        }

        movePawn(previousBox, box)
        boxes[previousBox] = previousContent

        return moves.toList()
    }

    private fun getKing(side: ChessSide): Box? {
        return boxes.toList().firstOrNull { it.second != null && it.second!!.side == ChessSide.WHITE && it.second!!.type == PawnType.KING }?.first
    }

    private fun getOppositeSide(side: ChessSide): ChessSide {
        return if (side == ChessSide.WHITE) ChessSide.BLACK else ChessSide.WHITE
    }

    private fun getAllMovePossibilities(side: ChessSide) : List<Box?> {
        val possibilities = mutableListOf<Box>()
        val refPawns = boxes
                .filter { it.value != null && it.value!!.side == side }

        refPawns.forEach { entry ->
            val moves = getMovePossibilities(entry.key)
            if (moves != null && moves.isNotEmpty()) {
                moves.forEach {
                    if (!possibilities.contains(it)) {
                        possibilities.add(it)
                    }
                }
            }
        }

        return possibilities
    }

    private fun getKingMovePossibilities(pawn: Pawn, box: Box): List<Box>? {
        val moves = Box.values().asIterable()

        val sides =  moves.filter {
            it != box &&
                    (it.letter == box.letter && (it.number == box.number + 1 || it.number == box.number - 1)) ||
                    (it.number == box.number && (it.letter == box.letter + 1 || it.letter == box.letter - 1)) ||
                    ((it.letter == box.letter - 1 || it.letter == box.letter + 1) && (it.number == box.number + 1 || it.number == box.number - 1))
        }.toMutableList()

        sides.removeIf {
            val p = getPawn(it)
            (p != null && p.side == pawn.side)
        }

        return sides.toList()
    }

    private fun movePawn(from: Box, to: Box) {
        if (from == to) return
        if (!boxes.contains(from) && !boxes.contains(to)) return
        boxes[to]= boxes[from]
        boxes[from] = null
    }

    private fun getKnightMovePossibilities(pawn: Pawn, box: Box): List<Box>? {
        val moves = Box.values().asIterable()

        val knights = moves.filter {
            (it != box && it.number == box.number + 2 && it.letter == box.letter - 1) ||
                    (it != box && it.number == box.number - 2 && it.letter == box.letter - 1) ||
                    (it != box && it.number == box.number + 2 && it.letter == box.letter + 1) ||
                    (it != box && it.number == box.number - 2 && it.letter == box.letter + 1) ||
                    (it != box && it.number == box.number - 1 && it.letter == box.letter - 2) ||
                    (it != box && it.number == box.number + 1 && it.letter == box.letter - 2) ||
                    (it != box && it.number == box.number - 1 && it.letter == box.letter + 2) ||
                    (it != box && it.number == box.number + 1 && it.letter == box.letter + 2)
        }.toMutableList()

        knights.removeIf {
            val p = getPawn(it)
            p != null && p.side == pawn.side
        }

        return knights.toList()
    }

    private fun getQueenMovePossibilities(pawn: Pawn, box: Box): List<Box>? {
        val rook = getRookMovePossibilities(pawn, box)
        val bishop = getBishopMovePossibilities(pawn, box) ?: return rook
        if (rook == null) return bishop
        val lines = rook.union(bishop).toMutableList()

        return lines.toList()
    }

    private fun getBishopMovePossibilities(pawn: Pawn, box: Box): List<Box>? {
        val moves = Box.values().asIterable()
        val diagonal =  moves.filter {
            ((box.letter - it.letter == box.number - it.number)
                || (it.letter - box.letter == box.number - it.number))
        }.toMutableList()

        var maxUpLeft = diagonal.filter { getPawn(it) != null && it.letter < box.letter && it.number > box.number }.minBy { it.number }
        var maxDownLeft = diagonal.filter { getPawn(it) != null && it.letter < box.letter && it.number < box.number }.maxBy { it.number }
        var maxUpRight = diagonal.filter { getPawn(it) != null && it.letter > box.letter && it.number > box.number }.minBy { it.number }
        var maxDownRight = diagonal.filter { getPawn(it) != null && it.letter > box.letter && it.number < box.number }.maxBy { it.number }

        if (maxUpLeft != null && getPawn(maxUpLeft)?.side == pawn.side)
            maxUpLeft = diagonal.firstOrNull { it.letter == maxUpLeft!!.letter + 1 && it.number == maxUpLeft!!.number - 1}
        if (maxDownLeft != null && getPawn(maxDownLeft)?.side == pawn.side)
            maxDownLeft = diagonal.firstOrNull { it.letter == maxDownLeft!!.letter + 1 && it.number == maxDownLeft!!.number + 1}
        if (maxUpRight != null && getPawn(maxUpRight)?.side == pawn.side)
            maxUpRight = diagonal.firstOrNull { it.letter == maxUpRight!!.letter - 1 && it.number == maxUpRight!!.number - 1}
        if (maxDownRight != null && getPawn(maxDownRight)?.side == pawn.side)
            maxDownRight = diagonal.firstOrNull { it.letter == maxDownRight!!.letter - 1 && it.number == maxDownRight!!.number + 1}

        diagonal.removeIf {
            (maxUpLeft != null && it.number > maxUpLeft.number && it.letter < maxUpLeft.letter) ||
                    (maxDownLeft != null && it.number < maxDownLeft.number && it.letter < maxDownLeft.letter) ||
                    (maxUpRight != null && it.number > maxUpRight.number && it.letter > maxUpRight.letter) ||
                    (maxDownRight != null && it.number < maxDownRight.number && it.letter > maxDownRight.letter) ||
                    (it == box)
        }

        return diagonal
    }

    private fun getRookMovePossibilities(pawn: Pawn, pos: Box): List<Box>? {
        val moves = Box.values().asIterable()
        val lines = moves
                .filter { (it != pos) && it.letter == pos.letter || it.number == pos.number }
                .toMutableList()

        var westMax = lines.filter { getPawn(it) != null && it.letter < pos.letter }.maxBy { it.letter }
        var northMax = lines.filter { getPawn(it) != null && it.letter == pos.letter && it.number > pos.number }.minBy { it.number }
        var southMax = lines.filter { getPawn(it) != null && it.letter == pos.letter && it.number < pos.number }.maxBy { it.number }
        var eastMax = lines.filter { getPawn(it) != null && it.letter > pos.letter }.minBy { it.letter }

        if (westMax != null && getPawn(westMax)?.side == pawn.side) {
            westMax = lines.firstOrNull { it.number == pos.number && it.letter == westMax!!.letter + 1 }
        }
        if(northMax != null && getPawn(northMax)?.side == pawn.side) {
            northMax = lines.firstOrNull { it.letter == pos.letter && it.number == northMax!!.number - 1 }
        }
        if(southMax != null && getPawn(southMax)?.side == pawn.side) {
            southMax = lines.firstOrNull { it.letter == pos.letter && it.number == southMax!!.number + 1 }
        }
        if(eastMax != null && getPawn(eastMax)?.side == pawn.side) {
            eastMax = lines.firstOrNull { it.number == pos.number && it.letter == eastMax!!.letter - 1 }
        }

        lines.removeIf {
            (it == pos ||
                    westMax != null && it.number == westMax.number && it.letter < westMax.letter) ||
                    (eastMax != null && it.number == eastMax.number && it.letter > eastMax.letter) ||
                    (northMax != null && it.letter == northMax.letter && it.number > northMax.number) ||
                    (southMax != null && it.letter == southMax.letter && it.number < southMax.number)
        }

        return lines.toList()
    }

    private fun getPawnMovePossibilities(pawn: Pawn, pos: Box): List<Box>? {
        var moves = Box.values().asIterable()

        var linearMax = getPawnLinearFor(pawn.side, pos)
        linearMax *= if (pawn.side == ChessSide.WHITE) 1 else -1
        val diag = if (pawn.side == ChessSide.WHITE) 1 else -1
        val positions = moves.filter {
             (it.letter == pos.letter && getPawnMaxForSide(pawn.side, linearMax, pos.number, it.number) && getPawn(it) == null) ||
                     (it.letter == pos.letter + 1 && it.number == pos.number + diag && getPawn(it)?.side == getOppositeSide(pawn.side)) ||
                     (it.letter == pos.letter - 1 && it.number == pos.number + diag && getPawn(it)?.side == getOppositeSide(pawn.side))
         }.toMutableList()

        return positions.toList()
    }

    private fun getPawnLinearFor(side: ChessSide, box: Box): Int {
        if ((side == ChessSide.WHITE && box.number == 2) || (side == ChessSide.BLACK && box.number == 7)) {
            return 2
        }

        return 1
    }

    private fun getPawnMaxForSide(side: ChessSide, max: Int, posNumber: Int, targetNumber: Int): Boolean {
        if (side == ChessSide.WHITE) {
            return targetNumber <= posNumber + max && targetNumber > posNumber
        }

        return targetNumber >= posNumber + max && targetNumber < posNumber
    }

    override fun cancelLastMove() {
        if (playsHistoric.isEmpty()) return
        movePawn(playsHistoric.last().to, playsHistoric.last().from)
        playsHistoric.remove(playsHistoric.last())
        switchSidePlaying()
    }

    override fun switchSidePlaying() {
        this.sidePlaying = if (this.sidePlaying == ChessSide.WHITE) ChessSide.BLACK else ChessSide.WHITE
    }

    override fun isPawnUnderAttack(pawn: Pawn): Boolean {
        return getAllMovePossibilities(getOppositeSide(pawn.side)).contains(getBox(pawn))
    }

    override fun printChessBoard() {
        println("\u001Bc")
        val t = TermColors()
        println(t.green("\n     A    B    C    D    E    F    G    H \n"))
        boxes.forEach {
            if (it.key.letter == 'A') print(t.green("${it.key.number}  "))
            if (it.value == null)
                print("|_ _|")
            else {
                if (it.value != null && it.value!!.side == ChessSide.WHITE)
                    print("|_${t.white(it.value!!.type.name[0].toString())}_|")
                else
                    print("|_${t.red(it.value!!.type.name[0].toString())}_|")
            }
            if (it.key.letter == 'H') println(t.green("  ${it.key.number}"))
        }
        println(t.green("\n     A    B    C    D    E    F    G    H \n"))
    }

    override fun printHistorical() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gradePawnPosition(pawn: Pawn) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}