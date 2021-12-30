package connectfour

const val defaultBoardDimension = "6 X 7"
fun main() {
    var boardDimensions= ""
    val range = 5..9
    val regex = Regex("\\s*?\\d\\d?\\s*?x\\s*?\\d\\d?\\s*?")

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
                val auxBoard = boardDimensions.lowercase().replace("\\s".toRegex(), "").split("x")
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
            wrongDimension = false
        }
    }
    //Output message
    println("$firstPlayer VS $secondPlayer\n$boardDimensions board")
}