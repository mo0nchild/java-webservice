package application.domen.webapi.services.validation;

import application.domen.webapi.services.validation.rules.FormatRule;
import application.domen.webapi.services.validation.rules.LengthRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ValidationEmail {
    private final List<String> errorMessage = new ArrayList<>();
    public void checkValidationRules(String value) {
        this.errorMessage.clear();
        ValidationRule.link(new LengthRule(5, 20), new FormatRule()).check(value, errorMessage);
    }
}