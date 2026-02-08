package lausilang.lexer

import lausilang.token.Token
import lausilang.token.TokenType
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class LexerTest {

    @Test
    fun readSEMICOLON() {
        val code = ";"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.SEMICOLON, ";", 0, 1), token)
    }

    @Test
    fun readCOMMA() {
        val code = ","
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.COMMA, ",", 0, 1), token)
    }

    @Test
    fun readASSIGN() {
        val code = "="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ASSIGN, "=", 0, 1), token)
    }

    @Test
    fun readPLUS() {
        val code = "+"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.PLUS, "+", 0, 1), token)
    }

    @Test
    fun readLPAREN() {
        val code = "("
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LPAREN, "(", 0, 1), token)
    }

    @Test
    fun readRPAREN() {
        val code = ")"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RPAREN, ")", 0, 1), token)
    }

    @Test
    fun readLBRACE() {
        val code = "{"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LBRACE, "{", 0, 1), token)
    }

    @Test
    fun readRBRACE() {
        val code = "}"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RBRACE, "}", 0, 1), token)
    }

    @Test
    fun readEOF() {
        val code = ""
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.EOF, "", 0, 1), token)
    }

    @Test
    fun readMINUS() {
        val code = "-"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.MINUS, "-", 0, 1), token)
    }

    @Test
    fun readBANG() {
        val code = "!"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.BANG, "!", 0, 1), token)
    }

    @Test
    fun readSLASH() {
        val code = "/"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.SLASH, "/", 0, 1), token)
    }

    @Test
    fun readASTERISK() {
        val code = "*"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ASTERISK, "*", 0, 1), token)
    }

    @Test
    fun readLT() {
        val code = "<"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LT, "<", 0, 1), token)
    }

    @Test
    fun readGT() {
        val code = ">"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.GT, ">", 0, 1), token)
    }

    @Test
    fun readTRUE() {
        val code = "true"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.TRUE, "true", 0, 1), token)
    }

    @Test
    fun readFALSE() {
        val code = "false"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.FALSE, "false", 0, 1), token)
    }

    @Test
    fun readIF() {
        val code = "if"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.IF, "if", 0, 1), token)
    }

    @Test
    fun readELSE() {
        val code = "else"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ELSE, "else", 0, 1), token)
    }

    @Test
    fun readRETURN() {
        val code = "return"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RETURN, "return", 0, 1), token)
    }

    @Test
    fun readEQ() {
        val code = "=="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.EQ, "==", 0, 2), token)
    }

    @Test
    fun readNEQ() {
        val code = "!="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.NEQ, "!=", 0, 2), token)
    }

    @Test
    fun readMultipleTokens() {
        val code = "(){}"
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.LPAREN, "(", 0, 1), lexer.nextToken())
        assertEquals(Token(TokenType.RPAREN, ")", 0, 2), lexer.nextToken())
        assertEquals(Token(TokenType.LBRACE, "{", 0, 3), lexer.nextToken())
        assertEquals(Token(TokenType.RBRACE, "}", 0, 4), lexer.nextToken())
    }

    @Test
    fun readIDENTIFIER() {
        val code = "one"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.IDENTIFIER, "one", 0, 1), token)
    }

    @Test
    fun readLET() {
        val code = "let"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LET, "let", 0, 1), token)
    }

    @Test
    fun readFUNCTION() {
        val code = "fn"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.FUNCTION, "fn", 0, 1), token)
    }

    @Test
    fun readINTEGER() {
        val code = "123"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.INTEGER, "123", 0, 1), token)
    }

    @Test
    fun readMultipleWordTokens() {
        val code = "fn let foo      123"
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.FUNCTION, "fn", 0, 1), lexer.nextToken())
        assertEquals(Token(TokenType.LET, "let", 0, 3), lexer.nextToken())
        assertEquals(Token(TokenType.IDENTIFIER, "foo", 0, 5), lexer.nextToken())
        assertEquals(Token(TokenType.INTEGER, "123", 0, 12), lexer.nextToken())
    }

    @Test
    fun readWordTokenAndSymbols() {
        val code = "fn("
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.FUNCTION, "fn", 0, 1), lexer.nextToken())
        assertEquals(Token(TokenType.LPAREN, "(", 0, 2), lexer.nextToken())
    }
}