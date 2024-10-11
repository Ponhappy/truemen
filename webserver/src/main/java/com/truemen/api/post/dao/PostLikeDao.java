package com.truemen.api.post.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.truemen.api.post.model.BulletScreen;
import com.truemen.api.post.model.PostLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostLikeDao extends BaseMapper<PostLike> {
}