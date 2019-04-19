package net.lzzy.practicesonline.activities.models;
/**
 * Created by lzzy_gxy on 2019/4/17.
 * Description:
 */
public class UserCookies {
    private static final UserCookies OUR_INSTANCE = new UserCookies();

    public static UserCookies getInstance() {
        return OUR_INSTANCE;
    }

    private UserCookies() {

    }
}
