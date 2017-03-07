package com.wechat.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechat.weixin.Ticket;
import com.wechat.weixin.WechatUtil;

/**
 * Created by Administrator on 2016/8/22.
 */
@Controller
@RequestMapping(value = "/index")
public class MainController extends BaseController{



	/**
	 * 首页
	 * @param model
	 */
	@GetMapping
	public String init(Model model,HttpServletRequest request) {
		String ticketUrl = request.getRequestURL().toString();
		Object from = request.getParameter("from");
		Object isappinstalled = request.getParameter("isappinstalled");
		if(from!=null&&isappinstalled!=null){
			ticketUrl+="?from="+from.toString()+"&isappinstalled="+isappinstalled.toString();
		}
		Ticket access_ticket = WechatUtil.getTicket(this.getWechat(), ticketUrl);
		model.addAttribute("access_ticket", access_ticket);
		return "views/index";
	}

}
