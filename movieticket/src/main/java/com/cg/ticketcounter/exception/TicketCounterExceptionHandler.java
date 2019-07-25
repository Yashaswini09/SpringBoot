package com.cg.ticketcounter.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ControllerAdvice
public class TicketCounterExceptionHandler extends ResponseEntityExceptionHandler implements ThrowsAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(com.cg.ticketcounter.web.UserController.class);

	@AfterThrowing(pointcut = "execution(* com.cg.ticketcounter.web.UserController.login(..))", throwing = "exc")
	public ModelAndView exceptionHandling(JoinPoint jp, Exception exc) throws Throwable {
		System.err.println("Inside CatchThrowException.afterThrowing() method...");
		System.err.println("Running after throwing exception...");
		System.err.println("Exception : " + exc);
		LOGGER.error("Exception Occured " + exc);
		return new ModelAndView("error", "errorMessage", exc);

	}

	@Before("execution(* com.cg.ticketcounter.web.UserController.*(..))")
	public void logBeforeAllMethods(JoinPoint joinPoint) {
		LOGGER.trace("Logging :Entered Method" + joinPoint.getSignature().getName());
	}

	@After("execution(* com.cg.ticketcounter.web.UserController.*(..))")
	public void logAfterAllMethods(JoinPoint joinPoint) {
		LOGGER.trace("Logging :Closed Method " + joinPoint.getSignature().getName());
		//System.out.println("****LoggingAspect.logAfterAllMethods() : " + joinPoint.getSignature().getName());
	}

	@ExceptionHandler(value = TicketCounterException.class)
	public String handleMyDataException(TicketCounterException e) {

		LOGGER.error("Checked exception occurred", e);
		ExceptionResult result = new ExceptionResult(e.getCode(), e.getStatus());
		new com.cg.ticketcounter.web.UserController();
		System.err.println("Handling Exception");
		
		return "redirect:/error?type="+result.getCode()+"&"+"status="+result.getStatus();
	}
	
  
    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected String handleValidationErrors(Exception e) {                    
                ConstraintViolationException nevEx = (ConstraintViolationException)e;
                StringBuilder message = new StringBuilder();
                Set<ConstraintViolation<?>> violations  = nevEx.getConstraintViolations();
                for (ConstraintViolation<?> violation : violations) {
                      message.append(violation.getMessage().concat(";"));
                }
                String errorMessage = message.toString();
                ExceptionResult result = new ExceptionResult(HttpStatus.BAD_REQUEST.value(), errorMessage);
                return "redirect:/error?type="+result.getCode()+"&"+"status="+result.getStatus();
    }
   
  
    @ExceptionHandler(value = Exception.class)  
    public String handleConflict(Exception e){              
                LOGGER.error("Runtime exception occurred",e);        
                ExceptionResult result = new ExceptionResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Application encountered runtime exception, please contact system administrator");
                return "redirect:/error?type="+result.getCode()+"&"+"status="+result.getStatus();
 
    }
    
    @ExceptionHandler(value = AccessDeniedException.class)  
    public String accessDeniedConflict(Exception e){              
                LOGGER.error("AccessDeniedException occurred",e);        
                ExceptionResult result = new ExceptionResult(HttpStatus.UNAUTHORIZED.value(),"Access denied, please contact system administrator");
                return "redirect:/error?type="+result.getCode()+"&"+"status="+result.getStatus();
 
    }
    
    


	
}
