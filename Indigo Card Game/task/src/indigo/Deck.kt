package indigo

import indigo.card.Card
import indigo.card.Symbol
import indigo.card.Value

class Deck {
	private val deck = mutableListOf<Card>()

	init {
		reset()
		shuffle()
	}

	public fun reset() {
		deck.clear()

		for (symbol in Symbol.values()) {
			for (value in Value.values()) {
				deck.add(Card(value, symbol))
			}
		}
	}

	public fun shuffle() {
		deck.shuffle()
	}

	public fun get(count: Int): List<Card> {
		if (count <= 0 || count > deck.size) {
			throw IllegalArgumentException()
		}

		val res = deck.takeLast(count)
		deck.removeAll(res)
		return res
	}

	public fun countCards(): Int {
		return deck.size
	}

	override fun toString(): String {
		return deck.joinToString(" ")
	}
}