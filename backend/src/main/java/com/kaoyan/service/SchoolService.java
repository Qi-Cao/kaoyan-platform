package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.School;
import com.kaoyan.mapper.SchoolMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolService extends ServiceImpl<SchoolMapper, School> {
    
    public Page<School> getSchoolPage(int pageNum, int pageSize, String province, String keyword) {
        Page<School> page = new Page<>(pageNum, pageSize);
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        
        if (province != null && !province.isEmpty()) {
            queryWrapper.eq("province", province);
        }
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("name", keyword);
        }
        
        queryWrapper.orderByAsc("rank");
        return this.page(page, queryWrapper);
    }
    
    public List<String> getAllProvinces() {
        List<String> provinces = new ArrayList<>();
        List<School> schools = this.list();
        for (School school : schools) {
            if (school.getProvince() != null && !provinces.contains(school.getProvince())) {
                provinces.add(school.getProvince());
            }
        }
        return provinces;
    }
}
