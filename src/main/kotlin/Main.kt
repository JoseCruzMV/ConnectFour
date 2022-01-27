package connectfour

class Player (var name: String = "",
              var symbol: String = "",
              var score: Int = 0)

class Game (var numberOfGames: Int = 1,
            var columns: Int = 7,
            var rows: Int = 6)


const val firstPlayerSymbol = "o"
const val secondPlayerSymbol = "*"
const val defaultCellValue = " "
const val endGameKeyWord = "end"
const val successfulPlayersTurn = -1
const val noSpaceInColumn = -2
const val noSpaceInBoard = -3
const val playerWins = -4
const val finishGame = 0
const val playing = 1
fun main(args: Array<String>) {
    val player1 = Player()
    val player2 = Player()
    val game = Game()

    println("Connect Four")
    setPlayersName(player1, player2)
    setGameSettings(game)
    println("${player1.name} VS ${player2.name}\n" +
            "${game.rows} X ${game.columns} board")
    println( if (game.numberOfGames == 1) "Single game" else "Total ${game.numberOfGames} games")

    //Creation of the game field
    var gameBoard = MutableList(game.rows) {
        MutableList(game.columns) { defaultCellValue }
    }

    if (game.numberOfGames == 1) {
        playGame(gameBoard = gameBoard, player1 = player1, player2 = player2)
    } else {
        //multiple game
        var firstPlayerToMove = player1
        for (i in 1..game.numberOfGames) {
            //reset the gameBoard
            gameBoard = MutableList(game.rows) {
                MutableList(game.columns) { defaultCellValue }
            }
            println("Game #$i")
            val endGame = playGame(
                gameBoard = gameBoard,
                player1 = player1,
                player2 = player2,
                firstPlayer = firstPlayerToMove
            )
            //if key word "end" is used it breaks the game
            if (endGame == finishGame) break
            println("Score")
            println("${player1.name}: ${player1.score} ${player2.name}: ${player2.score}")
            firstPlayerToMove = if (firstPlayerToMove == player1) player2 else player1
        }
    }

    println("Game over!")
}

fun setPlayersName(player1: Player, player2: Player) {
    //ask for players names
    println("First player's name:")
    player1.name = readLine()!!
    player1.symbol = firstPlayerSymbol
    println("Second player's name:")
    player2.name = readLine()!!
    player2.symbol = secondPlayerSymbol
}

fun setGameSettings(game: Game) {
    var wrongDimension = true
    var boardDimensions: String
    val range = 5..9
    val regex = Regex("\\s*?\\d\\d?\\s*?x\\s*?\\d\\d?\\s*?")

    //ask for boards dimensions
    while (wrongDimension) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        boardDimensions = readLine()!!
        when {
            //boardDimensions is empty, takes default values
            boardDimensions.isEmpty() -> {
                wrongDimension = false
            }
            //boardDimensions has a correct format
            boardDimensions.lowercase().matches(regex) -> {
                val auxDimensions = boardDimensions.lowercase()
                    .replace("\\s".toRegex(), "")
                    .split("x").map { it.toInt() }

                val auxRows = auxDimensions[0]
                val auxColumns = auxDimensions[1]
                //rows between 5 and 9
                if (auxRows in range) {
                    //columns between 5 and 9
                    if (auxColumns in range) {
                        wrongDimension = false
                        //Set board dimensions in game object
                        game.rows = auxRows
                        game.columns = auxColumns
                    } else {
                        println("Board columns should be from 5 to 9")
                    }
                } else {
                    println("Board rows should be from 5 to 9")
                }
            }
            //wrong format or other inputs
            else -> {
                println("Invalid Input")
            }
        }
    }

    //ask for number of games to play
    do {
        var wrongInput = true
        println("Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:")
        val input = readLine()!!
        when {
            //uses the default value in game object
            input.isEmpty() -> wrongInput = false
            //if is a number
            isNumber(input) && input.toInt() > 0 -> {
                game.numberOfGames = input.toInt()
                wrongInput = false
            }
            else -> println("Invalid input")
        }
    } while (wrongInput)
}

fun printGameBoard (gameBoard: MutableList<MutableList<String>>) {
    //Print rows number
    for (i in 1..gameBoard[0].size) { print(" $i") }
    println()
    //Prints gameBoard
    for (rows in gameBoard){
        for (columns in rows) {
            print("║$columns")
        }
        print("║\n") //prints last ║ of the column and new line
    }
    //prints the last row to close game board
    println("╚"+"═╩".repeat(gameBoard[0].size - 1)+"═╝")
}

fun playGame(
    gameBoard: MutableList<MutableList<String>>,
    player1: Player,
    player2: Player,
    firstPlayer: Player = player1
) : Int
{
    var keepPlaying = true
    var actualTurn = firstPlayer
    var endGame = playing

    printGameBoard(gameBoard)

    while (keepPlaying) {
        println("${actualTurn.name}'s turn")
        val input = readLine()!!
        when {
            input == endGameKeyWord -> {
                keepPlaying = false
                endGame = finishGame
            }
            !isNumber(input) ->  println("Incorrect column number") // input is not a number
            //input is not in columns range
            input.toInt() !in 1..gameBoard[0].size -> println("The column number is out of range (1 - ${gameBoard[0].size})")
            //everything is ok
            else -> {
                val playersMove = playerMove(
                    gameBoard = gameBoard,
                    column = input.toInt() - 1, //because is an array
                    pattern = actualTurn.symbol
                )
                when (playersMove) {
                    successfulPlayersTurn -> {
                        printGameBoard(gameBoard = gameBoard)
                        actualTurn = if (actualTurn == player1) player2 else player1
                    }
                    noSpaceInColumn -> {
                        println("Column $input is full")
                    }
                    noSpaceInBoard -> {
                        printGameBoard(gameBoard = gameBoard)
                        println("It is a draw")
                        player1.score += 1
                        player2.score += 1
                        keepPlaying = false
                    }
                    playerWins -> {
                        printGameBoard(gameBoard = gameBoard)
                        println("Player ${actualTurn.name} won")
                        actualTurn.score += 2
                        keepPlaying = false
                    }
                }
            }
        }
    }
    return endGame
}

fun playerMove(gameBoard: MutableList<MutableList<String>>,
               column: Int,
               pattern: String
): Int
{
    var result = 0
    for (i in gameBoard.size - 1 downTo 0) {
        if (gameBoard[i][column] == defaultCellValue) {
            gameBoard[i][column] = pattern

            var boardIsFull = false
            //it will check if the board is completely full
            for (row in gameBoard) {
                // if there is a space in any column it will be true
                if (row.contains(defaultCellValue)) break else boardIsFull = true
            }
            //After saving the play, it will check if actual player wins
            result =
                if (checkWinConditions(gameBoard =  gameBoard,pattern = pattern))
                    playerWins //actual player already wins
                else if (boardIsFull)
                    noSpaceInBoard // if there is no more space
                else
                    successfulPlayersTurn //just a normal play
            break
        } else if ( i == 0 ){
            result = noSpaceInColumn
            break
        }
    }
    return result
}

fun checkWinConditions(gameBoard: MutableList<MutableList<String>>, pattern: String): Boolean {
    //win condition: if it finds four characters of the same pattern
    val winCondition = if (pattern == "o")
        Regex(".*?${pattern.repeat(4)}.*?")
    else
        Regex(".*?\\*\\*\\*\\*.*?")
    //Looking for it horizontally
    for (row in gameBoard) {
        if (row.joinToString("").matches(winCondition)) return true else continue
    }

    //Looking for it vertically
    for (i in 0 until gameBoard[0].size) { //number of columns
        val auxM = mutableListOf<String>()
        for (j in gameBoard.indices) { //number of rows
            auxM.add(gameBoard[j][i])
        }
        if (auxM.joinToString("").matches(winCondition)) return true else continue
    }

    //Looking for it diagonally
    //left to right
    val aux = gameBoard.size + gameBoard[0].size - 2
    for (i in 0..aux) {
        val auxM = mutableListOf<String>()
        for (j in 0..i) {
            val k = i - j
            if (k < gameBoard.size && j < gameBoard[0].size) auxM.add(gameBoard[k][j])
        }
        if (auxM.joinToString("").matches(winCondition)) return true else continue
    }
    //right to left
    for (i in 0..aux) {
        val auxM = mutableListOf<String>()
        var k = i
        for (j in gameBoard[0].size - 1 downTo 0){
            if (k < gameBoard.size && j < gameBoard[0].size && k >= 0 ) auxM.add(gameBoard[k][j])
            k--
        }
        if (auxM.joinToString("").matches(winCondition)) return true else continue
    }

    return false
}

fun isNumber (string: String) = string.matches("\\d*".toRegex())