package lausilang.parser

import lausilang.ast.Identifier
import lausilang.ast.LetStatement
import lausilang.ast.NullExpression
import lausilang.ast.Program
import lausilang.ast.ReturnStatement
import lausilang.ast.Statement
import lausilang.lexer.Lexer
import lausilang.token.Token
import lausilang.token.TokenType

class Parser(private val lexer: Lexer) {
    private var currentToken: Token
    private var peekToken: Token
    private val statements: MutableList<Statement> = mutableListOf()
    private val _errors: MutableList<String> = mutableListOf()
    val errors: List<String> get() = _errors

    init {
        currentToken = lexer.nextToken()
        peekToken = lexer.nextToken()
    }

    fun parseProgram(): Program {
        if (!statements.isEmpty()) {
            return Program(statements)
        }

        while (currentToken.type != TokenType.EOF) {
            parseStatement()?.let {
                statements.add(it)
            }
            nextToken()
        }

        return Program(statements)
    }

    private fun nextToken() {
        currentToken = peekToken
        peekToken = lexer.nextToken()
    }

    private fun parseStatement(): Statement? = when(currentToken.type) {
        TokenType.LET -> parseLetStatement()
        TokenType.RETURN -> parseReturnStatement()
        else -> {
            _errors.add("Could not parse statement.")
            null
        }
    }

    private fun parseLetStatement(): Statement? {
        if (!expectPeek(TokenType.IDENTIFIER)) {
            return null
        }

        val name = Identifier(currentToken.literal)

        if (!expectPeek(TokenType.ASSIGN)) {
            return null
        }

        while(!currentTokenIs(TokenType.SEMICOLON)) {
            nextToken()
        }

        return LetStatement(name, NullExpression())
    }

    private fun parseReturnStatement(): Statement? {
        while(!currentTokenIs(TokenType.SEMICOLON)) {
            nextToken()
        }

        return ReturnStatement(NullExpression())
    }

    private fun currentTokenIs(type: TokenType) = currentToken.type == type

    private fun peekTokenIs(type: TokenType) = peekToken.type == type

    private fun expectPeek(type: TokenType): Boolean {
        if (peekTokenIs(type)) {
            nextToken()
            return true
        } else {
            _errors.add("Expected next token to be of type: ${type}, but is ${peekToken.type}")
            return false
        }
    }
}
