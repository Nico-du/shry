package net.chinanets.service;

import java.util.List;

import net.chinanets.pojos.ShryUploadfileData;

public interface DocumentService {
	/*
	 * 获取所有的项目和所属文档(non-Javadoc)
	 * @see net.chinanets.service.DocumentService#getAllDocument()
	 */
	public String getDocumentTree(Long id);
	/**
	 * 根据文档获得项目结构树
	 */
	public List getDocTreeAndXm(Long id);
	/**
	 * 获得一个项目下所有的文档
	 */
	public List getOneProjectDoc(Long id);
	/**
	 * 删除文档
	 * 文档List
	 */
	public void deleteDoc(Long id ,String path);
	/**
	 * 删除文档
	 * @param xmdocList
	 * 文档List
	 */
	public void deleteDocument(List<ShryUploadfileData> xmdocList);
	/**
	 * 判断文件是否存在
	 */
	public boolean isFileExist(String filePath);
}
