package lausilang.parser

import lausilang.ast.BlockStatement
import lausilang.ast.BooleanLiteral
import lausilang.ast.Expression
import lausilang.ast.ExpressionStatement
import lausilang.ast.FunctionLiteral
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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
            let foobar = 13124421;
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
        assertEquals("Expected next token to be of type: ASSIGN, but is INTEGER, at Token(type=IDENTIFIER, literal=x, line=0, column=3)", parser.errors[0])
    }

    @Test
    fun parseIdentifier() {
        withParsedProgram("foobar;") { program ->
            program.statements[0].assertExpressionStatement {
                it.expression.assertIdentifier("foobar")
            }
        }
    }

    @Test
    fun parseIntegerLiteral() {
        withParsedProgram("5;") { program ->
            program.statements[0].assertExpressionStatement {
                it.expression.assertIntegerLiteral(5)
            }
        }
    }

    @Test
    fun parseBangPrefixExpression() {
        withParsedProgram("!5;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertOperatorExpression("!") { operatorExpression ->
                    operatorExpression.right.assertIntegerLiteral(5)
                }
            }
        }
    }

    @Test
    fun parseNegativePrefixExpression() {
        withParsedProgram("-15;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertOperatorExpression("-") { operatorExpression ->
                    operatorExpression.right.assertIntegerLiteral(15)
                }
            }
        }
    }

    @Test
    fun parsePlusInfixExpression() {
        withParsedProgram("5 + 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("+") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseMinusInfixExpression() {
        withParsedProgram("5 - 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("-") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseAsteriskInfixExpression() {
        withParsedProgram("5 * 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("*") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseSlashInfixExpression() {
        withParsedProgram("5 / 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("/") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseGtInfixExpression() {
        withParsedProgram("5 > 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression(">") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseLtInfixExpression() {
        withParsedProgram("5 < 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("<") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseEqInfixExpression() {
        withParsedProgram("5 == 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("==") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseNeqInfixExpression() {
        withParsedProgram("5 != 10;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertInfixExpression("!=") { operatorExpression ->
                    operatorExpression.left.assertIntegerLiteral(5)
                    operatorExpression.right.assertIntegerLiteral(10)
                }
            }
        }
    }

    @Test
    fun parseTrueBooleanLiteral() {
        withParsedProgram("true;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertBooleanLiteral(true)
            }
        }
    }

    @Test
    fun parseFalseBooleanLiteral() {
        withParsedProgram("false;") { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertBooleanLiteral(false)
            }
        }
    }

    @Test
    fun parseComplexExpression() {
        withParsedProgram("5 + 10 * 3 == -5 * 10 / 3 + 5 * 3;") { program ->
            assertEquals(
                "((5) + ((10) * (3))) == ((((-(5)) * (10)) / (3)) + ((5) * (3)))",
                program.statements[0].format()
            )
        }
    }

    @Test
    fun parseUngroupedExpression() {
        withParsedProgram("5 + 5 * 3;") { program ->
            assertEquals("(5) + ((5) * (3))", program.statements[0].format())
        }
    }

    @Test
    fun parseGroupedExpression() {
        withParsedProgram("(5 + 5) * 3;") { program ->
            assertEquals("((5) + (5)) * (3)", program.statements[0].format())
        }
    }

    @Test
    fun parseFullIfStatement() {
        withParsedProgram(
            """
            if (a > b) {
                a;
            } else {
                b;
            };
        """.trimIndent()
        ) { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertIfExpression { ifExpression ->
                    ifExpression.consequence.assertBlockStatement { blockStatement ->
                        blockStatement.statements[0].assertExpressionStatement {
                            it.expression.assertIdentifier("a")
                        }
                    }
                    assertNotNull(ifExpression.alternative)
                    ifExpression.alternative.assertBlockStatement { blockStatement ->
                        blockStatement.statements[0].assertExpressionStatement {
                            it.expression.assertIdentifier("b")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun parseIfStatementWithNoElse() {
        withParsedProgram(
            """
            if (a > b) {
                a;
            };
        """.trimIndent()
        ) { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertIfExpression { ifExpression ->
                    ifExpression.consequence.assertBlockStatement { blockStatement ->
                        blockStatement.statements[0].assertExpressionStatement {
                            it.expression.assertIdentifier("a")
                        }
                    }
                    assertNull(ifExpression.alternative)
                }
            }
        }
    }

    @Test
    fun parseFunctionLiteral() {
        withParsedProgram(
            """
            fn (a, b, c) {
                return a + b + c;
            };
            """.trimIndent()
        ) { program ->
            program.statements[0].assertExpressionStatement { expressionStatement ->
                expressionStatement.expression.assertFunctionLiteral { functionLiteral ->
                    assertEquals(3, functionLiteral.parameters.size)
                    functionLiteral.parameters[0].assertIdentifier("a")
                    functionLiteral.parameters[1].assertIdentifier("b")
                    functionLiteral.parameters[2].assertIdentifier("c")
                    assertEquals(1, functionLiteral.body.statements.size)
                    functionLiteral.body.statements[0].assertReturnStatement()
                }
            }
        }
    }
}

fun withParsedProgram(code: String, statements: Int = 1, onSuccess: (Program) -> Unit) {
    val lexer = Lexer(code)
    val parser = Parser(lexer)
    val program = parser.parseProgram()
    assertNoParseErrors(parser)
    assertEquals(statements, program.statements.size)
    onSuccess(program)
}

fun assertNoParseErrors(parser: Parser) {
    if (parser.errors.isNotEmpty()) {
        fail("Expected no parse errors. But got ${parser.errors.size} errors:\n${parser.errors.joinToString("\n")}")
    }
}

fun Statement.assertExpressionStatement(onSuccess: (ExpressionStatement) -> Unit = {}) {
    assertTrue(
        this is ExpressionStatement,
        "Expected Statement to be ExpressionStatement, got ${this::class.simpleName}"
    )
    onSuccess(this)
}

fun Expression.assertIntegerLiteral(expectedValue: Int, onSuccess: (IntegerLiteral) -> Unit = {}) {
    assertTrue(this is IntegerLiteral, "Expected Expression to be IntegerLiteral, got ${this::class.simpleName}")
    assertEquals(expectedValue, this.value, "Expected integer literal value '$expectedValue', got ${this.value}")
    onSuccess(this)
}

fun Expression.assertBooleanLiteral(expectedValue: Boolean, onSuccess: (BooleanLiteral) -> Unit = {}) {
    assertTrue(this is BooleanLiteral, "Expected Expression to be BooleanLiteral, got ${this::class.simpleName}")
    assertEquals(expectedValue, this.value, "Expected boolean literal value '$expectedValue', got ${this.value}")
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

fun Expression.assertInfixExpression(expectedOperator: String, onSuccess: (InfixExpression) -> Unit = {}) {
    assertTrue(this is InfixExpression, "Expected Expression to be InfixExpression, got ${this::class.simpleName}")
    assertEquals(expectedOperator, this.operator, "Expected operator to be '$expectedOperator', got '${this.operator}'")
    onSuccess(this)
}

fun Statement.assertBlockStatement(onSuccess: (BlockStatement) -> Unit = {}) {
    assertTrue(this is BlockStatement, "Expected Statement to be BlockStatement, got ${this::class.simpleName}")
    onSuccess(this)
}

fun Statement.assertReturnStatement(onSuccess: (ReturnStatement) -> Unit = {}) {
    assertTrue(this is ReturnStatement, "Expected ReturnStatement, got ${this::class.simpleName}")
    onSuccess(this)
}

fun Expression.assertIfExpression(onSuccess: (IfExpression) -> Unit = {}) {
    assertTrue(this is IfExpression, "Expected Expression to be IfExpression, got ${this::class.simpleName}")
    onSuccess(this)
}

fun Expression.assertFunctionLiteral(onSuccess: (FunctionLiteral) -> Unit = {}) {
    assertTrue(this is FunctionLiteral, "Expected FunctionLiteral, got ${this::class.simpleName}")
    onSuccess(this)
}
