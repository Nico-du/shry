package net.chinanets.pojos;

/**
 * XmDocument entity. @author MyEclipse Persistence Tools
 */

public class XmDocument implements java.io.Serializable {

	// Fields

	private Long id;
	private String docMc;
	private String docRq;
	private String docParentid;
	private String docType;
	private String docNr;
	private String docParenttable;
	private String docSize;
	private String fjColumnname;

	// Constructors

	/** default constructor */
	public XmDocument() {
	}

	/** full constructor */
	public XmDocument(String docMc, String docRq, String docParentid,
			String docType, String docNr, String docParenttable,
			String docSize, String fjColumnname) {
		this.docMc = docMc;
		this.docRq = docRq;
		this.docParentid = docParentid;
		this.docType = docType;
		this.docNr = docNr;
		this.docParenttable = docParenttable;
		this.docSize = docSize;
		this.fjColumnname = fjColumnname;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocMc() {
		return this.docMc;
	}

	public void setDocMc(String docMc) {
		this.docMc = docMc;
	}

	public String getDocRq() {
		return this.docRq;
	}

	public void setDocRq(String docRq) {
		this.docRq = docRq;
	}

	public String getDocParentid() {
		return this.docParentid;
	}

	public void setDocParentid(String docParentid) {
		this.docParentid = docParentid;
	}

	public String getDocType() {
		return this.docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocNr() {
		return this.docNr;
	}

	public void setDocNr(String docNr) {
		this.docNr = docNr;
	}

	public String getDocParenttable() {
		return this.docParenttable;
	}

	public void setDocParenttable(String docParenttable) {
		this.docParenttable = docParenttable;
	}

	public String getDocSize() {
		return this.docSize;
	}

	public void setDocSize(String docSize) {
		this.docSize = docSize;
	}

	public String getFjColumnname() {
		return this.fjColumnname;
	}

	public void setFjColumnname(String fjColumnname) {
		this.fjColumnname = fjColumnname;
	}

}