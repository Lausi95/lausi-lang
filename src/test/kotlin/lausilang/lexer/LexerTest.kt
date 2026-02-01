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
        assertEquals(Token(TokenType.SEMICOLON, ";"), token)
    }

    @Test
    fun readCOMMA() {
        val code = ","
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.COMMA, ","), token)
    }

    @Test
    fun readASSIGN() {
        val code = "="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ASSIGN, "="), token)
    }

    @Test
    fun readPLUS() {
        val code = "+"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.PLUS, "+"), token)
    }

    @Test
    fun readLPAREN() {
        val code = "("
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LPAREN, "("), token)
    }

    @Test
    fun readRPAREN() {
        val code = ")"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RPAREN, ")"), token)
    }

    @Test
    fun readLBRACE() {
        val code = "{"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LBRACE, "{"), token)
    }

    @Test
    fun readRBRACE() {
        val code = "}"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RBRACE, "}"), token)
    }

    @Test
    fun readEOF() {
        val code = ""
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.EOF, ""), token)
    }

    @Test
    fun readMINUS() {
        val code = "-"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.MINUS, "-"), token)
    }

    @Test
    fun readBANG() {
        val code = "!"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.BANG, "!"), token)
    }

    @Test
    fun readSLASH() {
        val code = "/"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.SLASH, "/"), token)
    }

    @Test
    fun readASTERISK() {
        val code = "*"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ASTERISK, "*"), token)
    }

    @Test
    fun readLT() {
        val code = "<"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LT, "<"), token)
    }

    @Test
    fun readGT() {
        val code = ">"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.GT, ">"), token)
    }

    @Test
    fun readTRUE() {
        val code = "true"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.TRUE, "true"), token)
    }

    @Test
    fun readFALSE() {
        val code = "false"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.FALSE, "false"), token)
    }

    @Test
    fun readIF() {
        val code = "if"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.IF, "if"), token)
    }

    @Test
    fun readELSE() {
        val code = "else"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.ELSE, "else"), token)
    }

    @Test
    fun readRETURN() {
        val code = "return"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.RETURN, "return"), token)
    }

    @Test
    fun readEQ() {
        val code = "=="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.EQ, "=="), token)
    }

    @Test
    fun readNEQ() {
        val code = "!="
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.NEQ, "!="), token)
    }

    @Test
    fun readMultipleTokens() {
        val code = "(){}"
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.LPAREN, "("), lexer.nextToken())
        assertEquals(Token(TokenType.RPAREN, ")"), lexer.nextToken())
        assertEquals(Token(TokenType.LBRACE, "{"), lexer.nextToken())
        assertEquals(Token(TokenType.RBRACE, "}"), lexer.nextToken())
    }

    @Test
    fun readIDENTIFIER() {
        val code = "one"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.IDENTIFIER, "one"), token)
    }

    @Test
    fun readLET() {
        val code = "let"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.LET, "let"), token)
    }

    @Test
    fun readFUNCTION() {
        val code = "fn"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.FUNCTION, "fn"), token)
    }

    @Test
    fun readINTEGER() {
        val code = "123"
        val lexer = Lexer(code)
        val token = lexer.nextToken()
        assertEquals(Token(TokenType.INTEGER, "123"), token)
    }

    @Test
    fun readMultipleWordTokens() {
        val code = "fn let foo      123"
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.FUNCTION, "fn"), lexer.nextToken())
        assertEquals(Token(TokenType.LET, "let"), lexer.nextToken())
        assertEquals(Token(TokenType.IDENTIFIER, "foo"), lexer.nextToken())
        assertEquals(Token(TokenType.INTEGER, "123"), lexer.nextToken())
    }

    @Test
    fun readWordTokenAndSymbols() {
        val code = "fn("
        val lexer = Lexer(code)
        assertEquals(Token(TokenType.FUNCTION, "fn"), lexer.nextToken())
        assertEquals(Token(TokenType.LPAREN, "("), lexer.nextToken())
    }
}