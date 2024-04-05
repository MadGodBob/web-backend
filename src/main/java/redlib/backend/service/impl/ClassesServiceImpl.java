package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import redlib.backend.dao.ClassesMapper;
import redlib.backend.dao.StudentsMapper;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.query.ClassesQueryDTO;
import redlib.backend.model.Classes;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.model.Token;
import redlib.backend.service.ClassesService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.vo.ClassesVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 班级管理后端服务模块
 *
 * @author 彭勃
 * @description
 * @date 2024/3/10
 */
@Service
public class ClassesServiceImpl implements ClassesService {
    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    /**
     * 列出所有班级
     *
     * @return 班级列表
     */
    @Override
    public Page<ClassesVO> listClassesByPage(ClassesQueryDTO queryDTO){

        if (queryDTO == null) {
            queryDTO = new ClassesQueryDTO();
        }

        Integer size = classesMapper.count();
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (size == 0) {
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Classes> list = classesMapper.listClass(pageUtils.getOffset(), pageUtils.getLimit());

        List<ClassesVO> voList = new ArrayList<>();
        for (Classes classes : list){
            // Classes转ClassesDTO
            ClassesVO classesVO = new ClassesVO();
            classesVO.setId(classes.getId());
            classesVO.setClassId(classes.getClassId());
            classesVO.setClassName(classes.getClassName());
            classesVO.setDescription(classes.getDescription());
            classesVO.setCreatedAt(classes.getCreatedAt());
            classesVO.setCreatedBy("管理员");
            // 班级人数
            ClassesQueryDTO classesQueryDTO = new ClassesQueryDTO();
            classesQueryDTO.setCurrent(1);
            classesQueryDTO.setPageSize(100);
            classesQueryDTO.setClassId(classes.getClassId());
            Integer num = listStudents(classesQueryDTO).getTotal();
            classesVO.setSum(num);

            voList.add(classesVO);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);

    }

    /**
     * 新建班级
     *
     * @param classesDTO
     * @return
     */
    @Override
    public String addClass(ClassesDTO classesDTO){
        Token token = ThreadContextHolder.getToken();
        FormatUtils.trimFieldToNull(classesDTO);
        Assert.notNull(classesDTO, "班级输入数据不能为空");
        Assert.hasText(classesDTO.getClassName(), "班级名称不能为空");

        Classes classes = new Classes();
        BeanUtils.copyProperties(classesDTO, classes);
        classes.setCreatedAt(new Date());
        classes.setUpdatedAt(new Date());
        classes.setCreatedBy(Integer.toString(token.getUserId()));
        classes.setUpdatedBy(Integer.toString(token.getUserId()));

        classesMapper.insert(classes);
        return classes.getClassId();
    }

    /**
     * 删除班级
     *
     * @param ids
     * @return
     */
    @Override
    public void deleteClass(List<String> ids){
        Assert.notEmpty(ids, "部门id列表不能为空");
        for (String Id : ids){
            classesMapper.deleteByClassId(Id);
        }
    }

    /**
     * 通过班级id获取班级信息
     *
     * @param classId
     * @return
     */
    @Override
    public ClassesDTO getById(String classId){
        Classes classes = classesMapper.selectByClassId(classId);
        if (classes==null){
            return null;
        }
        ClassesDTO classesDTO = new ClassesDTO();
        classesDTO.setId(classes.getId());
        classesDTO.setClassId(classes.getClassId());
        classesDTO.setClassName(classes.getClassName());
        classesDTO.setDescription(classes.getDescription());
        return classesDTO;
    }

    /**
     * 更新班级数据
     *
     * @param classesDTO 班级输入对象
     * @return
     */
    @Override
    public String updateClass(ClassesDTO classesDTO){
        Token token = ThreadContextHolder.getToken();
        FormatUtils.trimFieldToNull(classesDTO);
        classesMapper.updateByPrimaryKey(classesDTO, new Date());
        return classesDTO.getClassId();
    }

    /**
     * 获取指定班级的学生列表
     *
     * @param queryDTO   分页信息
     * @return 学生列表
     */
    @Override
    public Page<Students> listStudents(ClassesQueryDTO queryDTO){

        Assert.notNull(queryDTO, "班级数据不能为空");
        Assert.notNull(queryDTO.getClassId(), "班级id不能为空");

        List<Students> list = studentsMapper.listStudents(queryDTO.getClassId(), (queryDTO.getCurrent()-1)*queryDTO.getPageSize(), queryDTO.getPageSize());
        Integer size = list.size();
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (size == 0) {
            return pageUtils.getNullPage();
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), list);
    }

    /**
     * 获取班级ID列表
     *
     */
    @Override
    public List<String> listClassId(){
        List<String> l = new ArrayList<>();
        for (Classes classes : classesMapper.listClassId()){
            l.add(classes.getClassId());
        }
        return l;
    }

    /**
     * 获取班级名称列表
     *
     */
    @Override
    public List<String> listClassName(){
        List<String> l = new ArrayList<>();
        for (Classes classes : classesMapper.listClassId()){
            l.add(classes.getClassName());
        }
        return l;
    }


    /**
     * 导入学生
     *
     * @param classesDTO       班级数据
     * @param ids       学生ID列表
     */
    @Override
    @Transactional
    public int importStudent(ClassesDTO classesDTO, List<Integer> ids){
        FormatUtils.trimFieldToNull(classesDTO);
        Assert.notNull(classesDTO, "班级输入数据不能为空");
        Assert.hasText(classesDTO.getClassName(), "班级名称不能为空");
        for (Integer id: ids){
            classesMapper.importStudent(classesDTO.getClassId(), classesDTO.getClassName(), id);
        }
        return 0;
    }
}
