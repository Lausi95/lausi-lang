package lausilang.parser

import lausilang.ast.Expression
import lausilang.ast.ExpressionStatement
import lausilang.ast.Identifier
import lausilang.ast.IntegerLiteral
import lausilang.ast.LetStatement
import lausilang.ast.PrefixExpression
import lausilang.ast.ReturnStatement
import lausilang.ast.Statement
import lausilang.lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ParserTest {

    @Test
    fun testLetStatement() {
        val code = "let x = 5;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()

        assertEquals(1, program.statements.size)
        val statement = program.statements[0]
        assertTrue(statement is LetStatement)
        statement as LetStatement
    }

    @Test
    fun testReturnStatement() {
        val code = "return 5;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()

        assertEquals(1, program.statements.size)
        val statement = program.statements[0]
        assertTrue(statement is ReturnStatement)
    }

    @Test
    fun testMultipleStatement() {
        val code = """
            let x = 5;
            let y = 10;
            let foobar = 13123124421;
        """.trimIndent()

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()

        assertEquals(3, program.statements.size)
    }

    @Test
    fun testErrorMessage() {
        val code = "let x 5;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        parser.parseProgram()

        assertEquals(1, parser.errors.size)
        assertEquals("Expected next token to be of type: ASSIGN, but is INTEGER", parser.errors[0])
    }

    @Test
    fun parseIdentifierExpression() {
        val code = "foobar;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()
        assertNoParseErrors(parser)

        assertEquals(1, program.statements.size)
        program.statements[0].assertExpressionStatement {
            it.expression.assertIdentifier("foobar")
        }
    }

    @Test
    fun parseIntegerExpression() {
        val code = "5;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()
        assertNoParseErrors(parser)

        assertEquals(1, program.statements.size)
        program.statements[0].assertExpressionStatement {
            it.expression.assertIntegerLiteral(5)
        }
    }

    @Test
    fun parseBangPrefixExpression() {
        val code = "!5;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()
        assertNoParseErrors(parser)

        assertEquals(1, program.statements.size)
        program.statements[0].assertExpressionStatement { expressionStatement ->
            expressionStatement.expression.assertOperatorExpression("!") { operatorExpression ->
                operatorExpression.right.assertIntegerLiteral(5)
            }
        }
    }

    @Test
    fun parseNegativePrefixExpression() {
        val code = "-15;"

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val program = parser.parseProgram()
        assertNoParseErrors(parser)

        assertEquals(1, program.statements.size)
        program.statements[0].assertExpressionStatement { expressionStatement ->
            expressionStatement.expression.assertOperatorExpression("-") { operatorExpression ->
                operatorExpression.right.assertIntegerLiteral(15)
            }
        }
    }
}

fun assertNoParseErrors(parser: Parser) {
    if (parser.errors.isNotEmpty()) {
        fail("Expected no parse errors. But got ${parser.errors.size} errors:\n${parser.errors.joinToString("\n")}")
    }
}

fun Statement.assertExpressionStatement(onSuccess: (ExpressionStatement) -> Unit = {}) {
    assertTrue(this is ExpressionStatement, "Expected Statement to be ExpressionStatement, got ${this::class.simpleName}")
    onSuccess(this)
}

fun Expression.assertIntegerLiteral(expectedValue: Int, onSuccess: (IntegerLiteral) -> Unit = {}) {
    assertTrue(this is IntegerLiteral, "Expected Expression to be IntegerLiteral, got ${this::class.simpleName}")
    assertEquals(expectedValue, this.value, "Expected integer literal value '$expectedValue', got ${this.value}")
    onSuccess(this)
}

fun Expression.assertIdentifier(expectedValue: String, onSuccess: (Identifier) -> Unit = {}) {
    assertTrue(this is Identifier, "Expected Expression to be Identifier, got ${this::class.simpleName}")
    assertEquals(expectedValue, this.name, "Expected identifier name '$expectedValue', got '${this.name}'")
    onSuccess(this)
}

fun Expression.assertOperatorExpression(expectedOperator: String, onSuccess: (PrefixExpression) -> Unit = {}) {
    assertTrue(this is PrefixExpression, "Expected Expression to be PrefixExpression, got ${this::class.simpleName}")
    assertEquals(expectedOperator, this.operator, "Expected operator to be '$expectedOperator', got '${this.operator}'")
    onSuccess(this)
}