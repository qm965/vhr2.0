package org.javaboy.vhr.system.service;

import org.javaboy.vhr.system.entity.Joblevel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
public interface IJoblevelService extends IService<Joblevel> {

    RespPageBean getJoblevelsByPage(Integer page, Integer size);

    RespBean addJoblevel(Joblevel joblevel);

    RespBean updateJoblevel(Joblevel joblevel);

    RespBean deleteJoblevel(Integer id);

}
