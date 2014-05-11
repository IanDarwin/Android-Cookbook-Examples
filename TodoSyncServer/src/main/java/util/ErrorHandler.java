package util;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class ErrorHandler {

	public Throwable getException() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();

		Throwable exception = (Throwable) requestMap.get("javax.servlet.error.exception");
		return exception;
	}
}
