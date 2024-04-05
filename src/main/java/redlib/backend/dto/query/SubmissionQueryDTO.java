package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Blob;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
public class SubmissionQueryDTO extends PageQueryDTO{

    private Integer id;

    private String name;

    private Integer studentid;

    private String classId;

    private String className;

    private String taskId;

    private String answer;

    private byte[] img;
}