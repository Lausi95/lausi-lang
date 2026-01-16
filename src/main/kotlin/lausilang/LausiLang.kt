package lausilang

import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
  try {
    val inputFile = parseInputFile(args)
    val code = loadCodeFromInputFile(inputFile)
    val tokens = tokenize(code)
    val statements = parse(tokens)

    val scope = HashMap<String, Int>()
    statements.forEach { it.execute(scope) }
  } catch (ex: Exception) {
    log.error(ex) { "Error during interpretation: ${ex.message}" }
  }
}

fun parseInputFile(args: Array<String>): Path {
  log.debug { "Parsing input file from args..." }
  check(args.isNotEmpty()) {"Expected 1 program argument. Got ${args.size}"}
  val path = Path.of(args[0])
  log.debug { "Parsed input file from args: $path" }
  return path
}

fun loadCodeFromInputFile(inputFile: Path): String {
  log.debug { "Loading code from input file..." }
  check(Files.exists(inputFile)) { "Input file does not exist: $inputFile" }
  check(Files.isRegularFile(inputFile)) { "Input is not a file: $inputFile" }
  val code = Files.readAllLines(inputFile).joinToString("\n")
  log.debug { "Loaded code from input file: $code" }
  return code
}