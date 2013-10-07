package tipi.bean;

public class UserProfileBeanImpl implements UserProfileBean {
	
	private String id;
	private String fName;
	private String lName;
	private String phoneNo;
	private String email;
	private String username;
	private UserCompanyBean company;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UserCompanyBean getCompany() {
		return company;
	}
	public void setCompany(UserCompanyBean company) {
		this.company = company;
	}

}
