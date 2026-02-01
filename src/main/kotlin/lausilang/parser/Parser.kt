package lausilang.parser

import lausilang.ast.Expression
import lausilang.ast.ExpressionStatement
import lausilang.ast.Identifier
import lausilang.ast.IntegerLiteral
import lausilang.ast.LetStatement
import lausilang.ast.NullExpression
import lausilang.ast.PrefixExpression
import lausilang.ast.Program
import lausilang.ast.ReturnStatement
import lausilang.ast.Statement
import lausilang.lexer.Lexer
import lausilang.token.Token
import lausilang.token.TokenType

typealias PrefixParseFn = () -> Expression?
typealias InfixParseFn = (Expression) -> Expression?

object Precedence {
    const val LOWEST = 0
    const val EQUALS = 1
    const val LESS_GREATER = 2
    const val SUM = 3
    const val PRODUCT = 4
    const val PREFIX = 5
    const val CALL = 6
}

class Parser(private val lexer: Lexer) {
    private var currentToken: Token
    private var peekToken: Token
    private val statements: MutableList<Statement> = mutableListOf()

    private val prefixParseFns: MutableMap<TokenType, PrefixParseFn> = mutableMapOf()
    private val infixParseFns: MutableMap<TokenType, InfixParseFn> = mutableMapOf()

    private val _errors: MutableList<String> = mutableListOf()
    val errors: List<String> get() = _errors

    init {
        currentToken = lexer.nextToken()
        peekToken = lexer.nextToken()

        prefixParseFns[TokenType.IDENTIFIER] = this::parseIdentifierExpression
        prefixParseFns[TokenType.INTEGER] = this::parseIntegerExpression
        prefixParseFns[TokenType.BANG] = this::parsePrefixExpression
        prefixParseFns[TokenType.MINUS] = this::parsePrefixExpression
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
        else -> parseExpressionStatement()
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

    private fun parseReturnStatement(): Statement {
        while(!currentTokenIs(TokenType.SEMICOLON)) {
            nextToken()
        }

        return ReturnStatement(NullExpression())
    }

    private fun parseExpressionStatement(): Statement? {
        val expression = parseExpression(Precedence.LOWEST) ?: return null
        expectPeek(TokenType.SEMICOLON)
        return ExpressionStatement(expression)
    }

    private fun parseExpression(precedence: Int): Expression? {
        val prefixParseFn = prefixParseFns[currentToken.type]
        if (prefixParseFn == null) {
            _errors.add("No prefix parser for operation '${currentToken.literal}' found.")
            return null
        }

        val leftExpression = prefixParseFn()

        return leftExpression
    }

    private fun parseIdentifierExpression(): Expression {
        return Identifier(currentToken.literal)
    }

    private fun parseIntegerExpression(): Expression {
        return IntegerLiteral(currentToken.literal.toInt())
    }

    private fun parsePrefixExpression(): Expression? {
        val operator = currentToken.literal
        nextToken()
        val rightExpression = parseExpression(Precedence.PREFIX)
        if (rightExpression == null) {
            _errors.add("Could not parse right operation.")
            return null
        }
        return PrefixExpression(operator, rightExpression)
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
