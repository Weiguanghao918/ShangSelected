package cn.itedus.ssyx.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 15:22
 * @description: 文件上传服务接口
 */
public interface FileUploadService {
    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问url
     */
    String fileUpload(MultipartFile file);
}
