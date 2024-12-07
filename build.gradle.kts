plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

tasks.register("createDayFiles") {
    val baseDir = projectDir.resolve("src")

    doLast {
        // Prompt user for day number if not provided as a property
        val dayNumber = if (project.hasProperty("dayNumber")) {
            project.properties["dayNumber"] as String
        } else {
            print("Enter the day number: ")
            System.console()?.readLine() ?: readLine() ?: error("No input provided")
        }

        val baseFileName = "Day$dayNumber"

        // Create .kt file
        val ktFile = baseDir.resolve("$baseFileName.kt")
        if (!ktFile.exists()) {
            ktFile.writeText(
                """
                fun main() {
                    fun part1(input: List<String>): Int {
                        return 1
                    }
                
                    fun part2(input: List<String>): Int {
                        return 1
                    }
                
                    // Test if implementation meets criteria from the description, like:
                    // check(part1(listOf("test_input")) == 1)
                
                    // Or read a large test input from the `src/${baseFileName}_test.txt` file:
                    val testInput = readInput("${baseFileName}_test")
                    check(part1(testInput) == 1)
                
                    // Read the input from the `src/${baseFileName}.txt` file.
                    val input = readInput("$baseFileName")
                    part1(input).println() // Answer:
                
                    check(part2(testInput) == 1)
                    part2(input).println() // Answer: 
                }
                """.trimIndent()
            )
            println("Created: ${ktFile.absolutePath}")
        }

        // Create .txt files
        listOf("${baseFileName}.txt", "${baseFileName}_test.txt").forEach { fileName ->
            val txtFile = baseDir.resolve(fileName)
            if (!txtFile.exists()) {
                txtFile.writeText("")
                println("Created: ${txtFile.absolutePath}")
            }
        }
    }
}
