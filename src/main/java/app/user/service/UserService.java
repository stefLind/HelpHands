package app.user.service;

import app.exception.DomainException;
import app.exception.EmailAlreadyExistsException;
import app.exception.PasswordsNotMatchingException;
import app.exception.UsernameAlreadyExistsException;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmedPassword())) {
            throw new PasswordsNotMatchingException("Passwords do not match.");
        }
        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistsException("Username [%s] already exists.".formatted(registerRequest.getUsername()));
        }
        optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException("User with email [%s] already exists.".formatted(registerRequest.getEmail()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        log.info("User with username [%s] and id [%s] has been created successfully.".formatted(user.getUsername(), user.getId()));

    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .type(registerRequest.getUserType())
                .email(registerRequest.getEmail())
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.getType(), user.isActive());
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(userId)));
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest, MultipartFile file) {
        User user = getUserById(userId);
        User company = null;
        if (userEditRequest.getCompanyId() != null) {
            company = getUserById(userEditRequest.getCompanyId());
        }

        try {
            user.setFirstName(userEditRequest.getFirstName());
            user.setSurname(userEditRequest.getSurname());
            user.setLastName(userEditRequest.getLastName());
            user.setEmail(userEditRequest.getEmail());
            user.setAddress(userEditRequest.getAddress());
            user.setTown(userEditRequest.getTown());
            if (company != null) {
                user.setCompany(company);
            }
            user.setCompanyName(userEditRequest.getCompanyName());
            user.setProfilePictureData(file.getBytes());
            user.setUpdatedOn(LocalDateTime.now());

            userRepository.save(user);
        } catch (IOException e) {
            log.error("Can't edit user details for user with id [%s].".formatted(userId), e);
            throw new RuntimeException("Unable to save user with id: [%s]. Please try again.".formatted(userId));
        }
    }
}
