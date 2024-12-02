import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val parsed = parseInput(input)
        val firstSorted = parsed.first.sorted()
        val secondSorted = parsed.second.sorted()

        val result = firstSorted.zip(secondSorted) { a, b -> abs( a - b)}

        return abs(result.sum())
    }

    fun part2(input: List<String>): Int {
        val parsedInput = parseInput(input)
        val cache = mutableMapOf<Int, Int>()
        var count = 0

        parsedInput.first.forEach { element ->
            val value = cache.getOrPut(element) {
                parsedInput.second.count { it == element }
            }
            count += element * value
        }

        return count
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println() // 2086478

    check(part2(testInput) == 31)
    part2(input).println() // 24941624
}

fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()

    input.forEach { line ->
        val (first, second) = line.split("\\s+".toRegex()).map { it.toInt() }
        firstList.add(first)
        secondList.add(second)
    }
    return Pair(firstList, secondList)
}
