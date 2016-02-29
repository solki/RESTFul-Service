package com.ors.web.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.springframework.ui.ModelMap;

import com.ors.bean.JobBean;
import com.ors.dao.JobDao;
import com.ors.web.helper.JobJSONHandler;

public class ProcessSearch {

	private String keyWord, positionType, location, salary;
	private ModelMap model;

	public ProcessSearch(ModelMap model, String keyWord, String positionType,
			String location, String salary) {
		super();
		this.model = model;
		this.keyWord = keyWord;
		this.positionType = positionType;
		this.location = location;
		this.salary = salary;
	}

	public String execute() {

		String returnData = "searchResults";
		String res_jobs = "";
		JobDao jobDao = new JobDao();
		Response res = jobDao.searchJob(keyWord.trim(), positionType.trim(),
				location.trim());
		int responseCode = res.getStatus();
		res_jobs = res.readEntity(String.class);
		ArrayList<JobBean> jobs = null;
		if (responseCode == 200) {

			JobJSONHandler handler = new JobJSONHandler();

			jobs = handler.JSONtoPOJOList(res_jobs);
			if (jobs == null) {
				model.addAttribute("code", "500");
				model.addAttribute("message", "Internal Server Error");
				return "error";
			}
			for (int i = 0; i < jobs.size(); ++i) {
				if (jobs.get(i).getSalary().equals("Negotiable")) {
					continue;
				}

				if ((Integer.parseInt(jobs.get(i).getSalary()) <= Integer
						.parseInt(salary))
						|| (!jobs.get(i).getStatus().equals("created")
								&& !jobs.get(i).getStatus().equals("fixed"))) {
					jobs.remove(i);
					i--;
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			for (int i = 0; i < jobs.size(); ++i) {
				String closeDateStr = jobs.get(i).getClosingDate();
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
					res = jobDao.updateJobStatus(jobs.get(i).get_jobId(),
							"closed");
					if (res.getStatus() != 200) {
						model.addAttribute("code", responseCode);
						model.addAttribute("message",
								res.getStatusInfo().getReasonPhrase());
						return "error";
					}
					jobs.remove(i);
					i--;
				}
			}
			if (jobs.isEmpty()) {
				jobs = null;
			}

		} else if (responseCode == 204) {
			System.out.println(responseCode + " from ProcessSearch");// test
		} else {

			System.out.println(responseCode + " from ProcessSearch");// test
			model.addAttribute("code", responseCode);
			model.addAttribute("message", res.getStatusInfo());
			returnData = "error";
		}

		model.addAttribute("jobs", jobs);
		return returnData;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
