package com.sl.tdbms.web.admin.web.service.systemmng;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDsdSaveDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDsdSaveDTO.TazInfo;
import com.sl.tdbms.web.admin.common.dto.region.RegionGnSaveDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionGnSaveDTO.GnInfo;
import com.sl.tdbms.web.admin.common.dto.region.RegionListDTO;
import com.sl.tdbms.web.admin.common.entity.TcDsdarMng;
import com.sl.tdbms.web.admin.common.entity.TcGnarMng;
import com.sl.tdbms.web.admin.common.querydsl.QTcDsdarMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcGnarMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcDsdarMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcGnarMngRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Service
public class RegionMngService {
	
	@Autowired
    TcDsdarMngRepository tcDsdarMngRepository;
	
	@Autowired
	TcGnarMngRepository tcGnarMngRepository;
	
	@Autowired
    QTcDsdarMngRepository qTcDsdarMngRepository;
	
	@Autowired
    QTcGnarMngRepository qTcGnarMngRepository;

	@Transactional
	public void dsdMngSave(RegionDsdSaveDTO regionDsdSaveDTO) {
		String resMsg = CommonUtils.getMessage("region.dsdMngSave.fail");
		try {
			String dsdId = regionDsdSaveDTO.getDsdId();
			
			if(regionDsdSaveDTO.getTazInfoArr() != null) {
				int clsfNo = qTcDsdarMngRepository.getMaxClsfNo();
				for(TazInfo tazInfo : regionDsdSaveDTO.getTazInfoArr()) {
					clsfNo++;
					
					TcDsdarMng tcDsdarMng = new TcDsdarMng();
					tcDsdarMng.setClsfNo(clsfNo);
					tcDsdarMng.setDsdId(dsdId);
					tcDsdarMng.setDstrctCd(tazInfo.getDstrctCd());
					tcDsdarMng.setDistrbCnt(tazInfo.getDstrbCnt());
					tcDsdarMngRepository.save(tcDsdarMng);
				}
			}
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

	public List<RegionListDTO> getDsdMngList(SearchCommonDTO searchDTO, PagingUtils paging) {
		return qTcDsdarMngRepository.getDsdMngList(searchDTO,paging);
	}

	public long getDsdTotalCnt(SearchCommonDTO searchDTO) {
		return qTcDsdarMngRepository.getDsdTotalCnt(searchDTO);
	}

	public Long getGnTotalCnt(SearchCommonDTO searchDTO) {
		return qTcGnarMngRepository.getGnTotalCnt(searchDTO);
	}

	public List<RegionListDTO> getGnMngList(SearchCommonDTO searchDTO, PagingUtils paging) {
		return qTcGnarMngRepository.getGnMngList(searchDTO, paging);
	}
	
	/**
	  * @Method Name : dsdMngUpdate
	  * @작성일 : 2024. 7. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 수정 (삭제후 -> 인서트)
	  * @param regionSaveDTO
	  */
	@Transactional
	public void dsdMngUpdate(RegionDsdSaveDTO regionDsdSaveDTO) {
		String resMsg = CommonUtils.getMessage("region.regionDsdUpdate.fail");
		try {
			tcDsdarMngRepository.deleteAllByDsdId(regionDsdSaveDTO.getDsdId());
			dsdMngSave(regionDsdSaveDTO);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

	/**
	  * @Method Name : gnMngSave
	  * @작성일 : 2024. 8. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : GN/TAZ 코드 저장
	  * @param regionGnSaveDTO
	  */
	@Transactional
	public void gnMngSave(RegionGnSaveDTO regionGnSaveDTO) {
		String resMsg = CommonUtils.getMessage("region.regionGnSave.fail");
		try {
			String dstrctCd = regionGnSaveDTO.getDstrctCd();
			
			if(regionGnSaveDTO.getGnInfoArr() != null) {
				int clsfNo = qTcGnarMngRepository.getMaxClsfNo();
				for(GnInfo gnInfo : regionGnSaveDTO.getGnInfoArr()) {
					clsfNo++;
					TcGnarMng tcGnarMng = new TcGnarMng();
					tcGnarMng.setClsfNo(clsfNo);
					tcGnarMng.setGnId(gnInfo.getGnId());
					tcGnarMng.setDstrctCd(dstrctCd);
					tcGnarMng.setDistrbCnt(gnInfo.getDstrbCnt());
					tcGnarMngRepository.save(tcGnarMng);
				}
			}
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

	/**
	  * @Method Name : gnMngUpdate
	  * @작성일 : 2024. 8. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : GN/TAZ 코드 수정 (삭제후 -> 인서트)
	  * @param regionGnSaveDTO
	  */
	@Transactional
	public void gnMngUpdate(RegionGnSaveDTO regionGnSaveDTO) {
		String resMsg = CommonUtils.getMessage("region.regionGnCodeUpdate.fail");
		try {
			tcGnarMngRepository.deleteAllByDstrctCd(regionGnSaveDTO.getDstrctCd());
			gnMngSave(regionGnSaveDTO);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}
}
