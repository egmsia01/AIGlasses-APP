package com.aiglasses.tts.listener;

/**
 * 下载监听
 *
 * @author
 * @date
 */
public interface DownloadListener {

    /**
     * 开始下载
     */
    void onStart();

    /**
     * 下载进度
     * @param progress 当前进度
     */
    void onProgress(int progress);

    /**
     * 下载完成
     * @param path 文件路径
     */
    void onFinish(String path);

    /**
     * 下载失败
     * @param errorInfo 错误信息
     */
    void onFail(String errorInfo);

}
