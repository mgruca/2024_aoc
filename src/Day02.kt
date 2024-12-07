import kotlin.math.abs

//I was working on it for too many days. It looks like a corporate nightmare. At very least I could revisit the naming of things
fun main() {
    fun part1(input: List<String>): Int {
        val reports = Reports(input, Day02Task1Filtering() )
        return reports.filter { r -> r.isSafe }.count()
    }

    fun part2(input: List<String>): Int {
        val reports = Reports(input, Day02Task2Filtering())
        println(reports)
        return reports.filter { r -> r.isSafe }.count()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println() // Answer: 341

    check(part2(testInput) == 4)
    part2(input).println() // Answer: 404
}

class Reports(input: List<String>, reportFilteringStrategy: ReportFilteringStrategy) : Iterable<Report> {
    private val reports: List<Report> = input.map { line -> Report(line, reportFilteringStrategy) }

    override fun iterator(): Iterator<Report> {
        return reports.iterator()
    }

    override fun toString(): String {
        return "Reports(reports=$reports)"
    }
}

class Report(input: String, reportFilteringStrategy: ReportFilteringStrategy) {
    private val data: List<Int> = input.split(" ").map { it.toInt() }
    internal val isSafe: Boolean = reportFilteringStrategy.filter(data)

    override fun toString(): String {
        return "Report safety- $isSafe; (data=$data)"
    }
}

abstract class ReportFilteringStrategy {
    fun filter(data: List<Int>): Boolean {
        val rangeCheck: (Int) -> Boolean = { x -> abs(x) in 1..3 }
        val isGrowing: (Int, Int) -> Boolean = { a, b -> b < a && rangeCheck(a-b)}
        val isDecreasing: (Int, Int) -> Boolean = { a, b -> b > a  && rangeCheck(a-b) }
        val comparator = if (data.first()-data.last() > 0) isGrowing else isDecreasing
        return filterWithStrategy(data, true, comparator)
    }

    abstract fun filterWithStrategy(data: List<Int>, canRetry: Boolean, filterStrategy: (Int, Int) -> Boolean) : Boolean
}

class Day02Task1Filtering : ReportFilteringStrategy() {
    override fun filterWithStrategy(data: List<Int>, canRetry: Boolean, filterStrategy: (Int, Int) -> Boolean): Boolean {
        return data.zipWithNext().all { (a, b) -> filterStrategy(a, b) }
    }
}

class Day02Task2Filtering : ReportFilteringStrategy() {
    override fun filterWithStrategy(data: List<Int>, canRetry: Boolean, filterStrategy: (Int, Int) -> Boolean): Boolean {
        for(i in 1 until data.size){
            val first = data.elementAt(i-1)
            val second = data.elementAt(i)
            if(!filterStrategy(first, second)){
                if(!canRetry) {
                    return false
                }
                val tmpList1 = data.toMutableList()
                tmpList1.removeAt(i-1)
                val one = filterWithStrategy(tmpList1, false, filterStrategy)

                val tmpList2 = data.toMutableList()
                tmpList2.removeAt(i)
                val two = filterWithStrategy(tmpList2, false, filterStrategy)


                return one || two
            }
        }
        return true
    }
}
