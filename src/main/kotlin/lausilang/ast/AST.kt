package lausilang.ast

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

class PrefixExpression(
    val operator: String,
    val right: Expression
) : Expression {
    override fun format() = "$operator(${right.format()})"
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

