package lausilang

interface Statement {

  fun execute(scope: MutableMap<String, Int>)
}

interface Expression {

  fun evaluate(scope: MutableMap<String, Int>): Int
}

data class Literal(val value: Int): Expression {

  override fun evaluate(scope: MutableMap<String, Int>): Int {
    return value
  }
}

data class Symbol(val name: String): Expression {

  override fun evaluate(scope: MutableMap<String, Int>): Int {
    val value: Int = scope[name] ?: error("Symbol $name not found in scope")
    return value
  }
}

data class Addition(val left: Expression, val right: Expression): Expression {

  override fun evaluate(scope: MutableMap<String, Int>): Int {
    val leftValue = left.evaluate(scope)
    val rightValue = right.evaluate(scope)
    return leftValue + rightValue
  }
}

data class Assignment(val target: Symbol, val expression: Expression): Statement {

  override fun execute(scope: MutableMap<String, Int>) {
    val value = expression.evaluate(scope)
    scope[target.name] = value
  }
}

data class Print(val argument: Expression): Statement {

  override fun execute(scope: MutableMap<String, Int>) {
    val toPrint = argument.evaluate(scope)
    println(toPrint)
  }
}

fun parseExpression(tokens: List<Token>): Expression {
  if (tokens.size == 1 && tokens.typeAtIs(0, TokenType.SYMBOL)) {
    return Symbol(tokens[0].value)
  }

  if (tokens.size == 1 && tokens.typeAtIs(0, 0, TokenType.INTEGER)) {
    return Literal(tokens[0].value.toInt())
  }

  if (tokens.typeAtIs(0, 1, TokenType.PLUS)) {
    val leftExpression = parseExpression(tokens.subList(0, 1))
    val rightExpression = parseExpression(tokens.subList(2, tokens.size))
    return Addition(leftExpression, rightExpression)
  }

  error("cannot parse expression.")
}

fun parseStatement(tokens: List<Token>): Statement {
  if (tokens.typeAtIs(0, TokenType.SYMBOL) && tokens.typeAtIs(1, TokenType.EQUAL)) {
    val symbol = Symbol(tokens[0].value)
    val expression = parseExpression(tokens.subList(2, tokens.size))
    return Assignment(symbol, expression)
  }

  if (tokens.typeAtIs(0, TokenType.PRINT) && tokens.typeAtIs(1, TokenType.OPENING_PAREN) && tokens.typeAtIs(tokens.size - 1, TokenType.CLOSING_PAREN)) {
    val expression = parseExpression(tokens.subList(2, tokens.size - 1))
    return Print(expression)
  }

  error("Could not parse statement.")
}

fun parse(tokens: List<Token>): List<Statement> {
  val statements = mutableListOf<Statement>()

  var idx = 0
  while (idx < tokens.size - 1) {
    val tokenBuffer = mutableListOf<Token>()

    while (idx < tokens.size && !tokens.typeAtIs(idx, 0, TokenType.SEMICOLON)) {
      tokenBuffer.add(tokens[idx])
      idx++
    }

    statements.add(parseStatement(tokenBuffer))
    idx++
  }

  if (!tokens.typeAtIs(idx, 0, TokenType.END)) {
    error("unexpected end of file")
  }

  return statements
}

private fun List<Token>.tokenAt(idx: Int, dx: Int): TokenType? {
  if (idx + dx < size) {
    return this[idx + dx].type
  }
  return null
}

private fun List<Token>.typeAtIs(idx: Int, dx: Int, type: TokenType) = tokenAt(idx, dx) == type

private fun List<Token>.typeAtIs(dx: Int, type: TokenType) = typeAtIs(0, dx, type)
