package com.winsun.upssm.controller;

import com.winsun.upssm.service.userService;
import com.winsun.upssm.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class userController {

    @Autowired
    private userService userService;

    //系统首页
    @RequestMapping(value = "/")
    public String first() {
        return "/firstPage";
    }

    //登录页面
    @RequestMapping(value = "/loginPage")
    public String login() {
        return "login";
    }

    //注册页面
    @RequestMapping(value = "/registeredPage")
    public String registered() {
        return "registered";
    }

    /**
     * 登录
     *
     * @param request
     * @param session
     * @return 登录成功跳转到成功页面 登录失败跳转到失败页面
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ClientCode = request.getParameter("VerifyCode");
        String name = userService.login(username, password);
        session.setAttribute("username", name);
        response.setHeader("Content-Type", "test/html;charset=utf-8");
        //从共享域取出验证码
        String ServiceCode =(String) session.getAttribute("VerifyCode");
        //与用户输入的验证码对比
            if ((name != null)&&(ServiceCode.equals(ClientCode))) {
                return "success";
            } else {
                return "relogin";
            }
        }


    //注册
    @RequestMapping(value = "/registered", method = RequestMethod.POST)
    public String registered(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userService.registered(username, password);
        String login = userService.login(username, password);
        session.setAttribute("username", login);
        return "registeredSuccess";
    }

    //验证码
    @RequestMapping(value = "/image")
    public void InitImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        // 生成随机字串
        String VerifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 存入会话session
        HttpSession session = request.getSession(true);
        //生成的验证码存入共享域
        session.setAttribute("VerifyCode", VerifyCode.toLowerCase());
        String testCode =(String) session.getAttribute("VerifyCode");

        // 生成图片
        int w = 200, h = 80;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), VerifyCode);
    }
}
