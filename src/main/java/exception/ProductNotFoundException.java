package exception;
// creating our own exception
public class ProductNotFoundException extends Exception {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }


