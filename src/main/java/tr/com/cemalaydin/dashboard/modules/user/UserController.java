package tr.com.cemalaydin.dashboard.modules.user;

import tr.com.cemalaydin.dashboard.base.DataResult;
import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.modules.user.dto.EmailValidateDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.UserChangePasswordDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/findUserBySearch/{searchText}")
    public ResponseEntity findUserBySearch(@PathVariable("searchText") String searchText) {
        try {
            List<UserDto> dtoList = new ArrayList<>();
            List<User> userList = userService.findBySearch(searchText);
            for (User user: userList) {
                dtoList.add(UserDto.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .fullName(user.getFullName())
                                .pictureUrl(user.getPictureUrl())
                                .username(user.getUsername())
                                .emailValidated(user.getEmailValidated())
                        .build());
            }
            return new ResponseEntity(new DataResult(dtoList), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity getUser() {
        try {
            User user = userService.getUser();
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .pictureUrl(user.getPictureUrl())
                    .username(user.getUsername())
                    .provider(user.getProvider())
                    .emailValidated(user.getEmailValidated())
                    .allowSendValidationEmailAgain(DateUtils.addMinutes(user.getEmailValidationCodeSentDate(), 10).before(new Date()))
                    .build();
            return new ResponseEntity(new DataResult(userDto), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity changePassword(@RequestBody UserChangePasswordDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordDto);
            return new ResponseEntity(new DataResult(),HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }


    @PostMapping(value = "/validateEmail")
    public ResponseEntity validateEmail(@Valid @RequestBody EmailValidateDto dto) {
        try {
            userService.validateEMail(dto);
            return new ResponseEntity(new DataResult(),HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }


    @GetMapping(value = "/resendRenewCode")
    public ResponseEntity resendRenewCode() {
        try {
            if (userService.resendRenewCode()) {
                return new ResponseEntity(new DataResult(),HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity(new DataResult("","Doğrulama e-postası gönderilemedi."), HttpStatusCode.valueOf(400));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }




}
