package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Department;
import redlib.backend.model.Students;

import java.util.List;

public interface StudentsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Students record);

    Students selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Students record);

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(StudentsQueryDTO queryDTO);

    /**
     * 获取指定班级id的学生列表
     *
     * @param classId   班级ID
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 学生列表
     */
    List<Students> listStudents(@Param("classId") String classId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 根据查询条件获取学生列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 学生列表
     */
    List<Students> list(@Param("queryDTO") StudentsQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取已提交的学生个数
     *
     */
    Integer countSubmitted(@Param("classId") String classId, @Param("taskId") String taskId);

    /**
     * 获取已提交的学生列表
     *
     */
    List<Students> getSubmitted(@Param("classId") String classId, @Param("taskId") String taskId);

    /**
     * 获取未提交的学生个数
     *
     */
    Integer countUnSubmitted(@Param("classId") String classId, @Param("taskId") String taskId);

    /**
     * 获取未提交的学生列表
     *
     */
    List<Students> getUnSubmitted(@Param("classId") String classId, @Param("taskId") String taskId);
}