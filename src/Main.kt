fun main(args: Array<String>) {
    val chess = ChessBoard()
    val pawn = chess.getPawn(Box.F2) ?: return
    println(chess.getMovePossibilities(pawn))
    //println(chess.getAllMovePossibilities(ChessSide.WHITE))
}