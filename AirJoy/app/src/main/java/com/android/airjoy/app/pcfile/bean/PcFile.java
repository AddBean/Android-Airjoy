package com.android.airjoy.app.pcfile.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * PC文件类；
 *
 * @author 贾豆
 *
 */
public class PcFile extends Base {
	// 文件名称；
	private String fileName;

	// 文件路径；
	private boolean directory;

	// 是否是文件；
	private boolean file;

	// 文件路径；
	private String filePath;

	//总空间大小（仅驱动器适用，其他为0）；
	private String totalSpace;

	//可用空间大小（仅驱动器适用，其他为0）；
	private String freeSpace;

	// 文件大小（仅文件适用）
	private long length;

	//父文件夹
	private String parent;

	// 父文件夹路径
	private String parentPath;

	//json解析；
	public static List<PcFile> parse(String json) {


		List<PcFile> fileList = new ArrayList<PcFile>();

		try {

			JSONArray jsonArray = new JSONArray(json);

			for (int i = 0; i < jsonArray.length(); i++) {

				PcFile file = new PcFile();

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				file.setFileName(jsonObject.getString("fileName"));

				file.setTotalSpace(jsonObject.getString("totalSpace"));

				file.setFreeSpace(jsonObject.getString("freeSpace"));

				file.setFilePath(jsonObject.getString("filePath"));

				file.setDirectory(jsonObject.getBoolean("directory"));

				file.setFile(jsonObject.getBoolean("file"));

				file.setParent(jsonObject.getString("parent"));

				file.setParentPath(jsonObject.getString("parentPath"));

				file.setLength(jsonObject.getLong("length"));
				fileList.add(file);
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fileList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(String totalSpace) {
		this.totalSpace = totalSpace;
	}

	public String getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(String freeSpace) {
		this.freeSpace = freeSpace;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
}

