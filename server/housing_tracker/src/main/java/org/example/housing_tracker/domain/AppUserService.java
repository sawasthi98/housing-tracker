package org.example.housing_tracker.domain;

import org.example.housing_tracker.data.AppUserRepository;
import org.example.housing_tracker.models.AppUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found!");
        }

        return appUser;
    }

    public Result<AppUser> create(String username, String password) {
        Result<AppUser> result = validate(username, password);
        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, true, List.of("USER"));
        List<AppUser> allUsers=findAll();

        try {
            appUser = repository.create(appUser);
            for(AppUser appUser1:allUsers){
                if(appUser1.getUsername().equals(appUser.getUsername())){
                    result.addErrorMessage("duplicate username",ResultType.INVALID);
                    return result;
                }
            }
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addErrorMessage("The provided username already exists", ResultType.INVALID);
        }

        return result;
    }

    private Result<AppUser> validate(String username, String password) {
        Result<AppUser> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addErrorMessage("Username is required.", ResultType.INVALID);
            return result;
        }

        if (repository.findByUsername(username) != null) {
            result.addErrorMessage("Username already exists. Choose another one.", ResultType.INVALID);
            return result;
        }

        if (password == null) {
            result.addErrorMessage("password is required", ResultType.INVALID);
            return result;
        }

        if (username.length() > 50) {
            result.addErrorMessage("username must be less than 50 characters", ResultType.INVALID);
        }

        if (!isValidPassword(password)) {
            result.addErrorMessage("password must be at least 8 character and contain a digit," +
                    " a letter, and a non-digit/non-letter", ResultType.INVALID);
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits > 0 && letters > 0 && others > 0;
    }

    public List<AppUser> findAll(){
        return repository.findAll();
    }


}
