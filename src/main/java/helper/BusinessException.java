package helper;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public abstract class BusinessException extends RuntimeException {
}