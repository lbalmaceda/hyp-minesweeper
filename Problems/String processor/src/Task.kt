import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    // write your code here

    val first = scanner.nextLine()
    val operation = scanner.nextLine()
    val second = scanner.nextLine()

    when(operation){
        "equals" -> {
            println(first ==second)
        }
        "plus" -> {
            println(first + second)
        }
        "endsWith" ->{
            println(first.endsWith(second))
        }
        else -> {
            println("Unknown operation")
        }
    }
}