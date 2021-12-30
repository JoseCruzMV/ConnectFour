package connectfour

const val defaultBoardDimension = "6 X 7"
const val defaultRows = "6"
const val defaultColumns = "7"
fun main() {
    var boardDimensions= ""
    val range = 5..9
    val regex = Regex("\\s*?\\d\\d?\\s*?x\\s*?\\d\\d?\\s*?")
    var auxBoard: MutableList<String> = mutableListOf()

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
                //Divide boardDimensions in rows and columns using x character
                //replace("\\s".toRegex(), "") to remove all whiteSpaces no matter if they are space or tab
                auxBoard = boardDimensions.lowercase().replace("\\s".toRegex(), "").split("x").toMutableList()
                //Checking that rows then columns are in range
                if (auxBoard[0].toInt() in range) {
                    if (auxBoard[1].toInt() in range) {
                        wrongDimension = false
                        boardDimensions = "${auxBoard[0]} X ${auxBoard[1]}"
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
            boardDimensions = defaultBoardDimension
            auxBoard.add(defaultRows)
            auxBoard.add(defaultColumns)
            wrongDimension = false
        }
    }
    //Output message
    println("$firstPlayer VS $secondPlayer\n$boardDimensions board")

    //Printing game's board
    printGameBoard(auxBoard)
}

//Get the auxBoard variable where auxBoard[0] = rows and auxBoard[1] = columns
fun printGameBoard(boardDimensions: MutableList<String>) {
    var board = ""
    //Rows plus numbers row
    val rows = boardDimensions[0].toInt() + 1
    val columns = boardDimensions[1].toInt()
    //iterating in rows
    for (i in 0..rows) {
        //Iterating in columns
        for (j in 1..columns) {
            //if i counter is in the last row
            if (i == rows) {
                board += when(j) {
                    1 -> "╚"
                    columns -> "═╩═╝"
                    else -> "═╩"
                }
            } else {
                board += if (i == 0) " $j" else "║ "
                board += if (j == columns && i != 0) "║" else ""
            }
        }
        board += "\n"
    }

    print(board)
}