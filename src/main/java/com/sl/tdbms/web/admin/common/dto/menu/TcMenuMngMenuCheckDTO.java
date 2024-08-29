package com.sl.tdbms.web.admin.common.dto.menu;

import lombok.Data;

@Data
public class TcMenuMngMenuCheckDTO {
	
	private String uppermenuUrlpttrn;
	private String menuId;
	private String inputYn;
	private String srchYn;
	private String updtYn;
	private String delYn;
	
	public TcMenuMngMenuCheckDTO () {};
	
	public TcMenuMngMenuCheckDTO(String Yn) {
		this.inputYn = Yn;
		this.srchYn = Yn;
		this.updtYn = Yn;
		this.delYn = Yn;
	}
}
