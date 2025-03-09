package dev.Rajnish.EComUserAuth.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import dev.Rajnish.EComUserAuth.client.ProductServiceClient;
import dev.Rajnish.EComUserAuth.dto.CreateCartRequestDTO;
import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.dto.ValidateTokenRequestDTO;
import dev.Rajnish.EComUserAuth.entity.Session;
import dev.Rajnish.EComUserAuth.entity.SessionStatus;
import dev.Rajnish.EComUserAuth.entity.User;
import dev.Rajnish.EComUserAuth.exceptions.InvalidTokenException;
import dev.Rajnish.EComUserAuth.exceptions.UserNotFoundException;
import dev.Rajnish.EComUserAuth.repository.SessionRepository;
import dev.Rajnish.EComUserAuth.repository.UserRepository;
import dev.Rajnish.EComUserAuth.service.interfaces.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

      private static final String SECRET = "31ab4082a1fe8c1423395e658176fb45cdf7f697017fb739b6cca33c40d6045e";
   private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes()); 


    @Override
    public ResponseEntity<UserResponseDTO> login(LoginRequestDTO loginRequestDTO) {

        Optional<User> userOptional = userRepository.findByEmail(loginRequestDTO.getEmail());
        if(userOptional.isEmpty())
        {
            throw new UserNotFoundException("Invalid email Data");
        }

        User savedUser = userOptional.get();
        if(!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), savedUser.getPassword()))
        {
            throw new UserNotFoundException("Invalid User Data");
        }

        Optional<List<Session>> userSessionsOptional = sessionRepository.findByUser_IdAndSessionStatus(savedUser.getId(),SessionStatus.ACTIVE);
        if(!userSessionsOptional.isEmpty())
        {
            List<Session> savedUserSessions = userSessionsOptional.get();
            if(savedUserSessions.size()==2)
            {
                Session savedUserSession = savedUserSessions.get(0);
                savedUserSession.setSessionStatus(SessionStatus.ENDED);
                savedUserSession.setToken(null);
                sessionRepository.save(savedUserSession);
            }
        }        

        // String token = RandomTokenGeneration.generateRandomString(30);
        //Code for generating JWT token
        MacAlgorithm alg = Jwts.SIG.HS256;
        
        Map<String,Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("userId", savedUser.getId());
        jsonForJWT.put("roles",savedUser.getUserRoles());
        jsonForJWT.put("createdAt",new Date());
        jsonForJWT.put("expiryAt", new Date(LocalDate.now().plusDays(2).toEpochDay()));
        String token = Jwts.builder().claims(jsonForJWT).signWith(SIGNING_KEY,alg).compact();


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

        CreateCartRequestDTO createCartRequestDTO = new CreateCartRequestDTO();
        createCartRequestDTO.setUserId(savedUser.getId());
        createCartRequestDTO.setCartName(savedUser.getName().concat("'s cart"));
        productServiceClient.createNewCart(createCartRequestDTO);
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

    @Override
    public SessionStatus validate(String token,ValidateTokenRequestDTO validateTokenRequestDTO) throws InvalidTokenException {

        //TODO: check for token expiry

        Claims claim = Jwts.parser().verifyWith(SIGNING_KEY).build().parseSignedClaims(token).getPayload();
        if(claim.isEmpty())
        {
            throw new InvalidTokenException("Token is empty");
        }

        UUID userId = validateTokenRequestDTO.getUserId();

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus().equals(SessionStatus.ENDED)) {
            throw new InvalidTokenException("token is invalid");
        }

        return SessionStatus.ACTIVE;
    }    
}
