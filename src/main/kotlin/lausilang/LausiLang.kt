package lausilang

import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
  if (args.size != 1) {
    error("No input file given")
  }
  val inputFile = Path.of(args[0])
  if (!Files.exists(inputFile)) {
    error("Input file does not exist")
  }
  val input = Files.readAllLines(inputFile).joinToString("\n")

  val tokens = tokenize(input)

  println(tokens)
}
