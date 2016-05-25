package apm.modules.sys.support;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apm.common.security.Digests;
import apm.common.utils.Encodes;
import apm.common.utils.SpringContextHolder;
import apm.modules.sys.dao.AreaDao;
import apm.modules.sys.dao.MenuDao;
import apm.modules.sys.dao.OfficeDao;
import apm.modules.sys.dao.UserDao;
import apm.modules.sys.entity.Area;
import apm.modules.sys.entity.Menu;
import apm.modules.sys.entity.Office;
import apm.modules.sys.entity.User;
import apm.modules.sys.security.SystemAuthorizingRealm.Principal;

import org.apache.shiro.SecurityUtils;


/**
 * 用户工具类
 * @author resite
 * @version 2013-01-15
 */
public class Users {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String CACHE_USER = "user";
	public static final String CACHE_MENU_LIST = "menus";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "offices";
	
	public static User currentUser(){
		User user = (User)getCache(CACHE_USER);
		if (user == null){
			Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
			if (principal!=null){
				user = userDao.findOne(principal.getId());
				putCache(CACHE_USER, user);
			}
		}
		if (user == null){
			user = new User();
			SecurityUtils.getSubject().logout();
		}
		return user;
	}
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return currentUser();
	}
	
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menus = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menus == null){
			User user = currentUser();
			menus = menuDao.findByUserId(user.getId());
			putCache(CACHE_MENU_LIST, menus);
		}
		return menus;
	}
	
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
//			User user = currentUser();
//			if (user.isAdmin()){
				areaList = areaDao.findAllList();
//			}else{
//				areaList = areaDao.findAllChild(user.getArea().getId(), "%,"+user.getArea().getId()+",%");
//			}
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> offices = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (offices == null){
			User user = currentUser();
			offices = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
			putCache(CACHE_OFFICE_LIST, offices);
		}
		return offices;
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	// ============== User Cache ==============
	
	@SuppressWarnings("unchecked")
    public static <T> T getCache(String key) {
		T t = (T) getCacheMap().get(key);
		return t == null ? null : t;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static void clearCache() {
		getCacheMap().clear();
	}

	private static Map<String, Object> getCacheMap() {
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		return principal != null ? principal.getCacheMap() : new HashMap<String, Object>();
	}
}
