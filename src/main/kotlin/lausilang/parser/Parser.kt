package lausilang.parser

import lausilang.ast.BlockStatement
import lausilang.ast.BooleanLiteral
import lausilang.ast.Expression
import lausilang.ast.ExpressionStatement
import lausilang.ast.Identifier
import lausilang.ast.IfExpression
import lausilang.ast.InfixExpression
import lausilang.ast.IntegerLiteral
import lausilang.ast.LetStatement
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

val TOKEN_TYPE_PRECEDENCES: Map<TokenType, Int> = mapOf(
    TokenType.EQ to Precedence.EQUALS,
    TokenType.NEQ to Precedence.EQUALS,
    TokenType.LT to Precedence.LESS_GREATER,
    TokenType.GT to Precedence.LESS_GREATER,
    TokenType.PLUS to Precedence.SUM,
    TokenType.MINUS to Precedence.SUM,
    TokenType.SLASH to Precedence.PRODUCT,
    TokenType.ASTERISK to Precedence.PRODUCT,
)

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
        prefixParseFns[TokenType.INTEGER] = this::parseIntegerLiteral
        prefixParseFns[TokenType.TRUE] = this::parseBooleanLiteral
        prefixParseFns[TokenType.FALSE] = this::parseBooleanLiteral
        prefixParseFns[TokenType.BANG] = this::parsePrefixExpression
        prefixParseFns[TokenType.MINUS] = this::parsePrefixExpression
        prefixParseFns[TokenType.LPAREN] = this::parseGroupedExpression
        prefixParseFns[TokenType.IF] = this::parseIfExpression

        infixParseFns[TokenType.PLUS] = this::parseInfixExpression
        infixParseFns[TokenType.MINUS] = this::parseInfixExpression
        infixParseFns[TokenType.ASTERISK] = this::parseInfixExpression
        infixParseFns[TokenType.SLASH] = this::parseInfixExpression
        infixParseFns[TokenType.EQ] = this::parseInfixExpression
        infixParseFns[TokenType.NEQ] = this::parseInfixExpression
        infixParseFns[TokenType.LT] = this::parseInfixExpression
        infixParseFns[TokenType.GT] = this::parseInfixExpression
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
        TokenType.LBRACE -> parseBlockStatement()
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

        nextToken()

        val expression = parseExpression(Precedence.LOWEST)
        if (expression == null) {
            _errors.add("Could not parse expression")
            return null
        }

        return LetStatement(name, expression)
    }

    private fun parseReturnStatement(): Statement? {
        nextToken()

        val expression = parseExpression(Precedence.LOWEST)
        if (expression == null) {
            _errors.add("Could not parse expression")
            return null
        }

        return ReturnStatement(expression)
    }

    private fun parseExpressionStatement(): Statement? {
        val expression = parseExpression(Precedence.LOWEST) ?: return null
        expectPeek(TokenType.SEMICOLON)
        return ExpressionStatement(expression)
    }

    private fun parseBlockStatement(): Statement {
        nextToken()
        val statements = mutableListOf<Statement>()
        while (!currentTokenIs(TokenType.RBRACE)) {
            parseStatement()?.also {
                statements.add(it)
            }
            nextToken()
        }
        return BlockStatement(statements)
    }

    private fun parseIfExpression(): Expression? {
        if (!expectPeek(TokenType.LPAREN)) {
            return null
        }
        nextToken()

        val condition = parseExpression(Precedence.LOWEST)
        if (condition == null) {
            _errors.add("If statement has no condition")
            return null
        }

        if (!expectPeek(TokenType.RPAREN)) {
            return null
        }

        nextToken()

        val consequence = parseStatement()
        if (consequence == null) {
            _errors.add("If statement has no valid then block")
            return null
        }

        if (!peekTokenIs(TokenType.ELSE)) {
            return IfExpression(condition, consequence, null)
        }
        nextToken()
        nextToken()

        val alternative = parseStatement()

        return IfExpression(condition, consequence, alternative)
    }

    private fun parseExpression(precedence: Int): Expression? {
        val prefixParseFn = prefixParseFns[currentToken.type]
        if (prefixParseFn == null) {
            _errors.add("No prefix parser for operation '${currentToken.literal}' found.")
            return null
        }

        var leftExpression = prefixParseFn() ?: return null

        while (precedence < peekPrecedence()) {
            val infixParseFn = infixParseFns[peekToken.type] ?: return leftExpression
            nextToken()
            leftExpression = infixParseFn(leftExpression) ?: return null
        }

        return leftExpression
    }

    private fun parseIdentifierExpression(): Expression {
        return Identifier(currentToken.literal)
    }

    private fun parseIntegerLiteral(): Expression {
        return IntegerLiteral(currentToken.literal.toInt())
    }

    private fun parseBooleanLiteral(): Expression {
        return BooleanLiteral(currentToken.type == TokenType.TRUE)
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

    private fun parseInfixExpression(left: Expression): Expression? {
        val operator = currentToken.literal
        val precedence = currentPrecedence()

        nextToken()

        val right = parseExpression(precedence)
        if (right == null) {
            _errors.add("Could not parse right expression.")
            return null
        }

        return InfixExpression(operator, left, right)
    }

    private fun parseGroupedExpression(): Expression? {
        nextToken()
        val expression = parseExpression(Precedence.LOWEST)
        if (!expectPeek(TokenType.RPAREN)) {
            return null
        }
        return expression
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

    private fun currentPrecedence(): Int {
        return TOKEN_TYPE_PRECEDENCES[currentToken.type] ?: Precedence.LOWEST
    }

    private fun peekPrecedence(): Int {
        return TOKEN_TYPE_PRECEDENCES[peekToken.type] ?: Precedence.LOWEST
    }
}
