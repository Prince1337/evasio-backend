package evasio.auth;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import evasio.dto.CustomerDTO;
import evasio.model.User;
import evasio.services.JwtService;
import evasio.services.UserDetailsServiceImplementation;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImplementation userDetailsService;
    private final JwtService jwtTokenUtil;

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest servletRequest
    ) throws StripeException {
        logger.info("Register request received. Body: {}", request);
        logger.info("Remote Address: {}", servletRequest.getRemoteAddr());

        ResponseEntity<AuthenticationResponse> response = ResponseEntity.ok(authenticationService.register(request));
        logger.info("Register response: {}", response);

        return response;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletRequest servletRequest
    ) {
        logger.info("Authenticate request received. Body: {}", request);
        logger.info("Remote Address: {}", servletRequest.getRemoteAddr());

        ResponseEntity<AuthenticationResponse> response = ResponseEntity.ok(authenticationService.authenticate(request));
        logger.info("Authenticate response: {}", response);

        return response;
    }

    @PostMapping("/logout")
    public HttpServletResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return response;
        } else {
            return null;
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequest request) {
        User user = authenticationService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (jwtTokenUtil.isTokenValid(refreshToken, userDetailsService.loadUserByUsername(jwtTokenUtil.extractUsername(refreshToken)))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is expired");
        }

        String username = jwtTokenUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtTokenUtil.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String newAccessToken = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken));
    }
}
