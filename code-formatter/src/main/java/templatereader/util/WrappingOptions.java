package templatereader.util;

public enum WrappingOptions {
    END_OF_LINE,
    NEXT_LINE_IF_WRAPPED,
    NEXT_LINE,
    NEXT_LINE_SHIFTED,
    NEXT_LINE_EACH_SHIFTED,

    DO_NOT_WRAP,
    WRAP_IF_LONG,
    CHOP_DOWN_OF_LONG,
    WRAP_ALWAYS,

    NEVER,
    ALWAYS,
    IF_LONG,
}
