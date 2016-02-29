package com.ors.web.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.ApplicationBean;
import com.ors.bean.JobBean;
import com.ors.bean.UserBean;
import com.ors.dao.ApplicationDao;
import com.ors.dao.JobDao;
import com.ors.web.helper.AppJSONHandler;
import com.ors.web.helper.JobJSONHandler;
import com.ors.web.helper.UserXMLHandler;

public class ProcessLogin {

	private static ArrayList<UserBean> userList;
	private ModelMap model;
	private HttpSession session;
	private String uid;
	private String pwd;
	private String shortKey;

	public ProcessLogin() {
		super();
	}

	public ProcessLogin(ModelMap model, HttpSession session, String uid,
			String pwd) {
		super();
		userList = new UserXMLHandler().translateToUser();
		this.model = model;
		this.session = session;
		this.uid = uid;
		this.pwd = pwd;
	}

	public String execute() {

		UserBean user = check(uid, pwd);
		if (user == null) {
			model.addAttribute("login", "failed");
			return "index";
		}
		shortKey = user.getShortKey();
		String r = "";
		if (user.getShortKey().contains("manager")) {
			JobDao jobDao = new JobDao();
			Response res = jobDao.getAllJobs(shortKey);
			int responseCode = res.getStatus();
			r = res.readEntity(String.class);
			System.out.println(res.readEntity(String.class));
			if (responseCode != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			ArrayList<JobBean> jobs = null;
			if (!r.equals("[]")) {
				JobJSONHandler handler = new JobJSONHandler();
				jobs = handler.JSONtoPOJOList(r);
				if (jobs == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			}
			else {
				model.addAttribute("jobs", jobs);
				session.setAttribute("user", user);
				return "welcomeMng";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			for (JobBean job: jobs) {
				if (!job.getStatus().equals("created") && !job.getStatus().equals("fixed")) {
					continue;
				}
				String closeDateStr = job.getClosingDate();
				Date closeDate;
				try {
					closeDate = sdf.parse(closeDateStr);
					closeDate = sdf.parse(
							sdf.format(closeDate.getTime() + 24 * 3600 * 1000));
				} catch (ParseException e) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					e.printStackTrace();
					return "error";
				}
				if (closeDate.before(now)) {
					res = jobDao.updateJobStatus(job.get_jobId(), "closed");
					if (res.getStatus() != 200) {
						model.addAttribute("code", responseCode);
						model.addAttribute("message",
								res.getStatusInfo().getReasonPhrase());
						return "error";
					}
				}
			}
			res = jobDao.getAllJobs(shortKey);
			System.out.println(res.readEntity(String.class));
			if (res.getStatus() != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			r = res.readEntity(String.class);
			jobs = null;
			if (!r.equals("[]")) {
				JobJSONHandler handler = new JobJSONHandler();
				jobs = handler.JSONtoPOJOList(r);
				if (jobs == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			}
			ApplicationDao appDao = new ApplicationDao();
			AppJSONHandler appHandler = new AppJSONHandler();
			for (JobBean job: jobs) {
				if (!job.getStatus().equals("closed")) {
					continue;
				}
				res = appDao.getAppsByJob(job.get_jobId(), shortKey);
				if (res.getStatus() != 200 && res.getStatus() != 404) {
					model.addAttribute("code", res.getStatus());
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				} else if (res.getStatus() == 200) {
					
					ArrayList<ApplicationBean> apps = appHandler
							.JSONtoPOJOList(res.readEntity(String.class));
					if (apps == null) {
						model.addAttribute("code", "500");
						model.addAttribute("message", "Internal Server Error");
						return "error";
					}
					String isAllArchived = "yes";
					for (ApplicationBean app : apps) {
						if (!app.getStatus().equals("archived")) {
							isAllArchived = "no";
							break;
						}
					}
					if (isAllArchived.equals("yes")) {
						res = jobDao.updateJobStatus(job.get_jobId(), "finalized");
						if (res.getStatus() != 200) {
							model.addAttribute("code", res.getStatus());
							model.addAttribute("message",
									res.getStatusInfo().getReasonPhrase());
							return "error";
						}
					}
				} else {
					res = jobDao.updateJobStatus(job.get_jobId(), "finalized");
					if (res.getStatus() != 200) {
						model.addAttribute("code", res.getStatus());
						model.addAttribute("message",
								res.getStatusInfo().getReasonPhrase());
						return "error";
					}
				}
				
			}
			
			res = jobDao.getAllJobs(shortKey);
			String res_jobs = "";
			
			res_jobs = res.readEntity(String.class);
			if (res.getStatus() != 200) {
				System.out.println(res.getStatus());
				model.addAttribute("code", res.getStatus());
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}
			jobs = null;
			if (!r.equals("[]")) {
				JobJSONHandler handler = new JobJSONHandler();

				jobs = handler.JSONtoPOJOList(res_jobs);
				if (jobs == null) {
					model.addAttribute("code", "500");
					model.addAttribute("message", "Internal Server Error");
					return "error";
				}
			}

			model.addAttribute("jobs", jobs);
			session.setAttribute("user", user);
			return "welcomeMng";

		} else if (user.getShortKey().contains("reviewer")) {

			String dept = user.getDepartment();
			ArrayList<ApplicationBean> apps = null;
			JobDao jobDao = new JobDao();
			Response res = jobDao.getAllJobs("001-manager");
			int responseCode = res.getStatus();
			r = res.readEntity(String.class);
			if (responseCode != 200) {
				System.out.println(responseCode);
				model.addAttribute("code", responseCode);
				model.addAttribute("message",
						res.getStatusInfo().getReasonPhrase());
				return "error";
			}

			ArrayList<JobBean> jobs = null;
			JobJSONHandler jobHandler = new JobJSONHandler();
			jobs = jobHandler.JSONtoPOJOListViaDept(r, dept);
			if (jobs == null) {
				System.out.println("true");
				model.addAttribute("apps", apps);
				session.setAttribute("user", user);
				return "welcomeRev";
			}

			ApplicationDao appDao = new ApplicationDao();
			for (JobBean job : jobs) {
				ArrayList<ApplicationBean> tmp_apps = new ArrayList<ApplicationBean>();
				res = appDao.getAppsByJob(job.get_jobId(), shortKey);
				responseCode = res.getStatus();
				r = res.readEntity(String.class);
				if (responseCode != 200 && responseCode != 404) {
					// System.out.println("I'm here.");
					System.out.println(responseCode);
					model.addAttribute("code", responseCode);
					model.addAttribute("message",
							res.getStatusInfo().getReasonPhrase());
					return "error";
				} else if (responseCode == 200) {

					AppJSONHandler handler = new AppJSONHandler();
					ArrayList<String> status = new ArrayList<String>();
					status.add("in_review");
					status.add("shortlisted");
					status.add("invited");
					status.add("itv_accepted");
					status.add("itv_rejected");
					//tmp_apps = handler.JSONtoPOJOListViaStatus(r, "in_review");
					tmp_apps = handler.JSONtoPOJOListViaStatus(r, status);
					if (tmp_apps == null) {
						continue;
					}
					if (apps == null) {
						apps = new ArrayList<ApplicationBean>();
					}
					for (ApplicationBean app : tmp_apps) {
						apps.add(app);
					}
				} else {
					continue;
				}
			}
			if (apps == null || apps.isEmpty()) {
				apps = null;
			}

			model.addAttribute("apps", apps);
			session.setAttribute("user", user);
			return "welcomeRev";
		} else {
			model.addAttribute("login", "failed");
			return "index";
		}
	}

	public void setModel(ModelMap model) {
		this.model = model;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private UserBean check(String uid, String pwd) {
		UserBean user = null;
		for (UserBean u : userList) {
			if (u.getUid().equals(uid) && u.getPwd().equals(pwd)) {
				user = u; // test if it can be done
				break;
			}
		}
		return user;
	}

}
