package lausilang

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerTest {

  @Test
  fun tokenizeFunctionKeyword() {
    val code = "fun"

    val tokens = Lexer(code).tokenize()

    val expectedTokens = listOf(
      Token(TokenType.FUNCTION_KEY, "fun")
    )
    assertEquals(expectedTokens, tokens)
  }

  @Test
  fun tokenizeMainFunctionSignature() {
    val code = "fun main() {}"

    val tokens = Lexer(code).tokenize()

    val expectedTokens = listOf(
      Token(TokenType.FUNCTION_KEY, "fun"),
      Token(TokenType.SYMBOL, "main"),
      Token(TokenType.OPENING_PAREN),
      Token(TokenType.CLOSING_PAREN),
      Token(TokenType.OPENING_CURLY),
      Token(TokenType.CLOSING_CURLY),
    )
    assertEquals(expectedTokens, tokens)
  }

  @Test
  fun tokenizeOpenParen() {
    assertEquals(listOf(Token(TokenType.OPENING_PAREN)), Lexer("(").tokenize())
  }

  @Test
  fun tokenizeClosingParen() {
    assertEquals(listOf(Token(TokenType.CLOSING_PAREN)), Lexer(")").tokenize())
  }

  @Test
  fun tokenizeOpenCurly() {
    assertEquals(listOf(Token(TokenType.OPENING_CURLY)), Lexer("{").tokenize())
  }

  @Test
  fun tokenizeClosingCurly() {
    assertEquals(listOf(Token(TokenType.CLOSING_CURLY)), Lexer("}").tokenize())
  }

  @Test
  fun tokenizeSemicolon() {
    assertEquals(listOf(Token(TokenType.SEMICOLON)), Lexer(";").tokenize())
  }

  @Test
  fun tokenizeSymbol() {
    assertEquals(listOf(Token(TokenType.SYMBOL, "foo")), Lexer("foo").tokenize())
  }

  @Test
  fun tokenizeString() {
    assertEquals(listOf(Token(TokenType.STRING, "fun string")), Lexer("\"fun string\"").tokenize())
  }
}
