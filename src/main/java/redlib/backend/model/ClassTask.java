package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:class_task表的实体类
 * @version
 * @author:  aaaaa
 * @创建时间: 2024-03-12
 */
@Data
public class ClassTask {
    /**
     * 
     */
    private Integer id;

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
     * 作业描述
     */
    private String taskDescription;

    /**
     * 状态
     */
    private String taskState;

    /**
     * 创建时间
     */
    private Date createdAt;
}