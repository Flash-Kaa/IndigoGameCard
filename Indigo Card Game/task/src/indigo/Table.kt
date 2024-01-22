package indigo

import indigo.card.Card
import indigo.card.Symbol
import indigo.card.Value

class Table(private val playerFirst: Boolean) {
	private val deck = Deck()
	private val cardsOnTable = mutableListOf<Card>()
	private var step = if (playerFirst) 1 else 0
	private val computer = Player()
	private val player = Player()
	private var lastWinPlayer: Player? = null

	private val listOfTops = listOf(Value.TEN, Value.JACK, Value.QUEEN, Value.KING, Value.ACE)

	init {
		cardsOnTable.addAll(deck.get(4))
		player.setCards(deck.get(6))
		computer.setCards(deck.get(6))
	}

	fun getCardsOnTable(): List<Card> {
		return cardsOnTable.toList()
	}

	fun move(): Boolean {
		if (cardsOnTable.size > 0) {
			println("${cardsOnTable.size} cards on the table, and the top card is ${cardsOnTable.last()}")
		} else {
			println("No cards on the table")
		}

		if (player.getCards().isEmpty() && computer.getCards().isEmpty()) {
			if (lastWinPlayer != null) {
				lastWinPlayer?.setDrop(cardsOnTable)
			} else if (playerFirst) {
				computer.setDrop(cardsOnTable)
			} else {
				player.setDrop(cardsOnTable)
			}

			cardsOnTable.clear()
			writeScore()

			return false
		}

		if (step % 2 == 1 && playerMove()) {
			return false
		} else if (step % 2 == 0) {
			computerMove()
		}

		step++

		return true
	}

	// return true if game over
	private fun playerMove(): Boolean {
		val sb = StringBuilder("Cards in hand: ")
		val count = player.getCards().size
		var ind = 1

		player.getCards().forEach {
			sb.append(" ${ind++})").append(it)
		}
		println(sb)

		while (true) {
			println("Choose a card to play (1-$count):")
			val input = readlnOrNull()

			if (input == "exit") {
				return true
			}

			val num: Int
			if (input != null && input.all { it.isDigit() }) {
				num = input.toInt()
				if (num in 1..count) {
					val card = player.removeCard(num - 1)
					cardsOnTable.add(card)

					if (count == 1 && deck.countCards() > 0) {
						player.setCards(deck.get(6))
					}

					checkWin("Player", player, card)

					return false
				}
			}
		}
	}

	private fun computerMove() {
		println(computer.getCards().joinToString(" "))

		val candidates = getCandidates()
		val indexToRemove: Int

		if (computer.getCards().size == 1) {
			indexToRemove = 0

			if (deck.countCards() > 0) {
				computer.setCards(deck.get(6))
			}
		} else if (candidates.size == 1) {
			indexToRemove = computer.getCards().indexOf(candidates.first())
		} else if (cardsOnTable.size == 0 || candidates.isEmpty()) {
			val maxSuit = getMaxSymbol(computer.getCards())
			val maxValue = getMaxValue(computer.getCards())

			indexToRemove = if (maxSuit.value >= 2) {
				val toRemove = computer.getCards().first { it.symbol == maxSuit.key }
				computer.getCards().indexOf(toRemove)
			} else if (maxValue.value >= 2) {
				val toRemove = computer.getCards().first { it.value == maxValue.key }
				computer.getCards().indexOf(toRemove)
			} else {
				0
			}
		} else {
			indexToRemove = computer.getCards().indexOf(toRemoveFromCandidates(candidates))
		}

		val card = computer.removeCard(indexToRemove)
		cardsOnTable.add(card)

		println("Computer plays $card")
		checkWin("Computer", computer, card)
	}

	private fun checkWin(playerName: String, player: Player, card: Card) {
		if (cardsOnTable.size == 1) {
			return
		}

		val topCard = cardsOnTable[cardsOnTable.size - 2]
		if (card.symbol != topCard.symbol && card.value != topCard.value) {
			return
		}

		player.setDrop(cardsOnTable)
		cardsOnTable.clear()
		writeScore(playerName)
		lastWinPlayer = player
	}

	private fun calculateCardDropScore(player: Player): Int {
		return player.getDrop().sumOf {
			0 + if (it.value in listOfTops) 1 else 0
		}
	}

	private fun getCandidates(): List<Card> {
		if (cardsOnTable.size == 0) {
			return emptyList()
		}

		val topCard = cardsOnTable[cardsOnTable.size - 1]
		val res = mutableListOf<Card>()

		computer.getCards().forEach { card ->
			if (card.symbol == topCard.symbol || card.value == topCard.value) {
				res.add(card)
			}
		}

		return res
	}

	private fun toRemoveFromCandidates(candidates: List<Card>): Card {
		val maxSuit = getMaxSymbol(candidates)
		val maxValue = getMaxValue(candidates)

		return if (maxSuit.value >= 2) {
			computer.getCards().first { it.symbol == maxSuit.key }
		} else if (maxValue.value >= 2) {
			computer.getCards().first { it.value == maxValue.key }
		} else {
			candidates.first()
		}
	}

	private fun getMaxSymbol(candidates: List<Card>): Map.Entry<Symbol, Int> {
		val suits = mutableMapOf<Symbol, Int>()
		candidates.forEach {
			suits[it.symbol] = suits.getOrDefault(it.symbol, 0) + 1
		}

		return suits.entries.maxByOrNull { it.value }!!
	}

	private fun getMaxValue(cards: List<Card>): Map.Entry<Value, Int> {
		val suits = mutableMapOf<Value, Int>()
		cards.forEach {
			suits[it.value] = suits.getOrDefault(it.value, 0) + 1
		}

		return suits.entries.maxByOrNull { it.value }!!
	}

	private fun writeScore(playerName: String? = null) {

		var playerScore = calculateCardDropScore(player)
		var computerScore = calculateCardDropScore(computer)

		if(playerName == null) {
			val compare = this.player.getDrop().size.compareTo(computer.getDrop().size)
			if (compare > 0 || compare == 0 && playerFirst) {
				playerScore += 3
			} else {
				computerScore += 3
			}
		} else {
			println("$playerName wins cards")
		}

		println("Score: Player $playerScore - Computer $computerScore")
		println("Cards: Player ${this.player.getDrop().size} - Computer ${computer.getDrop().size}")
	}
}