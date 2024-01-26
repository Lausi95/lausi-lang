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
  println("CODE:")
  println(input)

  val tokens = tokenize(input)
  println("\nTOKENS:")
  println(tokens)

  val statements = parse(tokens)
  println("\nSTATEMENTS:")
  println(statements.joinToString("\n") { it.toString() })

  println("\nInterpreting Lausi-Lang...")
  val scope = HashMap<String, Int>()
  statements.forEach { it.execute(scope) }
  println("done.")
}
