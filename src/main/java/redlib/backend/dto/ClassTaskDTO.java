package redlib.backend.dto;

import lombok.Data;

import java.util.Date;

/**
 * ����:class_task���ʵ����
 * @version
 * @author:  aaaaa
 * @����ʱ��: 2024-03-12
 */
@Data
public class ClassTaskDTO {
    /**
     * 
     */
    private Integer id;

    /**
     * �༶id
     */
    private String classId;

    /**
     * �༶����
     */
    private String className;

    /**
     * ��ҵ����id
     */
    private String taskId;

    /**
     * ��ҵ����
     */
    private String taskDescription;

    /**
     * ״̬
     */
    private String taskState;

    /**
     * 创建时间
     */
    private Date createdAt;

}