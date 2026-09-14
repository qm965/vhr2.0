package org.javaboy.vhr.employee.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.vhr.employee.entity.Employee;
import org.javaboy.vhr.employee.mapper.EmployeeMapper;
import org.javaboy.vhr.employee.service.EmployeeService;
import org.springframework.stereotype.Service;
@Service public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {}
