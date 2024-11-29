package com.truemen.api.user.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.truemen.api.user.dao.UserDAO;
import com.truemen.api.user.model.User;
import com.truemen.api.user.model.WechatUserInfo;
import com.truemen.api.user.storage.UserRepository;
import com.truemen.api.user.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userUserService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Autowired
    public UserService(UserRepository userRepository, HttpSession httpSession, UserDAO userDAO,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * 检查密码是否匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 根据手机号或微信ID检查用户是否存在
     *
     * @param phone    手机号
     * @param wechatId 微信ID
     * @return 用户信息
     */
    public User checkUser(String phone, String wechatId) {
        return userDAO.checkUserByPhoneNumberOrWeChatID(phone, wechatId);
    }

    /**
     * 注册新用户
     *
     * @param user 用户信息
     * @return 注册是否成功
     */
    public boolean register(User user) {
        if (userDAO.checkUserByPhoneNumberOrWeChatID(user.getPhone(), user.getWechatId()) != null) {
            return false; // 用户已存在
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setPermission(1);

        // 如果 userId 为空，生成默认值
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(generateUserId(user));
        }

        return userDAO.registerUser(user);
    }

    /**
     * 手机注册
     *
     * @param user 用户信息
     * @return 注册是否成功
     */
    public boolean registerByPhone(User user) {
        logger.info("registerByPhone called");
        if (userDAO.checkUserByPhoneNumberOrWeChatID(user.getPhone(), user.getWechatId()) != null) {
            logger.warn("registerByPhone 用户已经存在");
            return false; // 用户已存在
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setPermission(1);

        // 如果 userId 为空，生成默认值
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(generateUserId(user));
        }
        return userDAO.registerUserByPhone(user);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * 根据UID获取用户信息
     *
     * @param uid 用户UID
     * @return 用户信息
     */
    public User getUserByUid(Long uid) {
        logger.info("getUserByUid service called");
        return userDAO.getUserByUid(uid);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新是否成功
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    /**
     * 更新用户基本信息
     *
     * @param user 用户信息
     * @return 更新是否成功
     */
    public boolean updateUserBase(User user) {
        return userDAO.updateUserBase(user);
    }

    /**
     * 生成用户ID
     *
     * @param user 用户信息
     * @return 生成的用户ID
     */
    private String generateUserId(User user) {
        // 生成 userId 的逻辑，例如基于用户名或其他规则生成
        return "user_" + System.currentTimeMillis(); // 基于当前时间戳生成
    }

    /**
     * 创建新用户
     *
     * @param userInfo 微信用户信息
     * @return 创建的用户信息
     */
    public User createNewUser(WechatUserInfo userInfo) {
        User user = new User();
        user.setUserId(generateUserId(user));
        user.setUserId(userInfo.getOpenId());
        user.setUserName(userInfo.getNickname());
        user.setAvatar(userInfo.getAvatar());
        User savedUser = userRepository.save(user);

        // 将用户信息存储在 HttpSession 中
        httpSession.setAttribute("user", savedUser);

        return savedUser;
    }

    /**
     * 保存用户的标签
     *
     * @param uid  用户UID
     * @param tags 标签列表
     * @return 保存是否成功
     */
    public boolean saveTags(Long uid, List<String> tags) {
        User user = userDAO.getUserByUid(uid);
        if (user != null) {
            user.setTagsFromList(tags);
            return userDAO.updateUser(user);
        }
        return false;
    }

    /**
     * 获取用户的标签
     *
     * @param uid 用户UID
     * @return 标签列表
     */
    public List<String> getTags(Long uid) {
        User user = userDAO.getUserByUid(uid);
        if (user != null) {
            return user.getTagsAsList();
        }
        return null;
    }

    /**
     * 微信登录逻辑
     *
     * @param code 微信授权码
     * @return 微信用户信息
     * @throws RuntimeException 如果获取用户信息失败
     */
    public WechatUserInfo getUserInfoByCode(String code) {
        try {
            // 获取 access_token 和 openid
            Map<String, String> accessTokenResponse = getAccessTokenAndOpenid(code);
            String accessToken = accessTokenResponse.get("access_token");
            String openid = accessTokenResponse.get("openid");

            // 获取微信用户信息
            WechatUserInfo userInfo = getUserInfoByAccessToken(accessToken, openid);

            // 将用户信息存储在 HttpSession 中
            httpSession.setAttribute("wechatUserInfo", userInfo);

            return userInfo;
        } catch (Exception e) {
            logger.error("Failed to get user info by code: {}", code, e);
            throw new RuntimeException("Failed to get user info by code", e);
        }
    }

    /**
     * 获取微信 access_token 和 openid
     *
     * @param code 微信授权码
     * @return access_token 和 openid
     * @throws RuntimeException 如果获取 access_token 失败
     */
    private Map<String, String> getAccessTokenAndOpenid(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";

        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");

        String response;
        try {
            response = HttpClientUtils.get(url, HttpClientUtils.charset, params);
        } catch (Exception e) {
            logger.error("Failed to get access token from WeChat API with code: {}", code, e);
            throw new RuntimeException("Failed to get access token from WeChat API", e);
        }

        Map<String, String> responseMap = new Gson().fromJson(response, new TypeToken<Map<String, String>>() {}.getType());
        return responseMap;
    }

    /**
     * 获取微信用户信息
     *
     * @param accessToken access_token
     * @param openid      openid
     * @return 微信用户信息
     * @throws RuntimeException 如果获取用户信息失败
     */
    private WechatUserInfo getUserInfoByAccessToken(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo";

        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);

        String response;
        try {
            response = HttpClientUtils.get(url, HttpClientUtils.charset, params);
        } catch (Exception e) {
            logger.error("Failed to get user info from WeChat API with access token: {} and openid: {}", accessToken, openid, e);
            throw new RuntimeException("Failed to get user info from WeChat API", e);
        }

        WechatUserInfo userInfo = new Gson().fromJson(response, WechatUserInfo.class);
        return userInfo;
    }
}