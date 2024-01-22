package indigo.card

enum class Symbol {
	DIAMONDS, HEARTS, SPADES, CLUBS;

	override fun toString(): String {
		return when (this) {
			DIAMONDS -> "♦"
			HEARTS -> "♥"
			CLUBS -> "♣"
			SPADES -> "♠"
		}
	}
}