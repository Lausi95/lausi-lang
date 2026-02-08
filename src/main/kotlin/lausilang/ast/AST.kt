package lausilang.ast

import javax.swing.plaf.nimbus.State

interface Node {
    fun format(): String
}

interface Statement: Node

interface Expression: Node

class ExpressionStatement(
    val expression: Expression,
): Statement {
    override fun format() = expression.format()
}

class LetStatement(
    val name: Identifier,
    val value: Expression,
) : Statement {
    override fun format() = "let ${name.name} = ${value.format()};"
}

class ReturnStatement(
    val returnValue: Expression,
): Statement {
    override fun format() = "return ${returnValue.format()};"
}

class BlockStatement(
    val statements: List<Statement>
): Statement {
    override fun format() = "{\n" + statements.joinToString("\n    ") { it.format() } + "\n}"
}

class IfExpression(
    val condition: Expression,
    val consequence: Statement,
    val alternative: Statement?
): Expression {
    override fun format(): String {
        return "if (${condition.format()}) ${consequence.format()}" + (alternative?.let { " else ${it.format()} " } ?: "")
    }
}

class Identifier(
    val name: String,
): Expression {
    override fun format() = name
}

class IntegerLiteral(
    val value: Int,
) : Expression {
    override fun format() = value.toString()
}

class BooleanLiteral(
    val value: Boolean,
): Expression {
    override fun format() = value.toString()
}

class PrefixExpression(
    val operator: String,
    val right: Expression
) : Expression {
    override fun format() = "$operator(${right.format()})"
}

class FunctionLiteral(
    val parameters: List<Identifier>,
    val body: BlockStatement,
): Expression {
    override fun format() = "fn (${parameters.joinToString(", ") { it.format() }}) ${body.format()}"
}

class InfixExpression(
    val operator: String,
    val left: Expression,
    val right: Expression,
): Expression {
    override fun format() = "(${left.format()}) $operator (${right.format()})"
}

class NullExpression : Expression {
    override fun format() = "null"
}

class Program(val statements: List<Statement>) {

    fun format(): String = statements.joinToString("\n") { it.format() }
}

