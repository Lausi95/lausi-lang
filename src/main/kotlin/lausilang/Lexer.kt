package lausilang

/**
 * Type of token.
 */
enum class TokenType {
  INTEGER,
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
  DOT,
  END,
}

/**
 * A token in a string. Consists of a type and a value.
 * The value might be null.
 *
 * @param type Type of token
 * @param value Value of the token as a String
 */
data class Token(val type: TokenType, val value: String) {
  constructor(type: TokenType): this(type, "")
}

private val TOKEN_PATTERNS = mapOf(
  "^\\s".toRegex() to null,
  "^//.*".toRegex() to null,
  "^\\d+".toRegex() to TokenType.INTEGER,
  "^\"[^\"]*\"".toRegex() to TokenType.STRING,
  "^\\(".toRegex() to TokenType.OPENING_PAREN,
  "^\\)".toRegex() to TokenType.CLOSING_PAREN,
  "^\\{".toRegex() to TokenType.OPENING_CURLY,
  "^}".toRegex() to TokenType.CLOSING_CURLY,
  "^;".toRegex() to TokenType.SEMICOLON,
  "^:".toRegex() to TokenType.COLON,
  "^,".toRegex() to TokenType.COMMA,
  "^\\.".toRegex() to TokenType.DOT,
  "^\\+".toRegex() to TokenType.PLUS,
  "^-".toRegex() to TokenType.MINUS,
  "^\\*".toRegex() to TokenType.STAR,
  "^/".toRegex() to TokenType.SLASH,
  "^\\w+".toRegex() to TokenType.SYMBOL,
)

/**
 * Tokenizes the given string and transforms it to a list of tokens.
 *
 * @param input The string to tokenize
 * @return The list of tokens parsed from the given string
 */
fun tokenize(input: String): List<Token> {
  if (input.isEmpty()) {
    return listOf(Token(TokenType.END))
  }

  TOKEN_PATTERNS.forEach { (regex, tokenType) ->
    regex.matchAt(input, 0)?.let { result ->
      val nextTokens = tokenize(input.substring(result.value.length))
      return tokenType?.let {
        listOf(Token(tokenType, result.value)) + nextTokens
      } ?: nextTokens
    }
  }

  error("Unrecognized Token at \"$input\"")
}
