package com.mj.ddingdong.account.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.domain.UserAccount;
import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account signUp(SignUpForm signUpForm) {
        Account newAccount = saveAccount(signUpForm);
        return newAccount;
    }

    private Account saveAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .signUpId(signUpForm.getSignUpId())
                .name(signUpForm.getName())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .signUpAsManager(signUpForm.isSignUpAsManager())
                .signUpAt(LocalDateTime.now())
                .build();
        
        return  accountRepository.save(account);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
