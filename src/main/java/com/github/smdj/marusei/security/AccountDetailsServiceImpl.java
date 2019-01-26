package com.github.smdj.marusei.security;

import com.github.smdj.marusei.domain.Credential;
import com.github.smdj.marusei.jpa.repository.CredentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService {
    public static final Logger log = LoggerFactory.getLogger(AccountDetailsServiceImpl.class);

    @Autowired
    private CredentialRepository credentialRepository;

    @Override

    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (log.isTraceEnabled()) {
            log.trace(String.format("loadUserByUserName args : username=%s", username));
        }

        Credential credential = credentialRepository.findOneByPublicKey(username);

        if (credential == null) {
            if (log.isInfoEnabled()) {
                log.info(String.format("login load failed : username=%s", username));
            }

            throw new UsernameNotFoundException(String.format("account does not exist : username=%s", username));
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("login load success : username=%s", username));
        }
        AccountDetails details = new BasicAccountDetails(
                credential.getAccount().getId(),
                credential.getAccount().getNickname(),
                credential.getAccount().getEmail(),
                credential.getSecretHash()
        );

        return details;
    }
}
