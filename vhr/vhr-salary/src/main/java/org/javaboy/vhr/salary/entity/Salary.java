package org.javaboy.vhr.salary.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
@TableName("salary") public class Salary {
 @TableId(type=IdType.AUTO) public Integer id;
 public Integer basicSalary, bonus, lunchSalary, trafficSalary, allSalary, pensionBase, medicalBase, accumulationFundBase;
 public Float pensionPer, medicalPer, accumulationFundPer;
 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") public LocalDateTime createDate;
 public String name;
}
