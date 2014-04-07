package com.leisure.view.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.leisure.view.web.tools.ImageViewTools;
import com.leisure.core.mv.JModelAndView;
import com.leisure.core.security.support.SecurityUserHolder;
import com.leisure.core.tools.CommUtil;
import com.leisure.core.tools.Md5Encrypt;
import com.leisure.domain.Album;
import com.leisure.domain.Role;
import com.leisure.domain.User;
import com.leisure.service.IAlbumService;
import com.leisure.service.IRoleService;
import com.leisure.service.ISysConfigService;
import com.leisure.service.IUserConfigService;
import com.leisure.service.IUserService;

/**
 * 用户登录注册接口控制
 * 
 * @author xiaoxiong
 * 
 */
@Controller
public class LoginViewAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAlbumService albumService;
	@Autowired
	private ImageViewTools imageViewTools;

	/**
	 * @see 登录页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/login.htm")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response, String url) {
		ModelAndView mv = new JModelAndView("login.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		request.getSession(false).removeAttribute("verify_code");
		boolean domain_error = CommUtil.null2Boolean(request.getSession(false)
				.getAttribute("domain_error"));
		if (url != null && !url.equals("")) {
			request.getSession(false).setAttribute("refererUrl", url);
		}
		if (domain_error) {
			mv = new JModelAndView("error.html", configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request,
					response);
		} else {
			 mv.addObject("imageViewTools", imageViewTools);
		}
		mv.addObject("uc_logout_js",
				request.getSession(false).getAttribute("uc_logout_js"));
		String iskyshop_view_type = CommUtil.null2String(request.getSession(
				false).getAttribute("iskyshop_view_type"));
		if (iskyshop_view_type != null && !iskyshop_view_type.equals("")) {
			if (iskyshop_view_type.equals("weixin")) {
				String store_id = CommUtil.null2String(request
						.getSession(false).getAttribute("store_id"));
				mv = new JModelAndView("weixin/success.html",
						configService.getSysConfig(),
						this.userConfigService.getUserConfig(), 1, request,
						response);
				mv.addObject("op_title", "退出成功！");
				mv.addObject("url", CommUtil.getURL(request)
						+ "/weixin/index.htm?store_id=" + store_id);
			}
		}
		return mv;
	}

	/**
	 * @see 注册页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/register.htm")
	public ModelAndView register(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("register.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		request.getSession(false).removeAttribute("verify_code");
		return mv;
	}

	/**
	 * @see 完成注册
	 * 
	 * @param request
	 * @param userName
	 * @param password
	 * @param email
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	@RequestMapping("/register_finish.htm")
	public String register_finish(HttpServletRequest request,
			HttpServletResponse response, String userName, String password,
			String email, String code) throws HttpException, IOException {
		boolean reg = true;// 防止机器注册，如后台开启验证码则强行验证验证码
		if (code != null && !code.equals("")) {
			code = CommUtil.filterHTML(code);// 过滤验证码
		}
		System.out.println(this.configService.getSysConfig()
				.isSecurityCodeRegister());
		if (this.configService.getSysConfig().isSecurityCodeRegister()) {
			if (!request.getSession(false).getAttribute("verify_code")
					.equals(code)) {
				reg = false;
			}
		}
		// 进一步控制用户名不能重复，防止在未开启注册码的情况下注册机恶意注册
		Map params = new HashMap();
		params.put("userName", userName);
		List<User> users = this.userService.query(
				"select obj from User obj where obj.userName=:userName",
				params, -1, -1);
		if (users != null && users.size() > 0) {
			reg = false;
		}
		if (reg) {
			User user = new User();
			user.setUserName(userName);
			user.setUserRole("BUYER");
			user.setAddTime(new Date());
			user.setEmail(email);
			user.setPassword(Md5Encrypt.md5(password).toLowerCase());
			params.clear();
			params.put("type", "BUYER");
			List<Role> roles = this.roleService.query(
					"select obj from Role obj where obj.type=:type", params,
					-1, -1);
			user.getRoles().addAll(roles);
			if (this.configService.getSysConfig().isIntegral()) {
				user.setIntegral(this.configService.getSysConfig()
						.getMemberRegister());
				this.userService.save(user);
				/**
				 * IntegralLog log = new IntegralLog(); log.setAddTime(new
				 * Date()); log.setContent("用户注册增加" +
				 * this.configService.getSysConfig().getMemberRegister() + "分");
				 * log.setIntegral(this.configService.getSysConfig()
				 * .getMemberRegister()); log.setIntegral_user(user);
				 * log.setType("reg"); this.integralLogService.save(log);
				 */
			} else {
				this.userService.save(user);
			}
			// 创建用户默认相册
			Album album = new Album();
			album.setAddTime(new Date());
			album.setAlbum_default(true);
			album.setAlbum_name("默认相册");
			album.setAlbum_sequence(-10000);
			album.setUser(user);
			this.albumService.save(album);
			request.getSession(false).removeAttribute("verify_code");
			if (this.configService.getSysConfig().isUc_bbs()) {// 是否开启UC_bbs同步
				/**
				 * UCClient client = new UCClient(); String ret =
				 * client.uc_user_register(userName, password, email); int uid =
				 * Integer.parseInt(ret); if (uid <= 0) { if (uid == -1) {
				 * System.out.print("用户名不合法"); } else if (uid == -2) {
				 * System.out.print("包含要允许注册的词语"); } else if (uid == -3) {
				 * System.out.print("用户名已经存在"); } else if (uid == -4) {
				 * System.out.print("Email 格式有误"); } else if (uid == -5) {
				 * System.out.print("Email 不允许注册"); } else if (uid == -6) {
				 * System.out.print("该 Email 已经被注册"); } else {
				 * System.out.print("未定义"); } } else {
				 * this.ucTools.active_user(userName, user.getPassword(),
				 * email); }
				 */
			}
			return "redirect:zmparty_login.htm?username="
					+ CommUtil.encode(userName) + "&password=" + password
					+ "&encode=true";
		} else {
			return "redirect:register.htm";
		}
	}

	/**
	 * springsecurity登录成功后跳转到该页面，如有登录相关处理可以在该方法中完成
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user_login_success.htm")
	public ModelAndView user_login_success(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("success.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String url = CommUtil.getURL(request) + "/index.htm";
		String iskyshop_view_type = CommUtil.null2String(request.getSession(
				false).getAttribute("iskyshop_view_type"));
		if (iskyshop_view_type != null && !iskyshop_view_type.equals("")) {
			if (iskyshop_view_type.equals("weixin")) {
				String store_id = CommUtil.null2String(request
						.getSession(false).getAttribute("store_id"));
				mv = new JModelAndView("weixin/success.html",
						configService.getSysConfig(),
						this.userConfigService.getUserConfig(), 1, request,
						response);
				url = CommUtil.getURL(request) + "/weixin/index.htm?store_id="
						+ store_id;
			}
		}
		HttpSession session = request.getSession(false);
		if (session.getAttribute("refererUrl") != null
				&& !session.getAttribute("refererUrl").equals("")) {
			url = (String) session.getAttribute("refererUrl");
			session.removeAttribute("refererUrl");
		}
		if (this.configService.getSysConfig().isUc_bbs()) {
			String uc_login_js = CommUtil.null2String(request.getSession(false)
					.getAttribute("uc_login_js"));
			mv.addObject("uc_login_js", uc_login_js);
		}
		String bind = CommUtil.null2String(request.getSession(false)
				.getAttribute("bind"));
		if (!bind.equals("")) {
			mv = new JModelAndView(bind + "_login_bind.html",
					configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request,
					response);
			User user = SecurityUserHolder.getCurrentUser();
			mv.addObject("user", user);
			request.getSession(false).removeAttribute("bind");
		}
		mv.addObject("op_title", "登录成功");
		mv.addObject("url", url);
		return mv;
	}

	/**
	 * 弹窗登录页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user_dialog_login.htm")
	public ModelAndView user_dialog_login(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("user_dialog_login.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		return mv;
	}
}
