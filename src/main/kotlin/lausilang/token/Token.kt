package lausilang.token

enum class TokenType {
    ILLEGAL,
    EOF,

    IDENTIFIER,
    INTEGER,

    // Operators
    ASSIGN,
    PLUS,
    MINUS,
    BANG,
    ASTERISK,
    SLASH,

    LT,
    GT,
    EQ,
    NEQ,

    COMMA,
    SEMICOLON,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,

    FUNCTION,
    LET,
    TRUE,
    FALSE,
    IF,
    ELSE,
    RETURN,
}

data class Token(
    val type: TokenType,
    val literal: String,
    val line: Int,
    val column: Int,
)