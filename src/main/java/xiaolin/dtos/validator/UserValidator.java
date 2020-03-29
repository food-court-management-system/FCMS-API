package xiaolin.dtos.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import xiaolin.dtos.UserDto;


public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "lName", "user.lName.empty", "Last Name cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "lName", "user.lName.empty", "Last Name cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "lName", "user.lName.empty", "Last Name cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "lName", "user.lName.empty", "Last Name cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "lName", "user.lName.empty", "Last Name cannot be empty");
    }
}
