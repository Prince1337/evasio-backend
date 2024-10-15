package evasio.services;

import evasio.dto.UserResponse;
import evasio.model.Role;
import evasio.model.User;
import evasio.repositories.RoleRepository;
import evasio.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .email(user.getEmail())
                .build();
    }



    public List<String> getAllUsernames() {
        return userRepository.findAllUsernames();
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return entitiesToResponses(users);
    }

    private List<UserResponse> entitiesToResponses(List<User> users) {
        List<UserResponse> clientsResponse = new ArrayList<>();

        for (User user : users) {
            clientsResponse.add(entityToResponse(user));
        }
        return clientsResponse;
    }

    private UserResponse entityToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }

    public UserResponse getUser(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = optional.get();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(optional.get().getFirstname())
                .lastname(optional.get().getLastname())
                .roles(getUserRoles(user))
                .build();
    }

    private Set<String> getUserRoles(User user) {
        return user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }

    public String getCustomerId(String customerEmail) {
        return userRepository.findByEmail(customerEmail).getId();
    }
}
