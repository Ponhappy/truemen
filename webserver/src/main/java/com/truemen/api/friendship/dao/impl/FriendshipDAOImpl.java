package com.truemen.api.friendship.dao.impl;

import com.truemen.api.friendship.dao.FriendshipDAO;
import com.truemen.api.friendship.model.Friendship;
import com.truemen.api.friendship.model.UserCoreInfo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FriendshipDAOImpl implements FriendshipDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    @Override
    public List<Friendship> findFriendsByWeChatOrPhone(String identifier) {
        String sql = "SELECT f.id, f.friend_id, f.created_at, u.* " +
                "FROM friendship f " +
                "JOIN user_core_info u ON f.uid = u.uid " +
                "WHERE u.wechat_id = ? OR u.phone = ?";

        return jdbcTemplate.query(sql, new Object[] { identifier, identifier }, this::mapRowToFriendship);
    }

    private Friendship mapRowToFriendship(ResultSet rs, int rowNum) throws SQLException {
        UserCoreInfo user = new UserCoreInfo();
        user.setUid(rs.getLong("uid"));
        user.setPhone(rs.getString("phone"));
        user.setPermission(rs.getLong("permission"));

        // 将 Timestamp 转换为 LocalDateTime
        Timestamp timestamp = rs.getTimestamp("create_time");
        LocalDateTime createTime = timestamp != null ? timestamp.toLocalDateTime() : null;
        user.setCreateTime(createTime);

        Friendship friendship = new Friendship();
        friendship.setId(rs.getLong("id"));
        friendship.setUser(user);
        friendship.setFriendId(rs.getLong("friend_id"));

        // 将 Timestamp 转换为 LocalDateTime
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;
        friendship.setCreatedAt(Timestamp.valueOf(createdAt));

        return friendship;
    }

    @Override
    public int getMutualFriendsCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM friendship f1 JOIN friendship f2 ON f1.friend_id = f2.uid AND f2.friend_id = f1.uid WHERE f1.uid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    @Override
    public int getFollowingCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM friendship WHERE uid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    @Override
    public int getFollowersCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM friendship WHERE friend_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    @Override
    public List<Friendship> getMutualFriends(Long userId) {
        String sql = "SELECT f1.* FROM friendship f1 JOIN friendship f2 ON f1.friend_id = f2.uid AND f2.friend_id = f1.uid WHERE f1.uid = ?";
        return jdbcTemplate.query(sql, this::mapRowToFriendship, userId);
    }

    @Override
    public List<Friendship> getFollowingFriends(Long userId) {
        String sql = "SELECT * FROM friendship WHERE uid = ?";
        return jdbcTemplate.query(sql, this::mapRowToFriendship, userId);
    }

    @Override
    public List<Friendship> getFollowers(Long userId) {
        String sql = "SELECT * FROM friendship WHERE friend_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToFriendship, userId);
    }
}
