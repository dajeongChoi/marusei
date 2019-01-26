package com.github.smdj.marusei.controller;

import com.github.smdj.marusei.controller.request.SignUpRequest;
import com.github.smdj.marusei.domain.Account;
import com.github.smdj.marusei.service.AccountService;
import com.github.smdj.marusei.service.params.CreateAccountParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Controller
class IndexControllerImpl implements IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexControllerImpl.class);

    @Autowired
    private AccountService accountService;

    @Override
    public String index() {
        return "page/index-user";
    }

    @Override
    public String signUpForm(Model model) {
        if (log.isTraceEnabled()) {
            log.trace("signUpForm Model = {}", model);
        }

        model.addAttribute("signUpRequest", new SignUpRequest());

        return "page/signup";
    }

    @Override
    public String signUp(@ModelAttribute("signUpRequest") @Valid SignUpRequest signUpRequest, BindingResult bindingResult, Model model) {
        if (log.isTraceEnabled()) {
            log.trace("signUpRequest = {}, bindingResult = {}, signUpModel = {}", signUpRequest, bindingResult, model);
        }

        if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().equals(signUpRequest.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("signUpRequest", "passwordConfirm", "패스워드가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "page/signup";
        }

        CreateAccountParams createAccountParams = new CreateAccountParams(signUpRequest.getEmail(),
                signUpRequest.getNickname(), signUpRequest.getPassword());
        Account account = accountService.create(createAccountParams);

        if (account == null) {
            throw new RuntimeException("signup 데이터 db 입력 실패");
        }

        return "redirect:/login";
    }
}
