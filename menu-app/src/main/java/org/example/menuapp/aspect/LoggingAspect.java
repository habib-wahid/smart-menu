package org.example.menuapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.menuapp.annotation.LogEvent;
import org.example.menuapp.entity.Item;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution( * org.example.menuapp.service.OrderService.*(..))")
    public void logServiceInvocation() {
        System.out.println("Logging Service Invocation");
    }


    @Around("execution(* org.example.menuapp.service.ItemService.*(..))")
    public Object logItemCreation(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Logging Item Creation");
        Object result = joinPoint.proceed();
        System.out.println("Logging Item Creation");

        if (result instanceof Item) {
            System.out.println("Created item name: " + ((Item) result).getName());

        }
        return result;
    }


    @AfterReturning(
            pointcut = "@annotation(event)",
            returning = "result"
    )
    public void lafAfterReturning(Object result, LogEvent event) {
        System.out.println("Logging After Returning: " + event.action());

        if (result instanceof Item) {
            System.out.println("Logging after returning Created item name: " + ((Item) result).getName());
        }
    }
}
