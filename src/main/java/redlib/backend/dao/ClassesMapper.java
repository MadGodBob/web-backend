package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.model.Classes;

import java.util.Date;
import java.util.List;

public interface ClassesMapper {
    int deleteByPrimaryKey(Integer classId);

    /**
     * 新增班级
     *
     * @param record
     * @return
     */
    int insert(Classes record);

    /**
     * 获取总班级个数
     *
     * @return 数量
     */
    Integer count();

    /**
     * 根据ID获取班级详情
     *
     * @param id
     * @return
     */
    Classes selectByPrimaryKey(Integer id);

    /**
     * 根据班级ID获取班级详情
     *
     * @param classId
     * @return
     */
    Classes selectByClassId(@Param("classId") String classId);

    /**
     * 根据班级ID删除班级
     *
     * @param classId
     * @return
     */
    int deleteByClassId(@Param("classId") String classId);

    /**
     * 通过主键更新班级信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(@Param("record") ClassesDTO record, @Param("date") Date date);

    /**
     * 获取班级列表
     *
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 班级列表
     */
    List<Classes> listClass(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取班级ID列表
     *
     * @return 班级列表
     */
    List<Classes> listClassId();


    /**
     * 导入学生
     *
     * @param classId       班级ID
     * @param className     班级名称
     * @param id       学生ID
     * @return
     */
    int importStudent(@Param("classId") String classId, @Param("className") String className, @Param("id") Integer id);

}