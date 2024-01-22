package indigo.card

data class Card(val value: Value, val symbol: Symbol) {
	override fun toString(): String {
		return value.toString() + symbol.toString()
	}
}