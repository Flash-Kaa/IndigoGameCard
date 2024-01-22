package indigo

fun main() {
	println("Indigo Card Game")
	val playFirst: Boolean

	while (true) {
		println("Play first?")
		when (readlnOrNull()) {
			"yes" -> {
				playFirst = true
				break
			}

			"no" -> {
				playFirst = false
				break
			}
		}
	}

	val table = Table(playFirst)
	println("Initial cards on the table: " + table.getCardsOnTable().joinToString(" "))

	do {
		val haveMove = table.move()
	} while (haveMove)

	println("Game Over")
}