/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.haftrust.verifier.validator;

import java.util.ArrayList;
import java.util.List;
import org.haftrust.verifier.model.Verifier;
import org.haftrust.verifier.service.VerifierService;
import org.haftrust.verifier.view.RegisterVerifierBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Miroslav
 */
public class LogInValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(LogInValidator.class);

    private VerifierService verifierService;

    public VerifierService getVerifierService() {
        return verifierService;
    }

    public void setVerifierService(VerifierService verifierService) {
        this.verifierService = verifierService;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(RegisterVerifierBean.class);
    }

    public void validate(Object command, Errors errors) {
        RegisterVerifierBean rvBean = (RegisterVerifierBean) command;

        LOG.debug("--------------------------login validator validate method");

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "required.email",
                "Email is required.");

        LOG.debug("--------------------------login validator validate get field error:{}", errors.getFieldError("email"));
        errors.getFieldError("email");

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "required.password",
                "Password is required.");

        if (!rvBean.getEmail().isEmpty() && !rvBean.getPassword().isEmpty()) {
            List<Verifier> vl = new ArrayList<>();

            vl = this.verifierService.isVerifier(rvBean.getEmail(), rvBean.getPassword());

            LOG.debug("--------------------------login validator list size:{}", vl.size());

            if (vl.isEmpty()) {
                errors.rejectValue("email", "required.email", "The Email or Password entered is incorrect.");
            }
        }

        LOG.debug("--------------------------login validator validate get error count:{}", errors.getErrorCount());
    }
}
