package com.yehorpolishchuk.lexercpp.lexer;

import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenName;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import formatter.filewriter.TokenFileWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class LexerTest {
    public static void main(String[] args) throws IOException {

//        String s = "#include            \"mylib\"";
//        System.out.println(s.matches("[    ]*#[    ]*include(.)*"));

        if (true) return;

        String fileFullName = "C:\\Users\\user\\Desktop\\course4\\metaprogramming\\code-formatter\\src\\main\\resources\\testdata\\input\\test.cpp";
        Lexer lexer = new Lexer (fileFullName);
        lexer.parse();

        ArrayList<Token> tokens = lexer.getTokens();
        for (Token token : tokens ){
            System.out.println(token.getName().getTokenName().toString() + " --- " + token);
        }

        ListIterator<Token> it = tokens.listIterator();
        boolean isNewlineOrBlankline = false;
        while(it.hasNext()){
            Token t = it.next();
            if (!(t.getName().getTokenName() == TokenNameAllowed.NEWLINE || t.getName().getTokenName() == TokenNameAllowed.BLANK_LINE)){
                it.add(new Token(TokenNameAllowed.NEWLINE, "\n"));
            }
        }

        TokenFileWriter tokenFileWriter = new TokenFileWriter(tokens, "C:\\Users\\user\\Desktop\\course4\\metaprogramming\\code-formatter\\src\\main\\resources\\testdata\\input\\tt.cpp");
        tokenFileWriter.write();
    }

}
