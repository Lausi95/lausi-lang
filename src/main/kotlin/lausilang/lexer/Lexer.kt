package lausilang.lexer

import lausilang.token.Token
import lausilang.token.TokenType

private const val NULL_CHAR = 0.toChar()
private val ILLEGAL = Token(TokenType.ILLEGAL, "")

class Lexer(private val input: String) {
    private var position: Int = 0
    private var readPosition: Int = 0
    private var char: Char = NULL_CHAR

    init {
        readChar()
    }

    fun nextToken(): Token {
        var token = ILLEGAL

        skipWhitespace()

        when (char) {
            ';' -> token = Token(TokenType.SEMICOLON, char.toString())
            ',' -> token = Token(TokenType.COMMA, char.toString())
            '=' -> {
                if (peekChar() == '=') {
                    val c = char
                    readChar()
                    token = Token(TokenType.EQ, "" + c + char)
                } else {
                    token = Token(TokenType.ASSIGN, char.toString())
                }
            }
            '+' -> token = Token(TokenType.PLUS, char.toString())
            '-' -> token = Token(TokenType.MINUS, char.toString())
            '!' -> {
                if (peekChar() == '=') {
                    val c = char
                    readChar()
                    token = Token(TokenType.NEQ, "" + c + char)

                } else {
                    token = Token(TokenType.BANG, char.toString())
                }
            }
            '/' -> token = Token(TokenType.SLASH, char.toString())
            '*' -> token = Token(TokenType.ASTERISK, char.toString())
            '<' -> token = Token(TokenType.LT, char.toString())
            '>' -> token = Token(TokenType.GT, char.toString())
            '(' -> token = Token(TokenType.LPAREN, char.toString())
            ')' -> token = Token(TokenType.RPAREN, char.toString())
            '{' -> token = Token(TokenType.LBRACE, char.toString())
            '}' -> token = Token(TokenType.RBRACE, char.toString())
            NULL_CHAR -> token = Token(TokenType.EOF, "")
            else -> {
                if (char.isLetter()) {
                    val identifier = readIdentifier()
                    token = when (identifier) {
                        "let" -> Token(TokenType.LET, identifier)
                        "fn" -> Token(TokenType.FUNCTION, identifier)
                        "true" -> Token(TokenType.TRUE, identifier)
                        "false" -> Token(TokenType.FALSE, identifier)
                        "if" -> Token(TokenType.IF, identifier)
                        "else" -> Token(TokenType.ELSE, identifier)
                        "return" -> Token(TokenType.RETURN, identifier)
                        else -> Token(TokenType.IDENTIFIER, identifier)
                    }
                }
                if (char.isDigit()) {
                    val word = readInteger()
                    token = Token(TokenType.INTEGER, word)
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
    }

    private fun peekChar() =
        if (readPosition >= input.length) {
            NULL_CHAR
        } else {
            input[readPosition]
        }
}