package com.sysu.verto.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sysu.verto.dao.CommentDao;
import com.sysu.verto.framework.exception.ErrorCode;
import com.sysu.verto.framework.exception.ServerException;
import com.sysu.verto.framework.result.PageResult;
import com.sysu.verto.framework.util.ConVertUrlList;
import com.sysu.verto.model.Comment;
import com.sysu.verto.model.Post;
import com.sysu.verto.vo.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Service
public class CommentService extends ServiceImpl<CommentDao, Comment> {

    public PageResult<CommentVo> listPostComment(Integer postId,
                                                 CommentListPostQuery query) {
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getPostId,postId);
        Page page = new Page(query.getPage(), query.getLimit());
        IPage pageResult = baseMapper.selectPage(page, qw);

        System.out.println("总记录数：" + pageResult.getTotal());
        System.out.println("当前页码：" + pageResult.getCurrent());
        System.out.println("每页记录数：" + pageResult.getSize());
        System.out.println("当前页数据：" + pageResult.getRecords());

        return new PageResult<CommentVo>(pageResult.getRecords(),pageResult.getTotal());
    }

    public Integer upLoadComment(CommentUploadQuery commentUploadQuery){
        Comment comment = Comment.builder()
                .create_time(new Date())
                .uid(commentUploadQuery.getUid())
                .content(commentUploadQuery.getContent())
                .postId(commentUploadQuery.getPostId())
                .build();
        return baseMapper.insert(comment);
    }

}
