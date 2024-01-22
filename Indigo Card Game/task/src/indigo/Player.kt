package indigo

import indigo.card.Card

class Player {
	private val hand = mutableListOf<Card>()
	private val drop = mutableListOf<Card>()

	public fun getCards(): List<Card> {
		return hand.toList()
	}

	public fun setCards(cards: List<Card>) {
		hand.addAll(cards)
	}

	public fun removeCard(index: Int): Card {
		return hand.removeAt(index)
	}

	public fun getDrop(): List<Card> {
		return drop.toList()
	}

	public fun setDrop(add: List<Card>) {
		drop.addAll(add)
	}

	public fun setDrop(add: Card) {
		drop.add(add)
	}
}
