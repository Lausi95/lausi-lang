package lausilang.parser

import lausilang.ast.ExpressionStatement
import lausilang.ast.Identifier
import lausilang.ast.LetStatement
import lausilang.ast.ReturnStatement
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
        val expressionStatement = program.statements[0]
        assertTrue(expressionStatement is ExpressionStatement)

        assertTrue(expressionStatement.expression is Identifier, "Expression is no identifier")
        assertEquals("foobar", expressionStatement.expression.name)
    }

}

fun assertNoParseErrors(parser: Parser) {
    if (parser.errors.isNotEmpty()) {
        fail("Expected no parse errors. But got ${parser.errors.size} errors:\n${parser.errors.joinToString("\n")}")
    }
}