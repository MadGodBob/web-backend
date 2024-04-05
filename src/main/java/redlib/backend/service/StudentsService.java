package redlib.backend.service;

import org.springframework.web.multipart.MultipartFile;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;

import java.io.InputStream;
import java.util.List;

/**
 * 学生模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/12
 */

public interface StudentsService {

    /**
     * 列出符合条件的学生
     *
     * @return 学生列表
     */
    Page<StudentsDTO> listStudentsByPage(StudentsQueryDTO queryDTO);

    /**
     * 删除学生
     *
     */
    void deleteStudent(List<Integer> ids);

    StudentsDTO getStudent(StudentsDTO studentsDTO);

    /**
     * 更新学生数据
     *
     * @param studentsDTO 班级输入对象
     * @return
     */
    String updateStudent(StudentsDTO studentsDTO);

    /**
     * 添加学生
     *
     */
    void addStudent(StudentsDTO studentsDTO);

    /**
     * 批量导入学生
     *
     */
    int addStudentsFile(InputStream inputStream, String fileName) throws Exception;
}
