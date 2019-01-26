package com.github.smdj.marusei.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AccountDetailsService extends UserDetailsService {
    @Override
    AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
