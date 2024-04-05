package redlib.backend.service;

import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.ClassesQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.vo.ClassesVO;

import java.util.List;

/**
 * 班级模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/10
 */

public interface ClassesService {

    /**
     * 列出所有班级
     *
     * @return 班级列表
     */
    Page<ClassesVO> listClassesByPage(ClassesQueryDTO queryDTO);

    /**
     * 新建班级
     *
     * @param classesDTO
     * @return
     */
    String addClass(ClassesDTO classesDTO);

    /**
     * 删除班级
     *
     * @param ids
     */
    void deleteClass(List<String> ids);

    /**
     * 通过id获取班级信息
     *
     * @param classId
     * @return
     */
    ClassesDTO getById(String classId);

    /**
     * 更新班级数据
     *
     * @param classesDTO 班级输入对象
     * @return 部门编码
     */
    String updateClass(ClassesDTO classesDTO);

    /**
     * 获取指定班级的学生列表
     *
     * @param queryDTO   分页信息
     * @return 学生列表
     */
    Page<Students> listStudents(ClassesQueryDTO queryDTO);

    /**
     * 获取班级ID列表
     *
     */
    List<String> listClassId();

    /**
     * 获取班级名称列表
     *
     */
    List<String> listClassName();

    /**
     * 导入学生
     *
     * @param classesDTO       班级数据
     * @param ids       学生ID列表
     * @return
     */
    int importStudent(ClassesDTO classesDTO, List<Integer> ids);

}
