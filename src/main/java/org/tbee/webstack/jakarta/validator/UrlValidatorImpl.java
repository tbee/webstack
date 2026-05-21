package org.tbee.webstack.jakarta.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlValidatorImpl implements ConstraintValidator<UrlValidator, String> {

    public boolean isValid(String urlString, ConstraintValidatorContext constraintContext) {
        // "Null is not valid" is done using jakarta.validation.constraints.NotNull, so for this validator null is ok.
        if (urlString == null) {
            return true;
        }

        try {
            new URL(urlString).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }

    public static boolean isValid(String urlString) {
        return new UrlValidatorImpl().isValid(urlString, null);
    }
}