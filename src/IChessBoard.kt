interface IChessBoard {
    val boxes: MutableMap<Box, Pawn?>
    var sidePlaying: ChessSide
    var playCount: Int
    var playsHistoric: ArrayList<Move>?
    var rooksAvailable: List<ChessSide>

    fun getSideHistorical(side: ChessSide): ArrayList<Move>?
    fun move(from: Box, to: Box)
    fun cancelLastMove()
    fun switchSidePlaying()
    fun isKingChecked(side: ChessSide)
    fun isPawnUnderAttack(pawn: Pawn)
    fun printChessBoard()
    fun printHistorical()
    fun gradePawnPosition(pawn: Pawn)
}
