package pl.application.passwordwallet.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ModifyStatus {
    private boolean isReadOnly = true;
}
