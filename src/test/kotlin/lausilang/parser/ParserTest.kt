package lausilang.parser

import lausilang.ast.LetStatement
import lausilang.ast.ReturnStatement
import lausilang.lexer.Lexer
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

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

        assertEquals(3, parser.errors.size)
        assertEquals("Expected next token to be of type: ASSIGN, but is INTEGER", parser.errors[0])
        assertEquals("Could not parse statement.", parser.errors[1])
        assertEquals("Could not parse statement.", parser.errors[2])
    }
}