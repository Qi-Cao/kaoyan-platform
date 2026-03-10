package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.UserProfile;
import com.kaoyan.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceImpl<UserProfileMapper, UserProfile> {
    
    public UserProfile getProfileByUserId(Long userId) {
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }
    
    public boolean saveOrUpdateProfile(UserProfile profile) {
        UserProfile existing = getProfileByUserId(profile.getUserId());
        if (existing != null) {
            profile.setId(existing.getId());
            return this.updateById(profile);
        } else {
            return this.save(profile);
        }
    }
}
