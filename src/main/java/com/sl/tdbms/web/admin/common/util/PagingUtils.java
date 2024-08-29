package com.sl.tdbms.web.admin.common.util;

import lombok.Data;

@Data
public class PagingUtils {
    private long pageSize; // 게시 글 수
    private long firstPageNo; // 첫 번째 페이지 번호
    private long prevPageNo; // 이전 페이지 번호
    private long pageNo = 1; // 페이지 번호
    private long startPageNo; // 시작 페이지 (페이징 네비 기준)
    private long endPageNo; // 끝 페이지 (페이징 네비 기준)
    private long nextPageNo; // 다음 페이지 번호
    private long finalPageNo; // 마지막 페이지 번호
    private long totalCount; // 게시 글 전체 수
    private long limitCount = 10; // 페이징 LIMIT Default 10
    private long offsetCount = 0; // 페이징 Offset Default 10
	
    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        this.makePaging();
    }
    
	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
		if(pageNo < 1) {
			this.offsetCount = 0;
		} else {
			this.offsetCount = (pageNo - 1) * limitCount;
		}
	}
	
	public void setLimitCount(long limitCount) {
		this.limitCount = limitCount;
		if(pageNo < 1) {
			this.offsetCount = 0;
		} else {
			this.offsetCount = (pageNo - 1) * limitCount;
		}
	}
	
    /**
     * 페이징 생성
     */
    private void makePaging() {
        if (this.totalCount == 0) return; // 게시 글 전체 수가 없는 경우
        if (this.pageNo == 0) this.setPageNo(1); // 기본 값 설정
        if (this.pageSize == 0) this.setPageSize(10); // 기본 값 설정
 
        long finalPage = (totalCount + (pageSize - 1)) / pageSize; // 마지막 페이지
        if (this.pageNo > finalPage) this.setPageNo(finalPage); // 기본 값 설정
 
        if (this.pageNo < 0 || this.pageNo > finalPage) this.pageNo = 1; // 현재 페이지 유효성 체크
 
        boolean isNowFirst = pageNo == 1 ? true : false; // 시작 페이지 (전체)
        boolean isNowFinal = pageNo == finalPage ? true : false; // 마지막 페이지 (전체)
 
        long startPage = ((pageNo - 1) / 10) * 10 + 1; // 시작 페이지 (페이징 네비 기준)
        long endPage = startPage + 10 - 1; // 끝 페이지 (페이징 네비 기준)
 
        if (endPage > finalPage) { // [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우
            endPage = finalPage;       
        }
 
        this.setFirstPageNo(1L); // 첫 번째 페이지 번호
 
        if (isNowFirst) {
            this.setPrevPageNo(1L); // 이전 페이지 번호
        } else {
            this.setPrevPageNo(((pageNo - 1) < 1 ? 1 : (pageNo - 1))); // 이전 페이지 번호
        }
 
        this.setStartPageNo(startPage); // 시작 페이지 (페이징 네비 기준)
        this.setEndPageNo(endPage); // 끝 페이지 (페이징 네비 기준)
 
        if (isNowFinal) {
            this.setNextPageNo(finalPage); // 다음 페이지 번호
        } else {
            this.setNextPageNo(((pageNo + 1) > finalPage ? finalPage : (pageNo + 1))); // 다음 페이지 번호
        }
 
        this.setFinalPageNo(finalPage); // 마지막 페이지 번호
    }

	@Override
	public String toString() {
		return "Paging [pageSize=" + pageSize + ", firstPageNo=" + firstPageNo + ", prevPageNo=" + prevPageNo
				+ ", pageNo=" + pageNo + ", startPageNo=" + startPageNo + ", endPageNo=" + endPageNo + ", nextPageNo="
				+ nextPageNo + ", finalPageNo=" + finalPageNo + ", totalCount=" + totalCount + "]";
	}
}
