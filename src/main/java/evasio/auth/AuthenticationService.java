package evasio.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import evasio.dto.CustomerDTO;
import evasio.dto.RoleResponse;
import evasio.dto.UserResponse;
import evasio.exceptions.ResourceNotFoundException;
import evasio.model.Role;
import evasio.model.User;
import evasio.repositories.RoleRepository;
import evasio.repositories.UserRepository;
import evasio.services.JwtService;
import evasio.services.UserDetailsServiceImplementation;
import evasio.token.Token;
import evasio.token.TokenRepository;
import evasio.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    public AuthenticationResponse register(RegisterRequest request) throws StripeException {
        System.out.println(request);
        System.out.println(request.getRoles());
        if (request.getRoles() == null) {
            Role role = roleRepository.findByName("USER");
            request.setRoles(Set.of(role.getName()));
        }

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(request.getFirstname() + " " + request.getLastname())
                        .setEmail(request.getEmail())
                        .putMetadata("username", request.getUsername())
                        .putMetadata("Roles", request.getRoles().toString())
                        .build();
        Customer customer = Customer.create(params);
        if (customer == null) {
            System.out.println("Customer is null");
            return null;
        }

        User user = User.builder()
                .id(customer.getId())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(customer.getMetadata().get("username"))
                .email(customer.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roleRepository.getRolesByNameIsIn(request.getRoles()))
                .build();
        user = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userDetailsServiceImplementation.loadUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens((User) user);
        saveUserToken((User) user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserResponse updateUser(RegisterRequest updatedUser) {
        User existingUser = userRepository.findByUsername(updatedUser.getUsername());

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRoles(roleRepository.getRolesByNameIsIn(updatedUser.getRoles()));

        userRepository.save(existingUser);

        return entityToResponse(existingUser);
    }

    @Cacheable(value = "user")
    public RoleResponse getRole(Authentication authentication) {
        var username = authentication.getName();
        var user = userDetailsServiceImplementation.loadUserByUsername(username);
        String roleName = user.getAuthorities().toArray()[0].toString();
        System.out.println(user.getAuthorities().toArray()[0].toString());
        return RoleResponse.builder()
                .id(roleRepository.findByName(roleName).getId())
                .name(roleName)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userRepository.findByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public User updateUser(Long id, RegisterRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    private UserResponse entityToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}