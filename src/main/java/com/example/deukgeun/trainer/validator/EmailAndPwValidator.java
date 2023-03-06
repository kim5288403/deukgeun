package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAndPwValidator implements ConstraintValidator<ValidEmailAndPw, Object>{
  
  private final ValidateServiceImpl validateService;
  private final UserServiceImpl userService;
  private final PasswordEncoder passwordEncoder;
  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = false;
    
    String email = validateService.getFieldValue(object, "email");
    String password = validateService.getFieldValue(object, "password");
    
    System.out.println(password);
    System.out.println(email);
    
    
    try {
      User user = userService.getUser(email);
      boolean check = passwordEncoder.matches(password, user.getPassword());
      
      if (check) {
        flag =  true;
      }
    } catch (Exception e) {
      flag = false;
    }
    
    return flag;
  }

}
