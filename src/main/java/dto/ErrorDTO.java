package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.testng.annotations.Test;

@Getter
@Setter
@ToString
@Builder
public class ErrorDTO {

//    {
//        timestamp	string($date-time)
//        status	integer($int32)
//            error	string
//        message	{...}
//        path	string
//    }

    private int status;
    private String error;
    private Object message;
    private String path;


}