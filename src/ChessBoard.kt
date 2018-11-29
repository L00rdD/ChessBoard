class ChessBoard: IChessBoard {
    companion object {
        val CHESSBOARD_MAX_NUMBER = 8
        val CHESSBOARD_MIN_NUMBER = 1
        val CHESSBOARD_MAX_LETTER = 'H'
        val CHESSBOARD_MIN_LETTER = 'A'
    }

    override val boxes = arrayOf(
            //Black side
            Pair(Pawn(PawnType.ROOK, ChessSide.BLACK), Box.A8),
            Pair(Pawn(PawnType.KNIGHT, ChessSide.BLACK), Box.B8),
            Pair(Pawn(PawnType.BISHOP, ChessSide.BLACK), Box.C8),
            Pair(Pawn(PawnType.QUEEN, ChessSide.BLACK), Box.D8),
            //Pair(Pawn(PawnType.KING, ChessSide.BLACK), Box.E8),
            Pair(null, Box.E8),
            Pair(Pawn(PawnType.BISHOP, ChessSide.BLACK), Box.F8),
            Pair(Pawn(PawnType.KNIGHT, ChessSide.BLACK), Box.G8),
            Pair(Pawn(PawnType.ROOK, ChessSide.BLACK), Box.H8),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.A7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.B7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.C7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.D7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.E7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.F7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.G7),
            Pair(Pawn(PawnType.PAWN, ChessSide.BLACK), Box.H7),

            //Empty boxes
            Pair(null, Box.A6),
            Pair(null, Box.B6),
            Pair(null, Box.C6),
            Pair(null, Box.D6),
            Pair( null, Box.E6),
            Pair(null, Box.F6),
            Pair(null, Box.G6),
            Pair(null, Box.H6),
            Pair(Pawn(PawnType.ROOK, ChessSide.WHITE), Box.A5),
            Pair(null, Box.B5),
            Pair(null, Box.C5),
            Pair(null, Box.D5),
            Pair(null, Box.E5),
            Pair(null, Box.F5),
            Pair(null, Box.G5),
            Pair(null, Box.H5),
            Pair(null, Box.A4),
            Pair(null, Box.B4),
            Pair(null, Box.C4),
            Pair(null, Box.D4),
            Pair(null, Box.E4),
            Pair(null, Box.F4),
            Pair(Pawn(PawnType.KING, ChessSide.BLACK), Box.G4),
            Pair(null, Box.H4),
            Pair(null, Box.A3),
            Pair(null, Box.B3),
            Pair(null, Box.C3),
            Pair(null, Box.D3),
            Pair(null, Box.E3),
            Pair(null, Box.F3),
            Pair(null, Box.G3),
            Pair(null, Box.H3),

            //White side
            //Pair(Pawn(PawnType.ROOK, ChessSide.WHITE), Box.A1),
            Pair(null, Box.A1),
            Pair(Pawn(PawnType.KNIGHT, ChessSide.WHITE),Box.B1),
            Pair(Pawn(PawnType.BISHOP, ChessSide.WHITE), Box.C1),
            Pair(Pawn(PawnType.QUEEN, ChessSide.WHITE), Box.D1),
            Pair(Pawn(PawnType.KING, ChessSide.WHITE), Box.E1),
            Pair(Pawn(PawnType.BISHOP, ChessSide.WHITE), Box.F1),
            Pair(Pawn(PawnType.KNIGHT, ChessSide.WHITE), Box.G1),
            Pair(Pawn(PawnType.ROOK, ChessSide.WHITE), Box.H1),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.A2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.B2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.C2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.D2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.E2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.F2),
            //Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.G2),
            Pair(null, Box.G2),
            Pair(Pawn(PawnType.PAWN, ChessSide.WHITE), Box.H2)
            )
    override var sidePlaying = ChessSide.WHITE
    override var playCount = 0
    override var playsHistoric: ArrayList<Move>? = null
    override var rooksAvailable = listOf(ChessSide.WHITE, ChessSide.BLACK)

    private var sidesAvailableMoves: MutableMap<ChessSide, List<Box?>>? = mutableMapOf(
            ChessSide.WHITE to getAllMovePossibilities(ChessSide.WHITE),
            ChessSide.BLACK to getAllMovePossibilities(ChessSide.BLACK))

    override fun getSideHistorical(side: ChessSide): ArrayList<Move>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun move(from: Box, to: Box) {
        // Check if king is checked

        //Reload move possibilities
    }

    private fun getKingStatus(): KingStatus {
        val kingPairIndex = boxes.indexOfFirst { it.first != null && it.first!!.type == PawnType.KING &&  it.first!!.side  == sidePlaying }
        val king = boxes[kingPairIndex].first

        if (king == null ||!getAllMovePossibilities(getOppositeSide(king.side)).contains(boxes[kingPairIndex].second)) {
            return KingStatus.FREE
        }

        val possibilities = getKingMovePossibilities(king, boxes[kingPairIndex].second)
        return if(possibilities == null || possibilities.isEmpty()) KingStatus.MAT else KingStatus.CHECKED
    }

    fun getPawn(box: Box): Pawn? {
        val index = this.boxes.indexOfFirst { it.second == box }
        return  if (index != - 1) boxes[index].first else null
    }

    private fun getBox(pawn: Pawn): Box {
        return boxes[this.boxes.indexOfFirst { it.first === pawn }].second
    }

    private fun isLegalPawnLinearMove(side: ChessSide, target: Int, current: Int, maxNumber: Int): Boolean {
        return if (side == ChessSide.WHITE)
            target in (current + 1)..maxNumber
        else
            target in maxNumber..(current - 1)
    }

    public fun getMovePossibilities(pawn: Pawn): List<Box>? {
        return when (pawn.type) {
            PawnType.PAWN -> getPawnMovePossibilities(pawn ,getBox(pawn))
            PawnType.KNIGHT -> getKnightMovePossibilities(pawn, getBox(pawn))
            PawnType.QUEEN -> getQueenMovePossibilities(pawn, getBox(pawn))
            PawnType.ROOK -> getRookMovePossibilities(pawn, getBox(pawn))
            PawnType.BISHOP -> getBishopMovePossibilities(pawn, getBox(pawn))
            PawnType.KING -> getKingMovePossibilities(pawn, getBox(pawn))
        }
    }

    private fun getOppositeSide(side: ChessSide): ChessSide {
        return if (side == ChessSide.WHITE) ChessSide.BLACK else ChessSide.WHITE
    }

    fun getAllMovePossibilities(side: ChessSide) : List<Box?> {
        val possibilities = mutableListOf<Box>()
        val refPawns = boxes
                .filter { it.first != null && it.first!!.side == side }

        refPawns.forEach {
            val moves = getMovePossibilities(it.first!!)
            if (moves != null && moves.isNotEmpty())
                possibilities.addAll(moves.toMutableList())
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

        val advPossibilities = sidesAvailableMoves?.get(getOppositeSide(pawn.side)) ?: emptyList()
        println(advPossibilities)

        sides.removeIf {
            val p = getPawn(it)
            (p != null && p.side == pawn.side) || advPossibilities.contains(it)
        }

        return sides.toList()
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
        return rook.union(bishop).toList()
    }

    private fun getBishopMovePossibilities(pawn: Pawn, box: Box): List<Box>? {
        val moves = Box.values().asIterable()
        val diagonal =  moves.filter {
            it != box && ((box.letter - it.letter == box.number - it.number)
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
                    (maxDownRight != null && it.number < maxDownRight.number && it.letter > maxDownRight.letter)
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

        return lines
    }

    private fun getPawnMovePossibilities(pawn: Pawn, pos: Box): List<Box>? {
        val moves = Box.values().asIterable()
        val boxLinearMove = if (playsHistoric?.find { it.pawn === pawn } == null) 2 else 1
        val boxDiagonalMove = 1
        val boxLinearMaxNumber = if (pawn.side == ChessSide.WHITE) pos.number + boxLinearMove else pos.number - boxLinearMove
        val boxDiagonalMaxNumber = if (pawn.side == ChessSide.WHITE) pos.number + boxDiagonalMove else pos.number - boxDiagonalMove
        return moves.filter {
            (it.letter == pos.letter && isLegalPawnLinearMove(pawn.side, it.number, pos.number, boxLinearMaxNumber)  && getPawn(it) == null) ||
                    (it.letter == pos.letter.inc() && it.number ==   boxDiagonalMaxNumber && getPawn(it) != null) ||
                    (it.letter == pos.letter.dec() && it.number == boxDiagonalMaxNumber && getPawn(it) != null)
        }
    }

    override fun cancelLastMove() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchSidePlaying() {
        this.sidePlaying = if (this.sidePlaying == ChessSide.WHITE) ChessSide.BLACK else ChessSide.WHITE
    }

    override fun isKingChecked(side: ChessSide) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPawnUnderAttack(pawn: Pawn) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun printChessBoard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun printHistorical() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gradePawnPosition(pawn: Pawn) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}