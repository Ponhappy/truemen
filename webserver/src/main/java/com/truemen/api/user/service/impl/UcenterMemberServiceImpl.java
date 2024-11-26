package com.truemen.api.user.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.truemen.api.user.model.UcenterMember;
import com.truemen.api.user.service.UcenterMemberService;

@Service
@Primary
public class UcenterMemberServiceImpl implements UcenterMemberService {

    @Autowired
    private DataSource dataSource;

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        String sql = "SELECT * FROM ucenter_member WHERE openid = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, openid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UcenterMember member = new UcenterMember();
                    member.setId(resultSet.getLong("id"));
                    member.setOpenid(resultSet.getString("openid"));
                    member.setNickname(resultSet.getString("nickname"));
                    member.setAvatar(resultSet.getString("avatar"));
                    return member;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get member by openid", e);
        }
        return null;
    }

    @Override
    public void save(UcenterMember member) {
        String sql = "INSERT INTO ucenter_member (openid, nickname, avatar) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getOpenid());
            statement.setString(2, member.getNickname());
            statement.setString(3, member.getAvatar());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save member", e);
        }
    }
}