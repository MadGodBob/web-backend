package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.StudentToClassDTO;
import redlib.backend.dto.query.ClassesQueryDTO;
import redlib.backend.dto.ClassesDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.service.ClassesService;
import redlib.backend.vo.ClassesVO;

import java.util.List;
import java.util.Objects;

/**
 * 班级管理后端服务模块
 *
 * @author 彭勃
 * @description
 * @date 2024/3/10
 */

@RestController
@RequestMapping("/api/classes")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    @PostMapping("listClasses")
    @Privilege("page")
    public Page<ClassesVO> listClasses(@RequestBody ClassesQueryDTO queryDTO) {
        return classesService.listClassesByPage(queryDTO);
    }

    @PostMapping("listClassId")
    @Privilege("page")
    public List<String> listClassId() {
        return classesService.listClassId();
    }

    @PostMapping("listClassName")
    @Privilege("page")
    public List<String> listClassName() {
        return classesService.listClassName();
    }

    @PostMapping("addClass")
    @Privilege("add")
    public String addClass(@RequestBody ClassesDTO classesDTO) {
        Assert.isTrue(classesService.getById(classesDTO.getClassId()) == null, "已存在该班级ID");
        return classesService.addClass(classesDTO);
    }

    @PostMapping("deleteClass")
    @Privilege("delete")
    public void deleteClass(@RequestBody List<String> ids) {
        classesService.deleteClass(ids);
    }

    @GetMapping("getClass")
    @Privilege("update")
    public ClassesDTO getClass(@RequestBody String classId) {
        Assert.notNull(classesService.getById(classId), "不存在该班级");
        return classesService.getById(classId);
    }

    @PostMapping("updateClass")
    @Privilege("update")
    public String updateClass(@RequestBody ClassesDTO classesDTO) {
        return classesService.updateClass(classesDTO);
    }

    @PostMapping("listStudents")
    @Privilege("page")
    public Page<Students> listStudents(@RequestBody ClassesQueryDTO queryDTO) {
        return classesService.listStudents(queryDTO);
    }

    @PostMapping("importStudent")
    @Privilege("add")
    public Integer importStudent(@RequestBody StudentToClassDTO studentToClassDTO) {
        return classesService.importStudent(studentToClassDTO.getClassesDTO(), studentToClassDTO.getIds());
    }
}
