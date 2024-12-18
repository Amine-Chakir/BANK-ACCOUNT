package tgpr.bank.model;

import tgpr.framework.Error;
import tgpr.framework.ErrorList;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public abstract class UserValidator {


    public static Error isValidBirthdate(LocalDate birthdate) {
        if (birthdate == null)
            return Error.NOERROR;
        if (birthdate.compareTo(LocalDate.now()) > 0)
            return new Error("may not be born in the future", User.Fields.BirthDate);
        var age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < 18)
            return new Error("must be 18 years old", User.Fields.BirthDate);
        else if (age > 120)
            return new Error("may not be older then 120 years", User.Fields.BirthDate);
        return Error.NOERROR;
    }

    public static Error isValidFname(String Fname) {
        if (Fname == null || Fname.isBlank())
            return new Error("Fname required", User.Fields.Fname);
        if (!Pattern.matches("[a-zA-Z][a-zA-Z0-9]{2,15}", Fname))
            return new Error("invalid Fname", User.Fields.Fname);
        return Error.NOERROR;
    }
    public static Error isValidLname(String Lname) {
        if (Lname == null || Lname.isBlank())
            return new Error("Lname required", User.Fields.Lname);
        if (!Pattern.matches("[a-zA-Z][a-zA-Z0-9]{2,15}", Lname))
            return new Error("invalid Lname", User.Fields.Lname);
        return Error.NOERROR;
    }

    public static Error isValidEmail(String Email) {
        if (Email == null || Email.isBlank())
            return new Error("EMAIL required", User.Fields.Email);
        if (!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Email))
            return new Error("invalid Email", User.Fields.Email);
        return Error.NOERROR;
    }

    public static Error isValidPassword(String password) {
        if (password == null || password.isBlank())
            return new Error("password required", User.Fields.Password);
        if (!Pattern.matches("[a-zA-Z0-9]{5,}", password))
            return new Error("invalid password", User.Fields.Password);
        return Error.NOERROR;
    }



    public static List<Error> validate(User user) {
        var errors = new ErrorList();

        // field validations
        errors.add(isValidFname(user.getFirst_name()));
        errors.add(isValidBirthdate(user.getBirth_date()));
        errors.add(isValidLname(user.getLast_name()));
        errors.add(isValidPassword(user.getPassword()));
        errors.add(isValidEmail(user.getEmail()));


        return errors;
    }

//    public static Error isValidEmail(String email) {
//        return isValidEmail(email) ;
//    }
}


