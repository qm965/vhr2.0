package org.javaboy.vhr.salary.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.javaboy.vhr.salary.entity.Salary;
import org.javaboy.vhr.salary.mapper.SalaryMapper;
import org.javaboy.vhr.salary.service.SalaryService;
import org.springframework.stereotype.Service;
@Service public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {}
