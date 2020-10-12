package formatter.exceptions;

public class ConverterException extends Throwable{

    private String message;

    public ConverterException(String messsage) {
        this.message = messsage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
