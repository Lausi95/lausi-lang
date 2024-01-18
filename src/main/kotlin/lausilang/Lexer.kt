package lausilang

enum class TokenType {
  function_keyword,
  symbol,
  open_paren,
  close_paren,
  open_curly,
  close_curly,
  semicolon,
  string,
  plus,
  comma,
  colon,
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
      tokens.add(Token(TokenType.open_paren))
    } else if (input[i] == ')') {
      tokens.add(Token(TokenType.close_paren))
    } else if (input[i] == '{') {
      tokens.add(Token(TokenType.open_curly))
    } else if (input[i] == '}') {
      tokens.add(Token(TokenType.close_curly))
    } else if (input[i] == ';') {
      tokens.add(Token(TokenType.semicolon))
    } else if (input[i] == ':') {
      tokens.add(Token(TokenType.colon))
    } else if (input[i] == ',') {
      tokens.add(Token(TokenType.comma))
    } else if (input[i] == '+') {
      tokens.add(Token(TokenType.plus))
    } else if (input[i] == '\"') {
      i++
      while (i < input.length && input[i] != '\"') {
        buffer += input[i]
        i++
      }

      tokens.add(Token(TokenType.string, buffer))
      buffer = ""
    } else if (input[i].isLetter()) {
      while (i < input.length && input[i].isLetterOrDigit()) {
        buffer += input[i]
        i++
      }

      if (buffer == "fun") {
        tokens.add(Token(TokenType.function_keyword, buffer))
      } else {
        tokens.add(Token(TokenType.symbol, buffer))
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
