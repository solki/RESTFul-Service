package com.ors.web;

import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ors.bean.UserBean;
import com.ors.web.helper.IdCheckHelper;
import com.ors.web.process.*;

@Controller
@RequestMapping("/")
public class ServiceController {

	// AutoCheckService service;
	private IdCheckHelper checker;

	public ServiceController() {
		super();
		checker = new IdCheckHelper();
	}

	@RequestMapping("/login")
	public String login(ModelMap model, HttpSession session,
			@RequestParam(value = "uid") String uid,
			@RequestParam(value = "pwd") String pwd) {
		if (uid.equals("") || pwd.equals("")) {
			System.out.println("no uid or pwd");
			model.addAttribute("uidpwd", "no");
			return "index";
		}
		// HttpSession session = request.getSession();
		String returnData = new ProcessLogin(model, session, uid, pwd)
				.execute();
		return returnData;
	}

	@RequestMapping("/logout")
	public String logout(ModelMap model, HttpSession session) {

		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
			return "index";
		} else {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"FORBIDDEN You haven't logged in yet.");
			return "error";
		}

	}

	@RequestMapping("/search")
	public String search(ModelMap model,
			@FormParam(value = "keyWord") String keyWord,
			@FormParam(value = "positionType") String positionType,
			@FormParam(value = "location") String location,
			@FormParam(value = "salary") String salary) {
		// System.out.println(keyWord+ positionType+ location+ salary);
		if (keyWord == null || keyWord.equals("")) {
			System.out.println("no keyword");
			model.addAttribute("keyword", "no");
			return "index";
		}
		String returnData = new ProcessSearch(model, keyWord, positionType,
				location, salary).execute();
		return returnData;
	}

	@RequestMapping("/postjobform")
	public String postJobForm(ModelMap model, HttpSession session) {

		checker.setUser((UserBean) session.getAttribute("user"));
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		return "jobPost";
	}

	@RequestMapping("/postjob")
	public String postJob(ModelMap model, HttpSession session,
			@FormParam(value = "closingDate") String closingDate,
			@FormParam(value = "jobDescriptions") String jobDescriptions,
			@FormParam(value = "location") String location,
			@FormParam(value = "positionType") String positionType,
			@FormParam(value = "department") String department,
			@FormParam(value = "salary") String salary) throws Exception {

		String returnData = "postjobsuccess";
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		System.out.println("manager");// test
		ProcessPostJob postObject = new ProcessPostJob(model, closingDate,
				jobDescriptions, location, positionType, department, salary,
				user.getShortKey());
		returnData = postObject.execute();

		return returnData;
	}

	@RequestMapping("/detail")
	public String detail(ModelMap model, HttpSession session,
			@RequestParam(value = "jobId") String jobId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		String returnData = "";
		if (checker.isManager()) {
			returnData = new ProcessMngJobView(model, jobId, user.getShortKey())
					.execute();
		} else {

			returnData = new ProcessView(model, jobId).execute();
		}
		return returnData;
	}

	@RequestMapping("/jobupdating")
	public String updateJob(ModelMap model, HttpSession session,
			@RequestParam(value = "jobId") String jobId) {

		checker.setUser((UserBean) session.getAttribute("user"));
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessUpdateJob(jobId, model).execute();
		return returnData;
	}

	@RequestMapping("/updatejobconfirm")
	public String updateJobConfirm(ModelMap model, HttpSession session,
			@FormParam(value = "jobId") String jobId,
			@FormParam(value = "closingDate") String closingDate,
			@FormParam(value = "jobDescriptions") String jobDescriptions,
			@FormParam(value = "location") String location,
			@FormParam(value = "positionType") String positionType,
			@FormParam(value = "department") String department,
			@FormParam(value = "salary") String salary) {

		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}

		String returnData = new ProcessUpdateJobConfirm(jobId, closingDate,
				jobDescriptions, location, positionType, department, salary,
				model, user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/jobdeletion")
	public String deleteJob(ModelMap model, HttpSession session,
			@RequestParam(value = "jobId") String jobId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessDeleteJob(model, jobId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/jobclose")
	public String closeJob(ModelMap model, HttpSession session,
			@RequestParam(value = "jobId") String jobId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessCloseJob(model, jobId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/applications")
	public String allApplications(ModelMap model, HttpSession session) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessAllApps(model, user.getShortKey())
				.execute();
		return returnData;
	}

	@RequestMapping("/appdetail")
	public String appDetail(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		String returnData = "";
		returnData = new ProcessAppView(model, appId).execute();
		if (checker.isReviewer()) {
			returnData = new ProcessRevAppView(model, appId, user.getUid(),
					user.getShortKey()).execute();
		}
		if (!checker.isUser()) {
			returnData = new ProcessCandiAppView(model, appId).excecute();
		}
		return returnData;
	}

	@RequestMapping("/application")
	public String applyJob(ModelMap model,
			@RequestParam(value = "jobId") String jobId) {
		String returnData = new ProcessApplyJob(model, jobId).execute();
		return returnData;
	}

	@RequestMapping("/applyConfirmation")
	public String confirmApplyJob(ModelMap model,
			@FormParam(value = "jobId") String jobId,
			@FormParam(value = "licenseNumber") String licenseNumber,
			@FormParam(value = "fullName") String fullName,
			@FormParam(value = "postcode") String postcode,
			@FormParam(value = "coverLetter") String coverLetter,
			@FormParam(value = "briefResume") String briefResume) {
		String returnData = new ProcessConfirmApplyJob(model, jobId,
				licenseNumber, fullName, postcode, coverLetter, briefResume)
						.execute();
		return returnData;
	}

	@RequestMapping("/appUpdate")
	public String updateApp(ModelMap model,
			@RequestParam(value = "appId") String appId) {

		String returnData = new ProcessUpdateApp(appId, model).execute();
		return returnData;
	}

	@RequestMapping("/updateappconfirm")
	public String updateAppConfirm(ModelMap model,
			@FormParam(value = "appId") String appId,
			@FormParam(value = "jobId") String jobId,
			@FormParam(value = "licenseNumber") String licenseNumber,
			@FormParam(value = "fullName") String fullName,
			@FormParam(value = "postcode") String postcode,
			@FormParam(value = "coverLetter") String coverLetter,
			@FormParam(value = "briefResume") String briefResume) {

		String returnData = new ProcessUpdateAppConfirm(appId, jobId,
				licenseNumber, fullName, postcode, coverLetter, briefResume,
				model).execute();
		return returnData;
	}

	@RequestMapping("/autocheck")
	public String autocheck(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId,
			@RequestParam(value = "licenseNumber") String licenseNumber,
			@RequestParam(value = "fullName") String fullName,
			@RequestParam(value = "postcode") String postcode) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessAutoCheck(model, user.getShortKey(),
				appId, licenseNumber, fullName, postcode).excute();

		return returnData;
	}

	@RequestMapping("/appdeletion")
	public String deleteApp(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {

		String returnData = new ProcessDeleteApp(model, appId).execute();
		return returnData;
	}

	@RequestMapping("/assignment")
	public String assign(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessAssignment(model, appId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/review")
	public String review(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isReviewer()) {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"FORBIDDEN You are not from hiring team");
			return "error";
		}
		String returnData = new ProcessReview(model, user.getUid(), appId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/reviewResult")
	public String reviewConfirm(ModelMap model, HttpSession session,
			@FormParam(value = "comments") String comments,
			@FormParam(value = "decision") String decision,
			@FormParam(value = "jobId") String jobId,
			@FormParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isReviewer()) {
			model.addAttribute("code", "403");
			model.addAttribute("message",
					"FORBIDDEN You are not from hiring team");
			return "error";
		}
		String returnData = new ProcessReviewConfirm(model, user.getUid(),
				appId, jobId, comments, decision, user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/reviewdetail")
	public String reviewDetail(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isUser()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not a valid user");
			return "error";
		}
		String returnData = new ProcessReviewView(model, appId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/shortlist")
	public String shortList(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager!");
			return "error";
		}
		String returnData = new ProcessShortList(model, appId,
				user.getShortKey()).execute();
		return returnData;
	}

	@RequestMapping("/archive")
	public String archive(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager!");
			return "error";
		}
		String returnData = this.deleteApp(model, session, appId);
		if (returnData.equals("appQuickView")) {
			returnData = new ProcessArchiveApp(model, appId, user.getShortKey())
					.execute();
			if (returnData.equals("done")) {
				return this.allApplications(model, session);
			}
		}
		return "error";

	}

	@RequestMapping("/invite")
	public String invite(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager!");
			return "error";
		}
		String returnData = new ProcessInvite(model, appId).execute();
		return returnData;
	}

	@RequestMapping("/acceptinvite")
	public String acceptInvite(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {

		String returnData = new ProcessAcceptInvite(model, appId).execute();
		if (returnData.equals("done")) {
			return this.appDetail(model, session, appId);
		}
		return "error";
	}

	@RequestMapping("/rejectinvite")
	public String rejectInvite(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {

		String returnData = new ProcessRejectInvite(model, appId).execute();
		if (returnData.equals("done")) {
			return this.appDetail(model, session, appId);
		}
		return "error";
	}

	@RequestMapping("/accept")
	public String accept(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessAccept(model, appId).execute();
		if (returnData.equals("done")) {
			return this.appDetail(model, session, appId);
		}
		return "error";
	}

	@RequestMapping("/reject")
	public String reject(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessReject(model, appId).execute();
		if (returnData.equals("done")) {
			return this.appDetail(model, session, appId);
		}
		return "error";
	}

	@RequestMapping("/finalresult")
	public String finalResult(ModelMap model, HttpSession session,
			@RequestParam(value = "appId") String appId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessFinalResult(model, appId,
				user.getShortKey()).execute();
		if (returnData.equals("done")) {
			return this.reviewDetail(model, session, appId);
		}
		return "error";
	}

	@RequestMapping("/notification")
	public String notify(ModelMap model, HttpSession session,
			@RequestParam(value = "jobId") String jobId) {
		UserBean user = (UserBean) session.getAttribute("user");
		checker.setUser(user);
		if (!checker.isManager()) {
			model.addAttribute("code", "403");
			model.addAttribute("message", "FORBIDDEN You are not manager");
			return "error";
		}
		String returnData = new ProcessNotify(model, jobId, user.getShortKey())
				.execute();
		if (returnData.equals("done")) {
			return this.detail(model, session, jobId);
		}
		return "error";
	}

	@RequestMapping("/home")
	public String home(ModelMap model, HttpSession session) {
		// UserBean user = null;
		checker.setUser((UserBean) session.getAttribute("user"));
		if (!checker.isUser()) {
			return "index";
		} else {
			return this.login(model, session, checker.getUser().getUid(),
					checker.getUser().getPwd());
		}
	}

	@RequestMapping("/index")
	public String welcome(ModelMap model, HttpSession session) {
		if (session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}
		return "index";
	}

}
