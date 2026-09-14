package org.javaboy.vhr.employee.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
@TableName("nation") public class Nation { @TableId(type = IdType.AUTO) public Integer id; public String name; }
