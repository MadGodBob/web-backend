package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import redlib.backend.dao.AdminMapper;
import redlib.backend.dao.StudentsMapper;
import redlib.backend.dto.AdminDTO;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.service.AdminService;
import redlib.backend.service.ClassTaskService;
import redlib.backend.service.ClassesService;
import redlib.backend.service.StudentsService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 学生模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/12
 */

@Service
public class StudentsServiceImpl implements StudentsService {
    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private ClassTaskService classTaskService;

    /**
     * 列出符合条件的学生
     *
     * @return 学生列表
     */
    @Override
    public Page<StudentsDTO> listStudentsByPage(StudentsQueryDTO queryDTO){
        if (queryDTO == null) {
            queryDTO = new StudentsQueryDTO();
        }
        queryDTO.setName(FormatUtils.makeFuzzySearchTerm(queryDTO.getName()));
        queryDTO.setClassId(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassId()));
        queryDTO.setClassName(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassName()));
        Integer size = studentsMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Students> list = studentsMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<StudentsDTO> dtoList = new ArrayList<>();
        for (Students students : list) {
            StudentsDTO dto = new StudentsDTO();
            dto.setId(students.getId());
            dto.setName(students.getName());
            dto.setClassName(students.getClassName());
            dto.setClassId(students.getClassId());
            dto.setStudentid(students.getStudentid());

            // 作业完成情况
            List<Integer> l = classTaskService.getTaskAmount(students.getClassId(), students.getStudentid());
            dto.setSubmitted(l.get(0));
            dto.setUnSubmitted(l.get(1));

            dtoList.add(dto);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), dtoList);

    }
    

    /**
     * 删除学生
     *
     */
    @Override
    @Transactional
    public void deleteStudent(List<Integer> ids){
        for (Integer id : ids){
            Integer studentId = studentsMapper.selectByPrimaryKey(id).getStudentid();
            Integer AdminId = adminMapper.getByUserCode(Integer.toString(studentId)).getId();
            adminMapper.deleteByPrimaryKey(AdminId);
            studentsMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public StudentsDTO getStudent(StudentsDTO studentsDTO){
        StudentsQueryDTO queryDTO = new StudentsQueryDTO();
        queryDTO.setStudentid(studentsDTO.getStudentid());
        queryDTO.setCurrent(1);
        queryDTO.setPageSize(10);
        Page<StudentsDTO> p = listStudentsByPage(queryDTO);
        if (p.getList().size() != 1){
            return null;
        }
        return p.getList().getFirst();
    }

    /**
     * 更新学生数据
     *
     * @param studentsDTO 班级输入对象
     * @return
     */
    @Override
    public String updateStudent(StudentsDTO studentsDTO){
        FormatUtils.trimFieldToNull(studentsDTO);
        StudentsDTO dto = getStudent(studentsDTO);
        studentsDTO.setId(dto.getId());
        Students students = new Students();
        BeanUtils.copyProperties(studentsDTO, students);
        studentsMapper.updateByPrimaryKey(students);
        return studentsDTO.getName();
    }

    /**
     * 添加学生
     *
     */
    @Override
    public void addStudent(StudentsDTO studentsDTO){
        //检测学号是否符合标准
        Pattern pattern = Pattern.compile("^[0-9]{9}$");
        Matcher matcher = pattern.matcher(Integer.toString(studentsDTO.getStudentid()));
        Assert.isTrue(matcher.matches(), "学号不符合格式：9位数");
        StudentsDTO dto = getStudent(studentsDTO);
        Assert.isTrue(dto == null, "添加失败：学号不能重复");

        Students students = new Students();
        students.setStudentid(studentsDTO.getStudentid());
        students.setName(studentsDTO.getName());
        students.setClassId(studentsDTO.getClassId());
        students.setClassName(studentsDTO.getClassName());
        studentsMapper.insert(students);

        //添加账户
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setUserCode(Integer.toString(studentsDTO.getStudentid()));
        adminDTO.setName(studentsDTO.getName());
        adminDTO.setEnabled(true);
        adminDTO.setPassword(Integer.toString(studentsDTO.getStudentid()));
        adminService.add(adminDTO);

        //添加班级
        if (classesService.getById(studentsDTO.getClassId()) == null){
            //说明不存在该班级，可以添加
            ClassesDTO classesDTO = new ClassesDTO();
            classesDTO.setClassId(studentsDTO.getClassId());
            classesDTO.setClassName(studentsDTO.getClassName());
            classesService.addClass(classesDTO);
        }

    }

    /**
     * 批量导入学生
     *
     */
    @Override
    @Transactional
    public int addStudentsFile(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("序号", "id");
        map.put("姓名", "name");
        map.put("学号", "studentid");
        map.put("班级ID", "classId");
        map.put("班级名称", "className");
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (studentDTO) -> {
            addStudent(studentDTO);
            row.incrementAndGet();
        }, map, StudentsDTO.class);

        return row.get();
    }


}
