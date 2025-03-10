package dev.Rajnish.EComUserAuth.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import dev.Rajnish.EComUserAuth.entity.Role;
import dev.Rajnish.EComUserAuth.entity.Session;
import dev.Rajnish.EComUserAuth.entity.SessionStatus;
import dev.Rajnish.EComUserAuth.entity.User;
import dev.Rajnish.EComUserAuth.exceptions.InvalidTokenException;
import dev.Rajnish.EComUserAuth.exceptions.UserNotFoundException;
import dev.Rajnish.EComUserAuth.repository.SessionRepository;
import dev.Rajnish.EComUserAuth.repository.UserRepository;
import dev.Rajnish.EComUserAuth.service.interfaces.AuthService;
import dev.Rajnish.EComUserAuth.service.interfaces.RoleService;
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
    @Autowired
    private RoleService roleservice;

    private static final String SECRET = "31ab4082a1fe8c1423395e658176fb45cdf7f697017fb739b6cca33c40d6045e";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Override
    public Boolean adminSignUp(SignUpRequestDTO signUpRequestDTO) {

        User user = SignUpRequestDTO.createUser(signUpRequestDTO);
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()));
        Set<Role> userRoles = new HashSet<>();
        Role savedRole = roleservice.fetchByName("Admin");
        userRoles.add(savedRole);
        user.setUserRoles(userRoles);
        User savedUser = userRepository.save(user);

        if(savedUser==null)
        {
            return false;
        }

        return true;
    }

    @Override
    public Boolean signUp(SignUpRequestDTO signUpRequestDTO) {

        User user = SignUpRequestDTO.createUser(signUpRequestDTO);
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()));
        Set<Role> userRoles = new HashSet<>();
        Role savedRole = roleservice.fetchByName("User");
        userRoles.add(savedRole);
        user.setUserRoles(userRoles);
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
        
        Set<Role> savedUserRoles = savedUser.getUserRoles();
        List<String> userRoles = new ArrayList<>();

        for(Role savedUserRole: savedUserRoles)
        {
            userRoles.add(savedUserRole.getName());
        }

        // String token = RandomTokenGeneration.generateRandomString(30);
        //Code for generating JWT token
        MacAlgorithm alg = Jwts.SIG.HS256;
        
        Map<String,Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("userId", savedUser.getId());
        jsonForJWT.put("roles",userRoles);
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
    public boolean validate(String token,ValidateTokenRequestDTO validateTokenRequestDTO) throws InvalidTokenException {

        //TODO: 1. Check role and return true or false depending upon if role is available or not
        //TODO: 2. check for token expiry

        Claims claim = Jwts.parser().verifyWith(SIGNING_KEY).build().parseSignedClaims(token).getPayload();
        if(claim.isEmpty())
        {
            throw new InvalidTokenException("Token is empty");
        }

        // UUID userId = validateTokenRequestDTO.getUserId();
        UUID userId = UUID.fromString(claim.get("userId",String.class));
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus().equals(SessionStatus.ENDED)) {
            throw new InvalidTokenException("token is invalid");
        }

        // String[] userRoles = claim.get("userRoles",String.class).toString().split(",");
        ArrayList<String> userRoles = claim.get("roles",ArrayList.class);
        for(String userRole: userRoles)
        {
            if(userRole.equals(validateTokenRequestDTO.getRole()))
            {
                return true;
            }
        }
        System.out.println(userRoles);

        return false;
    }      
}
