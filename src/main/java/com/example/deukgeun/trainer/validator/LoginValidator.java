package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginValidator implements ConstraintValidator<ValidLogin, Object>{
  
  private final ValidateServiceImpl validateService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = validateService.getFieldValue(object, "email");
    String password = validateService.getFieldValue(object, "password");
    User user = userRepository.findByEmail(email).orElse(null);
    
    if (user == null) {
      return false;
    } 
      
    boolean check = passwordEncoder.matches(password, user.getPassword());
    if (check) {
      return true;
    }
  
    return false;
  }

}
