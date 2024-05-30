package com.ekene.onlinebookstore.user;


import com.ekene.onlinebookstore.auth.JwtUtil;
import com.ekene.onlinebookstore.user.model.ObsUser;
import com.ekene.onlinebookstore.user.repository.UserRepository;
import com.ekene.onlinebookstore.user.util.AuthPayload;
import com.ekene.onlinebookstore.user.util.ReturnObj;
import com.ekene.onlinebookstore.user.util.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ReturnObj createLmsUser (UserDto userDto){
        ObsUser.ObsUserBuilder builder = ObsUser.builder();
        builder.email(userDto.getEmail());
        builder.password(passwordEncoder.encode(userDto.getPassword()));
        builder.role(userDto.getRole());

        userRepository.save(builder.build());
        return extract(userDto.getEmail(), "");
    }

    public ReturnObj authenticateUser(AuthPayload authPayload){
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authPayload.getEmail(),
                        authPayload.getPassword()
                )
        );
        var user = userRepository.findObsUserByEmailIgnoreCase(authPayload.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return extract(user.getEmail(), getToken(user.getEmail()));
    }

    private String getToken(String email){
        return jwtUtil.generateToken(email);
    }
    private ReturnObj extract(String email, String token){
        return new ReturnObj(email, token);
    }

}
