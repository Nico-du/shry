package net.chinanets.pojos;

/**
 * ShryUploadfileData entity. @author dzj
 */

public class ShryUploadfileData implements java.io.Serializable {

	// Fields

	private Long docid;
	private String uploaddate;
	private String filename;
	private String filetype;
	private String filepath;
	private String filesize;
	private String tablename;
	private String columnname;
	private String dataid;
	private String datatype;//数据类型(图示图片:1,原始数据图:2)
	private String memo;

	// Constructors

	/** default constructor */
	public ShryUploadfileData() {
	}

	/** full constructor */
	public ShryUploadfileData(String uploaddate, String filename,
			String filetype, String filepath, String filesize,
			String tablename, String columnname, String dataid, String memo) {
		this.uploaddate = uploaddate;
		this.filename = filename;
		this.filetype = filetype;
		this.filepath = filepath;
		this.filesize = filesize;
		this.tablename = tablename;
		this.columnname = columnname;
		this.dataid = dataid;
		this.memo = memo;
	}

	// Property accessors

	public Long getDocid() {
		return this.docid;
	}

	public void setDocid(Long docid) {
		this.docid = docid;
	}

	public String getUploaddate() {
		return this.uploaddate;
	}

	public void setUploaddate(String uploaddate) {
		this.uploaddate = uploaddate;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return this.filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilesize() {
		return this.filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getColumnname() {
		return this.columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getDataid() {
		return this.dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}