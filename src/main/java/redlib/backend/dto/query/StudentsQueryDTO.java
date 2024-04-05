package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ����:students���ʵ����
 * @version
 * @author:  aaaaa
 * @����ʱ��: 2024-03-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentsQueryDTO extends PageQueryDTO{
    /**
     * 
     */
    private Integer id;

    /**
     * ����
     */
    private String name;

    /**
     * ѧ��
     */
    private Integer studentid;

    /**
     * �༶id
     */
    private String classId;

    /**
     * �༶����
     */
    private String className;
}