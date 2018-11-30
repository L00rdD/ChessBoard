interface IChessBoard {
    val boxes: MutableMap<Box, Pawn?>
    var sidePlaying: ChessSide
    var playCount: Int
    var playsHistoric: ArrayList<Move>
    var rooksAvailable: List<ChessSide>

    fun getSideHistorical(side: ChessSide): ArrayList<Move>?
    fun move(from: Box, to: Box): Boolean
    fun cancelLastMove()
    fun switchSidePlaying()
    fun getKingStatus(side: ChessSide): KingStatus
    fun isPawnUnderAttack(pawn: Pawn): Boolean
    fun printChessBoard()
    fun printHistorical()
    fun gradePawnPosition(pawn: Pawn)
}
