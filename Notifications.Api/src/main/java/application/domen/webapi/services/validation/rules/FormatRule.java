package application.domen.webapi.services.validation.rules;

import application.domen.webapi.services.validation.ValidationRule;

import java.util.List;
import java.util.regex.Pattern;

public class FormatRule extends ValidationRule {
    private final Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean check(String password, List<String> errorMessage) {
        if(!pattern.matcher(password).matches()){
            errorMessage.add("Почта имеет некорректный вид");
            return false;
        }
        return checkNext(password,errorMessage);
    }
}
