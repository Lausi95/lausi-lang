package lausilang

enum class TokenType {
  FUNCTION_KEY,
  SYMBOL,
  INTEGER,
  OPENING_PAREN,
  CLOSING_PAREN,
  OPENING_CURLY,
  CLOSING_CURLY,
  SEMICOLON,
  STRING,
  PLUS,
  MINUS,
  STAR,
  SLASH,
  COMMA,
  COLON,
}

data class Token(val type: TokenType, val value: String?) {
  constructor(type: TokenType): this(type, null)
}

/**
 * Lexer is NOT thread safe!
 */
class Lexer(private val input: String) {
  private var i = 0
  private var buffer = ""
  private val tokens = mutableListOf<Token>()

  fun tokenize(): List<Token> {
    i = 0
    buffer = ""
    tokens.clear()

    while (inBound()) {
      if (currentIs('(')) {
        tokens.add(Token(TokenType.OPENING_PAREN))
      } else if (currentIs(')')) {
        tokens.add(Token(TokenType.CLOSING_PAREN))
      } else if (currentIs('{')) {
        tokens.add(Token(TokenType.OPENING_CURLY))
      } else if (currentIs('}')) {
        tokens.add(Token(TokenType.CLOSING_CURLY))
      } else if (currentIs(';')) {
        tokens.add(Token(TokenType.SEMICOLON))
      } else if (currentIs(':')) {
        tokens.add(Token(TokenType.COLON))
      } else if (currentIs(',')) {
        tokens.add(Token(TokenType.COMMA))
      } else if (currentIs('+')) {
        tokens.add(Token(TokenType.PLUS))
      } else if (currentIs('-')) {
        tokens.add(Token(TokenType.MINUS))
      } else if (currentIs('*')) {
        tokens.add(Token(TokenType.STAR))
      } else if (currentIs('*')) {
        tokens.add(Token(TokenType.SLASH))
      } else if (currentIs('\"')) {
        parseString()
      } else if (current().isLetter()) {
        parseSymbol()
      } else if (!current().isWhitespace()) {
        error("Unrecognized token at position $i: ${input[i]}")
      }
      next()
    }

    return tokens
  }

  private fun current(): Char {
    return input[i]
  }

  private fun currentIs(char: Char): Boolean {
    return current() == char
  }

  private fun next(): Char? {
    i++
    if (inBound()) {
      return current()
    }
    return null
  }

  private fun prev(): Char {
    i--
    return current()
  }

  private fun inBound(): Boolean {
    return i < input.length
  }

  private fun clearBuffer() {
    buffer = ""
  }

  private fun appendBuffer(char: Char) {
    buffer += char
  }

  private fun bufferIs(check: String): Boolean {
    return buffer == check
  }

  private fun parseString() {
    // TODO(tla): cannot escape double quotes in strings yet
    while (next() != '\"') {
      buffer += current()
    }
    tokens.add(Token(TokenType.STRING, buffer))
    clearBuffer()
  }

  private fun parseSymbol() {
    do {
      appendBuffer(current())
    } while (next()?.isLetterOrDigit() == true)

    if (bufferIs("fun")) {
      tokens.add(Token(TokenType.FUNCTION_KEY, buffer))
    } else {
      tokens.add(Token(TokenType.SYMBOL, buffer))
    }

    clearBuffer()
    prev()
  }
}
