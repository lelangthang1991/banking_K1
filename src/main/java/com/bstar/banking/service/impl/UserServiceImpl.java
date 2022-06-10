package com.bstar.banking.service.impl;


import com.bstar.banking.common.RandomVerifycode;
import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.Session;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.BusinessException;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.jwt.AuthenticationHandler;
import com.bstar.banking.jwt.JwtUtil;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.LoginResponse;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.model.response.UserResponse;
import com.bstar.banking.repository.SessionRepository;
import com.bstar.banking.repository.TransactionRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.security.UserDetailsServiceImpl;
import com.bstar.banking.service.MailerService;
import com.bstar.banking.service.UserService;
import com.bstar.banking.utils.DeviceType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.bstar.banking.common.JwtString.*;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.*;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final SessionRepository sessionRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper modelMapper;
    private final DeviceType deviceType;
    private final HttpServletRequest request;
    private final MailerService mailerService;
    private final AuthenticationHandler authenticationHandler;

    @Override
    public RestResponse<LoginResponse> generateTokenAndRefreshToken(LoginDTO loginRequest) throws Exception {
        String password = loginRequest.getPassword();
        String email = loginRequest.getEmail();
        boolean isLogin = authenticationHandler.authenticate(email, password); // Call LDAP/Validate manual
        if (isLogin) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtUtil.generateToken(userDetails);
            Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
            long tokenExpireMillis = tokenExpire.getTime();
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
            long refreshTokenExpireMillis = refreshTokenExpire.getTime();
            Session session = new Session();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
            String clientIp;
            String device_type = request.getHeader("user-agent");
            String clientXForwardedForIp = request.getHeader("x-forwarded-for");
            if (nonNull(clientXForwardedForIp)) {
                clientIp = deviceType.parseXForwardedHeader(clientXForwardedForIp);
            } else {
                clientIp = request.getRemoteAddr();
            }
            session.setRefreshToken(refreshToken);
            session.setExpired(refreshTokenExpire);
            session.setCreateDate(new Date());
            session.setDeviceType(device_type);
            session.setIpAddress(clientIp);
            session.setUser(user);
            sessionRepository.save(session);
            LoginResponse data = new LoginResponse(token, tokenExpireMillis, refreshToken, refreshTokenExpireMillis);
            return new RestResponse<>(OK, LOGIN_SUCCESS, data);
        } else {
            throw new NotFoundException(LOGIN_FAILURE);
        }
    }


    @Override
    public RestResponse<?> forgotPassWord(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findById(forgotPasswordDTO.getEmail()).orElseThrow(() -> new NotFoundException("404", GET_USER_EMAIL_NOT_FOUND));
        if (!user.getVerifyCode().equals(forgotPasswordDTO.getVerifyCode())) {
            throw new CompareException(VERIFY_CODE_DOES_NOT_MATCH);
        } else if (forgotPasswordDTO.getNewPassword().equals(user.getPassword())) {
            throw new CompareException(NEW_PASSWORD_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE);
        }
        user.setVerifyCode("");
        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new RestResponse<>(OK, CHANGE_PASSWORD_SUCCESSFUL);
    }

    @Override
    public RestResponse<?> signupUser(SignupRequest signupRequest) {
        User userEmail = userRepository.findById(signupRequest.getEmail()).orElse(null);
        User userPhone = userRepository.findByPhone(signupRequest.getPhone()).orElse(null);
        if (userEmail.getEmail() != null) {
            throw new CompareException(EMAIl_WAS_REGISTERED);
        }
        if (userPhone.getPhone() != null) {
            throw new CompareException(PHONE_WAS_REGISTERED);
        }
        if (!signupRequest.getPassword().equals(signupRequest.getConfirm())) {
            throw new CompareException(PASSWORD_DOES_NOT_MATCH);
        }
        User user = new User();
        RandomVerifycode verifyCode = new RandomVerifycode();
        String code = verifyCode.Random();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setDob(signupRequest.getDob());
        user.setGender(signupRequest.getGender());
        user.setAddress(signupRequest.getAddress());
        user.setPhone(signupRequest.getPhone());
        user.setIsActivated(false);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setCreatePerson(signupRequest.getEmail());
        user.setUpdatePerson(signupRequest.getEmail());
        user.setVerifyCode(code);
        userRepository.save(user);
        mailerService.sendWelcome(user, code);
        return new RestResponse<>(OK, PLEASE_CHECK_YOUR_EMAIL, modelMapper.map(user, UserResponse.class));
    }

    @Override
    public RestResponse<?> activateUser(@PathVariable String email, @PathVariable String verify) {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (user.getVerifyCode().equals(verify)) {
            user.setRole(1);
            user.setIsActivated(true);
            userRepository.save(user);
            return new RestResponse<>(OK, USER_ACTIVATE_SUCCESSFUL);
        }
        throw new NotFoundException(USER_ACTIVATE_FAILURE);
    }

    @Override
    public RestResponse<?> updateUser(UserUpdateRequest updateRequest, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(user.getEmail());
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(user.getLastName());
        user.setDob(updateRequest.getDob());
        user.setGender(updateRequest.getGender());
        user.setAddress(updateRequest.getAddress());
        user.setPhone(updateRequest.getPhone());
        user.setUpdateDate(new Date());
        user.setUpdatePerson(user.getEmail());
        userRepository.save(user);
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserDTO.class));
    }

    @Override
    public RestResponse<?> infoUser(Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() ->
                new BusinessException(USER_NOT_FOUND));
        Date currentDate = new Date();
        List<Card> list = user.getCards();
        list.forEach(l -> l.setDailyAvailableTransfer(l.getDailyLimitAmount()
                - transactionRepository.dailyLimit(l.getCardNumber(),
                user.getEmail(), 3, currentDate)));
        list.forEach(f -> f.setMonthlyAvailableTransfer(f.getMonthlyLimitAmount()
                - transactionRepository.monthlyLimit(f.getCardNumber(), user.getEmail(),
                3, currentDate.getMonth() + 1,
                currentDate.getYear() + 1900)));
        user.setCards(list);
        userRepository.save(user);
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserResponse.class));
    }

    @Override
    public RestResponse<?> changePasswordByOldPassword(Authentication authentication, ChangePasswordDTO changePasswordDTO) throws Exception {
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new CompareException(CONFIRM_PASSWORD_DOES_NOT_MATCH);
        }
        User user = userRepository.findById(authentication.getName()).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (!passwordEncoder.matches(changePasswordDTO.getPassword(), user.getPassword())) {
            throw new CompareException(PASSWORD_DOES_NOT_MATCH);
        } else if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            throw new CompareException(NEW_PASSWORD_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE);
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new RestResponse<>(OK, CHANGE_PASSWORD_SUCCESSFUL);
    }

    @Override
    public RestResponse<LoginResponse> refreshToken() {
        final String requestTokenHeader = request.getHeader("Authorization");
        String email = "";
        String jwtToken = "";
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            email = jwtUtil.getEmailFromToken(jwtToken);
        }
        Session session = sessionRepository.findSessionByRefreshToken(jwtToken)
                .orElseThrow(() -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);
        Date tokenExpire = jwtUtil.getExpirationDateFromToken(token);
        long tokenExpireMillis = tokenExpire.getTime();
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Date refreshTokenExpire = jwtUtil.getExpirationDateFromToken(refreshToken);
        long refreshTokenExpireMillis = refreshTokenExpire.getTime();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        String clientIp = "";
        String device_type = request.getHeader("user-agent");
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = deviceType.parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }
        session.setRefreshToken(refreshToken);
        session.setExpired(refreshTokenExpire);
        session.setCreateDate(new Date());
        session.setDeviceType(device_type);
        session.setIpAddress(clientIp);
        session.setUser(user);
        sessionRepository.save(session);
        LoginResponse data = new LoginResponse(token, tokenExpireMillis, refreshToken, refreshTokenExpireMillis);
        return new RestResponse<>(OK, GENERATE_TOKEN_AND_REFRESH_TOKEN_SUCCESS, data);
    }

    @Override
    public RestResponse<?> logout() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return new RestResponse<>(OK, USER_LOGOUT_SUCCESS);
    }

    @Override
    public RestResponse<?> findOneUser(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, response);
    }

    @Override
    public RestResponse<?> findAllUserFiltered(FilterUserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getSortField()) || StringUtils.isBlank(userDTO.getSortDir())) {
            Pageable pageable = org.springframework.data.domain.PageRequest.of(userDTO.getPageNumber(), userDTO.getPageSize());
            Page<User> cards = userRepository.findAllUserFiltered(userDTO, pageable);
            List<UserResponse> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, UserResponse.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_USER_SUCCESSFUL, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        } else {
            Sort sort = Sort.by(userDTO.getSortField());
            sort = userDTO.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(userDTO.getPageNumber(), userDTO.getPageSize(), sort);
            Page<User> cards = userRepository.findAllUserFiltered(userDTO, pageable);
            List<UserResponse> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, UserResponse.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_USER_SUCCESSFUL, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        }
    }

    @Override
    public RestResponse<?> userAdminCreate(SignupRequest userDTO) {
        boolean isMailRegistered = userRepository.findById(userDTO.getEmail()).isPresent();
        boolean isPhoneRegistered = userRepository.findByPhone(userDTO.getPhone()).isPresent();
        if (isMailRegistered) {
            throw new CompareException(EMAIl_WAS_REGISTERED);
        } else if (isPhoneRegistered) {
            throw new CompareException(PHONE_WAS_REGISTERED);
        } else if (!userDTO.getPassword().equals(userDTO.getConfirm())) {
            throw new CompareException(PASSWORD_DOES_NOT_MATCH);
        } else {
            try {
                User user = new User();
                RandomVerifycode verifyCode = new RandomVerifycode();
                String code = verifyCode.Random();
                user.setEmail(userDTO.getEmail());
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setDob(userDTO.getDob());
                user.setGender(userDTO.getGender());
                user.setAddress(userDTO.getAddress());
                user.setPhone(userDTO.getPhone());
                user.setIsActivated(userDTO.getIsActivated());
                user.setCreateDate(new Date());
                user.setUpdateDate(new Date());
                user.setCreatePerson(userDTO.getEmail());
                user.setUpdatePerson(userDTO.getEmail());
                user.setVerifyCode(code);
                userRepository.save(user);
                mailerService.sendWelcome(user, code);
                return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, modelMapper.map(user, UserDTO.class));
            } catch (Exception e) {
                throw new CompareException(REGISTRATION_FAILED);
            }
        }
    }

    @Override
    public RestResponse<?> userAdminUpdate(String email, UserDTO userDTO, Authentication authentication) {
        String emailAdmin = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new CompareException(NEW_PHONE_NUMBER_ALREADY_EXISTS);
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setGender(userDTO.getGender());
        user.setPhone(userDTO.getPhone());
        user.setDob(userDTO.getDob());
        user.setUpdatePerson(emailAdmin);
        user.setUpdateDate(new Date());
        User userSave = userRepository.save(user);
        return new RestResponse<>(OK, UPDATE_SUCCESSFUL, modelMapper.map(userSave, UserDTO.class));
    }

    @Override
    public RestResponse<?> userAdminDisabled(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        user.setIsActivated(false);
        userRepository.save(user);
        return new RestResponse<>(OK, USER_DISABLED_SUCCESS);
    }

    @Override
    public RestResponse<?> userAdminDecentralization(DecentralizationDTO dto, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(dto.getEmail()).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        user.setRole(dto.getRole());
        user.setUpdatePerson(email);
        user.setUpdateDate(new Date());
        userRepository.save(user);
        return new RestResponse<>(OK, USER_DECENTRALIZATION_SUCCESS);
    }

    @Override
    public RestResponse<?> findAllCardUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        List<CardDTO> cardDTOS = user.getCards().stream()
                .map(ca -> modelMapper.map(ca, CardDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, cardDTOS);
    }
}
