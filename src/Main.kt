fun main(args: Array<String>) {
    val chess = ChessBoard()
    chess.printChessBoard()
    var kingStatus: KingStatus? = null
    do {
        kingStatus = playerMove(chess, kingStatus = kingStatus)
    } while (kingStatus != KingStatus.MAT)
}

fun playerMove(chessboard: ChessBoard, kingStatus: KingStatus?): KingStatus {
    if (kingStatus != null && kingStatus == KingStatus.CHECKED) {
        println("-------CHECK-------")
    }
    //(1) => ask player pawn to move box
    val from = fromMoveEntry(chessboard)
    if (from == null) {
        chessboard.cancelLastMove()
        chessboard.printChessBoard()
        return playerMove(chessboard, chessboard.getKingStatus())
    }
    // (2) => Display move possibilities for this box
    val possibilities = chessboard.getMovesAvailable(from)
    if (possibilities.isEmpty()) {
        playerMove(chessboard, kingStatus)
    }
    println(possibilities)
    // (3) => enter to change pawn -> (1) || move to box
    var to: Box?
    do {
        to = toBoxTarget(chessboard, from)
    } while (to == null)
    // (4) => move pawn
    if (!chessboard.move(from, to)) {
        return playerMove(chessboard, kingStatus)
    }

    chessboard.printChessBoard()
    return chessboard.getKingStatus()
}

fun toBoxTarget(chessboard: ChessBoard, from: Box, kingStatus: KingStatus? = null): Box? {
    return toMoveEntry()
}

fun fromMoveEntry(chessboard: ChessBoard): Box? {
    var box: Box?
    var pawn: Pawn? = null
    do {
        print("Pawn in : ")
        val entry = readLine() ?: ""
        if (entry == "c") {
            return null
        }
        box = getBox(entry)
        if (box != null) {
            pawn = chessboard.getPawn(box)
        }
    } while (box == null || pawn == null || pawn.side != chessboard.sidePlaying)

    return box
}

fun toMoveEntry(): Box? {
    val box: Box?

    print("to : ")
    val response = readLine()

    if (response == "")
        return null

    return getBox(response ?: "" ) ?: toMoveEntry()
}

fun getBox(entry: String): Box? {
    val number = entry[1].toString().toIntOrNull()
    if (entry.length != 2 || number == null || ((entry[0] < 'A' || entry[0] > 'H') && (number < 1 || number > 8))) {
        return null
    }

    return Box.values().firstOrNull {
        it.letter == entry[0] && it.number == number
    }
}