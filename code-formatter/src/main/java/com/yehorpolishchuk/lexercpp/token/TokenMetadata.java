package com.yehorpolishchuk.lexercpp.token;

public class TokenMetadata {
    private int lineNumber;
    private int linePos;
    private boolean isFirstFromLineStart;
    private String message = null;

    private static final int POS_DEFAULT = -1;

    public TokenMetadata(int line) {
        this.lineNumber = line;
        this.linePos = POS_DEFAULT;
    }

    public TokenMetadata(int line, int linePos) {
        this.lineNumber = line;
        this.linePos = linePos;
    }

    public void setFirstFromLineStart(boolean firstFromLineStart) {
        isFirstFromLineStart = firstFromLineStart;
    }

    public boolean isFirstFromLineStart() {
        return isFirstFromLineStart;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public String toString() {
        return "META: --- " + "line: " + this.lineNumber + " --- " + "linepos: " + this.linePos + " --- " +
                "first?: " + this.isFirstFromLineStart;
    }

    public void setLinePos(int linePos) {
        this.linePos = linePos;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLinePos() {
        return linePos;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}

