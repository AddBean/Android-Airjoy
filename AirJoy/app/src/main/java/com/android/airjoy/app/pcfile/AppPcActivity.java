package com.android.airjoy.app.pcfile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.addbean.autils.utils.AnimUtils;
import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.app.pcfile.bean.ConvertValue;
import com.android.airjoy.app.pcfile.bean.PcFile;
import com.android.airjoy.app.pcfile.listview.ListAdapter;
import com.android.airjoy.app.pcfile.listview.ListItem;
import com.android.airjoy.app.pcfile.net.SocketClient;
import com.android.airjoy.app.pcfile.net.download.DownloadActivity;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.widget.CustomMenuDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 手机访问PC文件系统页面；
 *
 * @author 贾豆
 */
public class AppPcActivity extends BaseActivity {
    public LinkedList<FileModel> mHostroyList = new LinkedList<FileModel>();
    public Context _context;
    ListView listView;
    ArrayList<ListItem> list_GroupItem;
    ListAdapter listGroup;
    String fileList = "";
    TextView up_dir_button;
    TextView dir_button;
    List<PcFile> mPcFlieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.app_pc_activity);
        this._context = this;
        initTital();
        initCustomListView();
        sendAndGetData(0, null);// 获取驱动器；
    }

    /* 初始化标题 */
    private void initTital() {

        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                backImageView.setAlpha(50);
                finish();
            }
        });
        dir_button = (TextView) findViewById(R.id.dirtext);
        up_dir_button = (TextView) findViewById(R.id.updirtext);
        dir_button.setText("正在连接……");
        // 主页
        dir_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                sendAndGetData(0, null);// 返回主页；
            }
        });
        //返回上一级；
        up_dir_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AnimUtils.ScaleAnim(v);
                dir_button.setText("正在连接……");
                if (mHostroyList.size() > 0) {
                    if (mHostroyList.getLast().getmPath().equals("null")) {
                        sendAndGetData(0, null);// 返回主页；
                        return;
                    } else {
                        sendAndGetData(1, mHostroyList.getLast().getmPath());// 获取驱动器；
                        mHostroyList.removeLast();
                    }
                } else {
                    sendAndGetData(0, null);// 返回主页；
                }
            }
        });
    }

    /* 初始化自定义ListView */
    private void initCustomListView() {
        listView = (ListView) findViewById(R.id.pc_listView);
        list_GroupItem = new ArrayList<ListItem>();
        listGroup = new ListAdapter(this, list_GroupItem);
        listGroup.AddType(R.layout.app_pc_folder);
        listGroup.AddType(R.layout.app_pc_file);
        listGroup.AddType(R.layout.app_pc_desk);
        listView.setAdapter(listGroup);
        listGroup.notifyDataSetChanged();
        // 点击发送Email和发送建议；
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                View v = listGroup.getView(arg2, arg1, arg0);
                getDateByType(v, arg2);
            }
        });
    }

    /* 获取桌面数据源 */
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Object> getHashMap2(int imgId, String fileName,
                                                 String totalSpace, String freeSpace) {
        HashMap<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.File_ico, imgId);
        map1.put(R.id.File_name, fileName);// 图像资源的ID
        map1.put(R.id.File_totalSpace, totalSpace);// 图像资源的ID
        map1.put(R.id.File_freeSpace, freeSpace);// 图像资源的ID
        return map1;
    }

    /* 获取文件夹数据源 */
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Object> getHashMap0(int imgId, String fileName,
                                                 String FileInf) {
        HashMap<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.File_ico, imgId);
        map1.put(R.id.File_name, fileName);// 图像资源的ID
        map1.put(R.id.File_inf, FileInf);// 图像资源的ID
        return map1;
    }

    /* 获取文件数据源 */
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Object> getHashMap1(int imgId, String fileName,
                                                 String fileInf) {
        HashMap<Integer, Object> map1 = new HashMap<Integer, Object>();
        map1.put(R.id.File_ico, imgId);
        map1.put(R.id.File_name, fileName);// 图像资源的ID
        map1.put(R.id.File_inf, fileInf);// 图像资源的ID
        return map1;
    }

    private void getDateByType(View v, int pos) {
        dir_button.setText("获取中……");
        AnimUtils.ScaleAnim(v);
        if (!mPcFlieList.get(pos).isFile()) {
            mHostroyList.add(new FileModel(mPcFlieList.get(pos).getParentPath(), mPcFlieList.get(pos).getFileName()));
            sendAndGetData(1, mPcFlieList.get(pos).getFilePath());
        } else {
            show_Dialog(_context, mPcFlieList.get(pos));
        }
    }

    private void notifyListView(String json) {
        mPcFlieList = PcFile.parse(json);
        list_GroupItem.clear();
        for (PcFile pcFile : mPcFlieList) {
            if (pcFile.isDirectory()) {// 如果是文件夹；
                list_GroupItem.add(new ListItem(0, getHashMap0(
                        R.drawable.app_folder, pcFile.getFileName(),
                        pcFile.getFilePath())));
            } else if (pcFile.isFile()) {// 如果是文件；
                int res_id = getIdByEx(pcFile.getFileName());
                list_GroupItem.add(new ListItem(1, getHashMap1(res_id,
                        pcFile.getFileName(),
                        ConvertValue.FormetFileSize(pcFile.getLength()))));
            } else if (!pcFile.isFile() && !pcFile.isDirectory()) {// 如果是驱动器；
                if (pcFile.getFreeSpace().equals(" ")) {// 如果是文件夹则:
                    list_GroupItem.add(new ListItem(0, getHashMap0(
                            R.drawable.app_folder, pcFile.getFileName(),
                            pcFile.getFilePath())));
                } else {
                    list_GroupItem.add(new ListItem(2, getHashMap2(
                            R.drawable.app_disk,
                            pcFile.getFilePath().split(":")[0] + "盘" + " "
                                    + pcFile.getFileName(),
                            "大小" + pcFile.getTotalSpace(),
                            "剩余:" + pcFile.getFreeSpace())));
                }

            }
        }
        if (mHostroyList.size() > 0) {
            dir_button.setText(mHostroyList.getLast().getmFoldName());

        } else {
            dir_button.setText("主页");
        }
        listGroup.notifyDataSetChanged();
    }

    /* 查询view的数据 */
    public void sendAndGetData(final int OptionId, final String path) {
        AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    switch (OptionId) {
                        case 0:// 获取驱动器；
                            fileList = SocketClient.getDiskInfo();
                            break;
                        case 1:// 获取路径下文件信息；
                            fileList = SocketClient.getFileInfo(path);
                            break;
                        case 2:// 打开文件；
                            SocketClient.openFile(path);
                            break;
                    }

                } catch (Exception error) {
                    return true;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                // Toast.makeText(_context, "数据"+fileList,
                // Toast.LENGTH_SHORT).show();
                notifyListView(fileList);
                super.onPostExecute(result);
            }
        };
        aTask.execute();
    }

    /* 显示文件操作选择 */
    private void show_Dialog(Context _context, final PcFile pcFile) {
        final CustomMenuDialog dialog = new CustomMenuDialog(_context, "文件操作");
        dialog.setSubmitVisiable(View.GONE);
        dialog.addItem("下载到手机", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("filePath",
                        pcFile.getFilePath());
                intent.putExtra("fileSize", pcFile.getLength());
                intent.putExtra("fileName",
                        pcFile.getFileName());
                intent.setClass(AppPcActivity.this,
                        DownloadActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.addItem("从电脑打开", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pcFile == null) return;
                sendAndGetData(2, pcFile.getFilePath());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class FileModel {
        private String mPath;
        private String mFoldName;

        public FileModel(String mPath, String mFoldName) {
            this.mPath = mPath;
            this.mFoldName = mFoldName;
        }

        public String getmFoldName() {
            return mFoldName;
        }

        public void setmFoldName(String mFoldName) {
            this.mFoldName = mFoldName;
        }

        public String getmPath() {
            return mPath;
        }

        public void setmPath(String mPath) {
            this.mPath = mPath;
        }
    }

    /* 获取文件图片 */
    public static int getIdByEx(String fileName) {
        try {
            String[] exStringList = fileName.split("\\.");// 获取扩展名；
            String exString = exStringList[exStringList.length - 1];
            // 音频部分
            if (exString.equals("mp3")) {
                return R.drawable.file_icon_mp3;
            } else if (exString.equals("cda")) {
                return R.drawable.file_icon_mp3;
            } else if (exString.equals("wma")) {
                return R.drawable.file_icon_mp3;
            } else if (exString.equals("voc")) {
                return R.drawable.file_icon_mp3;
            }

            // 视频部分
            else if (exString.equals("mp4")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("rmvb")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("avi")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("3gp")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("asf")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("flv")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("mkv")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("mov")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("swf")) {
                return R.drawable.file_icon_video;
            } else if (exString.equals("wmv")) {
                return R.drawable.file_icon_video;
            }

            // 图片部分
            else if (exString.equals("jpeg")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("jpg")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("ico")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("gif")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("bmp")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("TIFF")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("psd")) {
                return R.drawable.file_icon_picture;
            } else if (exString.equals("RAW")) {
                return R.drawable.file_icon_picture;
            }

            // 文档部分
            else if (exString.equals("txt")) {
                return R.drawable.file_icon_txt;
            } else if (exString.equals("rar")) {
                return R.drawable.file_icon_rar;
            } else if (exString.equals("doc")) {
                return R.drawable.file_icon_office;
            } else if (exString.equals("docx")) {
                return R.drawable.file_icon_office;
            } else if (exString.equals("xls")) {
                return R.drawable.file_icon_office;
            } else if (exString.equals("ppt")) {
                return R.drawable.file_icon_office;
            } else if (exString.equals("pdf")) {
                return R.drawable.file_icon_office;
            }

            // 未知文件;
            else {
                return R.drawable.app_file;
            }
        } catch (Exception error) {
            return R.drawable.app_file;
        }
    }
}
