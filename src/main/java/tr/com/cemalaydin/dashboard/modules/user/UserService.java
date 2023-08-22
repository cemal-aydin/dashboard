package tr.com.cemalaydin.dashboard.modules.user;

import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import tr.com.cemalaydin.dashboard.helpers.AuthHelper;
import tr.com.cemalaydin.dashboard.helpers.EmailHelper;
import tr.com.cemalaydin.dashboard.helpers.JedisHelper;
import tr.com.cemalaydin.dashboard.helpers.jedisdto.JedisForgotPasswordDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.EmailValidateDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.ForgotPasswordRequestDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.UserChangePasswordDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final EmailHelper emailHelper;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User registerUser(User user) throws Exception {
        checkRegisterValues(user);
        user.setRoles("ROLE_USER");
        user.setProvider(AuthProvider.LOCAL);
        String newEncryptedPassword = AuthHelper.passwordEncoder().encode(user.getPassword());
        user.setPassword(newEncryptedPassword);
        Long validationCode = new Random().nextLong(999999);
        user.setEmailValidationCode(validationCode.toString());
        user.setEmailValidationCodeSentDate(new Date());
        User respornseUser = save(user);
        sendRegisterValidationCode(respornseUser);
        return respornseUser;
    }

    public void sendRegisterValidationCode(User user) throws Exception {
        emailHelper.sendRegisterValidataion(user.getEmail(),user.getEmailValidationCode());
    }

    private void checkRegisterValues(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("emailUsed");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("usernameUsed");
        }

    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(UserChangePasswordDto changePasswordDto) throws Exception{
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loginUser.getId()).orElse(null);
        if (!AuthHelper.passwordEncoder().matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new Exception("Şifre hatalı");
        }
        String newEncryptedPassword = AuthHelper.passwordEncoder().encode(changePasswordDto.getNewPassword());
        user.setPassword(newEncryptedPassword);
        userRepository.save(user);
    }
    public void setPhoneNumber(String phoneNumber){
        //todo
    }
    public void setEmail(String setEmail){
        //todo
    }
    public void setUsername(String username){
        //todo
    }

    public List<User> findBySearch(String searchText)  throws Exception{
        if (StringUtils.isNotBlank(searchText) && searchText.length() <3) {
            throw new Exception("Arama için en az 3 karakter girilmelidir.");
        }
        String userId = getUser().getId();
        return userRepository.findBySearch(userId,"%"+StringUtils.lowerCase(searchText)+ "%");
    }

    public User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto)  throws Exception {
        User user = userRepository.findByUsername(forgotPasswordRequestDto.getUsername()).orElse(null);
        ObjectMapper mapper = new ObjectMapper();
        if (user == null) {
            throw new Exception("Kullanıcı bulunamadı.");
        }

        Long renewKey = new Random().nextLong(999999);
        Date endDate = DateUtils.addMinutes(new Date(), 4);

        JedisForgotPasswordDto dto = JedisForgotPasswordDto.builder()
                .date(endDate.getTime())
                .dateStr(endDate.toString())
                .id(user.getId())
                .renewKey(renewKey)
                .build();
        JedisHelper.jedisHset(JedisHelper.FORGOT_KEY, user.getId(),mapper.writeValueAsString(dto));

        emailHelper.sendForgatPassword(user.getEmail(), dto.getRenewKey());

        return true;
    }

    public boolean resetForgottenPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) throws Exception {
        User user = userRepository.findByUsername(forgotPasswordRequestDto.getUsername()).orElse(null);
        JedisForgotPasswordDto controlDto = new ObjectMapper().readValue(JedisHelper.jedisHget(JedisHelper.FORGOT_KEY, user.getId()), JedisForgotPasswordDto.class);
        Date now = new Date();

        if (!forgotPasswordRequestDto.getRenewKey().equals(controlDto.getRenewKey())) {
            throw new Exception("Yenileme anahtarı hatalı.");
        }

        if (now.after(new Date(controlDto.getDate()))) {
            System.out.printf("zaman geçmiş");
            throw new Exception("Yenileme zamanı geçmiş.");
        }

        user.setPassword(AuthHelper.passwordEncoder().encode(forgotPasswordRequestDto.getNewPassword()));
        userRepository.save(user);
        JedisHelper.jedisHdel(JedisHelper.FORGOT_KEY,user.getId());
        return true;
    }

    public boolean validateEMail(EmailValidateDto dto) throws Exception {
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new Exception("User not found");
        }
        if (user.getEmailValidated()) {
            throw new Exception("E-posta daha önce doğrulanmış");
        }
        if (!dto.getValidateKey().equals(user.getEmailValidationCode())) {
            throw new Exception("Doğrulama anahtarı hatalı.");
        }
        Date now = new Date();
        now = DateUtils.addDays(now, -3);

        if (now.after(user.getEmailValidationCodeSentDate())) {
            System.out.printf("zaman geçmiş");
            throw new Exception("Yenileme zamanı geçmiş.");
        }
        user.setEmailValidationCode(null);
        user.setEmailValidated(true);
        userRepository.save(user);
        return true;
    }

    public boolean resendRenewCode() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userRepository.findById(((User)authentication.getPrincipal()).getId()).orElse(null);
        if (user != null) {
            Long validationCode = new Random().nextLong(999999);
            user.setEmailValidationCode(validationCode.toString());
            user.setEmailValidationCodeSentDate(new Date());
            User respornseUser = save(user);
            sendRegisterValidationCode(respornseUser);
            return true;
        } else {
            return false;
        }
    }
}
