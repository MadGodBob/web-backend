package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.DepartmentMapper;
import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.model.Department;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.DepartmentService;
import redlib.backend.service.utils.DepartmentUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.DepartmentVO;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:33
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AdminService adminService;

    /**
     * 分页获取部门信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的部门数据列表
     */
    @Override
    public Page<DepartmentVO> listByPage(DepartmentQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new DepartmentQueryDTO();
        }

        queryDTO.setDepartmentName(FormatUtils.makeFuzzySearchTerm(queryDTO.getDepartmentName()));
        Integer size = departmentMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Department> list = departmentMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Department::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Department::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<DepartmentVO> voList = new ArrayList<>();
        for (Department department : list) {
            // Department对象转VO对象
            DepartmentVO vo = DepartmentUtils.convertToVO(department, nameMap);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }


    /**
     * 新建部门
     *
     * @param departmentDTO 部门输入对象
     * @return 部门编码
     */
    @Override
    public Integer addDepartment(DepartmentDTO departmentDTO) {
        Token token = ThreadContextHolder.getToken();
        // 校验输入数据正确性
        DepartmentUtils.validateDepartment(departmentDTO);
        // 创建实体对象，用以保存到数据库
        Department department = new Department();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(departmentDTO, department);
        department.setCreatedAt(new Date());
        department.setUpdatedAt(new Date());
        department.setCreatedBy(token.getUserId());
        department.setUpdatedBy(token.getUserId());
        // 调用DAO方法保存到数据库表
        departmentMapper.insert(department);
        return department.getId();
    }

    @Override
    public DepartmentDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "部门id不能为空");
        Department department = departmentMapper.selectByPrimaryKey(id);
        Assert.notNull(department, "id不存在");
        DepartmentDTO dto = new DepartmentDTO();
        BeanUtils.copyProperties(department, dto);
        return dto;
    }

    /**
     * 更新部门数据
     *
     * @param departmentDTO 部门输入对象
     * @return 部门编码
     */
    @Override
    public Integer updateDepartment(DepartmentDTO departmentDTO) {
        // 校验输入数据正确性
        Token token = ThreadContextHolder.getToken();
        DepartmentUtils.validateDepartment(departmentDTO);
        Assert.notNull(departmentDTO.getId(), "部门id不能为空");
        Department department = departmentMapper.selectByPrimaryKey(departmentDTO.getId());
        Assert.notNull(department, "没有找到部门，Id为：" + departmentDTO.getId());

        BeanUtils.copyProperties(departmentDTO, department);
        department.setUpdatedBy(token.getUserId());
        department.setUpdatedAt(new Date());
        departmentMapper.updateByPrimaryKey(department);
        return department.getId();
    }

    /**
     * 根据编码列表，批量删除部门
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "部门id列表不能为空");
        departmentMapper.deleteByCodes(ids);
    }

    @Override
    public Workbook export(DepartmentQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "部门ID");
        map.put("departmentName", "部门名称");
        map.put("contact", "联系人");
        map.put("contactPhone", "手机号");
        map.put("description", "描述");
        map.put("updatedAt", "更新时间");
        map.put("createdByDesc", "创建人");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<DepartmentVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }
}
