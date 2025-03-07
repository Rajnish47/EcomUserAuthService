package dev.Rajnish.EComUserAuth.service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.entity.Session;
import dev.Rajnish.EComUserAuth.entity.SessionStatus;
import dev.Rajnish.EComUserAuth.entity.User;
import dev.Rajnish.EComUserAuth.exceptions.user_exceptios.UserNotFoundException;
import dev.Rajnish.EComUserAuth.repository.SessionRepository;
import dev.Rajnish.EComUserAuth.repository.UserRepository;
import dev.Rajnish.EComUserAuth.security.RandomTokenGeneration;
import dev.Rajnish.EComUserAuth.service.interfaces.AuthService;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<UserResponseDTO> login(LoginRequestDTO loginRequestDTO) {

        Optional<User> userOptional = userRepository.findByEmail(loginRequestDTO.getEmail());
        if(userOptional.isEmpty())
        {
            throw new UserNotFoundException("Invalid User Data");
        }

        User savedUser = userOptional.get();
        if(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), savedUser.getPassword()))
        {
            throw new UserNotFoundException("Invalid User Data");
        }

        String token = RandomTokenGeneration.generateRandomString(30);
        Session session = new Session();
        session.setUser(savedUser);
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);

        sessionRepository.save(session);

        UserResponseDTO userResponseDTO = UserResponseDTO.createUserResponseDTO(savedUser);
        MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, token);

        ResponseEntity<UserResponseDTO> response = new ResponseEntity<>(userResponseDTO,headers,HttpStatus.OK);

        return response;
    }

    @Override
    public Boolean signUp(SignUpRequestDTO signUpRequestDTO) {

        User user = SignUpRequestDTO.createUser(signUpRequestDTO);
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()));
        User savedUser = userRepository.save(user);

        if(savedUser==null)
        {
            return false;
        }

        return true;
    }

    @Override
    public Boolean logout(UUID userId, String token) {

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(!sessionOptional.isEmpty())
        {
            Session savedSession = sessionOptional.get();
            savedSession.setSessionStatus(SessionStatus.ENDED);
            sessionRepository.save(savedSession);
        }

        return true;
    }    
}
