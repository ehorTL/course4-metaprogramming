package formatter.exceptions;

public class DialogException extends Throwable{
    private String message;

    public DialogException(String messsage) {
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



