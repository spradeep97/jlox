package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static src.TokenType.*;

class Scanner
{
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    
    public Scanner(String source) 
    {
        this.source = source;
    }

    List<Token> scanTokens()
    {
        while (!isAtEnd())
        {
            // We are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));

        return tokens;
    }

    private boolean isAtEnd()
    {
        return current >= source.length();
    }
    
    private void scanToken()
    {
        char c = advance();
        switch(c)
        {
            // Single character tokens
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '/': addToken(SLASH); break;
            case '*': addToken(STAR); break;

            // Operators
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            
            // Special case lexemes
            case '#':
                while (peek() != '\n' && !isAtEnd()) advance();
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;

            default:
                Lox.error(line, "Unexpected character.");
                break;

            
        }
    }

    private char advance()
    {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type)
    {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal)
    {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected)
    {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek()
    {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }
}
