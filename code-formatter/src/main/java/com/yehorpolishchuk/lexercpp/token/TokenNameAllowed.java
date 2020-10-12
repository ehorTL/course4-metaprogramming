package com.yehorpolishchuk.lexercpp.token;

public enum TokenNameAllowed {
    KEYWORD,
    OPERATOR,
    PUNCTUATOR, // separator ()[]{} , ;
    COMMENT,
    IDENTIFIER,
    LITERAL_STRING,
    LITERAL_CHAR,
    LITERAL_NUMBER,
    PREPROCESSOR_DIR,
    TRIGRAPH,
    ERROR,

    // newly added
    LEFT_FIGURE_BRACKET,
    RIGHT_FIGURE_BRACKET,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,

    SPACES

}
