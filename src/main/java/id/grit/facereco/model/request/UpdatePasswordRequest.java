package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdatePasswordRequest {
    private String passwordOld;
    private String passwordNew;
}
