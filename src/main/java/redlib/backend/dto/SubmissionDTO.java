package redlib.backend.dto;

import lombok.Data;

import java.sql.Blob;
import java.util.Date;


@Data
public class SubmissionDTO {

    private Integer id;

    private String name;

    private Integer studentid;

    private String classId;

    private String className;

    private String taskId;

    private String answer;

    private Date submitAt;
}