package api.phonecontacts.exception;

public class FileNotExistException extends RuntimeException{
    public FileNotExistException(String message) {
        super(message);
    }
}
