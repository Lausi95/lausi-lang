package lausilang

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerTest {

  @Test
  fun tokenizeFunctionKeyword() {
    assertEquals(listOf(Token(TokenType.FUNCTION_KEY, "fun")), tokenize("fun"))
  }

  @Test
  fun tokenizeOpenParen() {
    assertEquals(listOf(Token(TokenType.OPENING_PAREN)), tokenize("("))
  }

  @Test
  fun tokenizeClosingParen() {
    assertEquals(listOf(Token(TokenType.CLOSING_PAREN)), tokenize(")"))
  }

  @Test
  fun tokenizeOpenCurly() {
    assertEquals(listOf(Token(TokenType.OPENING_CURLY)), tokenize("{"))
  }

  @Test
  fun tokenizeClosingCurly() {
    assertEquals(listOf(Token(TokenType.CLOSING_CURLY)), tokenize("}"))
  }

  @Test
  fun tokenizeSemicolon() {
    assertEquals(listOf(Token(TokenType.SEMICOLON)), tokenize(";"))
  }

  @Test
  fun tokenizeSymbol() {
    assertEquals(listOf(Token(TokenType.SYMBOL, "foo")), tokenize("foo"))
  }

  @Test
  fun tokenizeString() {
    assertEquals(listOf(Token(TokenType.STRING, "fun string")), tokenize("\"fun string\""))
  }
}
