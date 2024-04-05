package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;

import redlib.backend.service.StudentsService;

import java.util.List;


/**
 * 学生管理后端服务模块
 *
 * @author 彭勃
 * @description
 * @date 2024/3/12
 */

@RestController
@RequestMapping("/api/students")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class StudentsController {
    @Autowired
    private StudentsService studentsService;

    @PostMapping("listStudents")
    @Privilege("page")
    public Page<StudentsDTO> listStudents(@RequestBody StudentsQueryDTO queryDTO) {
        return studentsService.listStudentsByPage(queryDTO);
    }

    @PostMapping("getStudent")
    @Privilege
    public StudentsDTO getStudent(@RequestBody Integer studentId) {
        StudentsDTO dto = new StudentsDTO();
        dto.setStudentid(studentId);
        return studentsService.getStudent(dto);
    }

    @PostMapping("deleteStudent")
    @Privilege("delete")
    public void deleteStudent(@RequestBody List<Integer> ids) {

        studentsService.deleteStudent(ids);
    }

    @PostMapping("updateStudent")
    @Privilege("update")
    public void updateStudent(@RequestBody StudentsDTO studentsDTO) {
        studentsService.updateStudent(studentsDTO);
    }

    @PostMapping("addStudent")
    @Privilege("add")
    public void addStudent(@RequestBody StudentsDTO studentsDTO) {
        studentsService.addStudent(studentsDTO);
    }

    @PostMapping("addStudentFile")
    @Privilege("add")
    public int addStudentFile(@RequestParam("file") MultipartFile file) throws Exception{
        return studentsService.addStudentsFile(file.getInputStream(), file.getOriginalFilename());
    }
}
