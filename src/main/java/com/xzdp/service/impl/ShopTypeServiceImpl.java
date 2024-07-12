package com.xzdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.xzdp.dto.Result;
import com.xzdp.entity.ShopType;
import com.xzdp.mapper.ShopTypeMapper;
import com.xzdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.xzdp.utils.RedisConstants.CACHE_SHOP_TYPE_LIST_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryShopType() {
        List<String> shopTypeJsonList = stringRedisTemplate.opsForList().range(CACHE_SHOP_TYPE_LIST_KEY, 0, -1);

        if (shopTypeJsonList != null && shopTypeJsonList.size() > 0) {
            ArrayList<ShopType> shopTypeList = new ArrayList<>();
            for (String shopTypeJson : shopTypeJsonList) {
                shopTypeList.add(JSONUtil.toBean(shopTypeJson, ShopType.class));
            }

            return Result.ok(shopTypeList);
        }

        List<ShopType> shopTypeList = query().orderByAsc("sort").list();

        if (shopTypeList == null || shopTypeList.isEmpty()) {
            return Result.fail("no exist shop type");
        }

        for (ShopType shopType : shopTypeList) {
            stringRedisTemplate.opsForList()
                    .rightPush(CACHE_SHOP_TYPE_LIST_KEY, JSONUtil.toJsonStr(shopType));
        }
        return Result.ok(shopTypeList);
    }
}
