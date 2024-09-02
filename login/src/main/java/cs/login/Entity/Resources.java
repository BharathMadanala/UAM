package cs.login.Entity;

public class Resources {
	private int Res_id;
	private String Res_name;
	public int getRes_id() {
		return Res_id;
	}
	public void setRes_id(int res_id) {
		Res_id = res_id;
	}
	public String getRes_name() {
		return Res_name;
	}
	public void setRes_name(String res_name) {
		Res_name = res_name;
	}
	
	

	
	@Override
	public String toString() {
		return "Resources [Res_id=" + Res_id + ", Res_name=" + Res_name ;
	}
	
}
