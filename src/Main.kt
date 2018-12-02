fun main(args: Array<String>) {
    val chess = ChessBoard()
    chess.printChessBoard()
    var kingStatus: KingStatus? = null
    do {
        kingStatus = playerMove(chess, kingStatus = kingStatus)
    } while (kingStatus != KingStatus.MAT)
}

fun getPlayerInstruction(previous: Instruction? = null): Pair<Instruction, Box?> {
    val instruction = readLine()?.toUpperCase() ?: ""
    return when (previous) {
        Instruction.CASTLING -> {
            if (instruction == "0") return Pair(Instruction.CANCEL, null)
            if (instruction == "1") return Pair(Instruction.CASTLING_SMALL, null)
            if (instruction == "2") return Pair(Instruction.CASTLING_BIG, null)
            Pair(Instruction.UNKNOWN, null)
        }
        null -> {
            if (instruction == "C") return Pair(Instruction.CANCEL, null)
            if (instruction == "Y") return Pair(Instruction.YES, null)
            if (instruction == "N") return Pair(Instruction.NO, null)
            if (instruction.length == 2) {
                val box = entryToBox(instruction) ?: return Pair(Instruction.UNKNOWN, null)
                return Pair(Instruction.MOVE, box)
            }
            if (instruction == "CASTLING") return Pair(Instruction.CASTLING, null)
            Pair(Instruction.UNKNOWN, null)
        }
        else -> Pair(Instruction.UNKNOWN, null)
    }
}

fun entryToBox(string: String): Box? {
    return try {
        Box.valueOf(string)
    } catch (e: IllegalArgumentException) {
        return null
    }
}

fun playerMove(chessboard: ChessBoard, kingStatus: KingStatus?): KingStatus {
    println(chessboard.sidePlaying)
    if (kingStatus != null && kingStatus == KingStatus.CHECKED) {
        println("-------CHECK-------")
    }
    //(1) => ask player pawn to move box
    print("Pawn in : ")
    val instruction = getPlayerInstruction()

    return when (instruction.first) {
        Instruction.UNKNOWN -> playerMove(chessboard, kingStatus)
        Instruction.MOVE -> {
            val from = instruction.second ?: return playerMove(chessboard, kingStatus)
            if (chessboard.getPawn(from)?.side != chessboard.sidePlaying) return playerMove(chessboard, kingStatus)
            val possibilities = chessboard.getMovesAvailable(from)
            if (possibilities.isEmpty()) {
                playerMove(chessboard, kingStatus)
            }
            println(possibilities)
            var targetInstruction: Pair<Instruction, Box?>
            var to: Box?
            do {
                print("to: ")
                targetInstruction = getPlayerInstruction()
                to = targetInstruction.second
                if (targetInstruction.first == Instruction.CANCEL) return playerMove(chessboard, kingStatus)
            } while ((targetInstruction.first != Instruction.MOVE && to == null))
            if (!chessboard.move(from, to!!)) {
                return playerMove(chessboard, kingStatus)
            }

            chessboard.printChessBoard()
            chessboard.getKingStatus()
        }
        Instruction.CANCEL -> {
            chessboard.cancelLastMove()
            chessboard.printChessBoard()
            playerMove(chessboard, chessboard.getKingStatus())
        }
        Instruction.CASTLING -> {
            val castling = chessboard.getCastling()
            when (castling) {
                RookType.NONE -> {
                    println("You can't perform castling")
                    return playerMove(chessboard, kingStatus)
                }
                RookType.SMALL -> {
                    var answer: Instruction?
                    do {
                        print("small castling (y/n) : ")
                        answer = getPlayerInstruction().first
                    } while (answer != Instruction.YES && answer != Instruction.NO)
                    if (answer == Instruction.YES) {
                        chessboard.castling(RookType.SMALL)
                        chessboard.printChessBoard()
                        return chessboard.getKingStatus()
                    }

                    playerMove(chessboard, kingStatus)
                }
                RookType.BIG -> {
                    var answer: Instruction?
                    do {
                        print("big castling (y/n) : ")
                        answer = getPlayerInstruction().first
                    } while (answer != Instruction.YES && answer != Instruction.NO)
                    if (answer == Instruction.YES) {
                        chessboard.castling(RookType.BIG)
                        chessboard.printChessBoard()
                        return chessboard.getKingStatus()
                    }

                    playerMove(chessboard, kingStatus)
                }
                RookType.ALL -> {
                    println("Which castling do you want to perform : ")

                    var answer: Instruction?
                    do {
                        println("0 -> NONE")
                        println("1 -> SMALL")
                        println("2 -> BIG")
                        answer = getPlayerInstruction(Instruction.CASTLING).first
                    } while (answer != Instruction.CASTLING_BIG && answer != Instruction.CASTLING_SMALL && answer != Instruction.CANCEL)
                    if (answer == Instruction.CASTLING_BIG) {
                        chessboard.castling(RookType.BIG)
                        chessboard.printChessBoard()
                        return chessboard.getKingStatus()
                    } else if (answer == Instruction.CASTLING_SMALL) {
                        chessboard.castling(RookType.SMALL)
                        chessboard.printChessBoard()
                        return chessboard.getKingStatus()
                    }

                    playerMove(chessboard, kingStatus)
                }
            }
        }
        else -> playerMove(chessboard, kingStatus)
    }
}