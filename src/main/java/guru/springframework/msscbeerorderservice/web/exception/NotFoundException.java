package guru.springframework.msscbeerorderservice.web.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public NotFoundException(String message) {
        super(message);
    }
	
	public NotFoundException(Throwable t) {
        super(t);
    }
	
}
