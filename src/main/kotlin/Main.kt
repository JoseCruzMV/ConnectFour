package connectfour

const val defaultRows = 6
const val defaultColumns = 7
const val endGameKeyWord = "end"
fun main() {
    var boardDimensions= ""
    val range = 5..9
    val regex = Regex("\\s*?\\d\\d?\\s*?x\\s*?\\d\\d?\\s*?")
    var rows = defaultRows
    var columns = defaultColumns

    println("Connect Four")
    println("First player's name:")
    val firstPlayer: String = readLine()!!
    println("Second player's name:")
    val secondPlayer: String = readLine()!!

    var wrongDimension = true
    while (wrongDimension) {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        boardDimensions = readLine()!!

        //boardDimensions is not empty
        if (boardDimensions.isNotEmpty()) {
            //boardDimensions matches with format no matter spaces
            if (boardDimensions.lowercase().matches(regex)) {
                //Dividing boardDimensions in rows and columns using x character
                //replace("\\s".toRegex(), "") to remove all whiteSpaces no matter if they are space or tab
                //map to cast them to int type
                val auxDimensions = boardDimensions.lowercase()
                    .replace("\\s".toRegex(), "")
                    .split("x").map { it.toInt() }
                rows = auxDimensions[0]
                columns = auxDimensions[1]

                //Checking that rows then columns are in range
                if (rows in range) {
                    if (columns in range) {
                        wrongDimension = false
                        boardDimensions = "$rows X $columns"
                    } else {
                        println("Board columns should be from 5 to 9")
                    }
                } else {
                    println("Board rows should be from 5 to 9")
                }
            } else {
                println("Invalid Input")
            }
        } else {
            boardDimensions = "$defaultRows X $defaultColumns"
            wrongDimension = false
        }
    }
    //Output message
    println("$firstPlayer VS $secondPlayer\n$boardDimensions board")

    val gameMatrix = MutableList(rows) {
        MutableList(columns) {" "}
    }
    //Printing game's board
    printGameBoard(gameBoard = gameMatrix)

    fun playerMove (column: Int, pattern: String) {
        for (i in gameMatrix.size - 1 downTo 0) {
            if (gameMatrix[i][column] ==  " ") {
                gameMatrix[i][column] = pattern
                break
            } else if (i == 0 ) {
                break
            }
        }
    }

    //playing game with turns
    var keepPlaying = true
    var actualTurn = firstPlayer
    while (keepPlaying) {
        println("$actualTurn's turn")
        val input = readLine()!!

        if (input == endGameKeyWord) {
            keepPlaying = false
            println("Game over!")
        } else {
            playerMove(
                column = input.toInt() - 1,
                pattern = if(actualTurn == firstPlayer) "o" else "*"
            )
            printGameBoard(gameBoard = gameMatrix)
            actualTurn = if (actualTurn == firstPlayer) secondPlayer else firstPlayer
        }
    }
}



//Get the auxBoard variable where auxBoard[0] = rows and auxBoard[1] = columns
fun printGameBoard(gameBoard: MutableList<MutableList<String>>) {
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