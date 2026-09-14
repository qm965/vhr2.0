package org.javaboy.vhr.employee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

/** Employee profile.  Field names intentionally retain the vhr 1.0 JSON contract. */
@TableName("employee")
public class Employee {
    @TableId(type = IdType.AUTO) public Integer id;
    public String name, gender, idCard, wedlock, nativePlace, email, phone, address;
    public Integer nationId, politicId, departmentId, jobLevelId, posId;
    public String engageForm, tiptopDegree, specialty, school, workState, workID;
    @JsonFormat(pattern = "yyyy-MM-dd") public LocalDate birthday, beginDate, conversionTime, notWorkDate, beginContract, endContract;
    public Double contractTerm;
    public Integer workAge;
    @TableField(exist = false) public Integer salaryId;
}
