package com.sl.tdbms.web.admin.common.enums;


public interface MsgQueueStatus {
    public String WAITING = "0";			//대기중
    public String PROCEEDING = "1";				//진행중
    public String SUCCESS = "2";				//성공
    public String FAILED = "9";				//실패
}
