package com.client.DTO;

import com.songshu.snt.base.common.DTO.SntBaseDTO;
import lombok.Data;

/**
 * @author weifeng
 * @description 同步锁实体
 * @date 2019/3/27
 */
@Data
public class TmsLockDTO extends SntBaseDTO {
    //锁名称
    public String lockKey;
    //锁持有时间
    public Long keepMills;
    //获取锁等待时间
    public Long maxSleepMills;
}
