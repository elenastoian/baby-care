package com.baby.care.service.security;

import com.baby.care.controller.repsonse.AuthenticationResponse;
import com.baby.care.controller.repsonse.TokenConfirmationResponse;
import com.baby.care.controller.request.AuthenticationRequest;
import com.baby.care.controller.request.RegisterRequest;
import com.baby.care.model.AppUser;
import com.baby.care.model.ConfirmationToken;
import com.baby.care.model.Token;
import com.baby.care.model.enums.TokenType;
import com.baby.care.repository.AppUserRepository;
import com.baby.care.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    static final String EMAIL_IS_ALREADY_USED_ERROR = "EMAIL IS ALREADY USED";
    static final String EMAIL_IS_NOT_VALID_ERROR = "EMAIL IS NOT VALID";

    static final String EMAIL_OR_PASSWORD_IS_NOT_VALID_ERROR = "EMAIL OR PASSWORD IS NOT VALID";

    static final String EMAIL_IS_NOT_ENABLED_ERROR = "EMAIL IS NOT ENABLED";


    //TODO: change it to port 4200 when the frontend will be implemented
    @Value("${frontend_host_url}")
    private String frontendHostURL;

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final EmailValidatorService emailValidatorService;
    private final EmailService emailService;

    private final ConfirmationTokenService confirmationTokenService;

    @Transactional
    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {

        if (!emailValidatorService.testIfEmailIsValid(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().token(EMAIL_IS_NOT_VALID_ERROR).build());
        }

        if(appUserRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().token(EMAIL_IS_ALREADY_USED_ERROR).build());
        }

        AppUser appUser = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build();

        AppUser savedAppUser = appUserRepository.save(appUser);

        String jwtToken = jwtService.generateToken(appUser);

        saveUserToken(savedAppUser, jwtToken);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        final String link = frontendHostURL + "/auth/confirm?token=" + token;

        this.emailService.send(appUser.getEmail(), emailService.buildEmail(link));

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder().token(jwtToken).build());
    }

    private void saveUserToken(AppUser savedAppUser, String jwtToken) {
        Token token = Token.builder()
                .user(savedAppUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {

        if(!appUserRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder()
                    .token(EMAIL_OR_PASSWORD_IS_NOT_VALID_ERROR)
                    .build());
        }

        if(!appUserRepository.findByEmail(request.getEmail()).get().getIsEnabled()){
            return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder()
                    .token(EMAIL_IS_NOT_ENABLED_ERROR)
                    .build());
        }

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //TODO: throw the correct exception and handle it
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow();

        String jwtToken = jwtService.generateToken(appUser);
        saveUserToken(appUser, jwtToken);

        return ResponseEntity.status(HttpStatus.OK).body(AuthenticationResponse.builder()
                        .id(appUser.getId())
                        .email(appUser.getEmail())
                .token(jwtToken)
                .build());
    }

    public ResponseEntity<TokenConfirmationResponse> confirmToken(String token) {

        Optional<ConfirmationToken> tokenFound;
        boolean tokenExists = confirmationTokenService.getToken(token).isPresent();

        if (tokenExists) {

            tokenFound = confirmationTokenService.getToken(token);
            ConfirmationToken tokenObject = tokenFound.get();

            if (tokenObject.getConfirmedAt() != null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new TokenConfirmationResponse(
                        false
                ));
            }

            LocalDateTime expiredAt = tokenObject.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new TokenConfirmationResponse(
                        false
                ));
            }

            Integer confirmed = confirmationTokenService.setConfirmedAt(token);
            Integer enabled = appUserRepository.enableAppUser(tokenObject.getAppUser().getEmail());

            if ((confirmed == 1) && (enabled == 1)) {
                return ResponseEntity.status(HttpStatus.OK).body(new TokenConfirmationResponse(
                        true
                ));
            }

        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new TokenConfirmationResponse(
                    false
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TokenConfirmationResponse(
                false
        ));

    }
}