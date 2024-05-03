package application.domen.webapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DetailsInfo implements IResponseInfo {
    private String info;
}
