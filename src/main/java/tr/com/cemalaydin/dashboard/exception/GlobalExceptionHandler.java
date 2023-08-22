package tr.com.cemalaydin.dashboard.exception;


import tr.com.cemalaydin.dashboard.base.DataResult;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        List<Map<String, String>> collect = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(fieldError -> Map.of(fieldError.getField(), fieldError.getDefaultMessage()))
//                .collect(Collectors.toList());
//        DataResult res = new DataResult("", "VALIDATION_ERRORS");
//        res.setMessageList(collect);
//        return new ResponseEntity(res, HttpStatusCode.valueOf(400));
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Map<String, String>> collect = ex.getConstraintViolations()
                .stream()
                .map(fieldError -> Map.of(fieldError.getPropertyPath().toString(), fieldError.getMessage()))
                .collect(Collectors.toList());
        DataResult res = new DataResult("", "VALIDATION_ERRORS");
        res.setMessageList(collect);
        return new ResponseEntity(res, HttpStatusCode.valueOf(400));
    }
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleConstrainException(TransactionSystemException ex) {
        if (ex.getRootCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getRootCause();
            return handleConstraintViolationException(constraintViolationException,null,null, null);
        }
        return handleAllOtherExceptions(ex);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        DataResult res = new DataResult("", "VALIDATION_ERRORS");
//
//        String s = ex.getMostSpecificCause().getMessage().split("\n")[1];
//        s = s.substring(s.indexOf("("));
//        s= s.substring(0, s.lastIndexOf(")"));
//        s= s.replace("(","");
//        s= s.replace(")","");
//        Map<String, Object> map = new HashMap<>();
//        map.put(s.split("=")[0],"Kayıt mevcut");
//        res.setMessageList(Arrays.asList(map));
//        return new ResponseEntity(res, HttpStatusCode.valueOf(400));
//    }

    @ExceptionHandler(Exception.class)
    ResponseEntity handleAllOtherExceptions(Exception e) {
        return new ResponseEntity(new DataResult("", e.getMessage()), HttpStatusCode.valueOf(400));
    }


}


//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.nio.file.AccessDeniedException;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {
//
//
//
//
////    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
////    protected ResponseEntity<Object> handleAllExceptions(RuntimeException ex, WebRequest request) {
////        String bodyOfResponse = "This should be application specific";
////        return handleExceptionInternal(ex, bodyOfResponse,
////                new HttpHeaders(), HttpStatus.CONFLICT, request);
////    }
////
////    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
////    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
////        String bodyOfResponse = "This should be application specific";
////        return handleExceptionInternal(ex, bodyOfResponse,
////                new HttpHeaders(), HttpStatus.CONFLICT, request);
////    }
////
////    @ExceptionHandler(value = {RuntimeException.class})
////    protected ResponseEntity<Object> handleRuntimeExceptions(RuntimeException ex, WebRequest request) {
////        String bodyOfResponse = "This should be application specific";
////        return handleExceptionInternal(ex, bodyOfResponse,
////                new HttpHeaders(), HttpStatus.CONFLICT, request);
////    }
//
//
//
////    @ResponseStatus(HttpStatus.BAD_REQUEST)
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public Map<String, String> handleValidationExceptions(
////            MethodArgumentNotValidException ex) {
////        Map<String, String> errors = new HashMap<>();
////        ex.getBindingResult().getAllErrors().forEach((error) -> {
////            String fieldName = ((FieldError) error).getField();
////            String errorMessage = error.getDefaultMessage();
////            errors.put(fieldName, errorMessage);
////        });
////        return errors;
////    }
//}
