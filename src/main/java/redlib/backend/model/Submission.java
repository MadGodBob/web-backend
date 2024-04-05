package redlib.backend.model;

import java.sql.Blob;
import java.util.Date;
import lombok.Data;

/**
 * 描述:submission表的实体类
 * @version
 * @author:  aaaaa
 * @创建时间: 2024-03-12
 */
@Data
public class Submission {
    /**
     * 
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 学号
     */
    private Integer studentid;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 作业任务id
     */
    private String taskId;

    /**
     * 答案
     */
    private String answer;

    /**
     * 提交时间
     */
    private Date submitAt;

    /**
     * 图片
     */
    private byte[] img;
}