package lausilang

enum class TokenType {
  FUNCTION_KEY,
  SYMBOL,
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

fun tokenize(input: String): List<Token> {
  val tokens = mutableListOf<Token>()

  var i = 0
  var buffer = ""

  while (i < input.length) {
    if (input[i] == '(') {
      tokens.add(Token(TokenType.OPENING_PAREN))
    } else if (input[i] == ')') {
      tokens.add(Token(TokenType.CLOSING_PAREN))
    } else if (input[i] == '{') {
      tokens.add(Token(TokenType.OPENING_CURLY))
    } else if (input[i] == '}') {
      tokens.add(Token(TokenType.CLOSING_CURLY))
    } else if (input[i] == ';') {
      tokens.add(Token(TokenType.SEMICOLON))
    } else if (input[i] == ':') {
      tokens.add(Token(TokenType.COLON))
    } else if (input[i] == ',') {
      tokens.add(Token(TokenType.COMMA))
    } else if (input[i] == '+') {
      tokens.add(Token(TokenType.PLUS))
    } else if (input[i] == '-') {
      tokens.add(Token(TokenType.MINUS))
    } else if (input[i] == '*') {
      tokens.add(Token(TokenType.STAR))
    } else if (input[i] == '/') {
      tokens.add(Token(TokenType.SLASH))
    } else if (input[i] == '\"') {
      i++
      while (i < input.length && input[i] != '\"') {
        buffer += input[i]
        i++
      }

      tokens.add(Token(TokenType.STRING, buffer))
      buffer = ""
    } else if (input[i].isLetter()) {
      while (i < input.length && input[i].isLetterOrDigit()) {
        buffer += input[i]
        i++
      }

      if (buffer == "fun") {
        tokens.add(Token(TokenType.FUNCTION_KEY, buffer))
      } else {
        tokens.add(Token(TokenType.SYMBOL, buffer))
      }

      buffer = ""
      i--
    } else if (!input[i].isWhitespace()) {
      error("Unrecognized token at position $i: ${input[i]}")
    }
    i++
  }

  return tokens
}
