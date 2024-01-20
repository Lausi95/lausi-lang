package lausilang

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerTest {

  @Test
  fun tokenizeFunctionKeyword() {
    val code = "fun"

    val tokens = tokenize(code)

    val expectedTokens = listOf(
      Token(TokenType.SYMBOL, "fun"),
      Token(TokenType.END)
    )
    assertEquals(expectedTokens, tokens)
  }

  @Test
  fun tokenizeMainFunctionSignature() {
    val code = "fun main() {}"

    val tokens = tokenize(code)

    val expectedTokens = listOf(
      Token(TokenType.SYMBOL, "fun"),
      Token(TokenType.SYMBOL, "main"),
      Token(TokenType.OPENING_PAREN, "("),
      Token(TokenType.CLOSING_PAREN, ")"),
      Token(TokenType.OPENING_CURLY, "{"),
      Token(TokenType.CLOSING_CURLY, "}"),
      Token(TokenType.END)
    )
    assertEquals(expectedTokens, tokens)
  }

  @Test
  fun tokenizeOpenParen() {
    assertEquals(listOf(Token(TokenType.OPENING_PAREN, "("), Token(TokenType.END)), tokenize("("))
  }

  @Test
  fun tokenizeClosingParen() {
    assertEquals(listOf(Token(TokenType.CLOSING_PAREN, ")"), Token(TokenType.END)), tokenize(")"))
  }

  @Test
  fun tokenizeOpenCurly() {
    assertEquals(listOf(Token(TokenType.OPENING_CURLY, "{"), Token(TokenType.END)), tokenize("{"))
  }

  @Test
  fun tokenizeClosingCurly() {
    assertEquals(listOf(Token(TokenType.CLOSING_CURLY, "}"), Token(TokenType.END)), tokenize("}"))
  }

  @Test
  fun tokenizeSemicolon() {
    assertEquals(listOf(Token(TokenType.SEMICOLON, ";"), Token(TokenType.END)), tokenize(";"))
  }

  @Test
  fun tokenizeSymbol() {
    assertEquals(listOf(Token(TokenType.SYMBOL, "foo"), Token(TokenType.END)), tokenize("foo"))
  }

  @Test
  fun tokenizeString() {
    assertEquals(listOf(Token(TokenType.STRING, "\"fun string\""), Token(TokenType.END)), tokenize("\"fun string\""))
  }
}
