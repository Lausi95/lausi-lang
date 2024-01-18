package lausilang

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerTest {

  @Test
  fun tokenizeFunctionKeyword() {
    assertEquals(listOf(Token(TokenType.function_keyword, "fun")), tokenize("fun"))
  }

  @Test
  fun tokenizeOpenParen() {
    assertEquals(listOf(Token(TokenType.open_paren)), tokenize("("))
  }

  @Test
  fun tokenizeClosingParen() {
    assertEquals(listOf(Token(TokenType.close_paren)), tokenize(")"))
  }

  @Test
  fun tokenizeOpenCurly() {
    assertEquals(listOf(Token(TokenType.open_curly)), tokenize("{"))
  }

  @Test
  fun tokenizeClosingCurly() {
    assertEquals(listOf(Token(TokenType.close_curly)), tokenize("}"))
  }

  @Test
  fun tokenizeSemicolon() {
    assertEquals(listOf(Token(TokenType.semicolon)), tokenize(";"))
  }

  @Test
  fun tokenizeSymbol() {
    assertEquals(listOf(Token(TokenType.symbol, "foo")), tokenize("foo"))
  }

  @Test
  fun tokenizeString() {
    assertEquals(listOf(Token(TokenType.string, "fun string")), tokenize("\"fun string\""))
  }
}
