package app.web;

import app.exception.EmailAlreadyExistsException;
import app.exception.PasswordsNotMatchingException;
import app.exception.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameAlreadyExists(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("usernameAlreadyExistsMessage", exception.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExists(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("emailAlreadyExistsMessage", exception.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(PasswordsNotMatchingException.class)
    public String handlePasswordsNotMatch(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("passwordsNotMatchMessage", exception.getMessage());
        return "redirect:/register";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundExceptions() {
        return new ModelAndView("not-found-error");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;
    }
}
