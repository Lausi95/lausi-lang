package lausilang.lexer

import lausilang.token.Token
import lausilang.token.TokenType

private const val NULL_CHAR = 0.toChar()
private val ILLEGAL = Token(TokenType.ILLEGAL, "", -1, -1)

class Lexer(private val input: String) {
    private var position: Int = 0
    private var readPosition: Int = 0
    private var char: Char = NULL_CHAR
    private var line = 0
    private var column = 0

    init {
        readChar()
    }

    fun nextToken(): Token {
        var token = ILLEGAL

        skipWhitespace()

        when (char) {
            ';' -> token = Token(TokenType.SEMICOLON, char.toString(), line, column)
            ',' -> token = Token(TokenType.COMMA, char.toString(), line,column)
            '=' -> {
                if (peekChar() == '=') {
                    val c = char
                    readChar()
                    token = Token(TokenType.EQ, "" + c + char, line,column)
                } else {
                    token = Token(TokenType.ASSIGN, char.toString(), line,column)
                }
            }
            '+' -> token = Token(TokenType.PLUS, char.toString(), line,column)
            '-' -> token = Token(TokenType.MINUS, char.toString(), line,column)
            '!' -> {
                if (peekChar() == '=') {
                    val c = char
                    readChar()
                    token = Token(TokenType.NEQ, "" + c + char, line,column)

                } else {
                    token = Token(TokenType.BANG, char.toString(), line,column)
                }
            }
            '/' -> token = Token(TokenType.SLASH, char.toString(), line,column)
            '*' -> token = Token(TokenType.ASTERISK, char.toString(), line,column)
            '<' -> token = Token(TokenType.LT, char.toString(), line,column)
            '>' -> token = Token(TokenType.GT, char.toString(), line,column)
            '(' -> token = Token(TokenType.LPAREN, char.toString(), line,column)
            ')' -> token = Token(TokenType.RPAREN, char.toString(), line,column)
            '{' -> token = Token(TokenType.LBRACE, char.toString(), line,column)
            '}' -> token = Token(TokenType.RBRACE, char.toString(), line,column)
            NULL_CHAR -> token = Token(TokenType.EOF, "", line,column)
            else -> {
                if (char.isLetter()) {
                    val identifier = readIdentifier()
                    token = when (identifier) {
                        "let" -> Token(TokenType.LET, identifier, line,column)
                        "fn" -> Token(TokenType.FUNCTION, identifier, line,column)
                        "true" -> Token(TokenType.TRUE, identifier, line,column)
                        "false" -> Token(TokenType.FALSE, identifier, line,column)
                        "if" -> Token(TokenType.IF, identifier, line,column)
                        "else" -> Token(TokenType.ELSE, identifier, line,column)
                        "return" -> Token(TokenType.RETURN, identifier, line,column)
                        else -> Token(TokenType.IDENTIFIER, identifier, line,column)
                    }
                }
                if (char.isDigit()) {
                    val word = readInteger()
                    token = Token(TokenType.INTEGER, word, line,column)
                }
            }
        }

        readChar()

        return token
    }

    private fun skipWhitespace() {
        while (char.isWhitespace()) {
            readChar()
        }
    }

    private fun readIdentifier(): String = readWord { it.isLetter() }

    private fun readInteger(): String = readWord { it.isDigit() }

    private fun readWord(cond: (Char) -> Boolean): String {
        while (readPosition < input.length && cond(input[readPosition])) {
            readPosition += 1
        }
        return input.substring(position, readPosition)
    }

    private fun readChar() {
        char = peekChar()
        position = readPosition
        readPosition += 1
        column++
        if (char == '\n') {
            line++
            column = 0
        }
    }

    private fun peekChar() =
        if (readPosition >= input.length) {
            NULL_CHAR
        } else {
            input[readPosition]
        }
}