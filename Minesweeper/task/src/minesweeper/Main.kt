package minesweeper

import java.util.*
import kotlin.system.exitProcess


const val fieldLength = 9
fun main() {
    val scanner = Scanner(System.`in`)
    var didFirstMove = false

    println("How many mines do you want on the field?")
    val minesCount = scanner.nextLine().toInt()

    var boardOfMines = generateGame(minesCount)
    val board = generateEmptyBoard(fieldLength)
    printBoard(board)

    println("Set/delete mines marks (x and y coordinates):")
    while (scanner.hasNext()) {
        val cY = scanner.nextInt() - 1
        val cX = scanner.nextInt() - 1
        val operation = scanner.next()
        when (operation) {
            "mine" -> markCell(board, cX, cY)
            "free" -> {
                if (isMine(boardOfMines, cX, cY) && !didFirstMove) {
                    val fieldToSkip = Pair(cX, cY)
                    boardOfMines = generateGame(minesCount, fieldToSkip)
                }
                explodeIfMine(board, boardOfMines, cX, cY)
                uncoverCell(board, boardOfMines, cX, cY)
                printBoard(board)
                didFirstMove = true
            }
        }
        checkGameStatus(board, boardOfMines, minesCount)
        println("Set/delete mines marks (x and y coordinates):")
    }
}

fun generateGame(minesCount: Int, fieldToSkip: Pair<Int, Int>? = null): Array<Array<String>> {
    var minesPosition = calculateMinesPosition(minesCount)
    while (fieldToSkip != null && minesPosition.contains(fieldToSkip)) {
        minesPosition = calculateMinesPosition(minesCount)
    }
    val boardOfMines = generateBoardWithMines(minesPosition)
    updateBoardWithHints(boardOfMines)
    return boardOfMines
}

fun isMine(boardOfMines: Array<Array<String>>, cX: Int, cY: Int): Boolean {
    return boardOfMines[cX][cY] == "X"
}

fun explodeIfMine(board: Array<Array<String>>, boardOfMines: Array<Array<String>>, cX: Int, cY: Int) {
    val isMine = boardOfMines[cX][cY] == "X"
    if (isMine) {
        uncoverMines(board, boardOfMines)
        printBoard(board)
        println("You stepped on a mine and failed!")
        exitProcess(0)
    }
}

fun uncoverMines(board: Array<Array<String>>, boardOfMines: Array<Array<String>>) {
    for (i in 0 until fieldLength) {
        for (j in 0 until fieldLength) {
            val isMine = boardOfMines[i][j] == "X"
            if (isMine) {
                board[i][j] = "X"
            }
        }
    }
}

fun uncoverCell(board: Array<Array<String>>, boardOfMines: Array<Array<String>>, cX: Int, cY: Int) {
    for (dI in -1..1) {
        for (dJ in -1..1) {
            val nextI = cX + dI
            val nextJ = cY + dJ
            if (isWithinBounds(nextI, nextJ)) {
                val neighborSlot = boardOfMines[nextI][nextJ]
                when (neighborSlot) {
                    "." -> {
                        board[nextI][nextJ] = "/"
                        boardOfMines[nextI][nextJ] = "/"
                        uncoverCell(board, boardOfMines, nextI, nextJ)
                    }
                    "*", "X", "/" -> {
                        //No-Op
                    }
                    else -> {
                        //Hints
                        board[nextI][nextJ] = neighborSlot
                    }
                }
            }
        }
    }
}

fun checkGameStatus(board: Array<Array<String>>, boardOfMines: Array<Array<String>>, minesCount: Int) {
    var markCount = 0
    var minesToGo = minesCount
    for (i in 0 until fieldLength) {
        for (j in 0 until fieldLength) {
            val isMark = board[i][j] == "*"
            val isMine = boardOfMines[i][j] == "X"
            if (isMark) {
                markCount++
                if (isMine) {
                    minesToGo--
                }
            }
        }
    }
    if (minesToGo == 0 && markCount == minesCount) {
        println("Congratulations! You found all mines!")
        exitProcess(0)
    }
}

fun markCell(board: Array<Array<String>>, cX: Int, cY: Int) {
    val element = board.get(cX).get(cY)
    when (element) {
        "." -> {
            //"empty", let's mark it and print the board
            board[cX][cY] = "*"
            printBoard(board)
        }
        "X" -> {
            //Not possible
        }
        "*" -> {
            board[cX][cY] = "."
            printBoard(board)
        }
        else -> {
            println("There is a number here!")
        }
    }
}

fun printBoard(board: Array<Array<String>>) {
    var allIndices = ""
    for (i in 1..fieldLength) {
        allIndices += i
    }
    println(String.format(" |%${fieldLength}s|", allIndices))
    //print table separator
    println("-|" + "|".padStart(fieldLength + 1, '-'))
    //print content
    board.forEachIndexed { index, row ->
        print("${index + 1}|")
        for (slot in row) {
            print(slot)
        }
        print("|")
        println()
    }
    //print table separator
    println("-|" + "|".padStart(fieldLength + 1, '-'))
}

fun calculateMinesPosition(minesCount: Int): MutableSet<Pair<Int, Int>> {
    val random = Random()
    val mines = mutableSetOf<Pair<Int, Int>>()

    while (mines.size != minesCount) {
        mines.add(Pair(random.nextInt(fieldLength), random.nextInt(fieldLength)))
    }
    return mines
}

fun generateBoardWithMines(minesPosition: MutableSet<Pair<Int, Int>>): Array<Array<String>> {
    val board = generateEmptyBoard(fieldLength)

    for (i in 0 until fieldLength) {
        for (j in 0 until fieldLength) {
            if (minesPosition.contains(Pair(i, j))) {
                board[i][j] = "X"
            }
        }
    }
    return board
}

fun generateEmptyBoard(size: Int): Array<Array<String>> {
    return Array(size) {
        Array(size) { "." }
    }
}

fun updateBoardWithHints(board: Array<Array<String>>) {
    for (i in 0 until fieldLength) {
        for (j in 0 until fieldLength) {
            val currentSlot = board[i][j]
            if (currentSlot != "X") {
                continue
            }

            for (dI in -1..1) {
                for (dJ in -1..1) {
                    val nextI = i + dI
                    val nextJ = j + dJ
                    if (isWithinBounds(nextI, nextJ)) {
                        val neighborSlot = board[nextI][nextJ]
                        when (neighborSlot) {
                            "." -> {
                                board[nextI][nextJ] = "1"
                            }
                            "X" -> {
                            }
                            else -> {
                                board[nextI][nextJ] = (neighborSlot.toInt() + 1).toString()
                            }
                        }
                    }
                }
            }
            //Remove 'X'
//            board[i][j] = "."
        }
    }
}

fun isWithinBounds(i: Int, j: Int): Boolean {
    if (i < 0 || i >= fieldLength || j < 0 || j >= fieldLength) {
        return false
    }
    return true
}
