package org.javaboy.vhr.salary.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;
public interface EmpSalaryMapper {
 @Select("select e.id,e.name,e.work_id as workID,e.email,e.phone,es.sid as salaryId from employee e left join empsalary es on e.id=es.eid order by e.id") List<Map<String,Object>> listAssignments();
 @Delete("delete from empsalary where eid=#{eid}") int clear(@Param("eid") Integer eid);
 @Insert("insert into empsalary(eid,sid) values(#{eid},#{sid})") int assign(@Param("eid") Integer eid,@Param("sid") Integer sid);
}
