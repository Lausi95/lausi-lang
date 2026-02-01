package lausilang

import lausilang.lexer.Lexer
import lausilang.token.TokenType
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    while (true) {
        print(">> ")
        val line = scanner.nextLine()
        if (line.trim() == "exit") {
            break
        }

        val lexer = Lexer(line)
        while (true) {
            val token = lexer.nextToken()
            if (token.type == TokenType.EOF) {
                break
            }
            println(token)
        }
    }
}