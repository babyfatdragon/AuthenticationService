package jp.co.workscm.ec.AuthenticationService.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;

/**
 * 
 * @author li_zh
 *
 */
@Entity
@Table(name = "CST_BASE")
@NamedQueries({
	@NamedQuery(name = User.FIND_BY_ML_ADDRESS_AND_PASSWORD, query = "SELECT u FROM CST_BASE u WHERE u.ml_address = :mlAddress AND u.password = :password"),
	@NamedQuery(name = User.FIND_ALL, query = "select u FROM CST_BASE u ORDER BY u.cstId ASC")
})
public class User {

	public static final String FIND_BY_ML_ADDRESS_AND_PASSWORD = "User.findByMlAddressAndPassword";
	public static final String FIND_ALL = "User.findAll";
	@Getter @Id
	@Column(name = "CST_ID")
	private long cstId;
	
	@Column(name="CST_SP_GRP_CD")
	private String cstSpGrpCd;

	@Column(name = "SP_CD")
	private String spCd;

	@Column(name="level")
	private short level;

	@Column(name = "MOB_DIV")
	private int mobDiv;

	@Column(name = "MEM_DIV")
	private int memDiv;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "CST_NO")
	private String cstNo;

	@Column(name = "CST_NM")
	private String cstNm;

	@Column(name = "KN_CST_NM")
	private String knCstNm;

	@Column(name = "NICK_NM")
	private String nickNm;

	private int seibetu;

	@Column(name = "BIRTH_DAY")
	private Timestamp birthDay;

	@Column(name = "ML_ADDRESS")
	private String mlAddress;

	@Column(name="ML_ADDR_DIV")
	private int mlAddrDiv;

	@Column(name="ML_SEND_TO_DIV")
	private int mlSendToDiv;

	@Column(name = "MOB_PHONE_NO01")
	private String mobPhoneNo01;

	@Column(name = "MOB_PHONE_NO02")
	private String mobPhoneNo02;

	@Column(name = "MOB_PHONE_NO03")
	private String mobPhoneNo03;

	@Column(name = "MOB_MAIL_ADDRESS")
	private String mobMailAddress;

	@Column(name = "MAIN_ADDRESS_NO")
	private int mainAddressNo;

	@Column(name = "DEF_ADDRESS_NO")
	private int defAddressNo;

	@Column(name="DEF_ADDRESS_DIV")
	private int defAddressDiv = 0;

	@Column(name = "DEF_ADDRESS_KIND")
	private int defAddressKind;

	@Column(name = "PHONE_NO01")
	private String phoneNo01;

	@Column(name = "PHONE_NO02")
	private String phoneNo02;

	@Column(name = "PHONE_NO03")
	private String phoneNo03;

	private String extension;

	@Column(name = "FAX_NO01")
	private String faxNo01;

	@Column(name = "FAX_NO02")
	private String faxNo02;

	@Column(name = "FAX_NO03")
	private String faxNo03;

	@Column(name="UID_DIV")
	private int uidDiv;

	@Column(name="MAIN_OFFICE_NO")
	private int mainOfficeNo;

	@Column(name="MAIN_CREDIT_NO")
	private int mainCreditNo;

	@Column(name="MAIN_BANK_NO")
	private int mainBankNo;

	@Column(name="MAIL_GET_DIV")
	private int mailGetDiv;

	@Column(name="HTML_GET_DIV")
	private int htmlGetDiv;

	@Column(name="HTML_MOB_GET_DIV")
	private int htmlMobGetDiv = -1;

	@Column(name="DELAY_SHIPPING_MAIL_GET_DIV")
	private int delayShippingMailGetDiv;

	@Column(name = "PHONE_OK_DIV")
	private int phoneOkDiv;

	@Column(name = "DAY_OK_DIV")
	private int dayOkDiv;

	@Column(name = "OFFICE_OK_DIV")
	private int officeOkDiv;

	@Column(name = "DM_OK_DIV")
	private int dmOkDiv;

	@Column(name = "CTLG_OK_DIV")
	private int ctlgOkDiv;

	@Column(name = "FIRST_BUY_DIV")
	private int firstBuyDiv;

	@Column(name = "QUIT_DIV")
	private int quitDiv;

	@Column(name = "QUIT_REASON_DIV")
	private int quitReasonDiv;

	@Column(name = "QUIT_REASON")
	private String quitReason;

	@Column(name="COMPANY_CD")
	private String companyCd;

	@Column(name="COMPANY_NM")
	private String companyNm;
	
	@Column(name = "COMPANY_PHONE_NO1")
	private String companyPhoneNo01;

	@Column(name = "COMPANY_PHONE_NO2")
	private String companyPhoneNo02;

	@Column(name = "COMPANY_PHONE_NO3")
	private String companyPhoneNo03;

	@Column(name="COMPANY_PRICE_VIEW_DIV")
	private int companyPriceViewDiv;

	@Column(name = "REAL_SHOP_CD")
	private String realShopCd;

	@Column(name = "PRM_ID")
	private long prmId;

	@Column(name="CP_CD")
	private String cpCd;

	@Column(name="ROUTE_CD")
	private String routeCd;

	@Column(name="MEDIA_DIV")
	private int mediaDiv;

	@Column(name = "SETTLE_DIV")
	private int settleDiv;

	@Column(name = "SQUESTION_DIV")
	private int squestionDiv;

	private String squestion;

	@Column(name = "SQ_ANSWER")
	private String sqAnswer;

	@Column(name = "ENT_DATE")
	private Timestamp entDate;

	@Column(name = "QUIT_DATE")
	private Timestamp quitDate;

	@Column(name = "DEL_EXTEND_DAYS")
	private int delExtendDays;

	@Column(name = "DEL_INFO_STATUS")
	private int delInfoStatus;

	@Column(name = "DEL_ERR_ID")
	private long delErrId;

	private int state;

	@Column(name = "MARRIED_DIV")
	private int marriedDiv;

	@Column(name = "RANK_CD")
	private String rankCd;

	@Column(name = "RANK_DATE")
	private Timestamp rankDate;

	@Column(name = "LAST_BUY_TIME")
	private Timestamp lastBuyTime;

	@Column(name = "LAST_LOGIN_TIME")
	private Timestamp lastLoginTime;

	@Column(name = "LAST_ACCESS_TIME")
	private Timestamp lastAccessTime;

	@Column(name = "CARD_HOLD_DIV")
	private int CardHoldDiv = 0;

	@Column(name="LANG_CD")
	private String langCd = "ja";

	@Column(name="APRV_DIV")
	private int aprvDiv = 0;

	@Column(name="AUTHENTIC_DIV")
	private int authenticDiv = -1;

	@Column(name = "CHK_USER_ID")
	private String chkUserId;

	@Column(name = "CHK_ML_ADDRESS")
	private String chkMlAddress;

	@Column(name = "CHK_MOB_ML_ADDRESS")
	private String chkMobMlAddress;

	@Column(name="RSRV_NUM01")
	private long rsrvNum01 = 0;

	@Column(name="JOB_ID")
	private Integer jobId;

	@Column(name="SHOPCST_RELATION_DIV")
	private int shopcstRelationDiv = 0;

	@Column(name="FIRST_LOGIN_FLG")
	private int firstLoginFlg;

	@Column(name="MAIL_LOGIN_NG_DIV")
    private int mailLoginNgDiv;
		
}
