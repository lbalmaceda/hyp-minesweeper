import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    // put your code here

    val map = mapCountryCurrency()

    val first = input.next()
    val second = input.next()
    val eq = map.getOrDefault(first, Currency.NLL) == map.getOrDefault(second, Currency.NLL)
    println(eq)
}

fun mapCountryCurrency(): Map<String, Currency> {
    return mapOf(
            "Germany" to Currency.EUR,
            "Mali" to Currency.CFA,
            "Dominica" to Currency.ECD,
            "Canada" to Currency.CAD,
            "Spain" to Currency.EUR,
            "Australia" to Currency.AUD,
            "Brazil" to Currency.BRR,
            "Senegal" to Currency.CFA,
            "France" to Currency.EUR,
            "Grenada" to Currency.ECD,
            "Kiribati" to Currency.AUD
    )
}

enum class Currency(val description: String) {
    EUR("Euro"),
    CFA("CFA franc"),
    ECD("Eastern Caribbean dollar"),
    CAD("Canadian dollar"),
    AUD("Australian dollar"),
    BRR("Brazilian real"),
    NLL("null"),
}